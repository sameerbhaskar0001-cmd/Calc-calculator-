import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

# Fix `not in` to `!in`
content = content.replace("ext not in listOf", "ext !in listOf")

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)

