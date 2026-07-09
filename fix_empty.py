import re

with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'r') as f:
    vm = f.read()

old_end = """            pendingDeleteOriginalPaths = urisToPersist
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, mediaStoreUris)
                _pendingDeleteSender.value = pendingIntent.intentSender
            } else {"""

new_end = """            if (mediaStoreUris.isEmpty()) return
            pendingDeleteOriginalPaths = urisToPersist
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, mediaStoreUris)
                _pendingDeleteSender.value = pendingIntent.intentSender
            } else {"""

if old_end in vm:
    vm = vm.replace(old_end, new_end)
    print("Replaced")
else:
    print("Not found")

with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'w') as f:
    f.write(vm)
