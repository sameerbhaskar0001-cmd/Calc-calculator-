import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

# Remove the rate customization dialog
pattern_dialog = r"    // Rate Customization Dialog\s+if \(showRateDialog\).*?onDismiss = \{ showRateDialog = false \}\s+\)\s+\}"
# Since it might have missed, let's use a simpler regex
pattern_dialog = r"    // Rate Customization Dialog[\s\S]*?            \)\s+        \}\s+    \}"
content = re.sub(pattern_dialog, "", content)

# Remove var showRateDialog by remember { mutableStateOf(false) }
content = re.sub(r"\s+var showRateDialog by remember \{ mutableStateOf\(false\) \}", "", content)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
