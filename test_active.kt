fun isTagActive(text: String, start: Int, tagOpen: String, tagClose: String): Boolean {
    if (start < 0 || start > text.length) return false
    val lastOpen = if (start > 0) text.lastIndexOf(tagOpen, start - 1) else -1
    if (lastOpen != -1) {
        val lastCloseBeforeOpen = if (start > 0) text.lastIndexOf(tagClose, start - 1) else -1
        if (lastCloseBeforeOpen < lastOpen) {
            val nextClose = text.indexOf(tagClose, start)
            val nextOpen = text.indexOf(tagOpen, start)
            if (nextClose != -1 && (nextOpen == -1 || nextClose < nextOpen || nextClose == start)) {
                return true
            }
            if (nextClose == start) return true
        }
    }
    return false
}

fun main() {
    println(isTagActive("<b></b>", 3, "<b>", "</b>")) // true
    println(isTagActive("<b>hello</b>", 3, "<b>", "</b>")) // true
    println(isTagActive("<b>hello</b>", 8, "<b>", "</b>")) // true (at end of hello)
    println(isTagActive("<b>hello</b>", 9, "<b>", "</b>")) // false (outside)
    println(isTagActive("<b>hello</b>", 0, "<b>", "</b>")) // false
}
