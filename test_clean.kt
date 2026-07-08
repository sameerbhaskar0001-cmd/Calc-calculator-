fun cleanDisplayName(rawName: String, fallbackType: String = ""): String {
    var cleaned = rawName
    val prefixes = listOf("IMG_", "VID_", "AUD_", "DOC_", "PXL_", "Screenshot_")
    var hasScreenshotPrefix = false
    for (prefix in prefixes) {
        if (cleaned.startsWith(prefix, ignoreCase = true)) {
            cleaned = cleaned.substring(prefix.length)
            if (prefix.equals("Screenshot_", ignoreCase = true)) {
                hasScreenshotPrefix = true
            }
        }
    }
    val lastDot = cleaned.lastIndexOf('.')
    val ext = if (lastDot > 0) rawName.substring(lastDot + 1).lowercase() else ""
    if (lastDot > 0) {
        cleaned = cleaned.substring(0, lastDot)
    }
    
    val isNumeric = cleaned.all { it.isDigit() || it == '_' || it == '-' }
    if (isNumeric || cleaned.trim().isEmpty()) {
        if (hasScreenshotPrefix) return "Screenshot"
        
        val inferredType = if (fallbackType.isNotEmpty()) fallbackType else {
            when (ext) {
                "jpg", "jpeg", "png", "webp", "gif" -> "Photo"
                "mp4", "mkv", "avi", "mov" -> "Video"
                "mp3", "wav", "ogg", "m4a", "aac" -> "Audio"
                "pdf", "doc", "docx", "txt" -> "Document"
                else -> "File"
            }
        }
        
        val numStr = cleaned.filter { it.isDigit() }
        val id = if (numStr.isNotEmpty()) numStr.takeLast(4).toIntOrNull() ?: 1 else 1
        return "$inferredType $id"
    }
    
    return cleaned
}

fun main() {
    println(cleanDisplayName("5299.jpg"))
    println(cleanDisplayName("IMG_20231015_123456.jpg"))
    println(cleanDisplayName("Screenshot_20231015-123456.png"))
    println(cleanDisplayName("Vacation_Paris.jpg"))
    println(cleanDisplayName("123.mp4"))
}
