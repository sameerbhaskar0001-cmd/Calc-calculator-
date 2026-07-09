import re

with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'r') as f:
    code = f.read()

old_batch_fallback = """                        if (originalPath.isNotEmpty()) {
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
                        }"""

new_batch_fallback = """                        val mimeType = contentResolver.getType(uri) ?: ""
                        val baseUri = when {
                            mimeType.startsWith("image/") -> android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            mimeType.startsWith("video/") -> android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            mimeType.startsWith("audio/") -> android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            else -> android.provider.MediaStore.Files.getContentUri("external")
                        }
                        if (originalPath.isNotEmpty()) {
                            val mediaCursor = contentResolver.query(
                                baseUri,
                                arrayOf(android.provider.MediaStore.MediaColumns._ID),
                                "${android.provider.MediaStore.MediaColumns.DATA} = ?",
                                arrayOf(originalPath),
                                null
                            )
                            mediaCursor?.use {
                                if (it.moveToFirst()) {
                                    val id = it.getLong(it.getColumnIndexOrThrow(android.provider.MediaStore.MediaColumns._ID))
                                    resolvedUri = android.content.ContentUris.withAppendedId(baseUri, id)
                                }
                            }
                        }
                        if (!resolvedUri.toString().contains("media/external") && originalName != "unnamed_file" && size > 0L) {
                            val mediaCursor = contentResolver.query(
                                baseUri,
                                arrayOf(android.provider.MediaStore.MediaColumns._ID),
                                "${android.provider.MediaStore.MediaColumns.DISPLAY_NAME} = ? AND ${android.provider.MediaStore.MediaColumns.SIZE} = ?",
                                arrayOf(originalName, size.toString()),
                                null
                            )
                            mediaCursor?.use {
                                if (it.moveToFirst()) {
                                    val id = it.getLong(it.getColumnIndexOrThrow(android.provider.MediaStore.MediaColumns._ID))
                                    resolvedUri = android.content.ContentUris.withAppendedId(baseUri, id)
                                }
                            }
                        }"""

code = code.replace(old_batch_fallback, new_batch_fallback)

with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'w') as f:
    f.write(code)
print("done batch")
