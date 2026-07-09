import re

vm_path = 'app/src/main/java/com/example/CalculatorViewModel.kt'
with open(vm_path, 'r') as f:
    vm = f.read()

# Replace the resolvedUri block in addVaultFile
old_resolve = """            // 4. Resolve the ORIGINAL MediaStore URI
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
            }"""

new_resolve = """            // 4. Resolve the ORIGINAL MediaStore URI without relying solely on DATA column
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
            
vm = vm.replace(old_resolve, new_resolve)

# Let's do the same for batchDeleteOriginalFiles
old_batch = """            for (uri in uris) {
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
                                val realUri = android.content.ContentUris.withAppendedId(android.provider.MediaStore.Files.getContentUri("external"), id)
                                mediaStoreUris.add(realUri)
                            } else {
                                mediaStoreUris.add(uri)
                            }
                        } ?: mediaStoreUris.add(uri)
                    } catch (e: Exception) {
                        android.util.Log.e("Vault", "Failed to resolve MediaStore URI for path: $path", e)
                        mediaStoreUris.add(uri)
                    }
                } else {
                    // Fallback
                    mediaStoreUris.add(uri)
                }
            }"""
            
new_batch = """            for (uri in uris) {
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
            }"""

vm = vm.replace(old_batch, new_batch)

with open(vm_path, 'w') as f:
    f.write(vm)
