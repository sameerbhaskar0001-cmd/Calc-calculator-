import re
vm_path = 'app/src/main/java/com/example/CalculatorViewModel.kt'
with open(vm_path, 'r') as f:
    vm = f.read()

single_old = """            // 4. Resolve the ORIGINAL MediaStore URI safely
            var resolvedUri = uri
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
                    android.util.Log.e("Vault", "Failed to resolve SAF URI", e)
                }
            }

            // Persist the resolved URI instead of DATA path
            val uriToPersist = resolvedUri.toString()

            // 5. Delete ONLY the original media from MediaStore.
            try {
                if (skipDelete) {
                    // Skip deletion for batch processing
                } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    if (resolvedUri.authority?.startsWith("media") == true && resolvedUri.toString().contains("external")) {
                        android.util.Log.d("Vault", "Delete request created for $resolvedUri")
                        val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, listOf(resolvedUri))
                        pendingDeleteOriginalPaths = listOf(uriToPersist)
                        _pendingDeleteSender.value = pendingIntent.intentSender
                    } else {
                        android.util.Log.w("Vault", "Cannot delete non-media URI: $resolvedUri")
                        onOriginalFileDeleted(context)
                    }
                } else {
                    android.util.Log.d("Vault", "Deleting original file directly (API < 30)")
                    val deletedRows = contentResolver.delete(resolvedUri, null, null)
                    if (deletedRows > 0) {
                        android.util.Log.d("Vault", "Delete success/failure: Delete success")
                        pendingDeleteOriginalPaths = listOf(uriToPersist)
                        onOriginalFileDeleted(context)
                    } else {
                        android.util.Log.e("Vault", "Delete success/failure: Delete failure")
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("Vault", "Any exception with full stack trace", e)
            }"""

single_new = """            // 4. Resolve the ORIGINAL MediaStore URI safely
            var resolvedUri = uri
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
                } catch (e: Exception) {}
            }
            
            if (!resolvedUri.toString().contains("media/external")) {
                try {
                    val mediaCursor = contentResolver.query(
                        android.provider.MediaStore.Files.getContentUri("external"),
                        arrayOf(android.provider.MediaStore.Files.FileColumns._ID),
                        "${android.provider.MediaStore.Files.FileColumns.DISPLAY_NAME} = ? AND ${android.provider.MediaStore.Files.FileColumns.SIZE} = ?",
                        arrayOf(originalName, size.toString()),
                        null
                    )
                    mediaCursor?.use {
                        if (it.moveToFirst()) {
                            val id = it.getLong(it.getColumnIndexOrThrow(android.provider.MediaStore.Files.FileColumns._ID))
                            resolvedUri = android.content.ContentUris.withAppendedId(android.provider.MediaStore.Files.getContentUri("external"), id)
                        }
                    }
                } catch (e: Exception) {}
            }

            // Persist the resolved URI instead of DATA path
            val uriToPersist = resolvedUri.toString()

            // 5. Delete ONLY the original media from MediaStore.
            try {
                if (skipDelete) {
                    // Skip deletion for batch processing
                } else if (!resolvedUri.toString().contains("media/external")) {
                    android.util.Log.e("Vault", "Aborting single delete: Could not resolve MediaStore URI for $uri")
                    return false
                } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, listOf(resolvedUri))
                    pendingDeleteOriginalPaths = listOf(uriToPersist)
                    _pendingDeleteSender.value = pendingIntent.intentSender
                } else {
                    val deletedRows = contentResolver.delete(resolvedUri, null, null)
                    if (deletedRows > 0) {
                        pendingDeleteOriginalPaths = listOf(uriToPersist)
                        onOriginalFileDeleted(context)
                    } else {
                        android.util.Log.e("Vault", "Delete failure")
                        return false
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("Vault", "Exception", e)
                return false
            }"""

if single_old in vm:
    vm = vm.replace(single_old, single_new)
else:
    print("WARNING: single_old not found!")

with open(vm_path, 'w') as f:
    f.write(vm)
print("Done replacing single.")
