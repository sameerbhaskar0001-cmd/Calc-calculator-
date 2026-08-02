import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

old_features_block = """                        val features = listOf(
                            "PIN Set" to hasPin,
                            "Recovery Configured" to recovery,
                            "Biometric Unlock" to biometric,
                            "Auto Lock on Background" to lockOnBg,
                            "Screenshot Protection" to prevSS,
                            "Intruder Monitoring" to intruder,
                            "Stealth Mode" to stealth,
                            "Shake to Exit" to panic,
                            "Hide Notifications" to hideNotif,
                            "Clipboard Protection" to clipProt
                        )"""

new_features_block = """                        val features = listOf(
                            Triple("PIN Set", hasPin, "Authentication"),
                            Triple("Recovery Configured", recovery, "Security"),
                            Triple("Biometric Unlock", biometric, "Authentication"),
                            Triple("Auto Lock on Background", lockOnBg, "Security"),
                            Triple("Screenshot Protection", prevSS, "Protection"),
                            Triple("Intruder Monitoring", intruder, "Monitoring"),
                            Triple("Stealth Mode", stealth, "Protection"),
                            Triple("Shake to Exit", panic, "Shake to Exit"),
                            Triple("Hide Notifications", hideNotif, "Protection"),
                            Triple("Clipboard Protection", clipProt, "Protection")
                        )"""

old_ui_block = """                        // Checklist
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF1B2031).copy(alpha = 0.95f))
                        ) {
                            features.forEachIndexed { index, (name, isEnabled) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 18.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = name,
                                        color = if (isEnabled) Color.White else TextMedium,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Icon(
                                        imageVector = if (isEnabled) Icons.Default.CheckCircle else Icons.Default.Close,
                                        contentDescription = null,
                                        tint = if (isEnabled) Color(0xFF00E676) else TextMedium.copy(alpha = 0.5f),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                if (index < features.size - 1) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp)
                                            .height(1.dp)
                                            .background(Color.White.copy(alpha = 0.04f))
                                    )
                                }
                            }
                        }"""

new_ui_block = """                        // Checklist
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF1B2031).copy(alpha = 0.95f))
                        ) {
                            features.forEachIndexed { index, featureTriple ->
                                val name = featureTriple.first
                                val isEnabled = featureTriple.second
                                val targetSection = featureTriple.third
                                
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.triggerKeypressEffects(context)
                                            activeSection = targetSection
                                        }
                                        .padding(horizontal = 20.dp, vertical = 18.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = name,
                                            color = if (isEnabled) Color.White else TextMedium,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                        if (!isEnabled) {
                                            Text(
                                                text = "Tap to resolve",
                                                color = Color(0xFFFF5252),
                                                fontSize = 11.sp,
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }
                                    }
                                    
                                    Icon(
                                        imageVector = if (isEnabled) Icons.Default.CheckCircle else Icons.Default.Close,
                                        contentDescription = null,
                                        tint = if (isEnabled) Color(0xFF00E676) else TextMedium.copy(alpha = 0.5f),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                if (index < features.size - 1) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp)
                                            .height(1.dp)
                                            .background(Color.White.copy(alpha = 0.04f))
                                    )
                                }
                            }
                        }"""

content = content.replace(old_features_block, new_features_block)
content = content.replace(old_ui_block, new_ui_block)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
print("Checklist updated")
