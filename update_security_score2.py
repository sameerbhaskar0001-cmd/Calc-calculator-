import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target_start = '                        val rawScore = (if (hasPin) 20 else 0) +'
target_end = '                        Spacer(modifier = Modifier.height(32.dp))\n                        }\n                    }\n                }\n                "Storage" -> {'

start_idx = content.find(target_start)
end_idx = content.find(target_end)

if start_idx == -1 or end_idx == -1:
    print("Could not find block. Trying fallback.")
    # Fallback if whitespace differs
    match_start = re.search(r'val rawScore = \(if \(hasPin\) 20 else 0\) \+', content)
    match_end = re.search(r'Spacer\(modifier = Modifier\.height\(32\.dp\)\)\n\s*}\n\s*}\n\s*}\n\s*"Storage" -> \{', content)
    
    if match_start and match_end:
        start_idx = match_start.start()
        end_idx = match_end.start() + len('Spacer(modifier = Modifier.height(32.dp))\n                        }\n                    }\n                }')
        target_end_len = 0
        target_end_str = '\n                "Storage" -> {'
    else:
        print("Still couldn't find it.")
        exit(1)
else:
    target_end_len = len(target_end) - len('\n                "Storage" -> {')
    target_end_str = '\n                "Storage" -> {'

new_block = """val rawScore = (if (hasPin) 20 else 0) +
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
                                easing = androidx.compose.animation.core.LinearOutSlowInEasing
                            ),
                            label = "scoreAnimDetail"
                        )

                        val scoreColor by androidx.compose.animation.animateColorAsState(
                            targetValue = when {
                                animatedScore >= 90 -> Color(0xFF10B981)
                                animatedScore >= 70 -> Color(0xFF0EA5E9)
                                animatedScore >= 50 -> Color(0xFFF97316)
                                else -> Color(0xFFEF4444)
                            },
                            animationSpec = androidx.compose.animation.core.tween(1500),
                            label = "scoreColorAnim"
                        )

                        val scoreCategory = when {
                            animatedScore >= 90 -> "Excellent"
                            animatedScore >= 70 -> "Strong"
                            animatedScore >= 50 -> "Medium"
                            else -> "Weak"
                        }
                        
                        val summaryText = when {
                            animatedScore >= 90 -> "Your vault is fully protected."
                            animatedScore >= 70 -> "Your vault is secure. A few enhancements can further strengthen your privacy."
                            animatedScore >= 50 -> "Security is active. Enable the remaining protections for maximum privacy."
                            else -> "Several important protections are still disabled."
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

                        var heroVisible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            heroVisible = true
                        }

                        // 1. Hero Score Section
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = heroVisible,
                                enter = androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(600)) + 
                                        androidx.compose.animation.slideInVertically(
                                            initialOffsetY = { 40 },
                                            animationSpec = androidx.compose.animation.core.tween(600, easing = androidx.compose.animation.core.LinearOutSlowInEasing)
                                        )
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Security Score",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextMedium,
                                        letterSpacing = 1.sp
                                    )
                                    Row(
                                        verticalAlignment = Alignment.Bottom,
                                        modifier = Modifier.padding(top = 8.dp)
                                    ) {
                                        Text(
                                            text = animatedScore.toString(),
                                            fontSize = 80.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = scoreColor,
                                            lineHeight = 80.sp,
                                            letterSpacing = (-2).sp
                                        )
                                        Text(
                                            text = "/100",
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = scoreColor.copy(alpha = 0.6f),
                                            modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                                        )
                                    }
                                    Text(
                                        text = scoreCategory,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = scoreColor,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                    Text(
                                        text = summaryText,
                                        fontSize = 14.sp,
                                        color = TextMedium.copy(alpha = 0.9f),
                                        textAlign = TextAlign.Center,
                                        lineHeight = 20.sp,
                                        modifier = Modifier.padding(top = 20.dp, start = 32.dp, end = 32.dp)
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
                                    kotlinx.coroutines.delay(100L + (index * 30L))
                                    visible = true
                                }
                                val alpha by androidx.compose.animation.core.animateFloatAsState(
                                    targetValue = if (visible) 1f else 0f,
                                    animationSpec = androidx.compose.animation.core.tween(300),
                                    label = "checklistFade"
                                )
                                Column(modifier = Modifier.alpha(alpha)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp, vertical = 20.dp),
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
                                                    .background(Color(0xFF10B981)),
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
                                                    .border(1.5.dp, Color.White.copy(alpha = 0.2f), androidx.compose.foundation.shape.CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {}
                                        }
                                    }
                                    if (index < features.size - 1) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 24.dp)
                                                .height(1.dp)
                                                .background(Color.White.copy(alpha = 0.03f))
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
                                color = TextMedium.copy(alpha = 0.7f),
                                letterSpacing = 1.2.sp,
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
                                        kotlinx.coroutines.delay(400L + (index * 40L))
                                        visible = true
                                    }
                                    val alpha by androidx.compose.animation.core.animateFloatAsState(
                                        targetValue = if (visible) 1f else 0f,
                                        animationSpec = androidx.compose.animation.core.tween(300),
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
                                            Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
                                                Text(
                                                    text = feature.name,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.SemiBold,
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
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(Color(0xFF2B324A))
                                                    .clickable {
                                                        viewModel.triggerKeypressEffects(context)
                                                        activeSection = feature.target
                                                    }
                                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "Configure",
                                                    color = Color.White,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }"""

new_content = content[:start_idx] + "                        " + new_block + target_end_str + content[end_idx + target_end_len + len(target_end_str):]

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(new_content)

print("Security Score updated successfully.")
