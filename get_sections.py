with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

in_when = False
section_starts = []

for i in range(2978, 10086):
    line = lines[i]
    if line.strip().startswith('when (section) {'):
        in_when = True
        continue
    if in_when:
        if line.strip().startswith('"') and '-> {' in line:
            section_starts.append((line.strip(), i))

print("Sections:")
for s in section_starts:
    print(s)
