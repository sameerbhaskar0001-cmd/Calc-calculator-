import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

import_statement = "import androidx.lifecycle.compose.collectAsStateWithLifecycle\n"
if "collectAsStateWithLifecycle" not in text:
    text = import_statement + text

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)

