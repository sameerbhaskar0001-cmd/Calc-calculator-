                                                    "#FFD54F" to Color(0xFFFFD54F),
                                                    "#81C784" to Color(0xFF81C784),
                                                    "#64B5F6" to Color(0xFF64B5F6),
                                                    "#F06292" to Color(0xFFF06292),
                                                    "#000000" to Color.Transparent
                                                )
                                                bgColors.forEach { (hex, color) ->
                                                    Box(
                                                        modifier = Modifier
                                                            .size(26.dp)
                                                            .clip(CircleShape)
                                                            .background(if (color == Color.Transparent) Color.White.copy(alpha = 0.15f) else color)
                                                            .border(1.5.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                                                            .clickable {
                                                                viewModel.triggerKeypressEffects(context)
                                                                if (hex != "#000000") {
                                                                    editedNoteContentValue = applyTagToSelection(editedNoteContentValue, "<bg hex=\"$hex\">", "</bg>")
                                                                } else {
                                                                    editedNoteContentValue = applyTagToSelection(editedNoteContentValue, "<bg hex=\"transparent\">", "</bg>")
                                                                }
                                                                viewModel.updateLastInteraction()
                                                                showBgColorOptions = false
                                                            }
                                                    ) {
                                                        if (color == Color.Transparent) {
                                                            Icon(Icons.Default.Clear, contentDescription = "Clear Highlight", tint = Color.White, modifier = Modifier.size(12.dp).align(Alignment.Center))
                                                        }
                                                    }
                                                }
                                            }
                                        }
