import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target = """                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                viewModel.onCalcKeyPress(char)
                            },
                            onLongClick = if (isEquals && biometricEnabled) {
                                {
                                    onTriggerBiometric()
                                }
                            } else null,"""

replacement = """                            onClick = {
                                viewModel.triggerCalculatorKeypressEffects(context, char)
                                viewModel.onCalcKeyPress(char)
                            },
                            onLongClick = when {
                                isEquals && biometricEnabled -> {
                                    {
                                        viewModel.triggerCalculatorKeypressEffects(context, "=")
                                        onTriggerBiometric()
                                    }
                                }
                                char == "⌫" -> {
                                    {
                                        viewModel.triggerCalculatorKeypressEffects(context, "=") // Heavy click for delete all
                                        viewModel.onCalcKeyPress("C")
                                    }
                                }
                                else -> null
                            },"""

if target in content:
    content = content.replace(target, replacement)
    with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
        f.write(content)
    print("Replaced successfully")
else:
    print("Target not found")
