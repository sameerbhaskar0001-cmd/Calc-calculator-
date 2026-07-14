import sys
import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

# Add snackbar state definition
pattern_state = r'(var lastNonNullNote by remember \{ mutableStateOf<String\?>\(null\) \})'
replacement_state = r'\1\n        val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }\n        val coroutineScope = rememberCoroutineScope()'
text = re.sub(pattern_state, replacement_state, text, count=1)

# Add SnackbarHost to the Box
pattern_box = r'(Box\(\s*modifier = Modifier\s*\.fillMaxSize\(\)\s*\.background\(Color\(0xFF090D1A\)\) // Force dark background for Secure Vault\s*\)\s*\{)([\s\S]*?)(\s*if\s*\(activeDocumentToView != null\) \{)'
# We will inject the SnackbarHost just before `if (activeDocumentToView != null)` 
# Wait, actually let's just find the closing brace of the Box. It's safer to put it after the Column ends.
# The Box contains: if (activeSection == "Private Browser") { ... } else { Column { ... } } ...
# Let's search for activeCameraMode
pattern_camera = r'(if \(activeCameraMode != null\) \{[\s\S]*?\}\s*\})'
replacement_camera = r'\1\n            androidx.compose.material3.SnackbarHost(\n                hostState = snackbarHostState,\n                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)\n            )'
text = re.sub(pattern_camera, replacement_camera, text, count=1)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)

print("Done")
