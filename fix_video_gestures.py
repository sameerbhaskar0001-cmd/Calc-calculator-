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
                                            
                                            // Removed custom gesture overlays to allow MediaController to show.
                                        }"""

replacement = """                                        var isDragging by remember { mutableStateOf(false) }
                                        val window = (context as? android.app.Activity)?.window

                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .pointerInput(Unit) {
                                                    detectTapGestures(
                                                        onDoubleTap = { offset ->
                                                            videoViewRef?.let { vv ->
                                                                val isRightHalf = offset.x > size.width / 2
                                                                val currentPos = vv.currentPosition
                                                                val newPos = if (isRightHalf) currentPos + 10000 else currentPos - 10000
                                                                vv.seekTo(newPos.coerceIn(0, vv.duration))
                                                                playbackPosition = newPos.coerceIn(0, vv.duration)
                                                                vv.start()
                                                                // Haptic
                                                                try {
                                                                    (context as? android.app.Activity)?.window?.decorView?.performHapticFeedback(android.view.HapticFeedbackConstants.CLOCK_TICK)
                                                                } catch (e: Exception) {}
                                                            }
                                                        },
                                                        onTap = {
                                                            videoViewRef?.let { vv ->
                                                                // Toggle play/pause or show media controller
                                                                if (vv.isPlaying) vv.pause() else vv.start()
                                                            }
                                                        }
                                                    )
                                                }
                                                .pointerInput(Unit) {
                                                    detectDragGestures(
                                                        onDragStart = { isDragging = true },
                                                        onDragEnd = {
                                                            isDragging = false
                                                            showBrightnessIndicator = false
                                                            showVolumeIndicator = false
                                                            accumulatedBrightnessDelta = 0f
                                                            accumulatedVolumeDelta = 0f
                                                        },
                                                        onDragCancel = {
                                                            isDragging = false
                                                            showBrightnessIndicator = false
                                                            showVolumeIndicator = false
                                                            accumulatedBrightnessDelta = 0f
                                                            accumulatedVolumeDelta = 0f
                                                        },
                                                        onDrag = { change, dragAmount ->
                                                            change.consume()
                                                            val isLeftSide = change.position.x < size.width / 2
                                                            val deltaY = -dragAmount.y // Negative because up is positive delta for increase

                                                            if (isLeftSide) {
                                                                // Brightness
                                                                accumulatedBrightnessDelta += deltaY
                                                                if (abs(accumulatedBrightnessDelta) > 10f) {
                                                                    showBrightnessIndicator = true
                                                                    val step = if (accumulatedBrightnessDelta > 0) 0.05f else -0.05f
                                                                    window?.let { win ->
                                                                        val lp = win.attributes
                                                                        val currentVal = if (lp.screenBrightness < 0) 0.5f else lp.screenBrightness
                                                                        val newVal = (currentVal + step).coerceIn(0.01f, 1f)
                                                                        lp.screenBrightness = newVal
                                                                        win.attributes = lp
                                                                        currentBrightnessState = newVal
                                                                    }
                                                                    accumulatedBrightnessDelta = 0f
                                                                }
                                                            } else {
                                                                // Volume
                                                                accumulatedVolumeDelta += deltaY
                                                                if (abs(accumulatedVolumeDelta) > 15f) {
                                                                    showVolumeIndicator = true
                                                                    maxVolume = audioManager.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC)
                                                                    val currentVol = audioManager.getStreamVolume(android.media.AudioManager.STREAM_MUSIC)
                                                                    val step = if (accumulatedVolumeDelta > 0) 1 else -1
                                                                    val newVol = (currentVol + step).coerceIn(0, maxVolume)
                                                                    audioManager.setStreamVolume(android.media.AudioManager.STREAM_MUSIC, newVol, 0)
                                                                    currentVolumeState = newVol
                                                                    accumulatedVolumeDelta = 0f
                                                                }
                                                            }
                                                        }
                                                    )
                                                }
                                        ) {
                                            AndroidView(
                                                factory = { ctx ->
                                                    android.widget.VideoView(ctx).apply {
                                                        setVideoPath(path)
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
                                            
                                            // Gesture Overlays
                                            if (showBrightnessIndicator) {
                                                Box(
                                                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 32.dp).background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp)).padding(16.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                        Icon(Icons.Default.WbSunny, contentDescription = "Brightness", tint = Color.White, modifier = Modifier.size(32.dp))
                                                        Spacer(modifier = Modifier.height(8.dp))
                                                        LinearProgressIndicator(progress = { currentBrightnessState }, modifier = Modifier.width(4.dp).height(64.dp), color = Color.White, trackColor = Color.Gray)
                                                    }
                                                }
                                            }
                                            if (showVolumeIndicator) {
                                                Box(
                                                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 32.dp).background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp)).padding(16.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                        Icon(Icons.Default.VolumeUp, contentDescription = "Volume", tint = Color.White, modifier = Modifier.size(32.dp))
                                                        Spacer(modifier = Modifier.height(8.dp))
                                                        LinearProgressIndicator(progress = { if (maxVolume > 0) currentVolumeState.toFloat() / maxVolume else 0f }, modifier = Modifier.width(4.dp).height(64.dp), color = Color.White, trackColor = Color.Gray)
                                                    }
                                                }
                                            }
                                        }"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
