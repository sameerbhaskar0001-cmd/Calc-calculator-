import re

with open('app/src/main/java/com/example/TranslationProvider.kt', 'r') as f:
    content = f.read()

new_translations = """            "settings" to "Settings",
            "more" to "More",
            "security_settings" to "Security Settings",
            "fake_vault" to "Fake Vault",
            "change_pin" to "Change PIN",
            "export_import" to "Export & Import",
            "hide_apps" to "Hide Apps",
            "about" to "About",
"""

content = content.replace('            "settings" to "Settings",', new_translations)

with open('app/src/main/java/com/example/TranslationProvider.kt', 'w') as f:
    f.write(content)
