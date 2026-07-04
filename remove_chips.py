import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

target = """                    // Material 3 Filter Chips for Sections
                    val decoyActive by viewModel.decoyActive.collectAsState()
                    val sections = if (decoyActive) {
                        listOf("Timeline", "Notes", "Photos & Videos", "Documents", "Private Browser", "Recently Deleted", "Settings")
                    } else {
                        listOf("Timeline", "Notes", "Photos & Videos", "Documents", "Private Browser", "Explore", "Recently Deleted", "Settings")
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        sections.forEach { label ->
                            val isSelected = activeSection == label
                            val chipBg = if (isSelected) ThemePurple else Color(0xFF1B2031)
                            val chipText = if (isSelected) Color.White else Color(0xFF8B92A5)
                            val chipBorder = if (isSelected) Color.Transparent else Color(0xFF383F56)

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(chipBg)
                                    .border(1.dp, chipBorder, RoundedCornerShape(12.dp))
                                    .clickable {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = label
                                    }
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when(label) {
                                        "Notes" -> viewModel.t("notes")
                                        "Photos & Videos" -> viewModel.t("photos_videos")
                                        "Documents" -> viewModel.t("documents")
                                        "Private Browser" -> viewModel.t("private_browser")
                                        "Explore" -> viewModel.t("explore")
                                        "Recently Deleted" -> viewModel.t("recently_deleted")
                                        "Settings" -> viewModel.t("settings")
                                        else -> label
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = chipText,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }"""

content = content.replace(target, "")

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
