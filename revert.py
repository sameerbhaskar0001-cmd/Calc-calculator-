import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

start_marker_detail = '                "Security Score Detail" -> {'
end_marker_detail = '                "Storage" -> {'

idx1_detail = content.find(start_marker_detail)
idx2_detail = content.find(end_marker_detail, idx1_detail)

new_detail_block = """                "Security Score Detail" -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val biometric = viewModel.biometricEnabled.collectAsStateWithLifecycle().value
                        val recovery = viewModel.recoveryCode.collectAsStateWithLifecycle().value.isNotEmpty()
                        val lockOnBg = viewModel.lockOnBackground.collectAsStateWithLifecycle().value
                        val prevSS = viewModel.preventScreenshots.collectAsStateWithLifecycle().value
                        val clipProt = viewModel.clipboardProtection.collectAsStateWithLifecycle().value
                        val stealth = viewModel.stealthMode.collectAsStateWithLifecycle().value
                        val hideNotif = viewModel.hideNotifications.collectAsStateWithLifecycle().value
                        val panic = viewModel.panicExitAction.collectAsStateWithLifecycle().value != "none"
                        val intruder = viewModel.intruderDetectionEnabled.collectAsStateWithLifecycle().value
                        val hasPin = viewModel.getVaultPin().isNotEmpty()

                        val rawScore = (if (hasPin) 20 else 0) +
                                (if (recovery) 15 else 0) +
                                (if (biometric) 10 else 0) +
                                (if (lockOnBg) 10 else 0) +
                                (if (prevSS) 10 else 0) +
                                (if (intruder) 10 else 0) +
                                (if (stealth) 10 else 0) +
                                (if (panic) 5 else 0) +
                                (if (hideNotif) 5 else 0) +
                                (if (clipProt) 5 else 0)

                        val animatedScore by androidx.compose.animation.core.animateIntAsState(
                            targetValue = rawScore,
                            animationSpec = androidx.compose.animation.core.tween(
                                durationMillis = 1500, 
                                easing = androidx.compose.animation.core.FastOutSlowInEasing
                            ),
                            label = "scoreAnimDetail"
                        )

                        val scoreColor = when {
                            animatedScore >= 95 -> Color(0xFF00E676)
                            animatedScore >= 80 -> Color(0xFF69F0AE)
                            animatedScore >= 60 -> Color(0xFFFFC107)
                            animatedScore >= 40 -> Color(0xFFFF9800)
                            else -> Color(0xFFFF5252)
                        }
                        
                        val summaryText = when {
                            animatedScore >= 95 -> "Your vault security is excellent."
                            animatedScore >= 80 -> "Strong protection. Most features enabled."
                            animatedScore >= 60 -> "Your vault security is good, but can be improved."
                            animatedScore >= 40 -> "Action required to secure your vault."
                            else -> "Weak protection. Enable features below."
                        }

                        val features = listOf(
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
                        )
                        
                        // Top summary
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "$animatedScore / 100",
                                fontSize = 56.sp,
                                fontWeight = FontWeight.Bold,
                                color = scoreColor
                            )
                            Text(
                                text = summaryText,
                                fontSize = 15.sp,
                                color = TextMedium
                            )
                        }

                        // Checklist
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
                                            .padding(horizontal = 20.dp)
                                            .height(1.dp)
                                            .background(Color.White.copy(alpha = 0.05f))
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
"""

if idx1_detail != -1 and idx2_detail != -1:
    content = content[:idx1_detail] + new_detail_block + content[idx2_detail:]
    print("Reverted Detail block.")
else:
    print("Failed to find Detail block boundaries.")

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
