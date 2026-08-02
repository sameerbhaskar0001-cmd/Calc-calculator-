import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

cases = [
    "\"Explore\" -> {",
    "\"Decoy Space\" -> {",
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
        lines = content[idx:idx+800].split("\n")
        for line in lines[:15]:
            print(line)
