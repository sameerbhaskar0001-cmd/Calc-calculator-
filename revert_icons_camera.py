with open("app/src/main/java/com/example/SecureCameraComponents.kt", "r") as f:
    content = f.read()

replacements = {
    "Icons.AutoMirrored.Filled.ArrowBack": "Icons.Default.ArrowBack"
}

for old, new in replacements.items():
    content = content.replace(old, new)

with open("app/src/main/java/com/example/SecureCameraComponents.kt", "w") as f:
    f.write(content)
