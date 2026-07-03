with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

depth = 4
for i in range(2923, 5643):
    line = lines[i]
    depth += line.count("{")
    depth -= line.count("}")
    if depth < 4:
        print(f"Depth drops below 4 at line {i+1}: {line.strip()}")
        break
