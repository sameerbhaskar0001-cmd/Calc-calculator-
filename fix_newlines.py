with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

content = content.replace("package com.exampleimport", "package com.example\nimport")
content = content.replace("PasswordVisualTransformationpackage", "PasswordVisualTransformation\npackage")
content = content.replace("WithLifecycleimport", "WithLifecycle\nimport")
content = content.replace("togetherWithimport", "togetherWith\nimport")
content = content.replace("Contentimport", "Content\nimport")
content = content.replace("tweenimport", "tween\nimport")
content = content.replace("fadeInimport", "fadeIn\nimport")
content = content.replace("fadeOutimport", "fadeOut\nimport")
content = content.replace("borderimport", "border\nimport")
content = content.replace("Textimport", "Text\nimport")

# Actually, let's just do a regex
import re
content = re.sub(r'(import [a-zA-Z0-9_.]*)import ', r'\1\nimport ', content)
content = re.sub(r'import ', r'\nimport ', content)
# But wait, there could be multiple instances.
