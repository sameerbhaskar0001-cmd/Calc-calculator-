import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target = """                                        val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
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

if target in content:
    content = content.replace(target, "")
    print("Removed DisposableEffect block")
else:
    print("Could not find DisposableEffect block")

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
