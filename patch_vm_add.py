import re

vm_path = 'app/src/main/java/com/example/CalculatorViewModel.kt'
vm = open(vm_path).read()

# Let's see if there is any other 'addVaultFile' usage in CalculatorViewModel.kt itself
print("Usages in VM:", vm.count("addVaultFile"))
