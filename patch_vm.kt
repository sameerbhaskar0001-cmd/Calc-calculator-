    // --- Audio Player State ---
    private var mediaPlayer: android.media.MediaPlayer? = null
    private var currentAudioPath: String? = null
    
    private val _isAudioPlaying = MutableStateFlow(false)
    val isAudioPlaying: StateFlow<Boolean> = _isAudioPlaying.asStateFlow()
    
    private val _audioPosition = MutableStateFlow(0f)
    val audioPosition: StateFlow<Float> = _audioPosition.asStateFlow()
    
    private val _audioDuration = MutableStateFlow(1f)
    val audioDuration: StateFlow<Float> = _audioDuration.asStateFlow()
    
    private var audioPositionJob: kotlinx.coroutines.Job? = null
    
    val currentAudioPlayingPath: String? get() = currentAudioPath

    fun playOrToggleAudio(path: String, context: android.content.Context) {
        if (currentAudioPath == path && mediaPlayer != null) {
            if (_isAudioPlaying.value) {
                pauseAudio()
            } else {
                resumeAudio()
            }
            return
        }
        
        stopAudio()
        currentAudioPath = path
        try {
            mediaPlayer = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                android.media.MediaPlayer(context)
            } else {
                android.media.MediaPlayer()
            }
            mediaPlayer?.setDataSource(path)
            mediaPlayer?.prepare()
            _audioDuration.value = (mediaPlayer?.duration ?: 1).toFloat()
            mediaPlayer?.setOnCompletionListener {
                _isAudioPlaying.value = false
                _audioPosition.value = 0f
                stopAudioPositionUpdates()
                try {
                    it.seekTo(0)
                } catch (e: Exception) {}
            }
            mediaPlayer?.start()
            _isAudioPlaying.value = true
            startAudioPositionUpdates()
        } catch (e: Exception) {
            e.printStackTrace()
            currentAudioPath = null
        }
    }
    
    fun resumeAudio() {
        try {
            mediaPlayer?.start()
            _isAudioPlaying.value = true
            startAudioPositionUpdates()
        } catch (e: Exception) {}
    }

    fun pauseAudio() {
        try {
            mediaPlayer?.pause()
            _isAudioPlaying.value = false
            stopAudioPositionUpdates()
        } catch (e: Exception) {}
    }
    
    fun stopAudio() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) {}
        mediaPlayer = null
        _isAudioPlaying.value = false
        currentAudioPath = null
        _audioPosition.value = 0f
        stopAudioPositionUpdates()
    }
    
    fun seekAudio(position: Float) {
        try {
            mediaPlayer?.seekTo(position.toInt())
            _audioPosition.value = position
        } catch (e: Exception) {}
    }
    
    private fun startAudioPositionUpdates() {
        audioPositionJob?.cancel()
        audioPositionJob = viewModelScope.launch {
            while (true) {
                try {
                    _audioPosition.value = (mediaPlayer?.currentPosition ?: 0).toFloat()
                } catch (e: Exception) {}
                kotlinx.coroutines.delay(250)
            }
        }
    }
    
    private fun stopAudioPositionUpdates() {
        audioPositionJob?.cancel()
        audioPositionJob = null
    }

    fun onAppBackgrounded() {
        if (!_backgroundAudioPlaybackEnabled.value) {
            pauseAudio()
        }
    }
