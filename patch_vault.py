import re

with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'r') as f:
    vm = f.read()

# Fix batchDeleteOriginalFiles: replace "return" with "continue" in the error branch
old_batch_abort = """                if (resolvedUri.toString().contains("media/external")) {
                    mediaStoreUris.add(resolvedUri)
                    urisToPersist.add(resolvedUri.toString())
                } else {
                    android.util.Log.e("Vault", "Aborting batch delete: Could not resolve MediaStore URI for $uri")
                    return
                }"""

new_batch_abort = """                if (resolvedUri.toString().contains("media/external")) {
                    mediaStoreUris.add(resolvedUri)
                    urisToPersist.add(resolvedUri.toString())
                } else {
                    android.util.Log.e("Vault", "Skipping in batch delete: Could not resolve MediaStore URI for $uri")
                    continue
                }"""

if old_batch_abort in vm:
    vm = vm.replace(old_batch_abort, new_batch_abort)
else:
    print("Warning: old_batch_abort not found")

# Fix addVaultFile: move skipDelete after the resolution check
old_add_abort = """            if (skipDelete) {
                return true
            }
            
            if (!resolvedUri.toString().contains("media/external")) {
                android.util.Log.e("Vault", "Aborting single delete: Could not resolve MediaStore URI for $uri")
                destFile.delete()
                stagedVaultFiles.remove(fileSerialized)
                return false
            }"""

new_add_abort = """            if (!resolvedUri.toString().contains("media/external")) {
                android.util.Log.e("Vault", "Aborting single delete: Could not resolve MediaStore URI for $uri")
                destFile.delete()
                stagedVaultFiles.remove(fileSerialized)
                return false
            }

            if (skipDelete) {
                return true
            }"""

if old_add_abort in vm:
    vm = vm.replace(old_add_abort, new_add_abort)
else:
    print("Warning: old_add_abort not found")

with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'w') as f:
    f.write(vm)
print("Patch applied successfully")
