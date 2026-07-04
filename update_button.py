import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

# We need to find the CalcButton function and replace the colors and box modifier.
# The user wants exact colors and a glass effect (inner glow).

calc_button_pattern = r"val bgColor = when \{.*?\).*?contentAlignment = Alignment\.Center\n    \) \{"

replacement = """val bgColor = when {
        isEquals -> Color(0xFFCBA074) // beige/orange
        else -> Color(0xFF2C2C2E)
    }
       
    val contentColor = Color.White

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clip(CircleShape)
            .background(bgColor)
            .background(
                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.15f), Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(x = Float.POSITIVE_INFINITY, y = Float.POSITIVE_INFINITY), // this might not work easily
                    radius = 1000f
                )
            )
            .border(
                width = 1.dp,
                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                    colors = listOf(Color.White.copy(alpha = 0.4f), Color.Transparent),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(0f, 300f)
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

# Replace using dotall
content = re.sub(calc_button_pattern, replacement, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
