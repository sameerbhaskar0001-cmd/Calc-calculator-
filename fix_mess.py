vm_path = 'app/src/main/java/com/example/CalculatorViewModel.kt'
with open(vm_path, 'r') as f:
    lines = f.readlines()

start_idx = -1
for i, line in enumerate(lines):
    if "fun batchDeleteOriginalFiles(context: Context, uris: List<Uri>)" in line:
        start_idx = i
        break

# The function addVaultFile comes right after batchDeleteOriginalFiles
end_idx = -1
for i in range(start_idx + 1, len(lines)):
    if "fun addVaultFile(context: Context" in line or "fun addVaultFile(" in lines[i]:
        end_idx = i
        break

if start_idx != -1 and end_idx != -1:
    print(f"batchDeleteOriginalFiles is from {start_idx} to {end_idx}")
    
    new_batch = """    fun batchDeleteOriginalFiles(context: Context, uris: List<Uri>) {
        android.util.Log.d("Vault", "batchDeleteOriginalFiles called with ${uris.size} uris")
        if (uris.isEmpty()) return
        try {
            val contentResolver = context.contentResolver
            val mediaStoreUris = mutableListOf<Uri>()
            val urisToPersist = mutableListOf<String>()
            
            for (uri in uris) {
                var resolvedUri = uri
                var path = ""
                try {
                    contentResolver.query(uri, null, null, null, null)?.use {
                        if (it.moveToFirst()) {
                            val dataIdx = it.getColumnIndex(android.provider.MediaStore.MediaColumns.DATA)
                            if (dataIdx != -1) path = it.getString(dataIdx) ?: ""
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.e("Vault", "Failed to query path for uri: $uri", e)
                }

                if (android.provider.DocumentsContract.isDocumentUri(context, uri) && uri.authority == "com.android.providers.media.documents") {
                    try {
                        val docId = android.provider.DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":")
                        if (split.size >= 2) {
                            val type = split[0]
                            val id = split[1].toLongOrNull()
                            if (id != null) {
                                val baseUri = when (type) {
                                    "image" -> android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    "video" -> android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                                    "audio" -> android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                                    else -> android.provider.MediaStore.Files.getContentUri("external")
                                }
                                resolvedUri = android.content.ContentUris.withAppendedId(baseUri, id)
                            }
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("Vault", "Failed to resolve SAF URI in batch", e)
                    }
                } else if (uri.scheme == "content" && uri.authority?.startsWith("media") == false && path.isNotEmpty()) {
                    try {
                        val mediaCursor = contentResolver.query(
                            android.provider.MediaStore.Files.getContentUri("external"),
                            arrayOf(android.provider.MediaStore.Files.FileColumns._ID),
                            "${android.provider.MediaStore.Files.FileColumns.DATA} = ?",
                            arrayOf(path),
                            null
                        )
                        mediaCursor?.use {
                            if (it.moveToFirst()) {
                                val id = it.getLong(it.getColumnIndexOrThrow(android.provider.MediaStore.Files.FileColumns._ID))
                                resolvedUri = android.content.ContentUris.withAppendedId(android.provider.MediaStore.Files.getContentUri("external"), id)
                            }
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("Vault", "Failed to resolve MediaStore URI for path: $path", e)
                    }
                }
                
                // ONLY add to mediaStoreUris if it's a valid media store URI
                if (resolvedUri.authority?.startsWith("media") == true) {
                    mediaStoreUris.add(resolvedUri)
                } else {
                    android.util.Log.w("Vault", "Cannot delete non-media URI: $resolvedUri")
                }
                urisToPersist.add(resolvedUri.toString())
            }
            
            pendingDeleteOriginalPaths = urisToPersist
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                if (mediaStoreUris.isNotEmpty()) {
                    try {
                        val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, mediaStoreUris)
                        _pendingDeleteSender.value = pendingIntent.intentSender
                    } catch (e: Exception) {
                        android.util.Log.e("Vault", "createDeleteRequest failed", e)
                        // If delete fails, still commit the vault files!
                        onOriginalFileDeleted(context)
                    }
                } else {
                    // Nothing to delete, just commit
                    onOriginalFileDeleted(context)
                }
            } else {
                for (uri in mediaStoreUris) {
                    try {
                        contentResolver.delete(uri, null, null)
                    } catch (e: Exception) {
                        android.util.Log.e("Vault", "Failed to delete uri: $uri", e)
                    }
                }
                onOriginalFileDeleted(context)
            }
        } catch (e: Exception) {
            android.util.Log.e("Vault", "Batch delete exception", e)
            // Ensure we commit the files even if delete crashes
            onOriginalFileDeleted(context)
        }
    }
"""
    
    new_lines = lines[:start_idx] + [new_batch] + lines[end_idx:]
    with open(vm_path, 'w') as f:
        f.writelines(new_lines)
    print("Fixed!")
else:
    print(f"Could not find bounds: {start_idx} {end_idx}")

