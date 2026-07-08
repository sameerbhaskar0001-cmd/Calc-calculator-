val numericNameCounters = mutableMapOf<String, Int>()
val currentTypeCounters = mutableMapOf<String, Int>()

fun generateUserFriendlyName(type: String, id: String, isScreenshot: Boolean = false): String {
    val effectiveId = if (id.isEmpty()) "unknown_id_${System.identityHashCode(Any())}" else id
    if (isScreenshot) {
        val key = "Screenshot-$effectiveId"
        if (!numericNameCounters.containsKey(key)) {
            val c = currentTypeCounters.getOrDefault("Screenshot", 1)
            numericNameCounters[key] = c
            currentTypeCounters["Screenshot"] = c + 1
        }
        val count = numericNameCounters[key]
        return if (count == 1) "Screenshot" else "Screenshot $count"
    }

    val displayType = when(type.lowercase()) {
        "image", "photo" -> "Photo"
        "video" -> "Video"
        "audio", "music" -> "Audio"
        "document", "doc" -> "Document"
        else -> type.replaceFirstChar { it.uppercase() }
    }
    val key = "$displayType-$effectiveId"
    if (!numericNameCounters.containsKey(key)) {
        val c = currentTypeCounters.getOrDefault(displayType, 1)
        numericNameCounters[key] = c
        currentTypeCounters[displayType] = c + 1
    }
    return "$displayType ${numericNameCounters[key]}"
}

fun cleanDisplayName(rawName: String, fallbackType: String = "file", id: String = ""): String {
    var cleaned = rawName
    val prefixes = listOf("IMG_", "VID_", "AUD_", "DOC_", "PXL_", "Screenshot_")
    var hasScreenshotPrefix = false
    for (prefix in prefixes) {
        if (cleaned.startsWith(prefix, ignoreCase = true)) {
            cleaned = cleaned.substring(prefix.length)
            if (prefix.equals("Screenshot_", ignoreCase = true)) {
                hasScreenshotPrefix = true
            }
            break
        }
    }
    val lastDot = cleaned.lastIndexOf('.')
    val ext = if (lastDot > 0) cleaned.substring(lastDot + 1).lowercase() else ""
    if (lastDot > 0) {
        cleaned = cleaned.substring(0, lastDot)
    }
    
    val isNumeric = cleaned.all { it.isDigit() || it == '_' || it == '-' }
    if (isNumeric || cleaned.trim().isEmpty()) {
        val typeToUse = if (fallbackType != "file" && fallbackType.isNotEmpty()) fallbackType else {
            when (ext) {
                "jpg", "jpeg", "png", "webp", "gif" -> "Photo"
                "mp4", "mkv", "avi", "mov" -> "Video"
                "mp3", "wav", "ogg", "m4a", "aac" -> "Audio"
                "pdf", "doc", "docx", "txt" -> "Document"
                else -> "File"
            }
        }
        val effectiveId = if (id.isNotEmpty()) id else rawName
        return generateUserFriendlyName(typeToUse, effectiveId, hasScreenshotPrefix)
    }
    
    return cleaned
}

fun main() {
    println(cleanDisplayName("5299.jpg"))
    println(cleanDisplayName("IMG_12345.jpg"))
    println(cleanDisplayName("Screenshot_20231015-123456.png"))
    println(cleanDisplayName("Vacation_Paris.jpg"))
    println(cleanDisplayName("123.mp4"))
    println(cleanDisplayName("123.mp4")) // Should output same as above
    println(cleanDisplayName("456.mp4")) // Should output Video 2
}
