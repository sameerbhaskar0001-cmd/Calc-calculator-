import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target = """        kotlinx.coroutines.launch {
            kotlinx.coroutines.delay(1000)
            onPreloadVault()
        }"""
replacement = """        launch {
            kotlinx.coroutines.delay(1000)
            onPreloadVault()
        }"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
print("Updated launch")
