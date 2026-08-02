import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

cases = [
    "\"App Privacy\" -> {",
    "\"Intruder Alerts\", \"Access Logs\", \"Monitoring\" -> {",
    "\"Explore\" -> {",
    "\"Recently Deleted\" -> {",
    "\"Decoy Space\" -> {",
    "\"Storage\" -> {",
    "\"Security\" -> {",
    "\"Authentication\" -> {",
    "\"Protection\" -> {",
    "\"Shake to Exit\" -> {",
    "\"Profile\" -> {"
]

for case in cases:
    idx = content.find(case)
    if idx != -1:
        print(f"--- {case} ---")
        lines = content[idx:idx+350].split("\n")
        for line in lines[:8]:
            print(line)
