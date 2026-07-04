import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

# 1. Remove Header Icon
header_icon_pattern = r"Icon\(\s*imageVector = Icons\.Default\.Calculate,\s*contentDescription = \"Calculator Icon\",\s*tint = TextMedium,\s*modifier = Modifier\.size\(20\.dp\)\s*\)"
content = re.sub(header_icon_pattern, "", content)

# 2. Add padding to ExchangeTabContent Column
exchange_col_pattern = r"(\.verticalScroll\(rememberScrollState\(\)\)\s*)\.padding\(vertical = 12\.dp\)"
content = re.sub(exchange_col_pattern, r"\1.padding(horizontal = 24.dp, vertical = 12.dp)", content)

# 3. Swap Button padding
swap_btn_pattern = r"(// SWAP BUTTON IN MIDDLE\s*Box\(\s*modifier = Modifier\.fillMaxWidth\(\))"
content = re.sub(swap_btn_pattern, r"\1.padding(vertical = 12.dp)", content)

# 4. Glassmorphism for Sync Banner
sync_banner_pattern = r"(\.fillMaxWidth\(\)\s*)\.clip\(RoundedCornerShape\(16\.dp\)\)\s*\.background\(Color\.White\)\s*\.border\(1\.dp, ThemeContainerBorder\.copy\(alpha = 0\.2f\), RoundedCornerShape\(16\.dp\)\)"
sync_banner_repl = r"""\1.clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .border(
                        width = 1.dp,
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(ThemePurple.copy(alpha = 0.3f), Color.Transparent),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(0f, 300f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )"""
content = re.sub(sync_banner_pattern, sync_banner_repl, content)

# 5. Glassmorphism for Info Strip
info_strip_pattern = r"(\.fillMaxWidth\(\)\s*)\.clip\(RoundedCornerShape\(16\.dp\)\)\s*\.background\(ThemeLightPurple\.copy\(alpha = 0\.5f\)\)\s*\.border\(1\.dp, ThemeContainerBorder\.copy\(alpha = 0\.3f\), RoundedCornerShape\(16\.dp\)\)"
info_strip_repl = r"""\1.clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .border(
                        width = 1.dp,
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(ThemePurple.copy(alpha = 0.3f), Color.Transparent),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(0f, 300f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )"""
content = re.sub(info_strip_pattern, info_strip_repl, content)

# 6. Glassmorphism for USD Card
usd_card_pattern = r"(Card\(\s*modifier = Modifier\s*\.fillMaxWidth\(\))\s*\.border\([\s\S]*?shape = RoundedCornerShape\(20\.dp\)\s*\)\s*(\.clickable \{[\s\S]*?\.testTag\(\"card_usd\"\),\s*colors = CardDefaults\.cardColors\([\s\S]*?containerColor = [\s\S]*?\)\s*,)"
usd_card_repl = r"""\1
                    .height(140.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .border(
                        width = 1.dp,
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(ThemePurple.copy(alpha = if (isSourceActive) 0.6f else 0.3f), Color.Transparent),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(0f, 300f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    \2"""
content = re.sub(usd_card_pattern, usd_card_repl, content)

# Remove the containerColor in USD Card
usd_card_colors_pattern = r"(testTag\(\"card_usd\"\),\s*)colors = CardDefaults\.cardColors\(\s*containerColor = [^\n]*\n\s*\),"
content = re.sub(usd_card_colors_pattern, r"\1colors = CardDefaults.cardColors(containerColor = Color.Transparent),", content)


# 7. Glassmorphism for INR Card
inr_card_pattern = r"(Card\(\s*modifier = Modifier\s*\.fillMaxWidth\(\))\s*\.border\([\s\S]*?shape = RoundedCornerShape\(20\.dp\)\s*\)\s*(\.clickable \{[\s\S]*?\.testTag\(\"card_inr\"\),\s*colors = CardDefaults\.cardColors\([\s\S]*?containerColor = [\s\S]*?\)\s*,)"
inr_card_repl = r"""\1
                    .height(140.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .border(
                        width = 1.dp,
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(ThemePurple.copy(alpha = if (isTargetActive) 0.6f else 0.3f), Color.Transparent),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(0f, 300f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    \2"""
content = re.sub(inr_card_pattern, inr_card_repl, content)

# Remove the containerColor in INR Card
inr_card_colors_pattern = r"(testTag\(\"card_inr\"\),\s*)colors = CardDefaults\.cardColors\(\s*containerColor = [^\n]*\n\s*\),"
content = re.sub(inr_card_colors_pattern, r"\1colors = CardDefaults.cardColors(containerColor = Color.Transparent),", content)


with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
