import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

content = content.replace('activeSection = "Settings"', 'activeSection = "More"')
content = content.replace('activeSection == "Settings"', 'activeSection == "More"')
content = content.replace('"Settings" -> "settings"', '"More" -> "more"\n                                            "Security Settings" -> "security_settings"\n                                            "Fake Vault" -> "fake_vault"\n                                            "Change PIN" -> "change_pin"\n                                            "Export / Import" -> "export_import"\n                                            "Hide Apps" -> "hide_apps"\n                                            "About" -> "about"')

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
