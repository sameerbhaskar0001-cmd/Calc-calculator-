import re

vm_path = 'app/src/main/java/com/example/CalculatorViewModel.kt'
with open(vm_path, 'r') as f:
    vm = f.read()

old_block = """                if (pathsToScan.isNotEmpty()) {
                    android.media.MediaScannerConnection.scanFile(
                        context, 
                        pathsToScan.toTypedArray(), 
                        null 
                    ) { p, _ ->
                        android.util.Log.d("Vault", "Scanned $p after batch deletion")
                    }
                }"""
                
new_block = """                onOriginalFileDeleted(context)"""

vm = vm.replace(old_block, new_block)

with open(vm_path, 'w') as f:
    f.write(vm)

