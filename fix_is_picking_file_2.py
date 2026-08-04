import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target_export = """    val exportBackupLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.CreateDocument("application/zip")
    ) { uri ->
        if (uri != null) {"""
replacement_export = """    val exportBackupLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.CreateDocument("application/zip")
    ) { uri ->
        viewModel.isPickingFile = false
        if (uri != null) {"""

target_import = """    val importBackupLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {"""
replacement_import = """    val importBackupLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.OpenDocument()
    ) { uri ->
        viewModel.isPickingFile = false
        if (uri != null) {"""

content = content.replace(target_export, replacement_export).replace(target_import, replacement_import)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
