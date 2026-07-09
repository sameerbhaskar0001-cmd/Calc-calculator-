import re

vm_path = 'app/src/main/java/com/example/CalculatorViewModel.kt'
with open(vm_path, 'r') as f:
    vm = f.read()

old_add_start = """            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIdx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIdx != -1) {
                        val nameVal = it.getString(nameIdx)
                        if (!nameVal.isNullOrEmpty()) {
                            originalName = nameVal
                        }
                    }
                    
                    val sizeIdx = it.getColumnIndex(OpenableColumns.SIZE)
                    if (sizeIdx != -1) {
                        size = it.getLong(sizeIdx)
                    }
                }
            }"""

new_add_start = """            var originalPath = ""
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIdx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIdx != -1) {
                        val nameVal = it.getString(nameIdx)
                        if (!nameVal.isNullOrEmpty()) {
                            originalName = nameVal
                        }
                    }
                    
                    val sizeIdx = it.getColumnIndex(OpenableColumns.SIZE)
                    if (sizeIdx != -1) {
                        size = it.getLong(sizeIdx)
                    }
                    
                    val dataIdx = it.getColumnIndex(android.provider.MediaStore.MediaColumns.DATA)
                    if (dataIdx != -1) {
                        originalPath = it.getString(dataIdx) ?: ""
                    }
                }
            }"""
vm = vm.replace(old_add_start, new_add_start)

old_add_resolve = """            // 4. Resolve the ORIGINAL MediaStore URI safely without DATA column
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
            }"""

new_add_resolve = """            // 4. Resolve the ORIGINAL MediaStore URI safely
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
vm = vm.replace(old_add_resolve, new_add_resolve)

# Check single delete case inside addVaultFile
old_single_delete = """                } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    android.util.Log.d("Vault", "Delete request created for $resolvedUri")
                    val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, listOf(resolvedUri))
                    pendingDeleteOriginalPaths = listOf(uriToPersist)
                    _pendingDeleteSender.value = pendingIntent.intentSender
                } else {"""
new_single_delete = """                } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    if (resolvedUri.authority?.startsWith("media") == true) {
                        android.util.Log.d("Vault", "Delete request created for $resolvedUri")
                        val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, listOf(resolvedUri))
                        pendingDeleteOriginalPaths = listOf(uriToPersist)
                        _pendingDeleteSender.value = pendingIntent.intentSender
                    } else {
                        android.util.Log.w("Vault", "Cannot delete non-media URI: $resolvedUri")
                        onOriginalFileDeleted(context)
                    }
                } else {"""
vm = vm.replace(old_single_delete, new_single_delete)

with open(vm_path, 'w') as f:
    f.write(vm)

