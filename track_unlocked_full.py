with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

depth = 0
for i in range(2474, 7255):
    line = lines[i]
    old = depth
    depth += line.count("{")
    depth -= line.count("}")
    if depth != old:
        if i > 7240:
            print(f"Line {i+1}: {line.strip()} -> {depth}")
        if depth < 0:
            print(f"Negative depth at line {i+1}: {line.strip()}")
            break
