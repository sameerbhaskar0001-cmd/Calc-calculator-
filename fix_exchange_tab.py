import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

pattern = r"@Composable\s+fun ExchangeTabContent\(.*?if \(showTargetSelector\) \{.*?\n        \)\n    \}\n\}"
replacement = """@Composable
fun ExchangeTabContent(
    viewModel: CalculatorViewModel,
    onEditRateClick: () -> Unit
) {
    val usdVal by viewModel.usdInput.collectAsState()
    val inrVal by viewModel.inrInput.collectAsState()
    val activeField by viewModel.activeCurrencyField.collectAsState()
    val rate by viewModel.exchangeRate.collectAsState()
    val sourceCurrency by viewModel.sourceCurrency.collectAsState()
    val targetCurrency by viewModel.targetCurrency.collectAsState()
    val currencies = viewModel.currencies
    val apiStatus by viewModel.apiStatus.collectAsState()

    var showSourceSelector by remember { mutableStateOf(false) }
    var showTargetSelector by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 12.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Minimal Sync & Rate Strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val statusColor = when (apiStatus) {
                        ApiStatus.LOADING -> ThemePurple
                        ApiStatus.SUCCESS -> Color(0xFF2E7D32)
                        ApiStatus.ERROR -> Color(0xFFC62828)
                        else -> TextMedium
                    }
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(statusColor))
                    Text(
                        text = "1 ${sourceCurrency.code} = ${String.format(java.util.Locale.US, "%.4f", rate)} ${targetCurrency.code}",
                        color = TextMedium,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                IconButton(
                    onClick = {
                        viewModel.triggerKeypressEffects(context)
                        viewModel.fetchLatestRates()
                    },
                    modifier = Modifier.size(32.dp),
                    enabled = apiStatus != ApiStatus.LOADING
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Sync rates",
                        tint = ThemePurple,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Cards container with reduced gap and swap button
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // SOURCE INPUT CARD
                    val isSourceActive = activeField == CurrencyField.USD
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
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
                            .clickable {
                                viewModel.triggerKeypressEffects(context)
                                viewModel.onCurrencyFieldSelect(CurrencyField.USD)
                            }
                            .testTag("card_usd"),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { showSourceSelector = true }
                                        .background(KeypadBg)
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(sourceCurrency.emoji, fontSize = 24.sp)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(sourceCurrency.code, fontWeight = FontWeight.Bold, color = TextDark, fontSize = 15.sp)
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Source", tint = ThemePurple)
                                    }
                                }

                                if (isSourceActive) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50))
                                            .background(ThemePurple.copy(alpha = 0.15f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text("ACTIVE", color = ThemePurple, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = usdVal.ifEmpty { "0" },
                                fontSize = 32.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (usdVal.isEmpty()) TextMedium.copy(alpha = 0.5f) else TextDark,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("usd_display"),
                                textAlign = TextAlign.Right,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // TARGET INPUT CARD
                    val isTargetActive = activeField == CurrencyField.INR
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
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
                            .clickable {
                                viewModel.triggerKeypressEffects(context)
                                viewModel.onCurrencyFieldSelect(CurrencyField.INR)
                            }
                            .testTag("card_inr"),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { showTargetSelector = true }
                                        .background(KeypadBg)
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(targetCurrency.emoji, fontSize = 24.sp)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(targetCurrency.code, fontWeight = FontWeight.Bold, color = TextDark, fontSize = 15.sp)
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Target", tint = ThemePurple)
                                    }
                                }

                                if (isTargetActive) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50))
                                            .background(ThemePurple.copy(alpha = 0.15f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text("ACTIVE", color = ThemePurple, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = inrVal.ifEmpty { "0" },
                                fontSize = 32.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (inrVal.isEmpty()) TextMedium.copy(alpha = 0.5f) else TextDark,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("inr_display"),
                                textAlign = TextAlign.Right,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // SWAP BUTTON IN MIDDLE OVERLAYING THE CARDS
                IconButton(
                    onClick = {
                        viewModel.triggerKeypressEffects(context)
                        viewModel.swapCurrencies()
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ThemePurple)
                        .border(4.dp, BrandBg, CircleShape) // Create a cutout effect
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapVert,
                        contentDescription = "Swap currencies",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Bottom Fixed Area: Quick Add & Keypad
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Quick Presets Row
            val presets = if (activeField == CurrencyField.USD) {
                listOf(1.0, 5.0, 10.0, 50.0, 100.0, 500.0)
            } else {
                listOf(100.0, 500.0, 1000.0, 2000.0, 5000.0, 10000.0)
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(presets) { preset ->
                    val symbol = if (activeField == CurrencyField.USD) sourceCurrency.symbol else targetCurrency.symbol
                    val formatted = if (preset % 1.0 == 0.0) preset.toLong().toString() else preset.toString()
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.Black.copy(alpha = 0.3f))
                            .clickable {
                                viewModel.triggerKeypressEffects(context)
                                viewModel.applyQuickAdd(preset)
                            }
                            .border(1.dp, ThemeContainerBorder.copy(alpha=0.5f), RoundedCornerShape(50))
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                            .testTag("quick_add_$preset"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+ $symbol$formatted",
                            color = TextDark,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Custom Numeric Keypad for Exchange Flow (4 Columns to match main calculator)
            val keys = listOf(
                listOf("7", "8", "9", "⌫"),
                listOf("4", "5", "6", "C"),
                listOf("1", "2", "3", " "),
                listOf(" ", "0", ".", " ")
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (row in keys) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (char in row) {
                            if (char.isBlank()) {
                                Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
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
                                    modifier = Modifier.weight(1f).testTag("currency_key_$char")
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showSourceSelector) {
        CurrencySelectorDialog(
            currencies = currencies,
            onDismiss = { showSourceSelector = false },
            onSelect = {
                viewModel.onSourceCurrencyChange(it)
                showSourceSelector = false
            }
        )
    }

    if (showTargetSelector) {
        CurrencySelectorDialog(
            currencies = currencies,
            onDismiss = { showTargetSelector = false },
            onSelect = {
                viewModel.onTargetCurrencyChange(it)
                showTargetSelector = false
            }
        )
    }
}"""

content = re.sub(pattern, replacement, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
