import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

# Define the new "More" menu
more_menu = """                "More" -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SettingsGroup(title = "SECURITY") {
                            SettingsActionRow(
                                title = "App Lock",
                                subtitle = "Lock apps with Calculator PIN",
                                icon = Icons.Default.Lock,
                                iconTint = Color(0xFF2979FF),
                                onClick = { activeSection = "App Lock" }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsActionRow(
                                title = "Hide Apps",
                                subtitle = "Hide apps from launcher",
                                icon = Icons.Default.Visibility,
                                iconTint = Color(0xFF00E676),
                                onClick = { activeSection = "Hide Apps" }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsActionRow(
                                title = "Intruder Detection",
                                subtitle = "Catch snoops trying to unlock",
                                icon = Icons.Default.Warning,
                                iconTint = Color(0xFFFF9100),
                                onClick = { activeSection = "Intruder Alerts" }
                            )
                        }

                        SettingsGroup(title = "VAULT SETTINGS") {
                            SettingsActionRow(
                                title = "Security Settings",
                                subtitle = "Stealth & panic options",
                                icon = Icons.Default.Shield,
                                iconTint = Color(0xFFE57373),
                                onClick = { activeSection = "Security Settings" }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsActionRow(
                                title = "Fake Vault",
                                subtitle = "Create a decoy space",
                                icon = Icons.Default.Folder,
                                iconTint = Color(0xFFAB47BC),
                                onClick = { activeSection = "Fake Vault" }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsActionRow(
                                title = "Change PIN",
                                subtitle = "Update your vault passcode",
                                icon = Icons.Default.Calculate,
                                iconTint = Color(0xFF26C6DA),
                                onClick = { activeSection = "Change PIN" }
                            )
                        }
                        
                        SettingsGroup(title = "DATA & ABOUT") {
                            SettingsActionRow(
                                title = "Export / Import",
                                subtitle = "Backup or restore data",
                                icon = Icons.Default.SwapVert,
                                iconTint = Color(0xFFD4E157),
                                onClick = { activeSection = "Export / Import" }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsActionRow(
                                title = "About",
                                subtitle = "App version & info",
                                icon = Icons.Default.Article,
                                iconTint = Color(0xFF8D6E63),
                                onClick = { activeSection = "About" }
                            )
                        }
                    }
                }
"""

