import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    code = f.read()

old_block = """                        // Top Action Bar overlaying everything
                        val activeFile = activeViewerFiles[activeViewerIndex]
                        val activeParts = activeFile.split("|||")
                        if (activeParts.size >= 6) {
                            val activeId = activeParts[0]
                            val activeName = activeParts[2]
                            val activeIsFav = favoriteFiles.contains(activeId)"""

new_block = """                        // Top Action Bar overlaying everything
                        val activeFile = activeViewerFiles.getOrNull(activeViewerIndex)
                        val activeParts = activeFile?.split("|||") ?: emptyList()
                        if (activeParts.size >= 6) {
                            val activeId = activeParts[0]
                            val activeName = activeParts[2]
                            val activeIsFav = favoriteFiles.contains(activeId)"""

if old_block in code:
    code = code.replace(old_block, new_block)
    with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
        f.write(code)
    print("Replaced successfully")
else:
    print("Not found")

