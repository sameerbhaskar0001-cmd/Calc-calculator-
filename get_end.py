with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

for i in range(10050, 10065):
    print(i+1, repr(lines[i]))
