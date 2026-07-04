import re
with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

content = content.replace("Icons.Default.Menu", "Icons.Default.MoreVert")
content = content.replace("Icons.Default.Person", "Icons.Default.Settings")

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
