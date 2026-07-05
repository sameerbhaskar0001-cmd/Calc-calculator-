import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

content = content.replace('"Settings" -> {', '"More" -> {')

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
