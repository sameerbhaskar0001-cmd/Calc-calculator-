with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

for i, line in enumerate(lines):
    if "VaultTabContent(" in line and "fun " not in line:
        if "modifier =" not in lines[i+2] and "modifier =" not in lines[i+3]:
            # we need to inject modifier = Modifier
            lines[i+2] = lines[i+2].rstrip() + ",\n"
            lines.insert(i+3, "                            modifier = Modifier\n")
            break

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.writelines(lines)
