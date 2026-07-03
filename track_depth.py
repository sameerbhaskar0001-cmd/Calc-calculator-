with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

depth = 0
for i in range(2474, 7250):
    line = lines[i]
    depth += line.count("{")
    depth -= line.count("}")
    if depth == 0 and i > 2500:
        print(f"Depth hits 0 at line {i+1}: {line.strip()}")
