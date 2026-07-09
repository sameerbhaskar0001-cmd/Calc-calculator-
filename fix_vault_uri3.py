import re

vm_path = 'app/src/main/java/com/example/CalculatorViewModel.kt'
with open(vm_path, 'r') as f:
    vm = f.read()

old_scan = """        // 6. Trigger MediaScannerConnection.scanFile()
        if (pendingDeleteOriginalPaths.isNotEmpty()) {
            android.media.MediaScannerConnection.scanFile(
                context, 
                pendingDeleteOriginalPaths.toTypedArray(), 
                null 
            ) { path, _ ->
                android.util.Log.d("Vault", "Scanned $path after deletion")
            }
            pendingDeleteOriginalPaths = emptyList()
        }"""
        
new_scan = """        // MediaStore is already updated via createDeleteRequest or contentResolver.delete
        pendingDeleteOriginalPaths = emptyList()"""

vm = vm.replace(old_scan, new_scan)

with open(vm_path, 'w') as f:
    f.write(vm)
