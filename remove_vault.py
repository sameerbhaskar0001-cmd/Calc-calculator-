with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

new_lines = []
skip = False
for line in lines:
    if "@Composable" in line and skip == False:
        pass
    if "fun VaultTabContent(" in line:
        skip = True
        # remove the @Composable above it
        new_lines.pop()
        continue
    
    if skip:
        if "}" in line and len(line.strip()) == 1:
            skip = False
        continue

    new_lines.append(line)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.writelines(new_lines)
