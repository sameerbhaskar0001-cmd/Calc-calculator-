fun formatRelativeTime(timestamp: Long): String {
    if (timestamp <= 0) return "Just now" // fallback
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < 60_000 -> "Just now"
        diff < 3600_000 -> "${diff / 60_000} min ago"
        diff < 86400_000 && android.text.format.DateUtils.isToday(timestamp) -> {
            val hrs = diff / 3600_000
            "$hrs ${if (hrs == 1L) "hour" else "hours"} ago"
        }
        android.text.format.DateUtils.isToday(timestamp) -> "Today"
        android.text.format.DateUtils.isToday(timestamp + 86400_000) -> "Yesterday"
        else -> java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date(timestamp))
    }
}
