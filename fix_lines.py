with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

if lines[0].startswith("import") and lines[1].startswith("package"):
    lines[0], lines[1] = lines[1], lines[0]

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.writelines(lines)
