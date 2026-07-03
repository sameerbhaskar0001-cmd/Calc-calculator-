with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    for i, line in enumerate(f):
        if "FloatingActionButton" in line:
            print(f"{i+1}: {line.strip()}")
