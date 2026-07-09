import re

with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'r') as f:
    code = f.read()

# Fix batchDeleteOriginalFiles
old_batch = """            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, mediaStoreUris)
                _pendingDeleteSender.value = pendingIntent.intentSender
            } else {
                var allDeleted = true
                for (uri in mediaStoreUris) {
                    try {
                        val deletedRows = contentResolver.delete(uri, null, null)
                        if (deletedRows <= 0) allDeleted = false
                    } catch (e: Exception) {
                        allDeleted = false
                    }
                }
                onOriginalFileDeleted(context)
            }"""

new_batch = """            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, mediaStoreUris)
                _pendingDeleteSender.value = pendingIntent.intentSender
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                for (uri in mediaStoreUris) {
                    try {
                        contentResolver.delete(uri, null, null)
                    } catch (securityException: SecurityException) {
                        val recoverableSecurityException = securityException as? android.app.RecoverableSecurityException
                            ?: continue
                        _pendingDeleteSender.value = recoverableSecurityException.userAction.actionIntent.intentSender
                        return // Just prompt for the first one that fails, ideally.
                    } catch (e: Exception) {
                    }
                }
                onOriginalFileDeleted(context)
            } else {
                for (uri in mediaStoreUris) {
                    try {
                        contentResolver.delete(uri, null, null)
                    } catch (e: Exception) {}
                }
                onOriginalFileDeleted(context)
            }"""

if old_batch in code:
    code = code.replace(old_batch, new_batch)
    print("Replaced batch delete")
else:
    print("Failed to replace batch delete")

# Fix addVaultFile
old_add = """            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, listOf(resolvedUri))
                pendingDeleteOriginalPaths = listOf(uriToPersist)
                _pendingDeleteSender.value = pendingIntent.intentSender
            } else {
                contentResolver.delete(resolvedUri, null, null)
                pendingDeleteOriginalPaths = listOf(uriToPersist)
                onOriginalFileDeleted(context)
            }"""

new_add = """            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, listOf(resolvedUri))
                pendingDeleteOriginalPaths = listOf(uriToPersist)
                _pendingDeleteSender.value = pendingIntent.intentSender
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                try {
                    contentResolver.delete(resolvedUri, null, null)
                    pendingDeleteOriginalPaths = listOf(uriToPersist)
                    onOriginalFileDeleted(context)
                } catch (securityException: SecurityException) {
                    val recoverableSecurityException = securityException as? android.app.RecoverableSecurityException
                        ?: throw securityException
                    pendingDeleteOriginalPaths = listOf(uriToPersist)
                    _pendingDeleteSender.value = recoverableSecurityException.userAction.actionIntent.intentSender
                }
            } else {
                contentResolver.delete(resolvedUri, null, null)
                pendingDeleteOriginalPaths = listOf(uriToPersist)
                onOriginalFileDeleted(context)
            }"""

if old_add in code:
    code = code.replace(old_add, new_add)
    print("Replaced add vault file")
else:
    print("Failed to replace add vault file")

with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'w') as f:
    f.write(code)

