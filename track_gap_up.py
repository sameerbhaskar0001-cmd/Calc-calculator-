with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

depth = 3
for i in range(2706, 2923):
    line = lines[i]
    old = depth
    depth += line.count("{")
    depth -= line.count("}")
    if depth != old:
        print(f"Line {i+1}: {line.strip()} -> {depth}")
