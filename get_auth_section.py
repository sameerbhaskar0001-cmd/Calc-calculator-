with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

start = 5065
end = 5607

print("".join(lines[start:start+20]))
