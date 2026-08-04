with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target = """                                        var isDragging by remember { mutableStateOf(false) }
                                        val window = (context as? android.app.Activity)?.window

                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .pointerInput(Unit) {"""

# I will replace the whole Box with a call to a new composable `VideoPlayerWithControls`
# And then append the composable at the end of the file.

# Let's find the full block first.
import re
match = re.search(r'var isDragging by remember { mutableStateOf\(false\) }.*?if \(showVolumeIndicator\) \{.*?\}\n\s*\}\n\s*\}', content, re.DOTALL)
if match:
    # Verify what we captured
    print("Found match, length:", len(match.group(0)))
else:
    print("Not found")
