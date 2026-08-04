with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

brace = 0
for i in range(14275, len(lines)):
    line = lines[i]
    brace += line.count('{')
    brace -= line.count('}')
print("Brace at end of file:", brace)
