with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

content = content.replace("import androidx.compose.foundation.layout.wrapContentSize", "")
content = content.replace("package com.example", "package com.example\nimport androidx.compose.foundation.layout.wrapContentSize\n")

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
