import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

target_start = '// Folders Grid'
target_end = '// RECENT ACTIVITY Header'

idx_start = content.find(target_start)
idx_end = content.find(target_end)

if idx_start == -1 or idx_end == -1:
    print("Could not find targets")
    exit(1)

new_grid = """// Folders Grid
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                VaultFolderCard(
                                    title = "Photos", 
                                    count = "${vaultFiles.count { val ext = it.substringAfterLast('.', "").lowercase(); ext in listOf("jpg", "jpeg", "png") }} Items", 
                                    icon = Icons.Default.Image, 
                                    iconTint = Color(0xFF0EA5E9),
                                    modifier = Modifier.weight(1f)
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Photos & Videos" 
                                }
                                VaultFolderCard(
                                    title = "Videos", 
                                    count = "${vaultFiles.count { val ext = it.substringAfterLast('.', "").lowercase(); ext in listOf("mp4", "mkv") }} Items", 
                                    icon = Icons.Default.PlayArrow, 
                                    iconTint = Color(0xFF8B5CF6),
                                    modifier = Modifier.weight(1f)
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Photos & Videos" 
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                VaultFolderCard(
                                    title = "Documents", 
                                    count = "${vaultFiles.count { val ext = it.substringAfterLast('.', "").lowercase(); ext !in listOf("jpg", "jpeg", "png", "mp4", "mkv", "mp3", "wav") }} Items", 
                                    icon = Icons.Default.Description, 
                                    iconTint = Color(0xFFEAB308),
                                    modifier = Modifier.weight(1f)
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Documents" 
                                }
                                VaultFolderCard(
                                    title = "Notes", 
                                    count = "${vaultNotes.size} Items", 
                                    icon = Icons.AutoMirrored.Filled.List, 
                                    iconTint = Color(0xFFF97316),
                                    modifier = Modifier.weight(1f)
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Notes" 
                                }
                            }
                            
                            """

content = content[:idx_start] + new_grid + content[idx_end:]

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)

