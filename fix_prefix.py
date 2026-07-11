import sys

with open("app/src/main/java/com/example/CalculatorScreen.kt") as f:
    text = f.read()

# Replace the broken lines
old_broken = """val lineStart = if (cursor > 0) text.lastIndexOf('
', searchEnd) + 1 else 0"""
new_fixed = """val lineStart = if (cursor > 0) text.lastIndexOf('\\n', searchEnd) + 1 else 0"""

text = text.replace(old_broken, new_fixed)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)
