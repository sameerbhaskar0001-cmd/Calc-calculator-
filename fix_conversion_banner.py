import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

conversion_banner_pattern = r"(Card\(\s*modifier = Modifier\s*\.fillMaxWidth\(\))\s*\.shadow\(elevation = 1\.dp, shape = RoundedCornerShape\(20\.dp\)\)\s*\.border\(\s*width = 1\.dp,\s*color = ThemeContainerBorder\.copy\(alpha = 0\.2f\),\s*shape = RoundedCornerShape\(20\.dp\)\s*\)\s*(\.testTag\(\"conversion_banner_card\"\),\s*)colors = CardDefaults\.cardColors\(containerColor = ThemeLightPurple\),\s*shape = RoundedCornerShape\(20\.dp\)"
conversion_banner_repl = r"""\1
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black.copy(alpha = 0.4f))
                        .border(
                            width = 1.dp,
                            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(ThemePurple.copy(alpha = 0.3f), Color.Transparent),
                                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                end = androidx.compose.ui.geometry.Offset(0f, 300f)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        \2colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(20.dp)"""
content = re.sub(conversion_banner_pattern, conversion_banner_repl, content)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
