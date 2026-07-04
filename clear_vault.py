import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

target_start = '// Status Card'
target_end = 'if (vaultFiles.isEmpty() && vaultNotes.isEmpty()) {'

idx_start = content.find(target_start)
idx_end = content.find(target_end)

if idx_start == -1 or idx_end == -1:
    print("Could not find targets")
    exit(1)

# Find the end of the recent activity block
target_end_block = """                                }
                            }"""

idx_end_block = content.find(target_end_block, idx_end) + len(target_end_block)

new_content = """// Main Vault Grid
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                VaultFolderCard(
                                    title = "Hidden Photos", 
                                    count = "12 Items", 
                                    icon = Icons.Default.Image, 
                                    iconTint = Color(0xFF0EA5E9),
                                    modifier = Modifier.weight(1f)
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Photos & Videos" 
                                }
                                VaultFolderCard(
                                    title = "Private Notes", 
                                    count = "3 Items", 
                                    icon = Icons.Default.List, 
                                    iconTint = Color(0xFFF97316),
                                    modifier = Modifier.weight(1f)
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Notes" 
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                VaultFolderCard(
                                    title = "Documents", 
                                    count = "0 Items", 
                                    icon = Icons.Default.Description, 
                                    iconTint = Color(0xFFEAB308),
                                    modifier = Modifier.weight(1f)
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Documents" 
                                }
                                VaultFolderCard(
                                    title = "Web Browser", 
                                    count = "Secure", 
                                    icon = Icons.Default.Language, 
                                    iconTint = Color(0xFF8B5CF6),
                                    modifier = Modifier.weight(1f)
                                ) { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "Private Browser"
                                }
                            }
                            """

content = content[:idx_start] + new_content + content[idx_end_block:]

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
