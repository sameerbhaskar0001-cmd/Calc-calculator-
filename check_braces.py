with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

depth = 0
for i, line in enumerate(lines):
    depth += line.count("{")
    depth -= line.count("}")
    if depth < 0:
        print(f"Negative depth at line {i+1}: {line.strip()}")
        break

print(f"Final depth: {depth}")
