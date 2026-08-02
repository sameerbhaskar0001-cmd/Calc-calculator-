import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

# We need to find the "More" -> { branch and replace its content down to "Storage" -> {
match_start = re.search(r'"More"\s*->\s*\{\s*Column\(\s*modifier\s*=\s*Modifier\s*\.weight\(1f\)\s*\.fillMaxWidth\(\)\s*\.padding\(horizontal\s*=\s*16\.dp\)\s*\.verticalScroll\(rememberScrollState\(\)\),\s*verticalArrangement\s*=\s*Arrangement\.spacedBy\(16\.dp\)\s*\)\s*\{', content)

if not match_start:
    print("Could not find start of More tab.")
    exit(1)

# Now find "Storage" -> {
match_end = re.search(r'\}\s*\}\s*"Storage"\s*->\s*\{', content[match_start.end():])

if not match_end:
    print("Could not find end of More tab.")
    exit(1)

start_idx = match_start.end()
end_idx = match_start.end() + match_end.start()

new_block = """
                        Spacer(modifier = Modifier.height(8.dp))

                        SettingsGroup(title = "GENERAL") {
                            SettingsActionRow(
                                title = "Vault Security",
                                subtitle = "Authentication, Protection, Panic",
                                icon = Icons.Default.Security,
                                iconTint = Color(0xFF635BFF),
                                onClick = { activeSection = "Security" }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsActionRow(
                                title = "Storage Manager",
                                subtitle = "Manage space and Recycle Bin",
                                icon = Icons.Default.Storage,
                                iconTint = Color(0xFF00E676),
                                onClick = { activeSection = "Storage" }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsActionRow(
                                title = "App Privacy",
                                subtitle = "Intruder monitoring, Stealth",
                                icon = Icons.Default.PrivacyTip,
                                iconTint = Color(0xFFFF9800),
                                onClick = { activeSection = "App Privacy" }
                            )
                        }

                        SettingsGroup(title = "CUSTOMIZATION") {
                            SettingsActionRow(
                                title = "Decoy Space",
                                subtitle = "Set up a fake vault",
                                icon = Icons.Default.LockReset,
                                iconTint = Color(0xFFE91E63),
                                onClick = { activeSection = "Decoy Space" }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsActionRow(
                                title = "App Disguise",
                                subtitle = "Change app icon & name",
                                icon = Icons.Default.Calculate,
                                iconTint = Color(0xFF00BCD4),
                                onClick = { activeSection = "App Disguise" }
                            )
                        }

                        SettingsGroup(title = "DATA & CLOUD") {
                            SettingsActionRow(
                                title = "Cloud Backup",
                                subtitle = "Sync your vault to Google Drive",
                                icon = Icons.Default.CloudSync,
                                iconTint = Color(0xFF2979FF),
                                onClick = { activeSection = "Backup" }
                            )
                        }

                        SettingsGroup(title = "ABOUT") {
                            SettingsActionRow(
                                title = "My Profile",
                                subtitle = "Account details and preferences",
                                icon = Icons.Default.Person,
                                iconTint = Color(0xFF9E9E9E),
                                onClick = { activeSection = "Profile" }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsActionRow(
                                title = "About App",
                                subtitle = "Version, Licenses, Support",
                                icon = Icons.Default.Info,
                                iconTint = Color(0xFF9E9E9E),
                                onClick = { activeSection = "About" }
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))
"""

new_content = content[:start_idx] + new_block + content[end_idx:]

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(new_content)

print("More tab restored successfully.")
