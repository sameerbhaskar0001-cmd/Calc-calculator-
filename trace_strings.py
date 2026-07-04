with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

in_str = False
for i in range(222, 500):
    text = lines[i]
    for j, c in enumerate(text):
        if c == '"':
            if j == 0 or text[j-1] != '\\':
                in_str = not in_str
    if in_str:
        print(f"{i+1}: string open!")
