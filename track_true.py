with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

depth = 0
for i in range(2684, 7250):
    line = lines[i]
    depth += line.count("{")
    depth -= line.count("}")
    if i == 5644:
        print(f"Depth at 5645: {depth}")
