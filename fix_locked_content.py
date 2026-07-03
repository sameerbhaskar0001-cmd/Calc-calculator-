with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

def find_line(substr, start=0):
    for i in range(start, len(lines)):
        if substr in lines[i]:
            return i
    return -1

# Find the start of VaultTabLockedContent
start = find_line("fun VaultTabLockedContent(")
if start != -1:
    # Find the end of VaultTabLockedContent (it's around line 2473)
    # We will just search for the start of VaultTabUnlockedContent
    end = find_line("@Composable", start + 1)
    
    # We also need to delete the random Part 2 piece (lines 2645 to 2698)
    part2_start = find_line("pinError = true", end)
    part2_end = find_line("fun VaultTabUnlockedContent(", part2_start)
    
    # Wait, Part 2 starts exactly at `pinError = true` after the sensor block.
    # The sensor block ends with `    }\n    }\n` around 2644.
    
    print(f"VaultTabLockedContent: {start} to {end}")
    print(f"Part 2: {part2_start} to {part2_end}")
