import sys
import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

pattern = r'(\s*if\s*\(activeDocumentToView != null\) \{)'
replacement = r'''
            androidx.compose.material3.SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
            )
        }\1'''
text = re.sub(pattern, replacement, text)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)

print("Done")