# Now we need to parse out the contents of the old Settings (now More)
# Let's write the empty frames for the new sections
security_settings = """                "Security Settings" -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SettingsGroup(title = "STEALTH & SECURITY OPTIONS") {
                            val preventScreenshots by viewModel.preventScreenshots.collectAsState()
                            val screenDownLock by viewModel.screenDownLock.collectAsState()
                            val panicEnabled by viewModel.panicEnabled.collectAsState()
                            val biometricEnabled by viewModel.biometricEnabled.collectAsState()

                            SettingsSwitchRow(
                                title = "Biometric Unlock",
                                subtitle = "Unlock using fingerprint or face scanning",
                                icon = Icons.Default.Fingerprint,
                                iconTint = Color(0xFF2979FF),
                                checked = biometricEnabled,
                                onCheckedChange = { viewModel.setBiometricEnabled(it) }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsSwitchRow(
                                title = "Prevent screenshots",
                                subtitle = "Blocks Android screen capture & recorders",
                                icon = Icons.Default.Visibility,
                                iconTint = Color(0xFF00E676),
                                checked = preventScreenshots,
                                onCheckedChange = { viewModel.setPreventScreenshots(it) }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsSwitchRow(
                                title = "Screen down lock",
                                subtitle = "Instantly closes and locks vault face-down",
                                icon = Icons.Default.ScreenRotation,
                                iconTint = Color(0xFFFFD600),
                                checked = screenDownLock,
                                onCheckedChange = { viewModel.setScreenDownLock(it) }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsSwitchRow(
                                title = "Shake panic gesture",
                                subtitle = "Shake phone hard to quickly lock vault",
                                icon = Icons.Default.Refresh,
                                iconTint = Color(0xFFEC407A),
                                checked = panicEnabled,
                                onCheckedChange = { viewModel.setPanicEnabled(it) }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))

                            val intruderDetectionEnabled by viewModel.intruderDetectionEnabled.collectAsState()
                            SettingsSwitchRow(
                                title = "Intruder Detection",
                                subtitle = "Log incorrect passcode attempts & keys entered",
                                icon = Icons.Default.Warning,
                                iconTint = Color(0xFFFF9100),
                                checked = intruderDetectionEnabled,
                                onCheckedChange = { viewModel.setIntruderDetectionEnabled(it) }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))

                            val blurThumbnails by viewModel.blurThumbnails.collectAsState()
                            SettingsSwitchRow(
                                title = "Blur Thumbnails",
                                subtitle = "Blurs media previews to prevent shoulder-surfing",
                                icon = Icons.Default.BlurOn,
                                iconTint = Color(0xFF00E5FF),
                                checked = blurThumbnails,
                                onCheckedChange = { viewModel.setBlurThumbnails(it) }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))

                            val autoLockDuration by viewModel.autoLockDuration.collectAsState()
                            var showAutoLockDialog by remember { mutableStateOf(false) }
                            val currentDurationLabel = when (autoLockDuration) {
                                30 -> "30 seconds"
                                60 -> "1 minute"
                                120 -> "2 minutes"
                                300 -> "5 minutes"
                                600 -> "10 minutes"
                                else -> "Never"
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showAutoLockDialog = true }
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = "Auto Lock Timer",
                                    tint = Color(0xFFFFD600),
                                    modifier = Modifier.size(24.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Auto Lock Timer", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text("Instantly locks vault after inactivity", color = TextMedium, fontSize = 11.sp)
                                }
                                Surface(
                                    color = ThemePurple.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = currentDurationLabel,
                                        color = ThemePurple,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            if (showAutoLockDialog) {
                                AlertDialog(
                                    onDismissRequest = { showAutoLockDialog = false },
                                    title = { Text("Select Auto Lock Timer", color = TextDark) },
                                    text = {
                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            listOf(
                                                -1 to "Never (Disable)",
                                                30 to "30 seconds",
                                                60 to "1 minute",
                                                120 to "2 minutes",
                                                300 to "5 minutes",
                                                600 to "10 minutes"
                                            ).forEach { (seconds, label) ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            viewModel.setAutoLockDuration(seconds)
                                                            showAutoLockDialog = false
                                                        }
                                                        .padding(vertical = 12.dp, horizontal = 8.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(label, color = TextDark, fontSize = 14.sp)
                                                    if (autoLockDuration == seconds) {
                                                        Icon(Icons.Default.Check, contentDescription = "Selected", tint = ThemePurple, modifier = Modifier.size(18.dp))
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    confirmButton = {
                                        TextButton(onClick = { showAutoLockDialog = false }) {
                                            Text("Close", color = ThemePurple)
                                        }
                                    },
                                    containerColor = Color.White,
                                    shape = RoundedCornerShape(16.dp)
                                )
                            }
                        }

                        // Panic actions directly visible if panic is enabled
                        val panicEnabled by viewModel.panicEnabled.collectAsState()
                        if (panicEnabled) {
                            val panicAction by viewModel.panicAction.collectAsState()
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031))
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = "PANIC ACTION CONFIGURATION",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFEC407A)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        listOf("lock" to "Lock Vault Only", "home" to "Lock & Go Home").forEach { (actionKey, labelText) ->
                                            val isSelected = panicAction == actionKey
                                            Card(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clickable {
                                                        viewModel.triggerKeypressEffects(context)
                                                        viewModel.setPanicAction(actionKey)
                                                    },
                                                shape = RoundedCornerShape(10.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = if (isSelected) ThemePurple.copy(alpha = 0.2f) else Color(0xFF161C2C)
                                                ),
                                                border = BorderStroke(
                                                    1.dp,
                                                    if (isSelected) ThemePurple else Color(0xFF383F56)
                                                )
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(12.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = labelText,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isSelected) ThemePurple else Color(0xFF8B92A5)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    
                                    Button(
                                        onClick = {
                                            viewModel.triggerKeypressEffects(context)
                                            viewModel.lockVault()
                                            if (panicAction == "lock") {
                                                android.widget.Toast.makeText(context, "Virtual Panic: Vault locked successfully!", android.widget.Toast.LENGTH_LONG).show()
                                            } else {
                                                android.widget.Toast.makeText(context, "Virtual Panic: Vault locked & returned Home!", android.widget.Toast.LENGTH_LONG).show()
                                                val homeIntent = android.content.Intent(android.content.Intent.ACTION_MAIN).apply {
                                                    addCategory(android.content.Intent.CATEGORY_HOME)
                                                    flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                                                }
                                                context.startActivity(homeIntent)
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Test Panic Trigger (Simulator)", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // GENERAL PREFERENCES
                        SettingsGroup(title = "GENERAL PREFERENCES") {
                            val currentLangCode by viewModel.selectedLanguage.collectAsState()
                            val currentLang = TranslationProvider.languages.find { it.code == currentLangCode }
                            val currentLangDisplay = currentLang?.let { "${it.name} ${it.flag}" } ?: "English 🇺🇸"
                            SettingsActionRow(
                                title = "App Language",
                                subtitle = currentLangDisplay,
                                icon = Icons.Default.Language,
                                iconTint = Color(0xFF00B0FF),
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    showLanguageDialog = true
                                }
                            )
                        }
                    }
                }
"""

