vm_path = 'app/src/main/java/com/example/CalculatorViewModel.kt'
with open(vm_path, 'r') as f:
    lines = f.readlines()

count = 0
in_class = False
for i, line in enumerate(lines):
    if "class CalculatorViewModel" in line:
        in_class = True
    if in_class:
        count += line.count('{') - line.count('}')
        if count == 0 and i > 50:
            print(f"Class closed at line {i+1}:\n{line}")
            break
