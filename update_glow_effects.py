import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

calc_button_pattern = r"val scale by animateFloatAsState\([\s\S]*?contentAlignment = Alignment\.Center\n    \) \{"

replacement = """val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_scale"
    )

    val glowAlpha by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        animationSpec = androidx.compose.animation.core.tween(150),
        label = "glow_alpha"
    )

    val bgColor = when {
        isEquals -> Color(0xFFE3AB79) // Lighter beige/orange
        isUtility -> Color(0xFF3F4145)
        isOperator -> Color(0xFF3F4145)
        else -> Color(0xFF2E3034)
    }

    val contentColor = Color.White

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .drawBehind {
                if (glowAlpha > 0f) {
                    // Outer glow - large radius to illuminate neighboring buttons
                    val outerGlowRadius = size.width * 2.5f
                    drawCircle(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.5f * glowAlpha),
                                Color.White.copy(alpha = 0.1f * glowAlpha),
                                Color.Transparent
                            ),
                            center = center,
                            radius = outerGlowRadius
                        ),
                        radius = outerGlowRadius,
                        center = center
                    )
                }
            }
            .shadow(
                elevation = 8.dp,
                shape = CircleShape,
                ambientColor = Color.Black.copy(alpha = 0.8f),
                spotColor = Color.Black.copy(alpha = 0.8f)
            )
            .clip(CircleShape)
            .background(bgColor)
            // Inner glow (only when pressed)
            .drawWithContent {
                drawContent()
                if (glowAlpha > 0f) {
                    drawRect(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.4f * glowAlpha),
                                Color.Transparent
                            ),
                            center = center,
                            radius = size.width / 1.2f
                        )
                    )
                }
            }
            // Top inner highlight
            .background(
                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                    colors = listOf(Color.White.copy(alpha = 0.20f), Color.Transparent),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
            // Premium glass border
            .border(
                width = 1.dp,
                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.4f), 
                        Color.White.copy(alpha = 0.05f), 
                        Color.Black.copy(alpha = 0.8f)
                    ),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(0f, Float.POSITIVE_INFINITY)
                ),
                shape = CircleShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {"""

content = re.sub(calc_button_pattern, replacement, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
