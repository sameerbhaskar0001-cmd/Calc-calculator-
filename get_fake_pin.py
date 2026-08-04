with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

start = 3060 - 1
print("".join(lines[start:start+20]))
