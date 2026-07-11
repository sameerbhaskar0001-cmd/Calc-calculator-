with open("app/src/main/java/com/example/CalculatorScreen.kt") as f:
    text = f.read()

import re

# Remove state declarations
text = re.sub(r"(\s*var showColorOptions by remember \{ mutableStateOf\(false\) \})", "", text)
text = re.sub(r"(\s*var showBgColorOptions by remember \{ mutableStateOf\(false\) \})", "", text)
text = re.sub(r"(\s*showColorOptions = [^\n]*)", "", text)
text = re.sub(r"(\s*showBgColorOptions = [^\n]*)", "", text)

# Remove IconButtons
# Color Button
pattern_color_btn = r"\s*IconButton\(\s*onClick = \{\s*viewModel\.triggerKeypressEffects\(context\)\s*viewModel\.updateLastInteraction\(\)\s*\},[^\}]*\}\) \{\s*Icon\(Icons\.Default\.FormatColorText[^\}]*\}\s*\}"
# But we already removed showColorOptions = ... so the onClick block might look different.
