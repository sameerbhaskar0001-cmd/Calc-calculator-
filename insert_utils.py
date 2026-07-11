import sys

with open("app/src/main/java/com/example/CalculatorScreen.kt") as f:
    text = f.read()

funcs = """
fun isTagActive(fieldValue: TextFieldValue, tagOpen: String, tagClose: String): Boolean {
    val text = fieldValue.text
    val start = fieldValue.selection.start.coerceIn(0, text.length)
    if (start == 0 && text.isEmpty()) return false
    val searchEnd = (start - 1).coerceAtLeast(0)
    
    // Actually Kotlin's lastIndexOf with startIndex means it searches backwards starting from startIndex.
    val lastOpen = if (start > 0) text.lastIndexOf(tagOpen, searchEnd) else -1
    if (lastOpen != -1) {
        val lastCloseBeforeOpen = if (start > 0) text.lastIndexOf(tagClose, searchEnd) else -1
        if (lastCloseBeforeOpen < lastOpen) {
            val nextClose = text.indexOf(tagClose, start)
            if (nextClose != -1) {
                val nextOpen = text.indexOf(tagOpen, start)
                if (nextOpen == -1 || nextClose < nextOpen || nextClose == start) {
                    return true
                }
            } else {
                return true 
            }
        }
    }
    return false
}

fun isPrefixActive(fieldValue: TextFieldValue, prefix: String): Boolean {
    val text = fieldValue.text
    val cursor = fieldValue.selection.start.coerceIn(0, text.length)
    val searchEnd = (cursor - 1).coerceAtLeast(0)
    val lineStart = if (cursor > 0) text.lastIndexOf('\n', searchEnd) + 1 else 0
    if (lineStart < 0 || lineStart > text.length) return false
    return text.substring(lineStart).startsWith(prefix) || 
           (prefix == "[ ] " && text.substring(lineStart).startsWith("[x] "))
}
"""

idx = text.find("fun toggleTag(")
text = text[:idx] + funcs + "\n" + text[idx:]

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)
