import sys

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

target = '''                "About" -> {
                    Column('''
replacement = '''                "Storage" -> {
                    StorageScreenSection(
                        onBack = { activeSection = "__BACK__" },
                        onNavigateToRecentlyDeleted = { activeSection = "Recently Deleted" }
                    )
                }
                "About" -> {
                    Column('''

text = text.replace(target, replacement)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)

print("Done")
