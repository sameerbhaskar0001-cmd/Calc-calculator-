with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

brace = 0
for i in range(4938, 5066):
    line = lines[i]
    brace += line.count('{')
    brace -= line.count('}')
print("Remaining braces at 5065:", brace)
