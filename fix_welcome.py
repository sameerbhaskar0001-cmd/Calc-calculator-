import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

target = """                    if (transitionState == 1 || transitionState == 2) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(authOverlayAlpha),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier.size(100.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Concentric circles
                                    val localThemePurple = ThemePurple
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        drawCircle(
                                            color = localThemePurple.copy(alpha = 0.2f),
                                            radius = size.width / 2,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                                        )
                                        drawCircle(
                                            color = localThemePurple.copy(alpha = 0.4f),
                                            radius = size.width / 2.5f,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                        )
                                        // A spinning arc for "authenticating"
                                        drawArc(
                                            color = localThemePurple,
                                            startAngle = -90f,
                                            sweepAngle = 120f,
                                            useCenter = false,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round),
                                            size = androidx.compose.ui.geometry.Size(size.width / 1.25f, size.height / 1.25f),
                                            topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.1f, size.height * 0.1f)
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Authenticating",
                                        tint = ThemePurple,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = "Unlocking Secure Vault...",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }"""

replacement = """                    if (transitionState == 1) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(authOverlayAlpha),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier.size(100.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Concentric circles
                                    val localThemePurple = ThemePurple
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        drawCircle(
                                            color = localThemePurple.copy(alpha = 0.2f),
                                            radius = size.width / 2,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                                        )
                                        drawCircle(
                                            color = localThemePurple.copy(alpha = 0.4f),
                                            radius = size.width / 2.5f,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                        )
                                        // A spinning arc for "authenticating"
                                        drawArc(
                                            color = localThemePurple,
                                            startAngle = -90f,
                                            sweepAngle = 120f,
                                            useCenter = false,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round),
                                            size = androidx.compose.ui.geometry.Size(size.width / 1.25f, size.height / 1.25f),
                                            topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.1f, size.height * 0.1f)
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Authenticating",
                                        tint = ThemePurple,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = "Unlocking Secure Vault...",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                    
                    if (transitionState == 2) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(welcomeAlpha)
                                .background(Color(0xFF0F121C)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                // Glowing Shield
                                Box(
                                    modifier = Modifier
                                        .size(120.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val localThemePurple = ThemePurple
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        // Simple glow effect
                                        drawCircle(
                                            color = localThemePurple.copy(alpha = 0.15f),
                                            radius = size.width / 2,
                                            style = androidx.compose.ui.graphics.drawscope.Fill
                                        )
                                        drawCircle(
                                            color = localThemePurple.copy(alpha = 0.1f),
                                            radius = size.width / 1.5f,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = "Secure",
                                        tint = ThemePurple,
                                        modifier = Modifier.size(64.dp)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Secure",
                                        tint = Color(0xFF0F121C),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(32.dp))
                                
                                Text(
                                    text = "Welcome Back!",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Your vault is opening",
                                    fontSize = 16.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Accent line
                                Box(
                                    modifier = Modifier
                                        .width(40.dp)
                                        .height(3.dp)
                                        .clip(RoundedCornerShape(1.5.dp))
                                        .background(ThemePurple)
                                )
                                
                                Spacer(modifier = Modifier.height(64.dp))
                                
                                // Preview Grid
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.padding(horizontal = 32.dp)
                                ) {
                                    VaultFolderCard(
                                        title = "Photos", 
                                        count = "12 Items", 
                                        icon = Icons.Default.Image, 
                                        iconTint = ThemePurple,
                                        modifier = Modifier.weight(1f)
                                    ) {}
                                    VaultFolderCard(
                                        title = "Videos", 
                                        count = "3 Items", 
                                        icon = Icons.Default.PlayArrow, 
                                        iconTint = ThemePurple,
                                        modifier = Modifier.weight(1f)
                                    ) {}
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.padding(horizontal = 32.dp)
                                ) {
                                    VaultFolderCard(
                                        title = "Documents", 
                                        count = "0 Items", 
                                        icon = Icons.Default.Description, 
                                        iconTint = ThemePurple,
                                        modifier = Modifier.weight(1f)
                                    ) {}
                                    VaultFolderCard(
                                        title = "Notes", 
                                        count = "3 Items", 
                                        icon = Icons.Default.List, 
                                        iconTint = ThemePurple,
                                        modifier = Modifier.weight(1f)
                                    ) {}
                                }
                            }
                        }
                    }"""

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
