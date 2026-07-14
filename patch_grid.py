import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

# First replace the EnhancedVaultCard for Notes to Audio
# But wait, there are multiple lines. I'll just find the exact block.

start_str = '                                EnhancedVaultCard(\n                                    title = "Notes",'
end_str = '                                )\n                            }'

start_idx = text.find(start_str)
end_idx = text.find(end_str, start_idx) + len(end_str)

if start_idx != -1 and end_idx != -1:
    target = text[start_idx:end_idx]
    
    replacement = """                                EnhancedVaultCard(
                                    title = "Audio",
                                     count = let { val c = vaultFiles.count { it.contains("|||audio/") }; "$c ${if (c == 1) "Item" else "Items"}" },
                                     icon = Icons.Default.AudioFile,
                                    modifier = Modifier.weight(1f),
                                    previewContent = {
                                        val latestAudio = vaultFiles.firstOrNull { it.contains("|||audio/") }
                                        if (latestAudio != null) {
                                            val parts = latestAudio.split("|||")
                                            val title = if (parts.size >= 3) parts[2] else latestAudio.split("|||")[0].substringAfterLast('/')
                                            Row(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Icon(Icons.Default.AudioFile, contentDescription = null, tint = ThemePurple, modifier = Modifier.size(16.dp))
                                                Text(cleanDisplayName(title, "audio", parts.getOrNull(0) ?: ""), color = Color.White, fontSize = 10.sp, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                            }
                                        } else {
                                            Row(
                                                modifier = Modifier.fillMaxSize().padding(8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Box(modifier = Modifier.width(4.dp).height(12.dp).clip(CircleShape).background(Color(0xFF262D45)))
                                                Box(modifier = Modifier.width(4.dp).height(20.dp).clip(CircleShape).background(ThemePurple.copy(alpha=0.5f)))
                                                Box(modifier = Modifier.width(4.dp).height(12.dp).clip(CircleShape).background(Color(0xFF262D45)))
                                                Box(modifier = Modifier.width(4.dp).height(8.dp).clip(CircleShape).background(Color(0xFF262D45)))
                                            }
                                        }
                                    },
                                    onClick = { 
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Music & Audio" 
                                    }
                                )
                            }
                            
                            // Notes full width
                            EnhancedVaultCard(
                                title = "Notes",
                                 count = let { val c = vaultNotes.size; "$c ${if (c == 1) "Item" else "Items"}" },
                                 icon = Icons.Default.List,
                                modifier = Modifier.fillMaxWidth(),
                                previewContent = {
                                    val latestNote = vaultNotes.firstOrNull()
                                    if (latestNote != null) {
                                        val parts = latestNote.split("|||")
                                        val body = if (parts.size >= 3) parts[2] else ""
                                        Text(parseRichTextToAnnotatedString(body), color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, maxLines = 2, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis, modifier = Modifier.padding(8.dp).fillMaxWidth(), lineHeight = 12.sp)
                                    } else {
                                        Column(
                                            modifier = Modifier.fillMaxSize().padding(8.dp),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(modifier = Modifier.fillMaxWidth(0.5f).height(6.dp).clip(CircleShape).background(ThemePurple.copy(alpha = 0.4f)))
                                            Box(modifier = Modifier.fillMaxWidth(0.9f).height(4.dp).clip(CircleShape).background(Color(0xFF262D45)))
                                            Box(modifier = Modifier.fillMaxWidth(0.8f).height(4.dp).clip(CircleShape).background(Color(0xFF262D45)))
                                        }
                                    }
                                },
                                onClick = { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Notes" 
                                }
                            )"""
    
    text = text.replace(target, replacement)
    with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
        f.write(text)
    print("Patched successfully")
else:
    print("Target not found")
