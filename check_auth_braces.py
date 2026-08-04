with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

brace = 0
for i in range(13726, 14274):
    line = lines[i]
    brace += line.count('{')
    brace -= line.count('}')
print("Brace at end of Auth Section:", brace)
