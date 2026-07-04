import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

import_statement = "import androidx.compose.material3.TextButton\nimport androidx.compose.material3.DropdownMenu\nimport androidx.compose.material3.DropdownMenuItem\n"
content = content.replace("import androidx.compose.material3.TextButton\n", import_statement)

old_header = """            // Header Bar
            if (activeTab != ActiveTab.VAULT) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(ThemePurple),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (activeTab == ActiveTab.VAULT) Icons.Default.Lock else Icons.Default.Calculate,
                                contentDescription = "Calculator Icon",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (activeTab == ActiveTab.VAULT) viewModel.t("secure_vault") else viewModel.t("app_title"),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextDark,
                                letterSpacing = (-0.5).sp
                            )
                        }
                    }

                    // Theme switcher button on top right of home page
                    IconButton(
                        onClick = {
                            viewModel.triggerKeypressEffects(context)
                            showThemeDialog = true
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(ThemePurple.copy(alpha = 0.12f))
                            .testTag("theme_switcher_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Change Theme",
                            tint = ThemePurple,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }"""

new_header = """            // Header Bar
            if (activeTab != ActiveTab.VAULT) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = "Calculator Icon",
                            tint = TextMedium,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = viewModel.t("app_title"),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = TextMedium,
                            letterSpacing = 0.sp
                        )
                    }

                    // Theme switcher button / Overflow menu
                    var showMenu by remember { mutableStateOf(false) }
                    Box {
                        IconButton(
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                showMenu = true
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options",
                                tint = TextMedium,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(BrandBg)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Theme", color = TextDark) },
                                onClick = {
                                    showMenu = false
                                    showThemeDialog = true
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Palette,
                                        contentDescription = null,
                                        tint = TextDark
                                    )
                                }
                            )
                        }
                    }
                }
            }"""
if old_header in content:
    content = content.replace(old_header, new_header)
    print("Replaced Header!")
else:
    print("Header not found")

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
