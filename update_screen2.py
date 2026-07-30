import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target2 = """                                            LaunchedEffect(isPlaying) {
                                                while (isPlaying) {"""
replacement2 = """                                            LaunchedEffect(actualIsPlaying) {
                                                while (actualIsPlaying) {"""
content = content.replace(target2, replacement2)


target3 = """                                                onValueChange = {
                                                    currentPosition = it
                                                    if (isPrepared) {
                                                        try {
                                                            mediaPlayer?.seekTo(it.toInt())
                                                        } catch (e: Exception) {}
                                                    }
                                                },"""
replacement3 = """                                                onValueChange = {
                                                    if (isCurrentAudio) {
                                                        viewModel.seekAudio(it)
                                                    }
                                                },"""
content = content.replace(target3, replacement3)

target4 = """                                                IconButton(
                                                    onClick = {
                                                        viewModel.triggerKeypressEffects(context)
                                                        if (mediaPlayer != null && isPrepared) {
                                                            if (isPlaying) {
                                                                try {
                                                                    mediaPlayer.pause()
                                                                } catch (e: Exception) {}
                                                                isPlaying = false
                                                            } else {
                                                                try {
                                                                    mediaPlayer.start()
                                                                } catch (e: Exception) {}
                                                                isPlaying = true
                                                            }
                                                        } else {
                                                            Toast.makeText(context, "Audio file failed to load", Toast.LENGTH_SHORT).show()
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .size(64.dp)
                                                        .background(ThemePurple, CircleShape)
                                                ) {
                                                    Icon(
                                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                        contentDescription = if (isPlaying) "Pause" else "Play",
                                                        tint = if (IsLightColor) Color(0xFF1B2031) else Color.White,
                                                        modifier = Modifier.size(36.dp)
                                                    )
                                                }"""
replacement4 = """                                                IconButton(
                                                    onClick = {
                                                        viewModel.triggerKeypressEffects(context)
                                                        viewModel.playOrToggleAudio(path, context)
                                                    },
                                                    modifier = Modifier
                                                        .size(64.dp)
                                                        .background(ThemePurple, CircleShape)
                                                ) {
                                                    Icon(
                                                        imageVector = if (actualIsPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                        contentDescription = if (actualIsPlaying) "Pause" else "Play",
                                                        tint = if (IsLightColor) Color(0xFF1B2031) else Color.White,
                                                        modifier = Modifier.size(36.dp)
                                                    )
                                                }"""
content = content.replace(target4, replacement4)


with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
