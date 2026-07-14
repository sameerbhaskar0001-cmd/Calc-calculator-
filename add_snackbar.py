import sys
import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

# Replace Toast with Snackbar for Restore
pattern_restore = r'val restored = viewModel\.restoreFromRecent\(recentStr\)\n\s*if\s*\(restored\)\s*\{\n\s*android\.widget\.Toast\.makeText\(context, "Restored to Vault", android\.widget\.Toast\.LENGTH_SHORT\)\.show\(\)\n\s*\} else \{\n\s*android\.widget\.Toast\.makeText\(context, "Failed to restore", android\.widget\.Toast\.LENGTH_SHORT\)\.show\(\)\n\s*\}'
replacement_restore = r'''val restored = viewModel.restoreFromRecent(recentStr)
                                                        if (restored) {
                                                            coroutineScope.launch { snackbarHostState.showSnackbar("File restored successfully.") }
                                                        } else {
                                                            coroutineScope.launch { snackbarHostState.showSnackbar("Failed to restore.") }
                                                        }'''
text = re.sub(pattern_restore, replacement_restore, text)

# Inject SnackbarHost Box before activeCameraMode
pattern_when = r'(\s*when\s*\(activeCameraMode\) \{)'
replacement_when = r'''
    Box(modifier = Modifier.fillMaxSize()) {
        androidx.compose.material3.SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
        )
    }\1'''
text = re.sub(pattern_when, replacement_when, text)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)

print("Done")
