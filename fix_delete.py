import re
vm_path = 'app/src/main/java/com/example/CalculatorViewModel.kt'
with open(vm_path, 'r') as f:
    vm = f.read()

batch_old = """    fun batchDeleteOriginalFiles(context: Context, uris: List<Uri>) {
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
                } else if (uri.scheme == "content" && !uri.toString().contains("media/external") && path.isNotEmpty()) {
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
                if (resolvedUri.authority?.startsWith("media") == true && resolvedUri.toString().contains("external")) {
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
    }"""

batch_new = """    fun batchDeleteOriginalFiles(context: Context, uris: List<Uri>) {
        if (uris.isEmpty()) return
        try {
            val contentResolver = context.contentResolver
            val mediaStoreUris = mutableListOf<Uri>()
            val urisToPersist = mutableListOf<String>()
            
            for (uri in uris) {
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
                        var fileName = ""
                        var fileSize = -1L
                        contentResolver.query(uri, null, null, null, null)?.use {
                            if (it.moveToFirst()) {
                                val nameIdx = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                                if (nameIdx != -1) fileName = it.getString(nameIdx) ?: ""
                                val sizeIdx = it.getColumnIndex(android.provider.OpenableColumns.SIZE)
                                if (sizeIdx != -1) fileSize = it.getLong(sizeIdx)
                            }
                        }
                        
                        if (fileName.isNotEmpty() && fileSize > 0) {
                            val mediaCursor = contentResolver.query(
                                android.provider.MediaStore.Files.getContentUri("external"),
                                arrayOf(android.provider.MediaStore.Files.FileColumns._ID),
                                "${android.provider.MediaStore.Files.FileColumns.DISPLAY_NAME} = ? AND ${android.provider.MediaStore.Files.FileColumns.SIZE} = ?",
                                arrayOf(fileName, fileSize.toString()),
                                null
                            )
                            mediaCursor?.use {
                                if (it.moveToFirst()) {
                                    val id = it.getLong(it.getColumnIndexOrThrow(android.provider.MediaStore.Files.FileColumns._ID))
                                    resolvedUri = android.content.ContentUris.withAppendedId(android.provider.MediaStore.Files.getContentUri("external"), id)
                                }
                            }
                        }
                    } catch (e: Exception) {}
                }
                
                if (resolvedUri.toString().contains("media/external")) {
                    mediaStoreUris.add(resolvedUri)
                    urisToPersist.add(resolvedUri.toString())
                } else {
                    android.util.Log.e("Vault", "Aborting batch delete: Could not resolve MediaStore URI for $uri")
                    return
                }
            }
            
            pendingDeleteOriginalPaths = urisToPersist
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
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
                if (allDeleted) {
                    onOriginalFileDeleted(context)
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("Vault", "Batch delete exception", e)
        }
    }"""

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
                    if (resolvedUri.authority?.startsWith("media") == true) {
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

if batch_old in vm:
    vm = vm.replace(batch_old, batch_new)
else:
    print("WARNING: batch_old not found!")

if single_old in vm:
    vm = vm.replace(single_old, single_new)
else:
    print("WARNING: single_old not found!")

with open(vm_path, 'w') as f:
    f.write(vm)
print("Done replacing.")
