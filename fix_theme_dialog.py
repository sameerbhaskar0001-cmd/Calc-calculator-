import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

bad_dialog = """    if (showThemeDialog) {
        val selectedTheme by viewModel.selectedTheme.collectAsState()
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = {
                Text(
                    text = viewModel.t("app_theme"),
                    color = TextDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            containerColor = BrandBg,
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("OK", color = ThemePurple, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth().heightIn(max = 350.dp)
                ) {
                    items(com.example.ui.theme.AppTheme.values()) { theme ->
                        val isSelected = selectedTheme == theme
                        val themeColors = when (theme) {
                            com.example.ui.theme.AppTheme.CLASSIC_LAVENDER -> com.example.ui.theme.ClassicLavenderColors
                            com.example.ui.theme.AppTheme.SUNSET_ROSE -> com.example.ui.theme.SunsetRoseColors
                            com.example.ui.theme.AppTheme.NORDIC_EMERALD -> com.example.ui.theme.NordicEmeraldColors
                            com.example.ui.theme.AppTheme.OCEAN_BREEZE -> com.example.ui.theme.OceanBreezeColors
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) ThemePurple.copy(alpha = 0.15f) else Color.Transparent)
                                .border(
                                    1.dp,
                                    if (isSelected) ThemePurple else Color.Gray.copy(alpha = 0.2f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    viewModel.triggerKeypressEffects(context)
                                    viewModel.setSelectedTheme(theme)
                                    showThemeDialog = false
                                }
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(theme.flag, fontSize = 24.sp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = theme.displayName,
                                    color = if (isSelected) ThemePurple else TextDark,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                // Swatch circles representing colors
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.size(12.dp).clip(CircleShape).background(themeColors.brandBg)
                                    )
                                    Box(
                                        modifier = Modifier.size(12.dp).clip(CircleShape).background(themeColors.themePurple)
                                    )
                                    Box(
                                        modifier = Modifier.size(12.dp).clip(CircleShape).background(themeColors.themeLightPurple)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }"""

good_dialog = """    if (showThemeDialog) {
        val selectedTheme by viewModel.selectedTheme.collectAsState()
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = {
                Text(
                    text = viewModel.t("app_theme"),
                    color = TextDark,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
            },
            containerColor = BrandBg,
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Cancel", color = ThemePurple, fontWeight = FontWeight.Medium)
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)
                ) {
                    items(com.example.ui.theme.AppTheme.values()) { theme ->
                        val isSelected = selectedTheme == theme

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.triggerKeypressEffects(context)
                                    viewModel.setSelectedTheme(theme)
                                    showThemeDialog = false
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(theme.previewColor)
                                )
                                Text(
                                    text = theme.displayName,
                                    color = TextDark,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = ThemePurple,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        )
    }"""

content = content.replace(bad_dialog, good_dialog)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
