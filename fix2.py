with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

def find_line(lines, substr):
    for i, l in enumerate(lines):
        if substr in l:
            return i
    return -1

# 1. Base up to VaultTabLockedContent signature
part_pre = lines[:2266]

# 2. VaultTabLockedContent signature and vars
# from 2266 to 2277
part_lock_sig = lines[2266:2278]

# 3. triggerBiometric & LaunchedEffect
start_trigger = find_line(lines, "fun triggerBiometric() {")
end_trigger_effect = find_line(lines, "if (!vaultUnlocked) {") # wait, where is it?
end_trigger_effect = find_line(lines, "Box(") # the start of the lock screen UI
# wait, there's `if (!vaultUnlocked)` and then `// Vault Lock Screen` and `Box(`
start_box = find_line(lines, "        // Vault Lock Screen")
part_trigger = lines[start_trigger:start_box]

# 4. Lock screen Part 1
start_lock1 = start_box
end_lock1 = find_line(lines, "fun VaultTabUnlockedContent(") - 1 # which is 2474
part_lock1 = lines[start_lock1:end_lock1]

# 5. Lock screen Part 2
# Starts at `pinError = true` after the sensor block.
# Let's find the `pinError = true` that follows the sensor block closing.
sensor_end = find_line(lines, "                                                        pinError = true")
end_lock2 = find_line(lines, "        // Vault Unlocked Content: Advanced Private Media Hub")
# Wait, there are closing braces before `Vault Unlocked Content`.
# Let's see:
#                         )
#                     }
#                 }
#             }
#         }
part_lock2 = lines[sensor_end:end_lock2]

# 6. VaultTabUnlockedContent signature and vars
start_unlock_sig = end_lock1 + 1 # 2474
end_unlock_sig = find_line(lines, "    // Unified sensor detector for Panic Gesture (Shake and Face Down)")
part_unlock_sig = lines[start_unlock_sig:end_unlock_sig]

# 7. Sensor block
start_sensor = end_unlock_sig
end_sensor = sensor_end
part_sensor = lines[start_sensor:end_sensor]

# 8. Unlocked UI
start_unlocked = end_lock2
end_unlocked = find_line(lines, "fun EmptyVaultSectionState(") - 1
part_unlocked = lines[start_unlocked:end_unlocked]

# 9. Post
part_post = lines[end_unlocked:]

# Now reconstruct!

new_lines = []
new_lines.extend(part_pre)

# We need the VaultTabContent wrapper:
new_lines.append("@Composable\nfun VaultTabContent(\n    viewModel: CalculatorViewModel,\n    onLockExit: () -> Unit,\n    modifier: Modifier = Modifier\n) {\n    val vaultUnlocked by viewModel.vaultUnlocked.collectAsState()\n    if (!vaultUnlocked) {\n        VaultTabLockedContent(viewModel, onLockExit, modifier)\n    } else {\n        VaultTabUnlockedContent(viewModel, onLockExit, modifier)\n    }\n}\n\n")

# VaultTabLockedContent
new_lines.extend(part_lock_sig)
new_lines.extend(part_trigger)
new_lines.extend(part_lock1)
new_lines.append("                                                    } else {\n")
new_lines.extend(part_lock2)
new_lines.append("}\n\n")

# VaultTabUnlockedContent
new_lines.extend(part_unlock_sig)
new_lines.extend(part_sensor)
new_lines.extend(part_unlocked)
# VaultTabUnlockedContent has its closing brace at the end of part_unlocked already?
# Actually, part_unlocked includes `    } // Closes Box` and `}`
# Let's append them.

new_lines.extend(part_post)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.writelines(new_lines)
