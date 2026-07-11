fun parseColorString(hex: String?): Color {
    if (hex == null) return Color.White
    val clean = hex.trim().replace("\"", "").replace("'", "")
    if (clean.isEmpty()) return Color.White
    val normalized = if (clean.startsWith("#")) clean else "#$clean"
    return try {
        Color(android.graphics.Color.parseColor(normalized))
    } catch (e: Exception) {
        when (clean.lowercase()) {
            "red" -> Color.Red
            "green" -> Color.Green
            "blue" -> Color.Blue
            "black" -> Color.Black
            "white" -> Color.White
            "transparent" -> Color.Transparent
            else -> Color.White
        }
    }
}

fun parseBgColorString(hex: String?): Color {
    if (hex == null) return Color.Transparent
    val clean = hex.trim().replace("\"", "").replace("'", "")
    if (clean.isEmpty() || clean == "transparent" || clean == "000000" || clean == "#000000") return Color.Transparent
    val normalized = if (clean.startsWith("#")) clean else "#$clean"
    return try {
        Color(android.graphics.Color.parseColor(normalized))
    } catch (e: Exception) {
        Color.Transparent
    }
}
