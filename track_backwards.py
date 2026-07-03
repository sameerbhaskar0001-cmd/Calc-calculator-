with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

depth = 0
for i in range(5644, 2688, -1):
    line = lines[i]
    depth += line.count("}")
    depth -= line.count("{")
    if depth == -1:
        print(f"Opening brace for else is at line {i+1}: {line.strip()}")
        break
