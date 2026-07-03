with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

depth = 0
for i in range(2684, 2720):
    line = lines[i]
    old_depth = depth
    depth += line.count("{")
    depth -= line.count("}")
    if depth != old_depth:
        print(f"Line {i+1}: {line.strip()} -> {depth}")
