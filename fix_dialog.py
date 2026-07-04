import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

pattern = re.compile(r'// Dynamic Theme Selection Dialog.*?            \}\s*\}\s*\}\s*\}\s*\}', re.DOTALL)
matches = pattern.findall(content)

if matches:
    print("Found dialog block")
else:
    print("Could not find dialog block")

# Let's do it safely
