with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

in_prot = False
brace = 0
start = -1
end = -1
for i, line in enumerate(lines):
    if '"Protection" -> {' in line:
        in_prot = True
        start = i
        brace = 1
        continue
    if in_prot:
        brace += line.count('{')
        brace -= line.count('}')
        if brace == 0:
            end = i
            break

print("Start:", start)
print("End:", end)
