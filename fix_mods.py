with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

import re

# In AuthenticationSection, find the Column that should have the modifier
content = re.sub(
    r'(fun AuthenticationSection.*?Column\(\s*modifier = Modifier\s*)\.fillMaxWidth\(\)\s*\.padding\(horizontal = 16\.dp\)',
    r'\1.then(modifier).fillMaxWidth().padding(horizontal = 16.dp)',
    content,
    flags=re.DOTALL
)

# In ProtectionSection, find the Column that should have the modifier
content = re.sub(
    r'(fun ProtectionSection.*?Column\(\s*modifier = Modifier\s*)\.fillMaxWidth\(\)\s*\.padding\(horizontal = 16\.dp\)',
    r'\1.then(modifier).fillMaxWidth().padding(horizontal = 16.dp)',
    content,
    flags=re.DOTALL
)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
