import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

# Let's find out how many braces there are
open_braces = content.count('{')
close_braces = content.count('}')
print(f"Open: {open_braces}, Close: {close_braces}")
