with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

replacements = {
    "Icons.Default.KeyboardBackspace": "Icons.AutoMirrored.Filled.KeyboardBackspace",
    "Icons.Default.TrendingUp": "Icons.AutoMirrored.Filled.TrendingUp",
    "Icons.Default.VolumeUp": "Icons.AutoMirrored.Filled.VolumeUp",
    "Icons.Default.VolumeOff": "Icons.AutoMirrored.Filled.VolumeOff",
    "Icons.Default.List": "Icons.AutoMirrored.Filled.List",
    "Icons.Default.Article": "Icons.AutoMirrored.Filled.Article",
    "Icons.Default.Sort": "Icons.AutoMirrored.Filled.Sort",
    "Icons.Default.ArrowBack": "Icons.AutoMirrored.Filled.ArrowBack"
}

for old, new in replacements.items():
    content = content.replace(old, new)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
