import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

print(content.count("val biometric = viewModel.biometricEnabled.collectAsStateWithLifecycle().value"))
