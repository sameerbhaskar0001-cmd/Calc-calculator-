import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

pattern = r"(// Minimal Sync & Rate Strip\s+Row\(\s+modifier = Modifier\.fillMaxWidth\(\),\s+horizontalArrangement = Arrangement\.SpaceBetween,\s+verticalAlignment = Alignment\.CenterVertically\s+\)\s+\{)(.*?)(// Cards container with reduced gap and swap button)"

replacement = r"""// Minimal Rate Display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "1 ${sourceCurrency.code} = ${String.format(java.util.Locale.US, "%.4f", rate)} ${targetCurrency.code}",
                    color = TextMedium.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Normal
                )
            }
            \3"""

content = re.sub(pattern, replacement, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
