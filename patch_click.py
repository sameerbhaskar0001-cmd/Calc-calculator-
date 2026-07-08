content = open('app/src/main/java/com/example/CalculatorScreen.kt').read()
old = """                UnifiedGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("conversion_banner_card"),
                    shape = RoundedCornerShape(24.dp),"""
new = """                UnifiedGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("conversion_banner_card")
                        .clickable { viewModel.fetchLatestRates() },
                    shape = RoundedCornerShape(24.dp),"""
content = content.replace(old, new)
with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
