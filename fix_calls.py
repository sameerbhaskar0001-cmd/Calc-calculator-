import re
with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

content = content.replace(
    "AuthenticationSection(viewModel = viewModel, onNavigate = { activeSection = it })",
    "AuthenticationSection(viewModel = viewModel, onNavigate = { activeSection = it }, modifier = Modifier.weight(1f))"
)

content = content.replace(
    "ProtectionSection(viewModel = viewModel)",
    "ProtectionSection(viewModel = viewModel, modifier = Modifier.weight(1f))"
)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
