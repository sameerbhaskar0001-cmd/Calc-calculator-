with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

depth = 5
for i in range(2924, 5642):
    line = lines[i]
    depth += line.count("{")
    depth -= line.count("}")
    if depth < 5:
        print(f"Depth dropped to {depth} at line {i+1}: {line.strip()}")
        break
