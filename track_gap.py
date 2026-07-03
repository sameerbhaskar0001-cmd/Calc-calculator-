with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

depth = 3
for i in range(2706, 2923):
    line = lines[i]
    depth += line.count("{")
    depth -= line.count("}")
    if depth < 3:
        print(f"Gap depth dropped to {depth} at line {i+1}: {line.strip()}")
        break
print(f"Gap final depth at 2923: {depth}")
