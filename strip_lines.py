import re
vm_path = 'app/src/main/java/com/example/CalculatorViewModel.kt'
with open(vm_path, 'r') as f:
    lines = f.readlines()

start_idx = -1
end_idx = -1

for i, line in enumerate(lines):
    if "Failed to query path for uri" in line:
        start_idx = i - 2 # Go back a couple lines to catch the extra braces
    if "fun addVaultFile" in line:
        end_idx = i
        break

if start_idx != -1 and end_idx != -1:
    new_lines = lines[:start_idx] + lines[end_idx:]
    with open(vm_path, 'w') as f:
        f.writelines(new_lines)
    print("Stripped!")
else:
    print("Not found bounds!")
