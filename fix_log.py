import re

vm_path = 'app/src/main/java/com/example/CalculatorViewModel.kt'
with open(vm_path, 'r') as f:
    vm = f.read()

old_batch = """    fun batchDeleteOriginalFiles(context: Context, uris: List<Uri>) {
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

new_batch = """    fun batchDeleteOriginalFiles(context: Context, uris: List<Uri>) {
        android.util.Log.d("Vault", "batchDeleteOriginalFiles called with ${uris.size} uris")
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
                android.util.Log.d("Vault", "Resolved URI for batch delete: $resolvedUri")
                mediaStoreUris.add(resolvedUri)
                urisToPersist.add(resolvedUri.toString())
            }
            
            pendingDeleteOriginalPaths = urisToPersist
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                if (mediaStoreUris.isNotEmpty()) {
                    android.util.Log.d("Vault", "Calling createDeleteRequest with ${mediaStoreUris.size} uris")
                    val pendingIntent = android.provider.MediaStore.createDeleteRequest(contentResolver, mediaStoreUris)
                    android.util.Log.d("Vault", "createDeleteRequest successful, setting _pendingDeleteSender")
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

