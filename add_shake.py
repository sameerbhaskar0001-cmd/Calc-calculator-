import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

# Let's find the PIN Dots Row in VaultTabLockedContent
# It is around:
#                 // Custom secure PIN Dots
#                 Row(
#                     horizontalArrangement = Arrangement.spacedBy(12.dp),
#                     verticalAlignment = Alignment.CenterVertically
#                 ) {

target_start = """                // Custom secure PIN Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {"""

new_pin_dots = """                // Custom secure PIN Dots
                var shakeOffset by remember { mutableStateOf(0f) }
                
                LaunchedEffect(pinError) {
                    if (pinError) {
                        try {
                            // Two short vibrations
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as android.os.Vibrator
                                vibrator.vibrate(android.os.VibrationEffect.createWaveform(longArrayOf(0, 50, 100, 50), -1))
                            } else {
                                val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as android.os.Vibrator
                                vibrator.vibrate(longArrayOf(0, 50, 100, 50), -1)
                            }
                        } catch (e: Exception) {}
                        
                        androidx.compose.animation.core.animate(
                            initialValue = 0f,
                            targetValue = 0f,
                            animationSpec = androidx.compose.animation.core.keyframes {
                                durationMillis = 400
                                0f at 0
                                -20f at 50
                                20f at 100
                                -20f at 150
                                20f at 200
                                -10f at 250
                                10f at 300
                                0f at 400
                            }
                        ) { value, _ ->
                            shakeOffset = value
                        }
                        pinError = false
                        pinInput = ""
                    }
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.offset(x = shakeOffset.dp)
                ) {"""

content = content.replace(target_start, new_pin_dots)

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)

