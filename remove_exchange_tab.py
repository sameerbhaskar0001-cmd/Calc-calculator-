import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

# 1. Remove EXCHANGE from ActiveTab enum
content = content.replace("    CALCULATOR,\n    EXCHANGE,\n    VAULT\n}", "    CALCULATOR,\n    VAULT\n}")

# 2. Remove showRateDialog from variables
content = re.sub(r"\s+var showRateDialog by remember \{ mutableStateOf\(false\) \}", "", content)

# 3. Remove ActiveTab.EXCHANGE -> { ... } branch
pattern_exchange_branch = r"                    ActiveTab\.EXCHANGE -> \{\s+ExchangeTabContent\(\s+viewModel = viewModel,\s+onEditRateClick = \{ showRateDialog = true \}\s+\)\s+\}"
content = re.sub(pattern_exchange_branch, "", content)

# 4. Remove Bottom Navigation Bar
pattern_bottom_nav = r"            // Professional Bottom Navigation Bar - only shown if not in vault\s+if \(activeTab != ActiveTab\.VAULT\) \{\s+Row\(\s+modifier = Modifier[\s\S]*?Modifier\.testTag\(\"nav_exchange\"\)\s+\)\s+\}\s+\}"
content = re.sub(pattern_bottom_nav, "", content)

# 5. Remove Rate Customization Dialog
pattern_rate_dialog = r"    // Rate Customization Dialog\s+if \(showRateDialog\) \{[\s\S]*?onDismiss = \{ showRateDialog = false \}\s+\)\s+\}"
content = re.sub(pattern_rate_dialog, "", content)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
