import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target_start = '                        val rawScore = (if (hasPin) 20 else 0) +'
target_end = '                        Spacer(modifier = Modifier.height(32.dp))\n                    }\n                }\n                "Storage" -> {'

# Find the exact indices
start_idx = content.find(target_start)
end_idx = content.find(target_end)

if start_idx == -1 or end_idx == -1:
    print("Could not find block")
    exit(1)

# Extract the replacement block
new_block = """                        val rawScore = (if (hasPin) 20 else 0) +
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

                        val scoreColor by androidx.compose.animation.animateColorAsState(
                            targetValue = when {
                                animatedScore >= 90 -> Color(0xFF10B981)
                                animatedScore >= 75 -> Color(0xFF3B82F6)
                                animatedScore >= 55 -> Color(0xFF06B6D4)
                                animatedScore >= 35 -> Color(0xFFF97316)
                                else -> Color(0xFFEF4444)
                            },
                            animationSpec = androidx.compose.animation.core.tween(1500),
                            label = "scoreColorAnim"
                        )

                        val scoreCategory = when {
                            animatedScore >= 90 -> "Excellent"
                            animatedScore >= 75 -> "Good Protection"
                            animatedScore >= 55 -> "Moderate Security"
                            animatedScore >= 35 -> "Needs Improvement"
                            else -> "Vulnerable"
                        }
                        
                        val summaryText = when {
                            animatedScore >= 90 -> "Your vault is fully protected. Maximum privacy is active."
                            animatedScore >= 75 -> "Your vault is secure. A few enhancements will maximize protection."
                            animatedScore >= 55 -> "Security is active. Enable remaining protections to strengthen privacy."
                            animatedScore >= 35 -> "Protection is limited. Enable additional features to secure your vault."
                            else -> "Your vault is vulnerable. Critical security features are disabled."
                        }

                        class SecFeature(val name: String, val isEnabled: Boolean, val target: String, val desc: String)
                        val features = listOf(
                            SecFeature("PIN Set", hasPin, "Authentication", "Set a master PIN to lock your private files."),
                            SecFeature("Recovery Configured", recovery, "Security", "Enable a backup method to recover access if you forget your PIN."),
                            SecFeature("Biometric Unlock", biometric, "Authentication", "Use your fingerprint or face to quickly unlock the vault."),
                            SecFeature("Auto Lock on Background", lockOnBg, "Security", "Automatically lock the vault when you leave the app."),
                            SecFeature("Screenshot Protection", prevSS, "Protection", "Block apps from taking screenshots inside the vault."),
                            SecFeature("Intruder Monitoring", intruder, "Monitoring", "Silently capture photos of anyone trying to guess your PIN."),
                            SecFeature("Stealth Mode", stealth, "Protection", "Hide the app from recent apps to prevent snooping."),
                            SecFeature("Shake to Exit", panic, "Shake to Exit", "Instantly lock and exit the app by shaking your device."),
                            SecFeature("Hide Notifications", hideNotif, "Protection", "Prevent sensitive info from showing up in notifications."),
                            SecFeature("Clipboard Protection", clipProt, "Protection", "Automatically clear sensitive copied text from the clipboard.")
                        )

                        // 1. Hero Score Section
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = true,
                                enter = androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(500))
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Security Score",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextMedium,
                                        letterSpacing = 1.sp
                                    )
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(
                                            text = animatedScore.toString(),
                                            fontSize = 72.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = scoreColor,
                                            lineHeight = 72.sp
                                        )
                                        Text(
                                            text = " /100",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = scoreColor.copy(alpha = 0.5f),
                                            modifier = Modifier.padding(bottom = 12.dp)
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(scoreColor.copy(alpha = 0.15f))
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = scoreCategory,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = scoreColor
                                        )
                                    }
                                    Text(
                                        text = summaryText,
                                        fontSize = 14.sp,
                                        color = TextMedium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(top = 12.dp, start = 32.dp, end = 32.dp)
                                    )
                                }
                            }
                        }

                        // 2. Checklist
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF1B2031).copy(alpha = 0.95f))
                        ) {
                            features.forEachIndexed { index, feature ->
                                var visible by remember { mutableStateOf(false) }
                                LaunchedEffect(Unit) {
                                    kotlinx.coroutines.delay(index * 25L)
                                    visible = true
                                }
                                val alpha by androidx.compose.animation.core.animateFloatAsState(
                                    targetValue = if (visible) 1f else 0f,
                                    animationSpec = androidx.compose.animation.core.tween(250),
                                    label = "checklistFade"
                                )
                                Column(modifier = Modifier.alpha(alpha)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp, vertical = 18.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = feature.name,
                                            color = if (feature.isEnabled) Color.White else TextMedium,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                        if (feature.isEnabled) {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                                    .background(Color(0xFF00E676)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                                    .background(Color(0xFF2A3143)),
                                                contentAlignment = Alignment.Center
                                            ) {}
                                        }
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
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // 3. Security Advisor
                        val disabledFeatures = features.filter { !it.isEnabled }
                        if (disabledFeatures.isNotEmpty()) {
                            Text(
                                text = "SECURITY ADVISOR",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMedium,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                disabledFeatures.forEachIndexed { index, feature ->
                                    var visible by remember { mutableStateOf(false) }
                                    LaunchedEffect(Unit) {
                                        kotlinx.coroutines.delay((features.size * 25L) + (index * 50L))
                                        visible = true
                                    }
                                    val alpha by androidx.compose.animation.core.animateFloatAsState(
                                        targetValue = if (visible) 1f else 0f,
                                        animationSpec = androidx.compose.animation.core.tween(250),
                                        label = "advisorFade"
                                    )
                                    
                                    UnifiedGlassCard(
                                        modifier = Modifier.fillMaxWidth().alpha(alpha),
                                        shape = RoundedCornerShape(20.dp),
                                        bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(20.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                                                Text(
                                                    text = feature.name,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = feature.desc,
                                                    fontSize = 13.sp,
                                                    color = TextMedium,
                                                    lineHeight = 18.sp
                                                )
                                            }
                                            // Premium Configure Button
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(Color(0xFF635BFF).copy(alpha = 0.2f))
                                                    .clickable {
                                                        viewModel.triggerKeypressEffects(context)
                                                        activeSection = feature.target
                                                    }
                                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "Configure",
                                                    color = Color(0xFF8C85FF),
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
                "Storage" -> {"""

new_content = content[:start_idx] + new_block + content[end_idx + len(target_end):]

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(new_content)

print("Security Score block replaced successfully.")
