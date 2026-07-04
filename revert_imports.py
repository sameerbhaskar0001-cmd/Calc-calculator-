with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

content = content.replace("import androidx.compose.material.icons.automirrored.filled.", "import androidx.compose.material.icons.filled.")

# Re-ensure Icons.AutoMirrored is replaced just in case
replacements = {
    "Icons.AutoMirrored.Filled.KeyboardBackspace": "Icons.Default.KeyboardBackspace",
    "Icons.AutoMirrored.Filled.TrendingUp": "Icons.Default.TrendingUp",
    "Icons.AutoMirrored.Filled.VolumeUp": "Icons.Default.VolumeUp",
    "Icons.AutoMirrored.Filled.VolumeOff": "Icons.Default.VolumeOff",
    "Icons.AutoMirrored.Filled.List": "Icons.Default.List",
    "Icons.AutoMirrored.Filled.Article": "Icons.Default.Article",
    "Icons.AutoMirrored.Filled.Sort": "Icons.Default.Sort",
    "Icons.AutoMirrored.Filled.ArrowBack": "Icons.Default.ArrowBack"
}
for old, new in replacements.items():
    content = content.replace(old, new)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
