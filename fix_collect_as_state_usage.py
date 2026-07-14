import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

text = text.replace(".collectAsState()", ".collectAsStateWithLifecycle()")

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)

