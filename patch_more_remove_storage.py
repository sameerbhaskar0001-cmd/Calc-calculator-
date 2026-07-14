import sys
import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

target = r'''                            SettingsActionRow\(
                                title = "Storage",
                                subtitle = "View vault storage details",
                                icon = Icons.Default.Folder,
                                iconTint = Color\(0xFF66BB6A\),
                                onClick = \{ activeSection = "Storage" \}
                            \)
                            Spacer\(modifier = Modifier\.fillMaxWidth\(\)\.height\(0\.5\.dp\)\.background\(Color\(0xFF383F56\)\.copy\(alpha = 0\.3f\)\)\)'''

replacement = r''''''

text = re.sub(target, replacement, text)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)

print("Done")
