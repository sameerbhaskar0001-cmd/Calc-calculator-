import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target = """                                            try {
                                                importBackupLauncher.launch(arrayOf("application/zip", "application/octet-stream", "*/*"))
                                            } catch (e: Exception) {"""
replacement = """                                            try {
                                                viewModel.isPickingFile = true
                                                importBackupLauncher.launch(arrayOf("application/zip", "application/octet-stream", "*/*"))
                                            } catch (e: Exception) {"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