change_pin = """                "Change PIN" -> {
                    LaunchedEffect(Unit) {
                        realPasscodeInput = viewModel.getVaultPin()
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = "Passcode security", tint = Color(0xFF2979FF), modifier = Modifier.size(22.dp))
                                    Text(
                                        text = "REAL VAULT PASSCODE",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2979FF)
                                    )
                                }
                                
                                Text(
                                    text = "Configure your secret numerical passcode. Entering your real passcode unlocks your private vault.",
                                    fontSize = 11.sp,
                                    color = TextMedium,
                                    lineHeight = 15.sp
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    OutlinedTextField(
                                        value = realPasscodeInput,
                                        onValueChange = { input ->
                                            if (input.all { it.isDigit() } && input.length <= 8) {
                                                realPasscodeInput = input
                                            }
                                        },
                                        placeholder = { Text("e.g. 7777", fontSize = 12.sp, color = TextMedium) },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White,
                                            focusedBorderColor = ThemePurple,
                                            unfocusedBorderColor = Color(0xFF383F56)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                Button(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        if (realPasscodeInput.isBlank()) {
                                            android.widget.Toast.makeText(context, "Passcode cannot be empty!", android.widget.Toast.LENGTH_SHORT).show()
                                        } else {
                                            viewModel.setVaultPin(realPasscodeInput)
                                            android.widget.Toast.makeText(context, "Passcode updated successfully!", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Save Passcode", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
"""

fake_vault = """                "Fake Vault" -> {
                    LaunchedEffect(Unit) {
                        decoyPasscodeInput = viewModel.getDecoyPin()
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(Icons.Default.Folder, contentDescription = "Fake Vault", tint = Color(0xFFAB47BC), modifier = Modifier.size(22.dp))
                                    Text(
                                        text = "DECOY / GUEST PASSCODE",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFAB47BC)
                                    )
                                }
                                
                                Text(
                                    text = "Entering your decoy passcode opens a completely empty guest vault. Use this for plausible deniability if forced to open the app.",
                                    fontSize = 11.sp,
                                    color = TextMedium,
                                    lineHeight = 15.sp
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    OutlinedTextField(
                                        value = decoyPasscodeInput,
                                        onValueChange = { input ->
                                            if (input.all { it.isDigit() } && input.length <= 8) {
                                                decoyPasscodeInput = input
                                            }
                                        },
                                        placeholder = { Text("e.g. 1111", fontSize = 12.sp, color = TextMedium) },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White,
                                            focusedBorderColor = Color(0xFFE57373),
                                            unfocusedBorderColor = Color(0xFF383F56)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                Button(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        if (decoyPasscodeInput.isBlank()) {
                                            android.widget.Toast.makeText(context, "Passcode cannot be empty!", android.widget.Toast.LENGTH_SHORT).show()
                                        } else {
                                            viewModel.setDecoyPin(decoyPasscodeInput)
                                            android.widget.Toast.makeText(context, "Decoy passcode updated successfully!", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Save Decoy Passcode", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
"""

hide_apps = """                "Hide Apps" -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Visibility, contentDescription = "Hide Apps", tint = Color(0xFF00E676), modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Hide Apps",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Coming Soon: Hide any app from your phone's launcher and access them only from this vault.",
                            color = TextMedium,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
"""

export_import = """                "Export / Import" -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.SwapVert, contentDescription = "Export/Import", tint = Color(0xFFD4E157), modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Backup & Restore",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Backup all your vault contents to an encrypted archive, or restore a previous backup.",
                            color = TextMedium,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { android.widget.Toast.makeText(context, "Exporting...", android.widget.Toast.LENGTH_SHORT).show() },
                            colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            Text("Create Backup", color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { android.widget.Toast.makeText(context, "Importing...", android.widget.Toast.LENGTH_SHORT).show() },
                            modifier = Modifier.fillMaxWidth(0.8f),
                            border = BorderStroke(1.dp, ThemePurple),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ThemePurple)
                        ) {
                            Text("Restore Backup")
                        }
                    }
                }
"""

about = """                "About" -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = "App Logo", tint = ThemePurple, modifier = Modifier.size(80.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Calculator Vault Pro",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Version 1.0.0",
                            color = TextMedium,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Your privacy is our priority. All files are encrypted and stored locally on your device.",
                            color = TextMedium,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
"""

# We need to find the exact old "More" -> { block to replace it.
# Let's use regex.
pattern = re.compile(r'\s*"More" -> \{\s*// Initialize inputs on Settings entry.*?// GENERAL PREFERENCES.*?SettingsGroup\(title = "GENERAL PREFERENCES"\) \{.*?\}\s*\}\s*\}', re.DOTALL)
match = pattern.search(content)

if match:
    old_more_block = match.group(0)
    new_blocks = more_menu + security_settings + change_pin + fake_vault + hide_apps + export_import + about
    content = content.replace(old_more_block, new_blocks)
    with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
        f.write(content)
    print("Replaced old More block with new sections.")
else:
    print("Could not find the old More block!")

