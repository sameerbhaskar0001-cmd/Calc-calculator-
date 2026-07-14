import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

target = """                                    LinearProgressIndicator(
                                        progress = { 0.4f },
                                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                                        color = ThemePurple,
                                        trackColor = Color(0xFF090D1A),
                                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("2.5 GB Used", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                        Text("7.5 GB Free", color = TextMedium, fontSize = 13.sp)
                                    }"""

replacement = """                                    val storageInfo by viewModel.storageInfo.collectAsState()
                                    val maxStorage = 15L * 1024 * 1024 * 1024 // 15 GB
                                    val progress = if (maxStorage > 0) (storageInfo.totalBytes.toFloat() / maxStorage.toFloat()).coerceIn(0f, 1f) else 0f
                                    LinearProgressIndicator(
                                        progress = { progress },
                                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                                        color = ThemePurple,
                                        trackColor = Color(0xFF090D1A),
                                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("${storageInfo.totalUsedFormatted} Used", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                        val freeBytes = maxStorage - storageInfo.totalBytes
                                        val freeFormatted = if (freeBytes <= 0) "0 B" else {
                                            val units = arrayOf("B", "KB", "MB", "GB", "TB")
                                            val digitGroups = (java.lang.Math.log10(freeBytes.toDouble()) / java.lang.Math.log10(1024.0)).toInt()
                                            val index = if (digitGroups > 4) 4 else digitGroups
                                            val num = freeBytes / java.lang.Math.pow(1024.0, index.toDouble())
                                            String.format(java.util.Locale.US, "%.1f %s", num, units[index])
                                        }
                                        Text("$freeFormatted Free", color = TextMedium, fontSize = 13.sp)
                                    }"""

text = text.replace(target, replacement)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)

