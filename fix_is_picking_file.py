import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target1 = """                                            try {
                                                exportBackupLauncher.launch("vault_backup.zip")
                                            } catch (e: Exception) {"""
replacement1 = """                                            try {
                                                viewModel.isPickingFile = true
                                                exportBackupLauncher.launch("vault_backup.zip")
                                            } catch (e: Exception) {"""

target2 = """                                            try {
                                                importBackupLauncher.launch(arrayOf("application/zip"))
                                            } catch (e: Exception) {"""
replacement2 = """                                            try {
                                                viewModel.isPickingFile = true
                                                importBackupLauncher.launch(arrayOf("application/zip"))
                                            } catch (e: Exception) {"""

content = content.replace(target1, replacement1).replace(target2, replacement2)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
