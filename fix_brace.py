import re

with open("app/src/main/java/com/example/VaultContentModule.kt", "r") as f:
    content = f.read()

target = """        // Selection Overlay
        if (isSelectionMode) {"""

replacement = """        }
        // Selection Overlay
        if (isSelectionMode) {"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/VaultContentModule.kt", "w") as f:
    f.write(content)
