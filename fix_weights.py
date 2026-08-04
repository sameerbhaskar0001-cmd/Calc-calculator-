import re
with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

# For AuthenticationSection
content = content.replace(
    "fun AuthenticationSection(viewModel: CalculatorViewModel, onNavigate: (String) -> Unit) {\n    val context = androidx.compose.ui.platform.LocalContext.current\n    val ThemePurple = Color(0xFF6C63FF)\n    val TextMedium = Color(0xFF9094A6)\n                    var showAppLockDialog by remember { mutableStateOf(false) }",
    "fun AuthenticationSection(viewModel: CalculatorViewModel, onNavigate: (String) -> Unit, modifier: Modifier = Modifier) {\n    val context = androidx.compose.ui.platform.LocalContext.current\n    val ThemePurple = Color(0xFF6C63FF)\n    val TextMedium = Color(0xFF9094A6)\n                    var showAppLockDialog by remember { mutableStateOf(false) }"
)

# For ProtectionSection
content = content.replace(
    "fun ProtectionSection(viewModel: CalculatorViewModel) {\n    val context = androidx.compose.ui.platform.LocalContext.current\n    val ThemePurple = Color(0xFF6C63FF)\n    val TextMedium = Color(0xFF9094A6)\n                    var showAutoLockDialog by remember { mutableStateOf(false) }",
    "fun ProtectionSection(viewModel: CalculatorViewModel, modifier: Modifier = Modifier) {\n    val context = androidx.compose.ui.platform.LocalContext.current\n    val ThemePurple = Color(0xFF6C63FF)\n    val TextMedium = Color(0xFF9094A6)\n                    var showAutoLockDialog by remember { mutableStateOf(false) }"
)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
