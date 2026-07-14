import sys
import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

pattern = r'(Text\(\n\s*text = "Private Workspace",\n\s*fontSize = 10\.sp,\n\s*color = Color\(0xFF4CAF50\),\n\s*fontWeight = FontWeight\.SemiBold\n\s*\)\n\s*\})'
replacement = r'''\1
                            if (activeSection == "Recently Deleted") {
                                Text(
                                    text = "Deleted items are automatically removed after 30 days.",
                                    fontSize = 10.sp,
                                    color = TextMedium.copy(alpha = 0.8f),
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }'''

text = re.sub(pattern, replacement, text)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)

print("Done")
