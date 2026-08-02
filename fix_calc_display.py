import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target1 = """                    AnimatedContent(
                        targetState = formulaDisplay,
                        transitionSpec = {
                            fadeIn(tween(120)) togetherWith fadeOut(tween(120))
                        },
                        label = "formula_anim"
                    ) { targetFormula ->
                        Text(
                            text = targetFormula,
                            fontSize = formulaFontSize,
                            fontWeight = FontWeight.Medium,
                            color = if (isEvaluated) TextMedium.copy(alpha = 0.65f) else ThemePurple.copy(alpha = 0.75f),
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            modifier = Modifier.testTag("expression_display")
                        )
                    }"""

replacement1 = """                        Text(
                            text = formulaDisplay,
                            fontSize = formulaFontSize,
                            fontWeight = FontWeight.Medium,
                            color = if (isEvaluated) TextMedium.copy(alpha = 0.65f) else ThemePurple.copy(alpha = 0.75f),
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            modifier = Modifier.testTag("expression_display")
                        )"""

content = content.replace(target1, replacement1)

target2 = """                AnimatedContent(
                    targetState = mainDisplay,
                    transitionSpec = {
                        fadeIn(tween(100)) togetherWith fadeOut(tween(100))
                    },
                    label = "main_display_anim"
                ) { targetDisplay ->
                    Text(
                        text = targetDisplay,
                        fontSize = mainFontSize,
                        fontWeight = FontWeight.Bold,
                        color = mainColor,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        modifier = Modifier.testTag("calc_result_display")
                    )
                }"""

replacement2 = """                    Text(
                        text = mainDisplay,
                        fontSize = mainFontSize,
                        fontWeight = FontWeight.Bold,
                        color = mainColor,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        modifier = Modifier.testTag("calc_result_display")
                    )"""

content = content.replace(target2, replacement2)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
print("Updated calculator display")
