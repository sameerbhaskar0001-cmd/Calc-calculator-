with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

new_lines = []
i = 0
while i < len(lines):
    if "ActiveTab.VAULT -> {" in lines[i]:
        new_lines.append(lines[i])
        new_lines.append("                        val vaultUnlocked by viewModel.vaultUnlocked.collectAsState()\n")
        new_lines.append("                        if (!vaultUnlocked) {\n")
        new_lines.append("                            VaultTabLockedContent(\n")
        new_lines.append("                                viewModel = viewModel,\n")
        new_lines.append("                                onLockExit = { activeTab = ActiveTab.CALCULATOR }\n")
        new_lines.append("                            )\n")
        new_lines.append("                        } else {\n")
        new_lines.append("                            VaultTabUnlockedContent(\n")
        new_lines.append("                                viewModel = viewModel,\n")
        new_lines.append("                                onLockExit = { activeTab = ActiveTab.CALCULATOR }\n")
        new_lines.append("                            )\n")
        new_lines.append("                        }\n")
        new_lines.append("                    }\n")
        
        # skip lines until the next block
        i += 1
        while i < len(lines) and not "if (activeTab != ActiveTab.VAULT) {" in lines[i] and not "// Professional Bottom Navigation Bar" in lines[i]:
            i += 1
        
        # we need to append the } that closes the when block before the bottom bar
        new_lines.append("                }\n")
        new_lines.append("            }\n\n")
        
        new_lines.append(lines[i]) # this is the comment or if block
    else:
        new_lines.append(lines[i])
    i += 1

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.writelines(new_lines)
