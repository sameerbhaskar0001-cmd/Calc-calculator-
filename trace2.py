with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

stack = []
for i in range(220, 485):
    line = lines[i]
    for char in line:
        if char == '{':
            stack.append(i + 1)
        elif char == '}':
            if stack:
                stack.pop()
print(f"Stack size at 485 is {len(stack)}")
for l in stack:
    print(f"Opened at {l}: {lines[l-1].strip()}")
