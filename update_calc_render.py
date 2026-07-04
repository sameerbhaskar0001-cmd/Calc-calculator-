import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

target = """                // We render Calculator if it's not fully transitioned to Vault
                if (transitionState < 2 || activeTab == ActiveTab.CALCULATOR) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .blur(blurRadius)
                        .alpha(calcAlpha)
                    ) {
                        CalculatorTabContent(viewModel = viewModel)
                    }
                }"""

replacement = """                // We render Calculator if it's not fully transitioned to Vault
                if (transitionState < 3 && activeTab == ActiveTab.CALCULATOR) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .blur(blurRadius)
                        .scale(calcScale)
                        .alpha(calcAlpha)
                    ) {
                        CalculatorTabContent(viewModel = viewModel)
                    }
                    
                    if (transitionState == 1 || transitionState == 2) {
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
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        drawCircle(
                                            color = ThemePurple.copy(alpha = 0.2f),
                                            radius = size.width / 2,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                                        )
                                        drawCircle(
                                            color = ThemePurple.copy(alpha = 0.4f),
                                            radius = size.width / 2.5f,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                        )
                                        // A spinning arc for "authenticating"
                                        drawArc(
                                            color = ThemePurple,
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
                                    text = "Authenticating...",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }"""

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)

