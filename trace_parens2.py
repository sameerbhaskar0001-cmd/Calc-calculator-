with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

def get_balance(text, open_char, close_char):
    b = 0
    in_str = False
    for i, c in enumerate(text):
        if c == '"':
            if i == 0 or text[i-1] != '\\':
                in_str = not in_str
        if not in_str:
            if c == open_char: b += 1
            if c == close_char: b -= 1
    return b

paren = 0
for i in range(222, 485):
    old_paren = paren
    paren += get_balance(lines[i], '(', ')')
    if paren != old_paren:
        print(f"{i+1}: paren changed {old_paren} -> {paren}")
