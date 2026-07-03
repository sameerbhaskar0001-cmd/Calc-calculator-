with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

depth = 0
for i in range(2474, 7255):
    line = lines[i]
    depth += line.count("{")
    depth -= line.count("}")
    if depth < 2 and i > 2690:
        print(f"Depth dropped below 2 at line {i+1}: {line.strip()}")
        break
