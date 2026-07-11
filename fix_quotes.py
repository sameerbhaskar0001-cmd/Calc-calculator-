import sys

with open("app/src/main/java/com/example/CalculatorScreen.kt") as f:
    text = f.read()

text = text.replace(r'\"<b>\"', '"<b>"')
text = text.replace(r'\"</b>\"', '"</b>"')
text = text.replace(r'\"<i>\"', '"<i>"')
text = text.replace(r'\"</i>\"', '"</i>"')
text = text.replace(r'\"<u>\"', '"<u>"')
text = text.replace(r'\"</u>\"', '"</u>"')

text = text.replace(r'\"• \"', '"• "')
text = text.replace(r'\"1. \"', '"1. "')
text = text.replace(r'\"[ ] \"', '"[ ] "')

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)
