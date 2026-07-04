import re
with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target = "        )\n    }"
replacement = "        )\n    }\n}\n"
content = content.replace(target, replacement, 1)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
