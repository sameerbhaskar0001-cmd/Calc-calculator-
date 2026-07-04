with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

def get_balance(text):
    b = 0
    in_str = False
    for i, c in enumerate(text):
        if c == '"':
            if i == 0 or text[i-1] != '\\':
                in_str = not in_str
        if not in_str:
            if c == '{': b += 1
            if c == '}': b -= 1
    return b

total_balance = 0
for i in range(223, 485):
    b = get_balance(lines[i])
    total_balance += b
    print(f"{i+1}: {total_balance} -> {lines[i].strip()}")

print(f"Total balance at 485: {total_balance}")
