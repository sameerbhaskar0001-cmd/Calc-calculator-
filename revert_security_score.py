import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

match_start = re.search(r'val rawScore = \(if \(hasPin\) 20 else 0\) \+', content)
match_end = re.search(r'Spacer\(modifier = Modifier\.height\(32\.dp\)\)\n\s*}\n\s*}\n\s*}\n\s*"Storage" -> \{', content)

if not match_start or not match_end:
    print("Could not find the block to revert.")
    exit(1)

start_idx = match_start.start()
end_idx = match_end.start() + len('Spacer(modifier = Modifier.height(32.dp))\n                        }\n                    }\n                }')

original_block = """val rawScore = (if (hasPin) 20 else 0) +
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
                }"""

new_content = content[:start_idx] + original_block + content[end_idx:]

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(new_content)

print("Changes reverted successfully.")
