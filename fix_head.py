with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

content = content.replace(
    "import androidx.compose.ui.text.input.PasswordVisualTransformationpackage com.example",
    "package com.example\nimport androidx.compose.ui.text.input.PasswordVisualTransformation\n"
)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
