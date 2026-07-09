import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    code = f.read()

code = code.replace("fileSerialized = activeFile,", "fileSerialized = activeFile!!,")
code = code.replace("viewModel.deleteVaultFile(activeFile)", "viewModel.deleteVaultFile(activeFile!!)")

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(code)
print("Replaced nullables")
