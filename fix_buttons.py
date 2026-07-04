import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

# Let's see the current CalcButton
# We need to change bgColor and the Box modifiers inside it.
