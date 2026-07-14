import sys
import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

# Add showDeleteConfirm state
pattern_state = r'(var newFolderName by remember \{ mutableStateOf\(""\) \})'
replacement_state = r'\1\n        var showDeleteConfirm by remember { mutableStateOf<String?>(null) }'
text = re.sub(pattern_state, replacement_state, text, count=1)

# Replace the "Delete Permanently" button click handler
pattern_delete_btn = r'(Button\(\n\s*onClick = \{\n\s*viewModel\.triggerKeypressEffects\(context\)\n\s*val deleted = viewModel\.deletePermanentlyFromRecent\(recentStr\)\n\s*if\s*\(deleted\)\s*\{\n\s*android\.widget\.Toast\.makeText\(context, "Permanently Deleted", android\.widget\.Toast\.LENGTH_SHORT\)\.show\(\)\n\s*\}\s*else\s*\{\n\s*android\.widget\.Toast\.makeText\(context, "Failed to delete", android\.widget\.Toast\.LENGTH_SHORT\)\.show\(\)\n\s*\}\n\s*\},)'
replacement_delete_btn = r'''Button(
                                                    onClick = {
                                                        viewModel.triggerKeypressEffects(context)
                                                        showDeleteConfirm = recentStr
                                                    },'''
text = re.sub(pattern_delete_btn, replacement_delete_btn, text)

# Inject the AlertDialog at the end of the activeSection == "Recently Deleted" block
pattern_end_recent = r'(\s*\) \{\n\s*Icon\(Icons\.Default\.DeleteForever, contentDescription = "Delete Permanently", modifier = Modifier\.size\(14\.dp\)\)\n\s*Spacer\(modifier = Modifier\.width\(4\.dp\)\)\n\s*Text\("Delete Permanently", fontSize = 11\.sp, color = Color\.White\)\n\s*\}\n\s*\}\n\s*\}\n\s*\}\n\s*\}\n\s*\}\n\s*\}\n\s*\})'
replacement_end_recent = r'''\1
                        if (showDeleteConfirm != null) {
                            androidx.compose.material3.AlertDialog(
                                onDismissRequest = { showDeleteConfirm = null },
                                title = { Text("Delete Permanently?", fontWeight = FontWeight.Bold, color = TextDark) },
                                text = { Text("This action cannot be undone.", color = TextMedium) },
                                containerColor = Color(0xFF1B2031),
                                titleContentColor = Color.White,
                                textContentColor = Color.White.copy(alpha = 0.8f),
                                confirmButton = {
                                    androidx.compose.material3.TextButton(
                                        onClick = {
                                            viewModel.triggerKeypressEffects(context)
                                            val deleted = viewModel.deletePermanentlyFromRecent(showDeleteConfirm!!)
                                            if (deleted) {
                                                coroutineScope.launch { snackbarHostState.showSnackbar("Permanently Deleted") }
                                            }
                                            showDeleteConfirm = null
                                        },
                                        colors = androidx.compose.material3.ButtonDefaults.textButtonColors(contentColor = Color(0xFFFF6B6B))
                                    ) {
                                        Text("Delete", fontWeight = FontWeight.Bold)
                                    }
                                },
                                dismissButton = {
                                    androidx.compose.material3.TextButton(onClick = { showDeleteConfirm = null }) {
                                        Text("Cancel", color = Color.White.copy(alpha = 0.7f))
                                    }
                                }
                            )
                        }'''
text = re.sub(pattern_end_recent, replacement_end_recent, text)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)

print("Done")
