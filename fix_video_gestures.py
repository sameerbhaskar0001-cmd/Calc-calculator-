import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target = """                                        Box(modifier = Modifier.fillMaxSize()) {
                                            AndroidView(
                                                factory = { ctx ->
                                                    android.widget.VideoView(ctx).apply {
                                                        setVideoPath(path)
                                                        val mediaController = android.widget.MediaController(ctx)
                                                        mediaController.setAnchorView(this)
                                                        setMediaController(mediaController)
                                                        setOnPreparedListener { mp ->
                                                            mp.isLooping = true
                                                            seekTo(playbackPosition)
                                                            start()
                                                        }
                                                        videoViewRef = this
                                                    }
                                                },
                                                modifier = Modifier.fillMaxSize(),
                                                onRelease = {
                                                    playbackPosition = it.currentPosition
                                                    videoViewRef = null
                                                }
                                            )
                                            
                                            // Left double tap to rewind
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .fillMaxWidth(0.3f)
                                                    .align(Alignment.CenterStart)
                                                    .pointerInput(Unit) {
                                                        detectTapGestures(
                                                            onDoubleTap = {
                                                                videoViewRef?.let {
                                                                    val newPos = maxOf(0, it.currentPosition - 10000) // Rewind 10s
                                                                    it.seekTo(newPos)
                                                                    playbackPosition = newPos
                                                                }
                                                            }
                                                        )
                                                    }
                                            )
                                            // Right double tap to forward
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .fillMaxWidth(0.3f)
                                                    .align(Alignment.CenterEnd)
                                                    .pointerInput(Unit) {
                                                        detectTapGestures(
                                                            onDoubleTap = {
                                                                videoViewRef?.let {
                                                                    val newPos = minOf(it.duration, it.currentPosition + 10000) // Forward 10s
                                                                    it.seekTo(newPos)
                                                                    playbackPosition = newPos
                                                                }
                                                            }
                                                        )
                                                    }
                                            )
                                        }"""

