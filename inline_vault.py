with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

new_lines = []
skip = False
for line in lines:
    if "ActiveTab.VAULT -> {" in line:
        new_lines.append(line)
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
        skip = True
        continue
    
    if skip:
        if "}" in line and "ActiveTab.VAULT" not in line:
            new_lines.append("                    }\n")
            skip = False
        continue

    new_lines.append(line)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.writelines(new_lines)
