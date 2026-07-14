import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

text = text.replace("import androidx.lifecycle.compose.collectAsStateWithLifecycle\npackage com.example\n", "package com.example\nimport androidx.lifecycle.compose.collectAsStateWithLifecycle\n")

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)

