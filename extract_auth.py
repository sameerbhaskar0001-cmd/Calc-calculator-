import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

start_idx = 5064
end_idx = 5606

block_lines = lines[start_idx:end_idx+1]
block = "".join(block_lines)

# It starts with:
#                 "Authentication" -> {
# and ends with
#                 }

inner_block = "".join(lines[start_idx+1:end_idx])

# Fix `activeSection = "..."`
inner_block = re.sub(r'activeSection\s*=\s*"([^"]+)"', r'onNavigate("\1")', inner_block)
inner_block = inner_block.replace('activeSection = "__BACK__"', 'onNavigate("__BACK__")')

replacement = """                "Authentication" -> {
                    AuthenticationSection(viewModel = viewModel, onNavigate = { activeSection = it })
                }
"""

new_lines = lines[:start_idx] + [replacement] + lines[end_idx+1:]

composable = """
@Composable
fun AuthenticationSection(viewModel: CalculatorViewModel, onNavigate: (String) -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val ThemePurple = Color(0xFF6C63FF)
    val TextMedium = Color(0xFF9094A6)
""" + inner_block + """
}
"""

new_content = "".join(new_lines) + composable

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(new_content)
