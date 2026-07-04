import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

pattern = r"@Composable\s+fun GlassCalculatorKey\(.*?\) \{\s+val themeColors = com\.example\.ui\.theme\.LocalAppThemeColors\.current.*?\}\s+\}\s+\}"

replacement = """@Composable
fun GlassCalculatorKey(
    char: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isOperator: Boolean = false,
    isUtility: Boolean = false,
    isEquals: Boolean = false,
    themePurple: Color,
    themeLightPurple: Color
) {
    val themeColors = com.example.ui.theme.LocalAppThemeColors.current
    val interactionSource = remember { MutableInteractionSource() }

    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
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
        isUtility -> themeColors.keypadBg
        isOperator -> themeColors.keypadBg
        else -> themeColors.digitBg
    }
    
    val contentColor = when {
        isEquals -> Color.Black
        isUtility -> themePurple
        isOperator -> themePurple
        else -> themeColors.textDark
    }

    Box(
        modifier = modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .aspectRatio(1f)
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .drawBehind {
                    if (glowAlpha > 0f) {
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
                    elevation = if (!isUtility && !isEquals) 4.dp else 0.dp,
                    shape = CircleShape,
                    ambientColor = Color.Black.copy(alpha = 0.8f),
                    spotColor = Color.Black.copy(alpha = 0.8f)
                )
                .clip(CircleShape)
                .background(bgColor)
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
                .background(
                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                        colors = listOf(Color.White.copy(alpha = 0.20f), Color.Transparent),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(0f, Float.POSITIVE_INFINITY)
                    )
                )
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
        ) {
            if (char == "⌫") {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardBackspace,
                    contentDescription = "Backspace",
                    tint = contentColor,
                    modifier = Modifier.size(28.dp)
                )
            } else if (char == "+/-") {
                 Text(
                    text = char,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    color = contentColor
                )
            } else {
                Text(
                    text = char,
                    fontSize = if (isOperator || isEquals) 32.sp else 32.sp,
                    fontWeight = FontWeight.Normal,
                    color = contentColor
                )
            }
        }
    }
}"""

content = re.sub(pattern, replacement, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
