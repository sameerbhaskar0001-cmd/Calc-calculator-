import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

cases = ["\"Security\" ->", "\"App Disguise\" ->", "\"Backup\" ->", "\"Privacy Settings\" ->", "\"Advanced Security\" ->", "\"Change PIN\" ->"]

for case in cases:
    if case in content:
        idx = content.find(case)
        print(f"CASE: {case}")
        print(content[idx:idx+250])
        print("----------------")
