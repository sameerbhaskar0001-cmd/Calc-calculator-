with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

lines.insert(5064, "                }\n")

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.writelines(lines)
