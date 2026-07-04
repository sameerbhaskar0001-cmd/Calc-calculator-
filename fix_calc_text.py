import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

# Fix the expression Row and Text
pattern_expr = r"(// Expression\s+if \(formulaDisplay\.isNotEmpty\(\)\) \{\s+Row\(\s+modifier = Modifier\s+\.fillMaxWidth\(\))\s+\.horizontalScroll\(scrollState\)(,\s+horizontalArrangement = Arrangement\.End\s+\)\s+\{\s+Text\(\s+text = formulaDisplay,\s+fontSize = 22\.sp,\s+fontWeight = FontWeight\.Medium,\s+color = [^\n]+,\s+fontFamily = FontFamily\.SansSerif,\s+textAlign = TextAlign\.End),\s+maxLines = 1,(\s+modifier = Modifier\.testTag\(\"expression_display\"\)\s+\)\s+\}\s+Spacer\(modifier = Modifier\.height\(4\.dp\)\)\s+\})"

replacement_expr = r"\1\2\3"
content = re.sub(pattern_expr, replacement_expr, content)

# Fix the main output Text
pattern_main = r"(// Main output\s+Row\(\s+modifier = Modifier\.fillMaxWidth\(\),\s+horizontalArrangement = Arrangement\.End\s+\)\s+\{\s+Text\(\s+text = mainDisplay,\s+fontSize = mainFontSize,\s+fontWeight = FontWeight\.Bold,\s+color = mainColor,\s+fontFamily = FontFamily\.SansSerif,\s+textAlign = TextAlign\.End),\s+maxLines = 1,\s+overflow = TextOverflow\.Ellipsis,(\s+modifier = Modifier\.testTag\(\"calc_result_display\"\)\s+\)\s+\})"

replacement_main = r"\1\2"
content = re.sub(pattern_main, replacement_main, content)

# Add verticalScroll to the new weight(1f) Column we created
pattern_col = r"(Column\(\s+modifier = Modifier\.fillMaxWidth\(\)\.weight\(1f\)),(\s+verticalArrangement = Arrangement\.Bottom\s+\)\s+\{\s+// Expression)"
replacement_col = r"\1.verticalScroll(rememberScrollState()),\2"
content = re.sub(pattern_col, replacement_col, content)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
