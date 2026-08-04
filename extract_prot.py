import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

start_idx = 5067
end_idx = 5593

block_lines = lines[start_idx:end_idx+1]

inner_block = "".join(lines[start_idx+1:end_idx])

replacement = """                "Protection" -> {
                    ProtectionSection(viewModel = viewModel)
                }
"""

new_lines = lines[:start_idx] + [replacement] + lines[end_idx+1:]

composable = """
@Composable
fun ProtectionSection(viewModel: CalculatorViewModel) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val ThemePurple = Color(0xFF6C63FF)
    val TextMedium = Color(0xFF9094A6)
""" + inner_block + """
}
"""

new_content = "".join(new_lines) + composable

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(new_content)
