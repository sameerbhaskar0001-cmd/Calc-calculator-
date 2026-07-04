import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

target = """                        modifier = Modifier.padding(horizontal = 8.dp, bottom = 24.dp)"""
replacement = """                        modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 24.dp)"""

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
