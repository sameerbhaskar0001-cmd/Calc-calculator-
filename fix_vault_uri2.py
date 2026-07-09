import re

vm_path = 'app/src/main/java/com/example/CalculatorViewModel.kt'
with open(vm_path, 'r') as f:
    vm = f.read()

# Replace the originalPath query block in addVaultFile
old_path_query = """            // Obtain original path from original MediaStore Uri (to refresh gallery after deletion) if not already obtained
            if (originalPath.isEmpty()) {
                try {
                    val projection = arrayOf(android.provider.MediaStore.MediaColumns.DATA)
                    contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            val dataIdx = cursor.getColumnIndex(android.provider.MediaStore.MediaColumns.DATA)
                            if (dataIdx != -1) {
                                originalPath = cursor.getString(dataIdx) ?: ""
                            }
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.e("Vault", "Any exception with full stack trace", e)
                }
            }"""

new_path_query = """            // We persist the original MediaStore URI and use it directly instead of relying on DATA column path"""

vm = vm.replace(old_path_query, new_path_query)

# Replace the resolvedUri block in addVaultFile
old_resolve = """            // 4. Resolve the ORIGINAL MediaStore URI without relying solely on DATA column
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
            } else if (uri.scheme == "content" && uri.authority?.startsWith("media") == false) {
                // Try to resolve using DATA as fallback for other content providers if absolutely needed, but prefer the direct uri
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
                        android.util.Log.e("Vault", "Failed to resolve MediaStore URI fallback", e)
                    }
                }
            }"""

new_resolve = """            // 4. Resolve the ORIGINAL MediaStore URI safely
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
            val uriToPersist = resolvedUri.toString()"""

vm = vm.replace(old_resolve, new_resolve)


old_delete_request = """            // 5. Delete ONLY the original media from MediaStore.
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

new_delete_request = """            // 5. Delete ONLY the original media from MediaStore.
            try {
                if (skipDelete) {
                    // Skip deletion for batch processing
                } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    android.util.Log.d("Vault", "Delete request created for $resolvedUri")
                    val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, listOf(resolvedUri))
                    pendingDeleteOriginalPaths = listOf(uriToPersist)
                    _pendingDeleteSender.value = pendingIntent.intentSender
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
            
vm = vm.replace(old_delete_request, new_delete_request)


# For batchDeleteOriginalFiles: completely rewrite it to NOT use DATA
old_batch = """        fun batchDeleteOriginalFiles(context: Context, uris: List<Uri>) {
        if (uris.isEmpty()) return
        try {
            val contentResolver = context.contentResolver
            val mediaStoreUris = mutableListOf<Uri>()
            val pathsToScan = mutableListOf<String>()
            for (uri in uris) {
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
                
                if (path.isNotEmpty()) {
                    pathsToScan.add(path)
                }
                
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
                mediaStoreUris.add(resolvedUri)
            }
            
            val targetUris = if (mediaStoreUris.isNotEmpty()) mediaStoreUris else uris
            pendingDeleteOriginalPaths = pathsToScan
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                if (targetUris.isNotEmpty()) {
                    val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, targetUris)
                    _pendingDeleteSender.value = pendingIntent.intentSender
                }
            } else {
                for (uri in targetUris) {
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
        }
    }"""

new_batch = """        fun batchDeleteOriginalFiles(context: Context, uris: List<Uri>) {
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
                    } catch (e: Exception) {
                        android.util.Log.e("Vault", "Failed to resolve SAF URI in batch", e)
                    }
                }
                mediaStoreUris.add(resolvedUri)
                urisToPersist.add(resolvedUri.toString())
            }
            
            pendingDeleteOriginalPaths = urisToPersist
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                if (mediaStoreUris.isNotEmpty()) {
                    val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, mediaStoreUris)
                    _pendingDeleteSender.value = pendingIntent.intentSender
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
        }
    }"""
    
vm = vm.replace(old_batch, new_batch)


with open(vm_path, 'w') as f:
    f.write(vm)
