with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

depth = 3
for i in range(2706, 5644):
    line = lines[i]
    depth += line.count("{")
    depth -= line.count("}")
    if depth < 3:
        print(f"Depth drops below 3 at line {i+1}: {line.strip()}")
        break
