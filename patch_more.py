import sys

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

target = '''SettingsGroup(title = "DATA & ABOUT") {'''
replacement = '''SettingsGroup(title = "DATA & ABOUT") {
                            SettingsActionRow(
                                title = "Storage",
                                subtitle = "View vault storage details",
                                icon = androidx.compose.material.icons.filled.Storage,
                                iconTint = Color(0xFF66BB6A),
                                onClick = { activeSection = "Storage" }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))'''

text = text.replace(target, replacement)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)

print("Done")
