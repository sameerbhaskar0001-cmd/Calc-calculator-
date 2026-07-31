import re

with open("app/src/main/java/com/example/CalculatorViewModel.kt", "r") as f:
    content = f.read()

target = """    private val _isAudioPlaying = MutableStateFlow(false)
    val isAudioPlaying: StateFlow<Boolean> = _isAudioPlaying"""

replacement = """    private val _isAudioPlaying = MutableStateFlow(false)
    val isAudioPlaying: StateFlow<Boolean> = _isAudioPlaying
    
    private val _audioPlaybackSpeed = MutableStateFlow(1.0f)
    val audioPlaybackSpeed: StateFlow<Float> = _audioPlaybackSpeed"""

if target in content:
    content = content.replace(target, replacement)
    
target2 = """    fun seekAudio(position: Float) {
        try {
            mediaPlayer?.seekTo(position.toInt())
            _audioPosition.value = position
        } catch (e: Exception) {}
    }"""

replacement2 = """    fun seekAudio(position: Float) {
        try {
            mediaPlayer?.seekTo(position.toInt())
            _audioPosition.value = position
        } catch (e: Exception) {}
    }
    
    fun setAudioPlaybackSpeed(speed: Float) {
        _audioPlaybackSpeed.value = speed
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                mediaPlayer?.playbackParams = mediaPlayer?.playbackParams?.setSpeed(speed) ?: android.media.PlaybackParams().setSpeed(speed)
            }
        } catch (e: Exception) {}
    }"""

if target2 in content:
    content = content.replace(target2, replacement2)

target3 = """            mediaPlayer?.setOnCompletionListener {
                _isAudioPlaying.value = false
                _audioPosition.value = 0f
                stopAudioPositionUpdates()
                try {
                    it.seekTo(0)
                } catch (e: Exception) {}
            }
            mediaPlayer?.start()
            _isAudioPlaying.value = true"""

replacement3 = """            mediaPlayer?.setOnCompletionListener {
                _isAudioPlaying.value = false
                _audioPosition.value = 0f
                stopAudioPositionUpdates()
                try {
                    it.seekTo(0)
                } catch (e: Exception) {}
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                try {
                    mediaPlayer?.playbackParams = android.media.PlaybackParams().setSpeed(_audioPlaybackSpeed.value)
                } catch (e: Exception) {}
            }
            mediaPlayer?.start()
            _isAudioPlaying.value = true"""

if target3 in content:
    content = content.replace(target3, replacement3)

with open("app/src/main/java/com/example/CalculatorViewModel.kt", "w") as f:
    f.write(content)
print("Updated ViewModel audio playback speed")
