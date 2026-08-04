with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

for i, line in enumerate(lines[2164:10086]):
    if "AnimatedContent" in line:
        print(2164 + i, line.strip())
