import re

with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'r') as f:
    vm = f.read()

# Replace batchDeleteOriginalFiles and addVaultFile
start_idx = vm.find("    fun batchDeleteOriginalFiles")
end_idx = vm.find("    fun deleteVaultFile")

new_code = """    fun batchDeleteOriginalFiles(context: Context, uris: List<Uri>) {
        if (uris.isEmpty()) return
        try {
            val contentResolver = context.contentResolver
            val mediaStoreUris = mutableListOf<Uri>()
            val urisToPersist = mutableListOf<String>()
            
            for (uri in uris) {
                var resolvedUri = uri
                var originalName = "unnamed_file"
                var size = 0L
                var originalPath = ""
                
                try {
                    val cursor = contentResolver.query(uri, null, null, null, null)
                    cursor?.use {
                        if (it.moveToFirst()) {
                            val nameIdx = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                            if (nameIdx != -1) originalName = it.getString(nameIdx) ?: originalName
                            val sizeIdx = it.getColumnIndex(android.provider.OpenableColumns.SIZE)
                            if (sizeIdx != -1) size = it.getLong(sizeIdx)
                            val dataIdx = it.getColumnIndex(android.provider.MediaStore.MediaColumns.DATA)
                            if (dataIdx != -1) originalPath = it.getString(dataIdx) ?: ""
                        }
                    }
                } catch (e: Exception) {}

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
                        if (originalPath.isNotEmpty()) {
                            val mediaCursor = contentResolver.query(
                                android.provider.MediaStore.Files.getContentUri("external"),
                                arrayOf(android.provider.MediaStore.Files.FileColumns._ID),
                                "${android.provider.MediaStore.Files.FileColumns.DATA} = ?",
                                arrayOf(originalPath),
                                null
                            )
                            mediaCursor?.use {
                                if (it.moveToFirst()) {
                                    val id = it.getLong(it.getColumnIndexOrThrow(android.provider.MediaStore.Files.FileColumns._ID))
                                    resolvedUri = android.content.ContentUris.withAppendedId(android.provider.MediaStore.Files.getContentUri("external"), id)
                                }
                            }
                        }
                        if (!resolvedUri.toString().contains("media/external") && originalName != "unnamed_file" && size > 0L) {
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
    }

    fun addVaultFile(context: Context, uri: Uri, skipDelete: Boolean = false): Boolean {
        return try {
            val contentResolver = context.contentResolver
            
            var originalName = "unnamed_file"
            var mimeType = "application/octet-stream"
            var size = 0L
            var originalPath = ""
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIdx = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (nameIdx != -1) {
                        val nameVal = it.getString(nameIdx)
                        if (!nameVal.isNullOrEmpty()) originalName = nameVal
                    }
                    val sizeIdx = it.getColumnIndex(android.provider.OpenableColumns.SIZE)
                    if (sizeIdx != -1) size = it.getLong(sizeIdx)
                    val dataIdx = it.getColumnIndex(android.provider.MediaStore.MediaColumns.DATA)
                    if (dataIdx != -1) originalPath = it.getString(dataIdx) ?: ""
                }
            }
            
            mimeType = contentResolver.getType(uri) ?: mimeType
            if (mimeType.isEmpty() || mimeType == "application/octet-stream") {
                val ext = java.io.File(originalName).extension.lowercase()
                mimeType = when (ext) {
                    "jpg", "jpeg", "png", "webp", "heic", "heif", "gif", "bmp" -> "image/$ext"
                    "mp4", "mkv", "3gp", "avi", "mov", "webm" -> "video/$ext"
                    "pdf" -> "application/pdf"
                    "txt", "csv", "log" -> "text/plain"
                    "zip" -> "application/zip"
                    else -> "application/octet-stream"
                }
            }
            
            val readableSize = formatFileSize(size)
            val id = System.currentTimeMillis().toString()
            val timestamp = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
            
            val vaultDir = java.io.File(context.filesDir, "vault_files")
            if (!vaultDir.exists()) vaultDir.mkdirs()
            
            val extension = java.io.File(originalName).extension.ifEmpty {
                when {
                    mimeType.startsWith("image/") -> "jpg"
                    mimeType.startsWith("video/") -> "mp4"
                    mimeType.contains("pdf") -> "pdf"
                    mimeType.contains("text") -> "txt"
                    else -> "dat"
                }
            }
            
            val destFileName = "$id.$extension"
            val destFile = java.io.File(vaultDir, destFileName)
            
            contentResolver.openInputStream(uri)?.use { input ->
                java.io.FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            
            if (!destFile.exists() || destFile.length() == 0L) return false
            
            var durationMs = 0L
            if (mimeType.startsWith("video/")) {
                try {
                    val retriever = android.media.MediaMetadataRetriever()
                    retriever.setDataSource(destFile.absolutePath)
                    val timeStr = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)
                    durationMs = timeStr?.toLongOrNull() ?: 0L
                    retriever.release()
                } catch (e: Exception) {}
            }
            
            val fileSerialized = "$id|||$timestamp|||$originalName|||$mimeType|||${destFile.absolutePath}|||$readableSize|||$durationMs"
            stagedVaultFiles.add(fileSerialized)

            var resolvedUri = uri
            if (android.provider.DocumentsContract.isDocumentUri(context, uri) && uri.authority == "com.android.providers.media.documents") {
                try {
                    val docId = android.provider.DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    if (split.size >= 2) {
                        val type = split[0]
                        val mediaId = split[1].toLongOrNull()
                        if (mediaId != null) {
                            val baseUri = when (type) {
                                "image" -> android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                "video" -> android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                                "audio" -> android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                                else -> android.provider.MediaStore.Files.getContentUri("external")
                            }
                            resolvedUri = android.content.ContentUris.withAppendedId(baseUri, mediaId)
                        }
                    }
                } catch (e: Exception) {}
            }
            
            if (!resolvedUri.toString().contains("media/external")) {
                try {
                    if (originalPath.isNotEmpty()) {
                        val mediaCursor = contentResolver.query(
                            android.provider.MediaStore.Files.getContentUri("external"),
                            arrayOf(android.provider.MediaStore.Files.FileColumns._ID),
                            "${android.provider.MediaStore.Files.FileColumns.DATA} = ?",
                            arrayOf(originalPath),
                            null
                        )
                        mediaCursor?.use {
                            if (it.moveToFirst()) {
                                val mediaId = it.getLong(it.getColumnIndexOrThrow(android.provider.MediaStore.Files.FileColumns._ID))
                                resolvedUri = android.content.ContentUris.withAppendedId(android.provider.MediaStore.Files.getContentUri("external"), mediaId)
                            }
                        }
                    }
                    if (!resolvedUri.toString().contains("media/external") && originalName != "unnamed_file" && size > 0L) {
                        val mediaCursor = contentResolver.query(
                            android.provider.MediaStore.Files.getContentUri("external"),
                            arrayOf(android.provider.MediaStore.Files.FileColumns._ID),
                            "${android.provider.MediaStore.Files.FileColumns.DISPLAY_NAME} = ? AND ${android.provider.MediaStore.Files.FileColumns.SIZE} = ?",
                            arrayOf(originalName, size.toString()),
                            null
                        )
                        mediaCursor?.use {
                            if (it.moveToFirst()) {
                                val mediaId = it.getLong(it.getColumnIndexOrThrow(android.provider.MediaStore.Files.FileColumns._ID))
                                resolvedUri = android.content.ContentUris.withAppendedId(android.provider.MediaStore.Files.getContentUri("external"), mediaId)
                            }
                        }
                    }
                } catch (e: Exception) {}
            }

            if (skipDelete) {
                return true
            }
            
            if (!resolvedUri.toString().contains("media/external")) {
                android.util.Log.e("Vault", "Aborting single delete: Could not resolve MediaStore URI for $uri")
                destFile.delete()
                stagedVaultFiles.remove(fileSerialized)
                return false
            }

            val uriToPersist = resolvedUri.toString()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, listOf(resolvedUri))
                pendingDeleteOriginalPaths = listOf(uriToPersist)
                _pendingDeleteSender.value = pendingIntent.intentSender
            } else {
                val deletedRows = contentResolver.delete(resolvedUri, null, null)
                if (deletedRows > 0) {
                    pendingDeleteOriginalPaths = listOf(uriToPersist)
                    onOriginalFileDeleted(context)
                } else {
                    destFile.delete()
                    stagedVaultFiles.remove(fileSerialized)
                    return false
                }
            }
            return true
        } catch (e: Exception) {
            android.util.Log.e("Vault", "Exception", e)
            return false
        }
    }
"""

if start_idx != -1 and end_idx != -1:
    final_code = vm[:start_idx] + new_code + vm[end_idx:]
    with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'w') as f:
        f.write(final_code)
    print("Replaced successfully")
else:
    print("Not found")

