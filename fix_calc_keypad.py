import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

pattern = r"(// Keypad Grid Zone\s+val buttons = listOf.*?Column\(\s+modifier = Modifier\s+\.fillMaxWidth\(\))\s+(\.padding\(bottom = 8\.dp\),\s+verticalArrangement = Arrangement\.spacedBy\(8\.dp\)\s+\)\s+\{\s+for \(row in buttons\) \{\s+Row\(\s+modifier = Modifier\.fillMaxWidth\(\))"

replacement = r"\1\n                .weight(1.5f)\n                \2.weight(1f)"

content = re.sub(pattern, replacement, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
