import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

# Fix Icons.Default.CheckCircle -> Icons.Default.Check
content = content.replace("Icons.Default.CheckCircle", "Icons.Default.Check")
# Fix Icons.Default.Notes -> Icons.Default.List
content = content.replace("Icons.Default.Notes", "Icons.Default.List")
# Fix Icons.Default.InsertDriveFile -> Icons.Default.Description
content = content.replace("Icons.Default.InsertDriveFile", "Icons.Default.Description")

# Fix vaultFiles.count { it.mimeType... }
# vaultFiles is List<String>
old_photos_count = 'count = "${vaultFiles.count { it.mimeType.startsWith("image/") || it.mimeType.startsWith("video/") }} Items",'
new_photos_count = 'count = "${vaultFiles.count { val ext = it.substringAfterLast(\'.\', \"\").lowercase(); ext in listOf(\"jpg\", \"jpeg\", \"png\", \"mp4\", \"mkv\") }} Items",'
content = content.replace(old_photos_count, new_photos_count)

old_docs_count = 'count = "${vaultFiles.count { !it.mimeType.startsWith("image/") && !it.mimeType.startsWith("video/") && !it.mimeType.startsWith("audio/") }} Items",'
new_docs_count = 'count = "${vaultFiles.count { val ext = it.substringAfterLast(\'.\', \"\").lowercase(); ext not in listOf(\"jpg\", \"jpeg\", \"png\", \"mp4\", \"mkv\", \"mp3\", \"wav\") }} Items",'
content = content.replace(old_docs_count, new_docs_count)

# Fix recentFiles
old_recent_files = 'val recentFiles = vaultFiles.sortedByDescending { it.lastModified }.take(3)'
new_recent_files = 'val recentFiles = vaultFiles.take(3)' # Can't sort by lastModified directly without java.io.File, just take top 3
content = content.replace(old_recent_files, new_recent_files)

# Fix file.mimeType
old_mime = 'imageVector = if (file.mimeType.startsWith("image/")) Icons.Default.Image else Icons.Default.Description,'
new_mime = 'imageVector = if (file.lowercase().endsWith(".jpg") || file.lowercase().endsWith(".png")) Icons.Default.Image else Icons.Default.Description,'
content = content.replace(old_mime, new_mime)

# Fix file.name
old_name = 'Text(file.name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)'
new_name = 'Text(file.substringAfterLast(\'/\'), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)'
content = content.replace(old_name, new_name)


with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)

