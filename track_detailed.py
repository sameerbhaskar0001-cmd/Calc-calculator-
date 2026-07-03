with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

depth = 0
for i in range(2684, 5650):
    line = lines[i]
    depth += line.count("{")
    depth -= line.count("}")
    if 5640 <= i < 5650:
        print(f"Line {i+1} [depth={depth}]: {line.strip()}")
