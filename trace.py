with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

stack = []
for i in range(220, 600):
    line = lines[i]
    for char in line:
        if char == '{':
            stack.append(i + 1)
        elif char == '}':
            if stack:
                popped = stack.pop()
                if popped == 227:
                    print(f"CalculatorScreen closed at line {i+1}")
