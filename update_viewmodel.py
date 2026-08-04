import re

with open("app/src/main/java/com/example/CalculatorViewModel.kt", "r") as f:
    content = f.read()

new_method = """    fun triggerCalculatorKeypressEffects(context: android.content.Context, key: String) {
        // play native click sound
        if (_soundEnabled.value) {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? android.media.AudioManager
            audioManager?.playSoundEffect(android.media.AudioManager.FX_KEY_CLICK)
        }

        // play haptic feedback based on key type
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? android.os.Vibrator
        if (vibrator != null && vibrator.hasVibrator()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                // API 29+ (Android 10+)
                val effectId = when {
                    key == "=" -> android.os.VibrationEffect.EFFECT_HEAVY_CLICK
                    key.matches(Regex("[0-9.]")) -> android.os.VibrationEffect.EFFECT_TICK
                    else -> android.os.VibrationEffect.EFFECT_CLICK // Medium tick for operators
                }
                try {
                    val effect = android.os.VibrationEffect.createPredefined(effectId)
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        val attributes = android.os.VibrationAttributes.Builder()
                            .setUsage(android.os.VibrationAttributes.USAGE_TOUCH)
                            .build()
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) { vibrator.vibrate(effect, attributes) } else { vibrator.vibrate(effect) }
                    } else {
                        vibrator.vibrate(effect)
                    }
                } catch (e: Exception) {
                    val fallbackDuration = when {
                        key == "=" -> 85L
                        key.matches(Regex("[0-9.]")) -> 18L
                        else -> 35L
                    }
                    if (fallbackDuration > 0) {
                        vibrator.vibrate(android.os.VibrationEffect.createOneShot(fallbackDuration, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
                    }
                }
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val duration = when {
                    key == "=" -> 85L
                    key.matches(Regex("[0-9.]")) -> 18L
                    else -> 35L
                }
                if (duration > 0) {
                    vibrator.vibrate(android.os.VibrationEffect.createOneShot(duration, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
                }
            } else {
                val duration = when {
                    key == "=" -> 85L
                    key.matches(Regex("[0-9.]")) -> 18L
                    else -> 35L
                }
                if (duration > 0) {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(duration)
                }
            }
        }
    }

"""

if "fun triggerCalculatorKeypressEffects" not in content:
    content = content.replace("    // --- Split Bill Methods ---", new_method + "    // --- Split Bill Methods ---")
    with open("app/src/main/java/com/example/CalculatorViewModel.kt", "w") as f:
        f.write(content)
