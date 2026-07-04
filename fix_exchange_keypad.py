import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

pattern = r"(// Bottom Fixed Area: Quick Add & Keypad\s+Column\(\s+modifier = Modifier\s+\.fillMaxWidth\(\))(\s+\.padding\(bottom = 16\.dp\),\s+verticalArrangement = Arrangement\.spacedBy\(24\.dp\)\s+\)\s+\{)(.*?)(// Custom Numeric Keypad for Exchange Flow.*?)(val keys = listOf\([^)]+\))(.*?)(Column\(\s+modifier = Modifier\.fillMaxWidth\(\),\s+verticalArrangement = Arrangement\.spacedBy\(8\.dp\)\s+\)\s+\{\s+for \(row in keys\) \{\s+Row\(\s+modifier = Modifier\.fillMaxWidth\(\),\s+horizontalArrangement = Arrangement\.spacedBy\(8\.dp\)\s+\)\s+\{\s+for \(char in row\) \{\s+if \(char\.isBlank\(\)\) \{\s+Spacer\(modifier = Modifier\.weight\(1f\))(\.aspectRatio\(1f\))(\)\s+\}\s+else\s+\{\s+val isBackspace = char == \"⌫\"\s+val isClear = char == \"C\"\s+GlassCalculatorKey\(\s+char = char,\s+isOperator = false,\s+isUtility = isClear \|\| isBackspace,\s+isEquals = false,\s+themePurple = ThemePurple,\s+themeLightPurple = ThemeLightPurple,\s+onClick = \{\s+viewModel\.triggerKeypressEffects\(context\)\s+viewModel\.onCurrencyKeyPress\(char\)\s+\},\s+modifier = Modifier\.weight\(1f\))(\.testTag\(\"currency_key_\$char\"\)\s+\)\s+\}\s+\}\s+\}\s+\}\s+\})"

def replacement(match):
    start1 = match.group(1)
    start2 = match.group(2)
    start3 = match.group(3)
    start4 = match.group(4)
    # Replace keys
    keys = """val keys = listOf(
                listOf("C", " ", "⌫"),
                listOf("7", "8", "9"),
                listOf("4", "5", "6"),
                listOf("1", "2", "3"),
                listOf(".", "0", "00")
            )"""
    start6 = match.group(6)
    
    column_str = """Column(
                modifier = Modifier.fillMaxWidth(0.75f).weight(1f).align(Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (row in keys) {
                    Row(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (char in row) {
                            if (char.isBlank()) {
                                Spacer(modifier = Modifier.weight(1f))
                            } else {
                                val isBackspace = char == "⌫"
                                val isClear = char == "C"
                                GlassCalculatorKey(
                                    char = char,
                                    isOperator = false,
                                    isUtility = isClear || isBackspace,
                                    isEquals = false,
                                    themePurple = ThemePurple,
                                    themeLightPurple = ThemeLightPurple,
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        viewModel.onCurrencyKeyPress(char)
                                    },
                                    modifier = Modifier.weight(1f)"""
    
    end_str = match.group(10)
    
    return start1 + "\n                .weight(1.5f)" + start2.replace("24.dp", "12.dp") + start3 + "// Custom Numeric Keypad for Exchange Flow (3 Columns to match main calculator perfectly)\n            " + keys + start6 + column_str + end_str

content = re.sub(pattern, replacement, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
