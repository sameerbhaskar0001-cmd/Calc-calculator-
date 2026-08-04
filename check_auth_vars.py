import re
with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

auth_text = "".join(lines[5065:5607])
print("ThemePurple:", "ThemePurple" in auth_text)
print("TextDark:", "TextDark" in auth_text)
print("TextMedium:", "TextMedium" in auth_text)
print("context:", "context" in auth_text)
