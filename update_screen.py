import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target = """                                        // Audio Player with beautiful Vinyl Record Layout
                                        var isPlaying by remember { mutableStateOf(false) }
                                        var currentPosition by remember { mutableStateOf(0f) }
                                        var duration by remember { mutableStateOf(1f) }
                                        val context = LocalContext.current
                                        val audioPlayerScope = rememberCoroutineScope()
                                        
                                        var isPrepared by remember(path) { mutableStateOf(false) }
                                        val mediaPlayer = remember(path) {
                                            try {
                                                val mp = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                                    android.media.MediaPlayer(context)
                                                } else {
                                                    android.media.MediaPlayer()
                                                }
                                                mp.setDataSource(path)
                                                mp.prepare()
                                                duration = mp.duration.toFloat()
                                                isPrepared = true
                                                mp.setOnCompletionListener {
                                                    isPlaying = false
                                                    currentPosition = 0f
                                                    try {
                                                        mp.seekTo(0)
                                                    } catch (e: Exception) {}
                                                }
                                                mp
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                                null
                                            }
                                        }
                                        
                                        // Pause if swiped away
                                        LaunchedEffect(pagerState.currentPage) {
                                            if (pagerState.currentPage != page && isPlaying) {
                                                isPlaying = false
                                                try {
                                                    mediaPlayer?.pause()
                                                } catch (e: Exception) {}
                                            }
                                        }
                                        LaunchedEffect(isPlaying, isPrepared) {
                                            while (isPlaying && isPrepared) {
                                                try {
                                                    currentPosition = (mediaPlayer?.currentPosition ?: 0).toFloat()
                                                } catch (e: Exception) {}
                                                kotlinx.coroutines.delay(250)
                                            }
                                        }
                                        DisposableEffect(path) {
                                            onDispose {
                                                try {
                                                    mediaPlayer?.stop()
                                                } catch (e: Exception) {}
                                                try {
                                                    mediaPlayer?.release()
                                                } catch (e: Exception) {}
                                            }
                                        }
                                        
                                        val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
                                        val isBackgroundEnabled by viewModel.backgroundAudioPlaybackEnabled.collectAsStateWithLifecycle()
                                        DisposableEffect(lifecycleOwner, isBackgroundEnabled) {
                                            val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
                                                if (event == androidx.lifecycle.Lifecycle.Event.ON_PAUSE) {
                                                    if (!isBackgroundEnabled && isPlaying) {
                                                        try {
                                                            mediaPlayer?.pause()
                                                        } catch (e: Exception) {}
                                                        isPlaying = false
                                                    }
                                                }
                                            }
                                            lifecycleOwner.lifecycle.addObserver(observer)
                                            onDispose {
                                                lifecycleOwner.lifecycle.removeObserver(observer)
                                            }
                                        }"""

replacement = """                                        // Audio Player with beautiful Vinyl Record Layout
                                        val context = LocalContext.current
                                        
                                        val isPlaying by viewModel.isAudioPlaying.collectAsStateWithLifecycle()
                                        val currentPosition by viewModel.audioPosition.collectAsStateWithLifecycle()
                                        val duration by viewModel.audioDuration.collectAsStateWithLifecycle()
                                        val isCurrentAudio = viewModel.currentAudioPlayingPath == path
                                        val actualIsPlaying = isCurrentAudio && isPlaying
                                        
                                        // Start automatically if it's the active page and we just opened it
                                        LaunchedEffect(path, pagerState.currentPage) {
                                            if (pagerState.currentPage == page && viewModel.currentAudioPlayingPath != path) {
                                                viewModel.playOrToggleAudio(path, context)
                                            }
                                        }"""

if target in content:
    content = content.replace(target, replacement)
    print("Found and replaced block 1")
else:
    print("Block 1 not found")

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