replacement = """                                        val audioManager = remember { context.getSystemService(android.content.Context.AUDIO_SERVICE) as android.media.AudioManager }
                                        var showBrightnessIndicator by remember { mutableStateOf(false) }
                                        var showVolumeIndicator by remember { mutableStateOf(false) }
                                        var currentBrightnessState by remember { mutableFloatStateOf(0f) }
                                        var currentVolumeState by remember { mutableIntStateOf(0) }
                                        var maxVolume by remember { mutableIntStateOf(1) }
                                        var accumulatedVolumeDelta by remember { mutableFloatStateOf(0f) }
                                        var accumulatedBrightnessDelta by remember { mutableFloatStateOf(0f) }
                                        
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            AndroidView(
                                                factory = { ctx ->
                                                    android.widget.VideoView(ctx).apply {
                                                        setVideoPath(path)
                                                        val mediaController = android.widget.MediaController(ctx)
                                                        mediaController.setAnchorView(this)
                                                        setMediaController(mediaController)
                                                        setOnPreparedListener { mp ->
                                                            mp.isLooping = true
                                                            seekTo(playbackPosition)
                                                            start()
                                                        }
                                                        videoViewRef = this
                                                    }
                                                },
                                                modifier = Modifier.fillMaxSize(),
                                                onRelease = {
                                                    playbackPosition = it.currentPosition
                                                    videoViewRef = null
                                                }
                                            )
                                            
                                            // Left half: Double tap rewind & vertical drag brightness
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .fillMaxWidth(0.5f)
                                                    .align(Alignment.CenterStart)
                                                    .pointerInput(Unit) {
                                                        detectTapGestures(
                                                            onDoubleTap = {
                                                                videoViewRef?.let {
                                                                    val newPos = maxOf(0, it.currentPosition - 10000) // Rewind 10s
                                                                    it.seekTo(newPos)
                                                                    playbackPosition = newPos
                                                                }
                                                            }
                                                        )
                                                    }
                                                    .pointerInput(Unit) {
                                                        detectVerticalDragGestures(
                                                            onDragStart = { 
                                                                val activity = generateSequence(context) { if (it is android.content.ContextWrapper) it.baseContext else null }.firstOrNull { it is android.app.Activity } as? android.app.Activity
                                                                val initialB = activity?.window?.attributes?.screenBrightness?.takeIf { it >= 0 } ?: 0.5f
                                                                currentBrightnessState = initialB
                                                                accumulatedBrightnessDelta = 0f
                                                                showBrightnessIndicator = true
                                                            },
                                                            onDragEnd = { showBrightnessIndicator = false },
                                                            onDragCancel = { showBrightnessIndicator = false },
                                                            onVerticalDrag = { change, dragAmount ->
                                                                change.consume()
                                                                val activity = generateSequence(context) { if (it is android.content.ContextWrapper) it.baseContext else null }.firstOrNull { it is android.app.Activity } as? android.app.Activity
                                                                if (activity != null) {
                                                                    // Full screen height represents about 1.5 of total brightness span for ease
                                                                    accumulatedBrightnessDelta -= (dragAmount / 800f) 
                                                                    val newB = (currentBrightnessState + accumulatedBrightnessDelta).coerceIn(0.01f, 1f)
                                                                    val params = activity.window.attributes
                                                                    params.screenBrightness = newB
                                                                    activity.window.attributes = params
                                                                    currentBrightnessState = newB
                                                                    accumulatedBrightnessDelta = 0f // reset after applying
                                                                }
                                                            }
                                                        )
                                                    }
                                            )
                                            // Right half: Double tap forward & vertical drag volume
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .fillMaxWidth(0.5f)
                                                    .align(Alignment.CenterEnd)
                                                    .pointerInput(Unit) {
                                                        detectTapGestures(
                                                            onDoubleTap = {
                                                                videoViewRef?.let {
                                                                    val newPos = minOf(it.duration, it.currentPosition + 10000) // Forward 10s
                                                                    it.seekTo(newPos)
                                                                    playbackPosition = newPos
                                                                }
                                                            }
                                                        )
                                                    }
                                                    .pointerInput(Unit) {
                                                        detectVerticalDragGestures(
                                                            onDragStart = {
                                                                maxVolume = audioManager.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC)
                                                                currentVolumeState = audioManager.getStreamVolume(android.media.AudioManager.STREAM_MUSIC)
                                                                accumulatedVolumeDelta = 0f
                                                                showVolumeIndicator = true
                                                            },
                                                            onDragEnd = { showVolumeIndicator = false },
                                                            onDragCancel = { showVolumeIndicator = false },
                                                            onVerticalDrag = { change, dragAmount ->
                                                                change.consume()
                                                                // Full screen height represents about maxVolume * 1.5 for ease
                                                                accumulatedVolumeDelta -= (dragAmount / 800f) * maxVolume
                                                                if (abs(accumulatedVolumeDelta) >= 1f) {
                                                                    val steps = accumulatedVolumeDelta.toInt()
                                                                    val newVol = (currentVolumeState + steps).coerceIn(0, maxVolume)
                                                                    audioManager.setStreamVolume(android.media.AudioManager.STREAM_MUSIC, newVol, 0)
                                                                    currentVolumeState = newVol
                                                                    accumulatedVolumeDelta -= steps
                                                                }
                                                            }
                                                        )
                                                    }
                                            )
                                            
                                            // Indicators
                                            if (showBrightnessIndicator) {
                                                Column(
                                                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 32.dp).background(Color.Black.copy(alpha=0.6f), RoundedCornerShape(16.dp)).padding(16.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    Icon(androidx.compose.material.icons.Icons.Default.WbSunny, contentDescription = "Brightness", tint = Color.White)
                                                    Text("${(currentBrightnessState * 100).toInt()}%", color = Color.White, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                            if (showVolumeIndicator) {
                                                Column(
                                                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 32.dp).background(Color.Black.copy(alpha=0.6f), RoundedCornerShape(16.dp)).padding(16.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    Icon(if (currentVolumeState == 0) androidx.compose.material.icons.Icons.Default.VolumeOff else androidx.compose.material.icons.Icons.Default.VolumeUp, contentDescription = "Volume", tint = Color.White)
                                                    Text("${(currentVolumeState.toFloat() / maxVolume * 100).toInt()}%", color = Color.White, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }"""

if target in content:
    content = content.replace(target, replacement)
    
    if "import androidx.compose.material.icons.filled.WbSunny" not in content:
        lines = content.split("\n")
        lines.insert(300, "import androidx.compose.material.icons.filled.WbSunny")
        lines.insert(300, "import kotlin.math.abs")
        content = "\n".join(lines)
        
    with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
        f.write(content)
    print("Updated Video Viewer with Smart Gestures")
else:
    print("Target not found")
