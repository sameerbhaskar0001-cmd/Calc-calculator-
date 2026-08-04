import re
with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

prot_text = "".join(lines[5068:5593])
print("ThemePurple:", "ThemePurple" in prot_text)
print("TextDark:", "TextDark" in prot_text)
print("TextMedium:", "TextMedium" in prot_text)
print("context:", "context" in prot_text)
print("activeSection =", "activeSection =" in prot_text)
