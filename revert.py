import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

# Revert localThemePurple globally
content = content.replace("localThemePurple.copy", "ThemePurple.copy")
content = content.replace("localThemePurple,", "ThemePurple,")
content = content.replace("val localThemePurple = ThemePurple", "val localThemePurple = ThemePurple")

# Now selectively fix the Canvas block
target = """                                    Canvas(modifier = Modifier.fillMaxSize()) {
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
                                            color = ThemePurple,"""
replacement = """                                    Canvas(modifier = Modifier.fillMaxSize()) {
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
                                            color = localThemePurple,"""
content = content.replace(target, replacement)

# Revert icons
content = content.replace("androidx.compose.material.icons.filled.Menu", "Icons.Default.Menu")
content = content.replace("androidx.compose.material.icons.filled.Person", "Icons.Default.Person")
content = content.replace("androidx.compose.material.icons.automirrored.filled.ArrowBack", "Icons.Default.ArrowBack")
content = content.replace("androidx.compose.material.icons.automirrored.filled.List", "Icons.Default.List")
content = content.replace("Icons.AutoMirrored.Filled.ArrowBack", "Icons.Default.ArrowBack")
content = content.replace("Icons.AutoMirrored.Filled.List", "Icons.Default.List")

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
