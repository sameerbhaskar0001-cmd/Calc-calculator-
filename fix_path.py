vm_path = 'app/src/main/java/com/example/CalculatorViewModel.kt'
with open(vm_path, 'r') as f:
    vm = f.read()

vm = vm.replace('            var originalPath = ""\n            \n            var originalPath = ""', '            var originalPath = ""')

with open(vm_path, 'w') as f:
    f.write(vm)
