import re

with open("app/src/main/java/com/example/CalculatorViewModel.kt", "r") as f:
    text = f.read()

# Add data class VaultStorageInfo
data_class = """
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.io.File

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
        if (bytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(bytes.toDouble()) / Math.log10(1024.0)).toInt()
        val index = if (digitGroups > 4) 4 else digitGroups
        val num = bytes / Math.pow(1024.0, index.toDouble())
        return String.format(java.util.Locale.US, "%.1f %s", num, units[index])
    }
}
"""

text = re.sub(r'import androidx\.lifecycle\.ViewModel', data_class + '\nimport androidx.lifecycle.ViewModel', text, count=1)

state_flow = """
    val storageInfo: StateFlow<VaultStorageInfo> = combine(
        _vaultFiles, _vaultNotes, _recentlyDeletedFiles
    ) { files, notes, trash ->
        var photos = 0L
        var videos = 0L
        var docs = 0L
        var audio = 0L
        var notesSize = 0L
        var trashSize = 0L
        
        files.forEach { file ->
            val parts = file.split("|||")
            if (parts.size >= 5) {
                val mimeType = parts[3].lowercase()
                val path = parts[4]
                val size = File(path).length()
                when {
                    mimeType.startsWith("image/") -> photos += size
                    mimeType.startsWith("video/") -> videos += size
                    mimeType.startsWith("audio/") -> audio += size
                    else -> docs += size
                }
            }
        }
        
        notes.forEach { note ->
            notesSize += note.toByteArray().size.toLong()
        }
        
        trash.forEach { item ->
            val parts = item.split("|||")
            if (parts.size >= 5) {
                // If it's a file, parts[4] is absolute path
                if (parts[4].startsWith("/")) {
                    trashSize += File(parts[4]).length()
                } else {
                    trashSize += item.toByteArray().size.toLong()
                }
            } else {
                trashSize += item.toByteArray().size.toLong()
            }
        }
        
        VaultStorageInfo(
            totalBytes = photos + videos + docs + audio + notesSize,
            photosBytes = photos,
            videosBytes = videos,
            docsBytes = docs,
            audioBytes = audio,
            notesBytes = notesSize,
            trashBytes = trashSize
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, VaultStorageInfo())

    val intruderAttempts: StateFlow<List<String>> = _intruderAttempts.asStateFlow()
"""

text = re.sub(r'val intruderAttempts: StateFlow<List<String>> = _intruderAttempts\.asStateFlow\(\)', state_flow, text, count=1)

with open("app/src/main/java/com/example/CalculatorViewModel.kt", "w") as f:
    f.write(text)

