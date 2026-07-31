import re

with open("app/src/main/java/com/example/VaultContentModule.kt", "r") as f:
    content = f.read()

target = """                    // Premium Empty State
                    val infiniteTransition = rememberInfiniteTransition()
                    val pulseScale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.05f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1500, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                    
                    Column(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .scale(pulseScale)
                                .clip(CircleShape)
                                .background(Brush.radialGradient(listOf(ThemePurple.copy(alpha = 0.15f), Color.Transparent)))
                                .border(1.dp, ThemePurple.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(emptyIcon, contentDescription = null, modifier = Modifier.size(56.dp), tint = ThemePurple)
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = if (searchQuery.isNotEmpty()) "No results found" else emptyTitle, 
                            color = Color.White, 
                            fontSize = 24.sp, 
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (searchQuery.isNotEmpty()) "Try adjusting your search terms." else emptySubtitle,
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }"""

replacement = """                    // Premium Empty State
                    val infiniteTransition = rememberInfiniteTransition(label = "emptyStatePulse")
                    val pulseScale by infiniteTransition.animateFloat(
                        initialValue = 0.8f,
                        targetValue = 1.2f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = LinearOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "pulseScale"
                    )
                    val pulseAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.5f,
                        targetValue = 0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = LinearOutSlowInEasing),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "pulseAlpha"
                    )
                    
                    Column(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier.size(180.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Outer ring
                            Box(
                                modifier = Modifier
                                    .size(160.dp)
                                    .scale(pulseScale)
                                    .clip(CircleShape)
                                    .background(ThemePurple.copy(alpha = pulseAlpha * 0.2f))
                                    .border(1.dp, ThemePurple.copy(alpha = pulseAlpha * 0.5f), CircleShape)
                            )
                            // Inner circle
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(Brush.radialGradient(listOf(ThemePurple.copy(alpha = 0.2f), Color.Transparent)))
                                    .border(1.dp, ThemePurple.copy(alpha = 0.3f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(emptyIcon, contentDescription = null, modifier = Modifier.size(42.dp), tint = ThemePurple)
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = if (searchQuery.isNotEmpty()) "No results found" else emptyTitle, 
                            color = Color.White, 
                            fontSize = 22.sp, 
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (searchQuery.isNotEmpty()) "Try adjusting your search terms." else emptySubtitle,
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                        if (searchQuery.isEmpty()) {
                            Spacer(modifier = Modifier.height(32.dp))
                            Box(modifier = Modifier.width(60.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(ThemePurple.copy(alpha = 0.4f)))
                        }
                    }"""

if target in content:
    content = content.replace(target, replacement)
    with open("app/src/main/java/com/example/VaultContentModule.kt", "w") as f:
        f.write(content)
    print("Empty state replaced")
else:
    print("Target empty state not found")
