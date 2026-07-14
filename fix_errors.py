import sys
import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

# 1. Remove duplicate coroutineScope and the snackbar host state at 1999
text = re.sub(r'        val snackbarHostState = remember \{ androidx\.compose\.material3\.SnackbarHostState\(\) \}\n        val coroutineScope = rememberCoroutineScope\(\)\n', r'        val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }\n', text)

# 2. Fix painterResource (line 3064)
text = text.replace('painterResource(id = android.R.drawable.ic_menu_gallery)', 'androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_gallery)')

# 3. Fix AudioFile (line 3078)
text = text.replace('Icons.Default.AudioFile', 'androidx.compose.material.icons.filled.AudioFile')

# 4. Remove the broken SnackbarHost block and fix the braces
pattern_broken = r'            androidx\.compose\.material3\.SnackbarHost\(\n                hostState = snackbarHostState,\n                modifier = Modifier\.align\(Alignment\.BottomCenter\)\.padding\(bottom = 16\.dp\)\n            \)\n        \}\n(\s*if\s*\(activeDocumentToView != null\) \{)'
replacement_broken = r'\1'
text = re.sub(pattern_broken, replacement_broken, text)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)

print("Done")
