with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

# delete line 13731
del lines[13730] # 0-indexed

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.writelines(lines)
