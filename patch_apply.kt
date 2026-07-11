fun clearTagTypeFromSelection(fieldValue: TextFieldValue, tagName: String): TextFieldValue {
    val text = fieldValue.text
    val start = minOf(fieldValue.selection.start, fieldValue.selection.end).coerceIn(0, text.length)
    val end = maxOf(fieldValue.selection.start, fieldValue.selection.end).coerceIn(0, text.length)
    if (start == end) return fieldValue
    
    val selectedText = text.substring(start, end)
    val regex = Regex("""<""" + tagName + """[^>]*>|</""" + tagName + """>""")
    val cleanedText = selectedText.replace(regex, "")
    
    val newText = text.substring(0, start) + cleanedText + text.substring(end)
    return safeTextFieldValue(newText, start, start + cleanedText.length)
}

fun applyStyleTagToSelection(fieldValue: TextFieldValue, tagName: String, attrName: String, attrValue: String): TextFieldValue {
    val text = fieldValue.text
    val start = minOf(fieldValue.selection.start, fieldValue.selection.end).coerceIn(0, text.length)
    val end = maxOf(fieldValue.selection.start, fieldValue.selection.end).coerceIn(0, text.length)
    
    val clearedValue = clearTagTypeFromSelection(fieldValue, tagName)
    
    val clearedText = clearedValue.text
    val newStart = minOf(clearedValue.selection.start, clearedValue.selection.end).coerceIn(0, clearedText.length)
    val newEnd = maxOf(clearedValue.selection.start, clearedValue.selection.end).coerceIn(0, clearedText.length)
    
    val selectedText = clearedText.substring(newStart, newEnd)
    if (attrValue == "transparent") return clearedValue // Just clear
    
    val tagOpen = "<$tagName $attrName=\"$attrValue\">"
    val tagClose = "</$tagName>"
    
    if (newStart == newEnd) return clearedValue
    
    val newText = clearedText.substring(0, newStart) + tagOpen + selectedText + tagClose + clearedText.substring(newEnd)
    return safeTextFieldValue(newText, newStart + tagOpen.length, newStart + tagOpen.length + selectedText.length)
}
