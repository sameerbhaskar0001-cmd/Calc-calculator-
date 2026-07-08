import re

vm_path = 'app/src/main/java/com/example/CalculatorViewModel.kt'
with open(vm_path, 'r') as f:
    vm = f.read()

start_marker = "            // Secure Deletion of Original Media File from System Gallery"
end_marker = "            true\n        } catch (e: Exception) {"

if start_marker in vm and end_marker in vm:
    idx_start = vm.find(start_marker)
    idx_end = vm.find(end_marker, idx_start)
    
    new_code = """            // 4. Resolve the ORIGINAL MediaStore URI
            var resolvedUri = uri
            if (originalPath.isNotEmpty() && !skipDelete) {
                try {
                    val mediaCursor = contentResolver.query(
                        android.provider.MediaStore.Files.getContentUri("external"),
                        arrayOf(android.provider.MediaStore.Files.FileColumns._ID),
                        "${android.provider.MediaStore.Files.FileColumns.DATA}=?",
                        arrayOf(originalPath),
                        null
                    )
                    mediaCursor?.use {
                        if (it.moveToFirst()) {
                            val mediaId = it.getLong(it.getColumnIndexOrThrow(android.provider.MediaStore.Files.FileColumns._ID))
                            resolvedUri = android.content.ContentUris.withAppendedId(android.provider.MediaStore.Files.getContentUri("external"), mediaId)
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.e("Vault", "Failed to resolve MediaStore URI", e)
                }
            }

            // 5. Delete ONLY the original media from MediaStore.
            try {
                if (skipDelete) {
                    // Skip deletion for batch processing
                } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    android.util.Log.d("Vault", "Delete request created for $resolvedUri")
                    val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, listOf(resolvedUri))
                    pendingDeleteOriginalPaths = if (originalPath.isNotEmpty()) listOf(originalPath) else emptyList()
                    _pendingDeleteSender.value = pendingIntent.intentSender
                } else {
                    android.util.Log.d("Vault", "Deleting original file directly (API < 30)")
                    val deletedRows = contentResolver.delete(resolvedUri, null, null)
                    if (deletedRows > 0) {
                        android.util.Log.d("Vault", "Delete success/failure: Delete success")
                        pendingDeleteOriginalPaths = if (originalPath.isNotEmpty()) listOf(originalPath) else emptyList()
                        onOriginalFileDeleted(context)
                    } else {
                        android.util.Log.e("Vault", "Delete success/failure: Delete failure")
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("Vault", "Any exception with full stack trace", e)
            }
"""
    
    vm = vm[:idx_start] + new_code + vm[idx_end:]
    
    with open(vm_path, 'w') as f:
        f.write(vm)
    print("Replaced successfully!")
else:
    print("Markers not found!")

