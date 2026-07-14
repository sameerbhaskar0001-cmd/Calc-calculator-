import re

with open("app/src/main/java/com/example/CalculatorViewModel.kt", "r") as f:
    text = f.read()

# Strip out the misplaced imports and VaultStorageInfo
# It was placed before `enum class ApiStatus`
bad_block = """
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

data class VaultStorageInfo(
    val totalBytes: Long = 0,
    val photosBytes: Long = 0,
    val videosBytes: Long = 0,
    val docsBytes: Long = 0,
    val audioBytes: Long = 0,
    val notesBytes: Long = 0,
    val trashBytes: Long = 0
) {
    val totalUsedFormatted: String get() = formatSize(totalBytes)
    val photosFormatted: String get() = formatSize(photosBytes)
    val videosFormatted: String get() = formatSize(videosBytes)
    val docsFormatted: String get() = formatSize(docsBytes)
    val audioFormatted: String get() = formatSize(audioBytes)
    val notesFormatted: String get() = formatSize(notesBytes)
    val trashFormatted: String get() = formatSize(trashBytes)
    
    private fun formatSize(bytes: Long): String {
        if (bytes <= 0L) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (java.lang.Math.log10(bytes.toDouble()) / java.lang.Math.log10(1024.0)).toInt()
        val index = if (digitGroups > 4) 4 else digitGroups
        val num = bytes / java.lang.Math.pow(1024.0, index.toDouble())
        return String.format(java.util.Locale.US, "%.1f %s", num, units[index])
    }
}
"""
text = text.replace(bad_block, "")

# Ensure proper imports at the top
import_str = """
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
"""
if "import kotlinx.coroutines.flow.combine" not in text:
    text = text.replace("import kotlinx.coroutines.flow.MutableStateFlow", import_str + "import kotlinx.coroutines.flow.MutableStateFlow")

# Append VaultStorageInfo at the end of the file
text += """

data class VaultStorageInfo(
    val totalBytes: Long = 0,
    val photosBytes: Long = 0,
    val videosBytes: Long = 0,
    val docsBytes: Long = 0,
    val audioBytes: Long = 0,
    val notesBytes: Long = 0,
    val trashBytes: Long = 0
) {
    val totalUsedFormatted: String get() = formatSize(totalBytes)
    val photosFormatted: String get() = formatSize(photosBytes)
    val videosFormatted: String get() = formatSize(videosBytes)
    val docsFormatted: String get() = formatSize(docsBytes)
    val audioFormatted: String get() = formatSize(audioBytes)
    val notesFormatted: String get() = formatSize(notesBytes)
    val trashFormatted: String get() = formatSize(trashBytes)
    
    private fun formatSize(bytes: Long): String {
        if (bytes <= 0L) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (java.lang.Math.log10(bytes.toDouble()) / java.lang.Math.log10(1024.0)).toInt()
        val index = if (digitGroups > 4) 4 else digitGroups
        val num = bytes / java.lang.Math.pow(1024.0, index.toDouble())
        return String.format(java.util.Locale.US, "%.1f %s", num, units[index])
    }
}
"""

with open("app/src/main/java/com/example/CalculatorViewModel.kt", "w") as f:
    f.write(text)

