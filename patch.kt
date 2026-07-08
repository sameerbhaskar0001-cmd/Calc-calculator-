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
