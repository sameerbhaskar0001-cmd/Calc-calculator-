with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

for i in range(4930, 4950):
    print(i+1, repr(lines[i]))
