with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

replacements = {
    "import androidx.compose.material.icons.filled.KeyboardBackspace": "import androidx.compose.material.icons.automirrored.filled.KeyboardBackspace",
    "import androidx.compose.material.icons.filled.TrendingUp": "import androidx.compose.material.icons.automirrored.filled.TrendingUp",
    "import androidx.compose.material.icons.filled.VolumeUp": "import androidx.compose.material.icons.automirrored.filled.VolumeUp",
    "import androidx.compose.material.icons.filled.VolumeOff": "import androidx.compose.material.icons.automirrored.filled.VolumeOff",
    "import androidx.compose.material.icons.filled.List": "import androidx.compose.material.icons.automirrored.filled.List",
    "import androidx.compose.material.icons.filled.Article": "import androidx.compose.material.icons.automirrored.filled.Article",
    "import androidx.compose.material.icons.filled.Sort": "import androidx.compose.material.icons.automirrored.filled.Sort",
    "import androidx.compose.material.icons.filled.ArrowBack": "import androidx.compose.material.icons.automirrored.filled.ArrowBack",
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

with open("app/src/main/java/com/example/SecureCameraComponents.kt", "r") as f:
    content_camera = f.read()

content_camera = content_camera.replace("Icons.Default.ArrowBack", "Icons.AutoMirrored.Filled.ArrowBack")
# Note SecureCameraComponents uses wildcard import: import androidx.compose.material.icons.filled.*
# So we need to add the automirrored wildcard
if "import androidx.compose.material.icons.automirrored.filled.*" not in content_camera:
    content_camera = content_camera.replace(
        "import androidx.compose.material.icons.filled.*",
        "import androidx.compose.material.icons.filled.*\nimport androidx.compose.material.icons.automirrored.filled.*"
    )

with open("app/src/main/java/com/example/SecureCameraComponents.kt", "w") as f:
    f.write(content_camera)

