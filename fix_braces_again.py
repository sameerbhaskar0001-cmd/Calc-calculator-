with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

for i, line in enumerate(lines):
    if line.strip() == "}":
        # check around 483
        pass

# I will just delete line 483 if it's just '}'
if lines[482].strip() == "}":
    print("Deleting line 483:", lines[482])
    del lines[482]

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.writelines(lines)
