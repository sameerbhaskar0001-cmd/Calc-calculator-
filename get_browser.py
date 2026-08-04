with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

start = 3799
for i in range(start, 3850):
    if lines[i].strip() == "}" and "Private Browser" in lines[start-1]:
        pass
print("".join(lines[start:start+20]))
