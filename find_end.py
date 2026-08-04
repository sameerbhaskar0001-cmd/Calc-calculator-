with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

for i, line in enumerate(lines[2164:]):
    if line.startswith("}"):
        print(2164 + i)
        break
