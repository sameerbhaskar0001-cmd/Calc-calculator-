fun toggleLinePrefix(fieldValue: TextFieldValue, prefix: String): TextFieldValue {
    try {
        val text = fieldValue.text
        val selection = fieldValue.selection
        val cursor = selection.start
        
        val lineStart = text.lastIndexOf('\n', cursor - 1) + 1
        
        val knownPrefixes = listOf("[ ] ", "[x] ", "• ", "- ")
        var currentPrefix = ""
        for (p in knownPrefixes) {
            if (text.startsWith(p, lineStart)) {
                currentPrefix = p
                break
            }
        }
        
        val newText: String
        val newCursor: Int
        if (currentPrefix == prefix) {
            // Remove prefix
            newText = text.substring(0, lineStart) + text.substring(lineStart + currentPrefix.length)
            newCursor = (cursor - currentPrefix.length).coerceAtLeast(lineStart)
        } else if (currentPrefix.isNotEmpty()) {
            // Replace prefix
            newText = text.substring(0, lineStart) + prefix + text.substring(lineStart + currentPrefix.length)
            val diff = prefix.length - currentPrefix.length
            newCursor = (cursor + diff).coerceAtLeast(lineStart)
        } else {
            // Add prefix
            newText = text.substring(0, lineStart) + prefix + text.substring(lineStart)
            newCursor = cursor + prefix.length
        }
        
        return TextFieldValue(
            text = newText,
            selection = TextRange(newCursor)
        )
    } catch(e: Exception) {
        e.printStackTrace()
        return fieldValue
    }
}
