import re
vm_path = 'app/src/main/java/com/example/CalculatorViewModel.kt'
with open(vm_path, 'r') as f:
    vm = f.read()

old_cond = '} else if (uri.scheme == "content" && uri.authority?.startsWith("media") == false && path.isNotEmpty()) {'
new_cond = '} else if (uri.scheme == "content" && !uri.toString().contains("media/external") && path.isNotEmpty()) {'
vm = vm.replace(old_cond, new_cond)

old_add = 'if (resolvedUri.authority?.startsWith("media") == true) {'
new_add = 'if (resolvedUri.authority?.startsWith("media") == true && resolvedUri.toString().contains("external")) {'
vm = vm.replace(old_add, new_add)

with open(vm_path, 'w') as f:
    f.write(vm)
