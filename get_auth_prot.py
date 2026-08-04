with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

import re

# 1. Fix AuthenticationSection
auth_match = re.search(r'fun AuthenticationSection.*?\n(.*?\n\}?)(\n\s*"Protection"|@Composable|$)', content, re.DOTALL)
# wait, it's easier to find it by line numbers
