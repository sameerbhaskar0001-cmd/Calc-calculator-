import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

pattern = re.compile(r'@Composable\s*fun GlassCalculatorKey\(.*?\}\s*\}\s*\}', re.DOTALL)
matches = pattern.findall(content)
if not matches:
    print("Could not find GlassCalculatorKey")

new_key = '''@Composable
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
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "button_scale"
    )

    val bgColor = when {
        isEquals -> themeColors.themePurple
        isOperator || isUtility -> themeColors.themeLightPurple
        else -> themeColors.digitBg
    }
    
    val contentColor = when {
        isEquals -> Color.White
        isOperator || isUtility -> themeColors.textDark
        else -> themeColors.textDark
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .shadow(
                elevation = if (isEquals) 4.dp else 1.dp,
                shape = CircleShape,
                ambientColor = Color.Black.copy(alpha = 0.5f),
                spotColor = Color.Black.copy(alpha = 0.5f)
            )
            .clip(CircleShape)
            .background(bgColor)
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material.ripple.rememberRipple(color = contentColor.copy(alpha = 0.2f)),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (char == "⌫") {
            Icon(
                imageVector = Icons.Default.KeyboardBackspace,
                contentDescription = "Backspace",
                tint = contentColor,
                modifier = Modifier.size(24.dp)
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
}'''

if matches:
    content = content.replace(matches[0], new_key)
    with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
        f.write(content)
    print("Replaced GlassCalculatorKey!")
