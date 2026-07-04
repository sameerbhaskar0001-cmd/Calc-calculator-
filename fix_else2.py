import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

target = """                            }
                                // List the 3 most recently opened items (or just default first 3 files/notes)
                                val recentFiles = vaultFiles.take(3)
                                recentFiles.forEach { file ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color(0xFF161B2B).copy(alpha = 0.8f)).border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFF262D45)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = if (file.lowercase().endsWith(".jpg") || file.lowercase().endsWith(".png")) Icons.Default.Image else Icons.Default.Description,
                                                contentDescription = null,
                                                tint = Color.White.copy(alpha = 0.7f)
                                            )
                                        }
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(file.substringAfterLast('/'), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                            Text("Added recently", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                                        }
                                    }
                                }"""

content = content.replace(target, "                            }")

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
