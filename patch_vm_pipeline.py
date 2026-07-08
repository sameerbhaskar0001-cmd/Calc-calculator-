import re

vm_path = 'app/src/main/java/com/example/CalculatorViewModel.kt'
with open(vm_path, 'r') as f:
    vm = f.read()

# 1. Add stagedVaultFiles
if 'var stagedVaultFiles = mutableListOf<String>()' not in vm:
    vm = vm.replace('var pendingDeleteOriginalPaths: List<String> = emptyList()', 'var pendingDeleteOriginalPaths: List<String> = emptyList()\n    var stagedVaultFiles = mutableListOf<String>()')

# 2. In addVaultFile, resolve original MediaStore URI before delete
# Find the block where it does deletion in addVaultFile
old_delete_block = """            // Secure Deletion of Original Media File from System Gallery
            try {
                android.util.Log.d("Vault", "Original MediaStore Uri: $uri")
                if (skipDelete) {
                    // Skip deletion for batch processing
                } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    android.util.Log.d("Vault", "Delete request created")
                    val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, listOf(uri))
                    pendingDeleteOriginalPaths = if (originalPath.isNotEmpty()) listOf(originalPath) else emptyList()
                    _pendingDeleteSender.value = pendingIntent.intentSender
                } else {
                    android.util.Log.d("Vault", "Deleting original file directly (API < 30)")
                    val deletedRows = contentResolver.delete(uri, null, null)
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
            }"""

new_delete_block = """            // 4. Resolve the ORIGINAL MediaStore URI
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
            }"""
vm = vm.replace(old_delete_block, new_delete_block)

# 3. Change Vault UI refresh to stage the file instead
old_refresh = """            val fileSerialized = "$id|||$timestamp|||$originalName|||$mimeType|||${destFile.absolutePath}|||$readableSize|||$durationMs"
            val updatedFiles = _vaultFiles.value + fileSerialized
            _vaultFiles.value = updatedFiles.sortedByDescending { it }
            
            val isDecoy = _decoyActive.value
            val filesKey = if (isDecoy) "decoy_files" else "vault_files"
            prefs.edit().putStringSet(filesKey, updatedFiles.toSet()).apply()"""

new_refresh = """            val fileSerialized = "$id|||$timestamp|||$originalName|||$mimeType|||${destFile.absolutePath}|||$readableSize|||$durationMs"
            stagedVaultFiles.add(fileSerialized)
            if (!skipDelete && android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.R) {
                // If direct delete (API < 30) was used and skipped sender, the onOriginalFileDeleted handles refresh.
                // Wait, in old_delete_block, if it deleted rows, it called onOriginalFileDeleted(context) which will flush stagedFiles.
            }"""
vm = vm.replace(old_refresh, new_refresh)

# 4. In onOriginalFileDeleted, flush stagedVaultFiles
old_on_del = """    fun onOriginalFileDeleted(context: android.content.Context) {
        if (pendingDeleteOriginalPaths.isNotEmpty()) {
            android.media.MediaScannerConnection.scanFile(
                context, 
                pendingDeleteOriginalPaths.toTypedArray(), 
                null 
            ) { path, _ ->
                android.util.Log.d("Vault", "Scanned $path after deletion")
            }
            pendingDeleteOriginalPaths = emptyList()
        }
    }"""
new_on_del = """    fun onOriginalFileDeleted(context: android.content.Context) {
        // 6. Trigger MediaScannerConnection.scanFile()
        if (pendingDeleteOriginalPaths.isNotEmpty()) {
            android.media.MediaScannerConnection.scanFile(
                context, 
                pendingDeleteOriginalPaths.toTypedArray(), 
                null 
            ) { path, _ ->
                android.util.Log.d("Vault", "Scanned $path after deletion")
            }
            pendingDeleteOriginalPaths = emptyList()
        }
        
        // 7. Refresh the Vault UI.
        if (stagedVaultFiles.isNotEmpty()) {
            val updatedFiles = _vaultFiles.value + stagedVaultFiles
            _vaultFiles.value = updatedFiles.sortedByDescending { it }
            val isDecoy = _decoyActive.value
            val filesKey = if (isDecoy) "decoy_files" else "vault_files"
            prefs.edit().putStringSet(filesKey, _vaultFiles.value.toSet()).apply()
            stagedVaultFiles.clear()
        }
    }"""
vm = vm.replace(old_on_del, new_on_del)

# 5. In clearPendingDelete, also flush if needed? 
# "Never delete before the Vault copy is verified." (We did verify, it's copied).
# If deletion fails or user denies, should it stay in Vault?
# Usually yes, but the user said EXACT order: delete -> refresh UI. If delete fails, do we refresh?
old_clear = """    fun clearPendingDelete() {
        _pendingDeleteSender.value = null
        pendingDeleteOriginalPaths = emptyList()
    }"""
new_clear = """    fun clearPendingDelete() {
        _pendingDeleteSender.value = null
        pendingDeleteOriginalPaths = emptyList()
        // If user denied deletion, we might still want to show the file in the vault, or rollback.
        // Let's just show it.
        if (stagedVaultFiles.isNotEmpty()) {
            val updatedFiles = _vaultFiles.value + stagedVaultFiles
            _vaultFiles.value = updatedFiles.sortedByDescending { it }
            val isDecoy = _decoyActive.value
            val filesKey = if (isDecoy) "decoy_files" else "vault_files"
            prefs.edit().putStringSet(filesKey, _vaultFiles.value.toSet()).apply()
            stagedVaultFiles.clear()
        }
    }"""
vm = vm.replace(old_clear, new_clear)

with open(vm_path, 'w') as f:
    f.write(vm)

