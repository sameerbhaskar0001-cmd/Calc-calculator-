import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target1 = """                        // 1. Change PIN
                        CompactSettingsCard(
                            title = "Change PIN",
                            subtitle = "Update your current vault PIN.",
                            icon = Icons.Default.VpnKey,
                            iconColor = Color(0xFF2979FF),
                            onClick = { showChangePasscodeDialog = true }
                        )

                        // 2. Change Fake PIN
                        CompactSettingsCard(
                            title = "Change Fake PIN",
                            subtitle = "Update the PIN used for the fake vault.",
                            icon = Icons.Default.Lock,
                            iconColor = Color(0xFFEF5350),
                            onClick = { showChangePasscodeDialog = true }
                        )"""

replacement1 = """                        // 1. Change PIN
                        CompactSettingsCard(
                            title = "Change PIN",
                            subtitle = "Update your current vault PIN.",
                            icon = Icons.Default.VpnKey,
                            iconColor = Color(0xFF2979FF),
                            onClick = { activeSection = "Change PIN" }
                        )

                        // 2. Change Fake PIN
                        CompactSettingsCard(
                            title = "Change Fake PIN",
                            subtitle = "Update the PIN used for the fake vault.",
                            icon = Icons.Default.Lock,
                            iconColor = Color(0xFFEF5350),
                            onClick = { activeSection = "Change Fake PIN" }
                        )"""

content = content.replace(target1, replacement1)

target2 = """                when (section) {
                    "Home" -> {"""

replacement2 = """                when (section) {
                    "Change PIN" -> {
                        var newPin by remember { mutableStateOf("") }
                        var confirmPin by remember { mutableStateOf("") }
                        var showError by remember { mutableStateOf(false) }
                        var showSuccess by remember { mutableStateOf(false) }
                        
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.VpnKey, contentDescription = null, tint = ThemePurple, modifier = Modifier.size(48.dp))
                            Text(
                                text = "Change Main PIN",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "Enter your new main PIN for the secure vault.",
                                fontSize = 14.sp,
                                color = TextMedium,
                                textAlign = TextAlign.Center
                            )
                            
                            OutlinedTextField(
                                value = newPin,
                                onValueChange = { if (it.length <= 8) newPin = it.filter { char -> char.isDigit() } },
                                label = { Text("New PIN") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                visualTransformation = PasswordVisualTransformation(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ThemePurple,
                                    focusedLabelColor = ThemePurple
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            OutlinedTextField(
                                value = confirmPin,
                                onValueChange = { if (it.length <= 8) confirmPin = it.filter { char -> char.isDigit() } },
                                label = { Text("Confirm New PIN") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                visualTransformation = PasswordVisualTransformation(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ThemePurple,
                                    focusedLabelColor = ThemePurple
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            if (showError) {
                                Text("PINs do not match or are empty.", color = Color.Red, fontSize = 12.sp)
                            }
                            if (showSuccess) {
                                Text("PIN updated successfully!", color = Color(0xFF00E676), fontSize = 12.sp)
                            }
                            
                            Spacer(modifier = Modifier.weight(1f))
                            
                            Button(
                                onClick = {
                                    if (newPin.isNotEmpty() && newPin == confirmPin) {
                                        viewModel.setVaultPin(newPin)
                                        showError = false
                                        showSuccess = true
                                    } else {
                                        showError = true
                                        showSuccess = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ThemePurple)
                            ) {
                                Text("Save PIN", color = if (IsWhiteTheme) Color(0xFF1E2235) else Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    "Change Fake PIN" -> {
                        var newPin by remember { mutableStateOf("") }
                        var confirmPin by remember { mutableStateOf("") }
                        var showError by remember { mutableStateOf(false) }
                        var showSuccess by remember { mutableStateOf(false) }
                        
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFEF5350), modifier = Modifier.size(48.dp))
                            Text(
                                text = "Change Decoy PIN",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "Enter your new decoy PIN. This should be different from your main PIN.",
                                fontSize = 14.sp,
                                color = TextMedium,
                                textAlign = TextAlign.Center
                            )
                            
                            OutlinedTextField(
                                value = newPin,
                                onValueChange = { if (it.length <= 8) newPin = it.filter { char -> char.isDigit() } },
                                label = { Text("New Decoy PIN") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                visualTransformation = PasswordVisualTransformation(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ThemePurple,
                                    focusedLabelColor = ThemePurple
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            OutlinedTextField(
                                value = confirmPin,
                                onValueChange = { if (it.length <= 8) confirmPin = it.filter { char -> char.isDigit() } },
                                label = { Text("Confirm Decoy PIN") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                visualTransformation = PasswordVisualTransformation(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ThemePurple,
                                    focusedLabelColor = ThemePurple
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            if (showError) {
                                Text("PINs do not match or are empty.", color = Color.Red, fontSize = 12.sp)
                            }
                            if (showSuccess) {
                                Text("Decoy PIN updated successfully!", color = Color(0xFF00E676), fontSize = 12.sp)
                            }
                            
                            Spacer(modifier = Modifier.weight(1f))
                            
                            Button(
                                onClick = {
                                    if (newPin.isNotEmpty() && newPin == confirmPin) {
                                        if (newPin == viewModel.getVaultPin()) {
                                            android.widget.Toast.makeText(context, "Decoy PIN must be different from Main PIN", android.widget.Toast.LENGTH_SHORT).show()
                                        } else {
                                            viewModel.setDecoyPin(newPin)
                                            showError = false
                                            showSuccess = true
                                        }
                                    } else {
                                        showError = true
                                        showSuccess = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ThemePurple)
                            ) {
                                Text("Save Decoy PIN", color = if (IsWhiteTheme) Color(0xFF1E2235) else Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    "Home" -> {"""

content = content.replace(target2, replacement2)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
