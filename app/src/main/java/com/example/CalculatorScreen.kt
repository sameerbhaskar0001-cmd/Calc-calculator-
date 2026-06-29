package com.example

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardBackspace
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

// "Professional Polish" Palette (Material 3 Purple/Lavender)
private val BrandBg = Color(0xFFFEF7FF)              // Primary soft canvas background
private val TextDark = Color(0xFF1D1B20)              // Primary high-contrast text
private val TextMedium = Color(0xFF49454F)            // Secondary body / utility text
private val ThemePurple = Color(0xFF6750A4)           // Brand primary purple
private val ThemeLightPurple = Color(0xFFE8DEF8)      // Brand secondary container purple
private val ThemeContainerBorder = Color(0xFFD0BCFF)  // Brand border accent

private val KeypadBg = Color(0xFFF3EDF7)              // Light background for utility keys
private val DigitBg = Color(0xFFFFFFFF)               // Crisp white for digit keys

enum class ActiveTab {
    CALCULATOR,
    EXCHANGE,
    RATES_HISTORY
}

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf(ActiveTab.CALCULATOR) }
    var showRateDialog by remember { mutableStateOf(false) }

    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = BrandBg
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ThemePurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = "Calculator Icon",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "CalcCurrency",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextDark,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "Play Store Signature Edition",
                            style = MaterialTheme.typography.bodySmall,
                            color = ThemePurple,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Context Action Button (Edit rate)
                IconButton(
                    onClick = {
                        viewModel.triggerKeypressEffects(context)
                        showRateDialog = true
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ThemeLightPurple.copy(alpha = 0.4f))
                        .testTag("header_action_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Context Option",
                        tint = ThemePurple,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Main View Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                when (activeTab) {
                    ActiveTab.CALCULATOR -> {
                        CalculatorTabContent(viewModel = viewModel)
                    }
                    ActiveTab.EXCHANGE -> {
                        ExchangeTabContent(
                            viewModel = viewModel,
                            onEditRateClick = { showRateDialog = true }
                        )
                    }
                    ActiveTab.RATES_HISTORY -> {
                        RatesHistoryTabContent(
                            viewModel = viewModel,
                            onEditRateClick = { showRateDialog = true }
                        )
                    }
                }
            }

            // Professional Bottom Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(KeypadBg)
                    .border(width = 1.dp, color = ThemeContainerBorder.copy(alpha = 0.3f))
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    label = "Calculator",
                    icon = Icons.Default.Calculate,
                    isSelected = activeTab == ActiveTab.CALCULATOR,
                    onClick = {
                        viewModel.triggerKeypressEffects(context)
                        activeTab = ActiveTab.CALCULATOR
                    },
                    modifier = Modifier.testTag("nav_calculator")
                )

                BottomNavItem(
                    label = "Exchange",
                    icon = Icons.Default.CurrencyExchange,
                    isSelected = activeTab == ActiveTab.EXCHANGE,
                    onClick = {
                        viewModel.triggerKeypressEffects(context)
                        activeTab = ActiveTab.EXCHANGE
                    },
                    modifier = Modifier.testTag("nav_exchange")
                )

                BottomNavItem(
                    label = "Settings & Rates",
                    icon = Icons.Default.Settings,
                    isSelected = activeTab == ActiveTab.RATES_HISTORY,
                    onClick = {
                        viewModel.triggerKeypressEffects(context)
                        activeTab = ActiveTab.RATES_HISTORY
                    },
                    modifier = Modifier.testTag("nav_rates")
                )
            }
        }
    }

    // Rate Customization Dialog
    if (showRateDialog) {
        val srcCurrency by viewModel.sourceCurrency.collectAsState()
        val tgtCurrency by viewModel.targetCurrency.collectAsState()
        var rateInput by remember { mutableStateOf(viewModel.exchangeRate.value.toString()) }
        var isError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showRateDialog = false },
            title = {
                Text(
                    text = "Update Custom Rate",
                    color = TextDark,
                    fontWeight = FontWeight.Bold
                )
            },
            containerColor = BrandBg,
            text = {
                Column {
                    Text(
                        text = "Configure current conversion value from ${srcCurrency.name} (${srcCurrency.code}) to ${tgtCurrency.name} (${tgtCurrency.code}):",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = rateInput,
                        onValueChange = {
                            rateInput = it
                            isError = it.toDoubleOrNull() == null || it.toDouble() <= 0
                        },
                        label = { Text("1 ${srcCurrency.code} = ... ${tgtCurrency.code}") },
                        isError = isError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ThemePurple,
                            focusedLabelColor = ThemePurple,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                            unfocusedLabelColor = Color.Gray,
                            focusedTextColor = TextDark,
                            unfocusedTextColor = TextDark,
                            errorTextColor = Color.Red
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("exchange_rate_input")
                    )
                    if (isError) {
                        Text(
                            text = "Please enter a valid rate greater than 0",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val rate = rateInput.toDoubleOrNull()
                        if (rate != null && rate > 0) {
                            viewModel.updateExchangeRate(rate)
                            showRateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                    enabled = !isError,
                    modifier = Modifier.testTag("save_rate_button")
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRateDialog = false }) {
                    Text("Cancel", color = TextMedium)
                }
            }
        )
    }
}

@Composable
fun BottomNavItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeColor = ThemePurple
    val inactiveColor = TextMedium.copy(alpha = 0.6f)
    val color by animateColorAsState(targetValue = if (isSelected) activeColor else inactiveColor, label = "nav_item_color")

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = color,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

// ==========================================
// CALCULATOR VIEW COMPONENT
// ==========================================
@Composable
fun CalculatorTabContent(
    viewModel: CalculatorViewModel
) {
    val expression by viewModel.expression.collectAsState()
    val calcResult by viewModel.calcResult.collectAsState()
    val rate by viewModel.exchangeRate.collectAsState()
    val sourceCurrency by viewModel.sourceCurrency.collectAsState()
    val targetCurrency by viewModel.targetCurrency.collectAsState()

    val scrollState = rememberScrollState()
    var isSplitterExpanded by remember { mutableStateOf(false) }

    // Determine numerical value for live split calculation
    val numericResult = calcResult.toDoubleOrNull() ?: expression.toDoubleOrNull() ?: 0.0
    val convertedTargetVal = numericResult * rate
    val df = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Upper Display Zone
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            // Expression
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = expression.ifEmpty { "0" },
                    fontSize = 18.sp,
                    color = TextMedium.copy(alpha = 0.7f),
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier.testTag("expression_display")
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Main output
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = calcResult.ifEmpty { if (expression.isEmpty()) "0" else "" },
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Light,
                    color = TextDark,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.testTag("calc_result_display")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Conversion and split panel container
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Real-time Conversion Banner (Premium Glassmorphic-style M3 card)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 1.dp, shape = RoundedCornerShape(20.dp))
                        .border(
                            width = 1.dp,
                            color = ThemeContainerBorder.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { isSplitterExpanded = !isSplitterExpanded }
                        .testTag("conversion_banner_card"),
                    colors = CardDefaults.cardColors(containerColor = ThemeLightPurple),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "CONVERSION (${sourceCurrency.code} → ${targetCurrency.code})",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ThemePurple,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                              ) {
                                Text(
                                    text = "${sourceCurrency.symbol}${df.format(numericResult)}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextDark
                                )
                                Text(
                                    text = "=",
                                    fontSize = 11.sp,
                                    color = TextMedium.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "${targetCurrency.symbol}${df.format(convertedTargetVal)}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF21005D)
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (isSplitterExpanded) "CLOSE SPLIT" else "SPLIT BILL",
                                fontSize = 10.sp,
                                color = ThemePurple,
                                fontWeight = FontWeight.Black
                            )
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(ThemePurple),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CurrencyExchange,
                                    contentDescription = "Exchange Indicator",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // Smooth expandable Split Bill panel
                AnimatedVisibility(visible = isSplitterExpanded) {
                    BillSplitterCard(
                        viewModel = viewModel,
                        totalBill = numericResult,
                        currencySymbol = sourceCurrency.symbol,
                        targetCurrencySymbol = targetCurrency.symbol,
                        targetExchangeRate = rate
                    )
                }
            }
        }

        // Keypad Grid Zone
        val buttons = listOf(
            listOf("C", "+/-", "%", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "⌫", "=")
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (row in buttons) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (char in row) {
                        val isOperator = char == "÷" || char == "×" || char == "-" || char == "+"
                        val isUtility = char == "C" || char == "+/-" || char == "%" || char == "⌫"
                        val isEquals = char == "="

                        val containerColor = when {
                            isEquals -> ThemePurple
                            isOperator -> ThemeLightPurple
                            isUtility -> KeypadBg
                            else -> DigitBg
                        }

                        val contentColor = when {
                            isEquals -> Color.White
                            isOperator -> Color(0xFF21005D)
                            isUtility -> ThemePurple
                            else -> TextDark
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1.25f)
                                .shadow(
                                    elevation = if (isEquals) 3.dp else if (!isUtility && !isOperator) 1.dp else 0.dp,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clip(RoundedCornerShape(20.dp))
                                .background(containerColor)
                                .clickable {
                                    viewModel.triggerKeypressEffects(context)
                                    viewModel.onCalcKeyPress(char)
                                }
                                .testTag("key_$char"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (char == "⌫") {
                                Icon(
                                    imageVector = Icons.Default.KeyboardBackspace,
                                    contentDescription = "Backspace",
                                    tint = contentColor,
                                    modifier = Modifier.size(22.dp)
                                )
                            } else {
                                Text(
                                    text = char,
                                    fontSize = if (isOperator || isEquals) 22.sp else 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = contentColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// BILL SPLITTER CARD
// ==========================================
@Composable
fun BillSplitterCard(
    viewModel: CalculatorViewModel,
    totalBill: Double,
    currencySymbol: String,
    targetCurrencySymbol: String,
    targetExchangeRate: Double,
    modifier: Modifier = Modifier
) {
    val numPeople by viewModel.numPeople.collectAsState()
    val tipPercentage by viewModel.tipPercentage.collectAsState()

    val tipAmount = totalBill * (tipPercentage / 100.0)
    val grandTotal = totalBill + tipAmount
    val amountPerPerson = grandTotal / numPeople

    val convertedAmountPerPerson = amountPerPerson * targetExchangeRate

    val df = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = ThemeContainerBorder.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = DigitBg),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Smart Bill Splitter",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextDark
                )
                Text(
                    text = "No dead ends",
                    fontSize = 10.sp,
                    color = ThemePurple,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Split and Tip Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tip Selection
                Column(modifier = Modifier.weight(1.2f)) {
                    Text("Select Tip", fontSize = 10.sp, color = TextMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(10, 15, 20).forEach { pct ->
                            val isSelected = tipPercentage == pct
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) ThemePurple else KeypadBg)
                                    .clickable { viewModel.updateTipPercentage(pct) }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "$pct%",
                                    color = if (isSelected) Color.White else TextDark,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // People counter
                Column(
                    modifier = Modifier.weight(0.8f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text("Split count", fontSize = 10.sp, color = TextMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.decrementPeople() },
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(KeypadBg)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Less", modifier = Modifier.size(14.dp), tint = ThemePurple)
                        }
                        Text(
                            text = numPeople.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextDark
                        )
                        IconButton(
                            onClick = { viewModel.incrementPeople() },
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(KeypadBg)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "More", modifier = Modifier.size(14.dp), tint = ThemePurple)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Split breakdown display
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ThemeLightPurple.copy(alpha = 0.3f))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("EACH PAY", fontSize = 9.sp, color = ThemePurple, fontWeight = FontWeight.Bold)
                    Text(
                        text = "$currencySymbol${df.format(amountPerPerson)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF21005D)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("IN TARGET VALUE", fontSize = 9.sp, color = TextMedium, fontWeight = FontWeight.Bold)
                    Text(
                        text = "$targetCurrencySymbol${df.format(convertedAmountPerPerson)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Total details summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tip: $currencySymbol${df.format(tipAmount)} (${tipPercentage}%)",
                    fontSize = 10.sp,
                    color = TextMedium
                )
                Text(
                    text = "Total: $currencySymbol${df.format(grandTotal)}",
                    fontSize = 10.sp,
                    color = TextDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ==========================================
// CURRENCY EXCHANGE VIEW COMPONENT
// ==========================================
@Composable
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

    var showSourceSelector by remember { mutableStateOf(false) }
    var showTargetSelector by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Live Exchange Info Strip with flag icons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ThemeLightPurple.copy(alpha = 0.5f))
                    .border(1.dp, ThemeContainerBorder.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .clickable { onEditRateClick() }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = "Trending Rate",
                        tint = ThemePurple,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Rate: 1 ${sourceCurrency.code} = ${String.format(Locale.US, "%.4f", rate)} ${targetCurrency.code}",
                        color = Color(0xFF21005D),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "EDIT RATE",
                    color = ThemePurple,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black
                )
            }

            // SOURCE INPUT CARD
            val isSourceActive = activeField == CurrencyField.USD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (isSourceActive) 2.dp else 1.dp,
                        color = if (isSourceActive) ThemePurple else ThemeContainerBorder.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable {
                        viewModel.triggerKeypressEffects(context)
                        viewModel.onCurrencyFieldSelect(CurrencyField.USD)
                    }
                    .testTag("card_usd"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSourceActive) ThemeLightPurple.copy(alpha = 0.4f) else DigitBg
                ),
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
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(sourceCurrency.code, fontWeight = FontWeight.Bold, color = TextDark, fontSize = 15.sp)
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Source", tint = ThemePurple)
                                }
                                Text(sourceCurrency.name, color = TextMedium, fontSize = 10.sp)
                            }
                        }

                        if (isSourceActive) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(ThemePurple.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("ACTIVE INPUT", color = ThemePurple, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = usdVal.ifEmpty { "0" },
                        fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (usdVal.isEmpty()) TextMedium.copy(alpha = 0.5f) else TextDark,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("usd_display")
                    )
                }
            }

            // SWAP BUTTON IN MIDDLE
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        viewModel.triggerKeypressEffects(context)
                        viewModel.swapCurrencies()
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(ThemePurple)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Swap currencies",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // TARGET INPUT CARD
            val isTargetActive = activeField == CurrencyField.INR
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (isTargetActive) 2.dp else 1.dp,
                        color = if (isTargetActive) ThemePurple else ThemeContainerBorder.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable {
                        viewModel.triggerKeypressEffects(context)
                        viewModel.onCurrencyFieldSelect(CurrencyField.INR)
                    }
                    .testTag("card_inr"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isTargetActive) ThemeLightPurple.copy(alpha = 0.4f) else DigitBg
                ),
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
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(targetCurrency.code, fontWeight = FontWeight.Bold, color = TextDark, fontSize = 15.sp)
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Target", tint = ThemePurple)
                                }
                                Text(targetCurrency.name, color = TextMedium, fontSize = 10.sp)
                            }
                        }

                        if (isTargetActive) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(ThemePurple.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("ACTIVE INPUT", color = ThemePurple, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = inrVal.ifEmpty { "0" },
                        fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (inrVal.isEmpty()) TextMedium.copy(alpha = 0.5f) else TextDark,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("inr_display")
                    )
                }
            }

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
                            .background(ThemeLightPurple)
                            .clickable {
                                viewModel.triggerKeypressEffects(context)
                                viewModel.applyQuickAdd(preset)
                            }
                            .border(1.dp, ThemeContainerBorder, RoundedCornerShape(50))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("quick_add_$preset"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+ $symbol$formatted",
                            color = Color(0xFF21005D),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Custom Numeric Keypad for Exchange Flow
        val keys = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("C", "0", "."),
            listOf("⌫")
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (row in keys) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (char in row) {
                        val isBackspace = char == "⌫"
                        val isClear = char == "C"
                        val containerBg = if (isBackspace || isClear) KeypadBg else DigitBg
                        val contentColor = if (isBackspace || isClear) ThemePurple else TextDark

                        Box(
                            modifier = Modifier
                                .weight(if (isBackspace) 3f else 1f)
                                .height(54.dp)
                                .shadow(elevation = if (!isBackspace && !isClear) 1.dp else 0.dp, shape = RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                                .background(containerBg)
                                .clickable {
                                    viewModel.triggerKeypressEffects(context)
                                    viewModel.onCurrencyKeyPress(char)
                                }
                                .testTag("currency_key_$char"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (char == "⌫") {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardBackspace,
                                        contentDescription = "Backspace",
                                        tint = contentColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "DELETE",
                                        color = contentColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                Text(
                                    text = char,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = contentColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Source Currency Select dialog
    if (showSourceSelector) {
        CurrencySelectDialog(
            currencies = currencies,
            onDismiss = { showSourceSelector = false },
            onSelect = { viewModel.selectSourceCurrency(it) }
        )
    }

    // Target Currency Select dialog
    if (showTargetSelector) {
        CurrencySelectDialog(
            currencies = currencies,
            onDismiss = { showTargetSelector = false },
            onSelect = { viewModel.selectTargetCurrency(it) }
        )
    }
}

// ==========================================
// CURRENCY SELECTOR DIALOG
// ==========================================
@Composable
fun CurrencySelectDialog(
    currencies: List<Currency>,
    onDismiss: () -> Unit,
    onSelect: (Currency) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filtered = currencies.filter {
        it.code.contains(searchQuery, ignoreCase = true) ||
        it.name.contains(searchQuery, ignoreCase = true)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Country Currency",
                fontWeight = FontWeight.Bold,
                color = TextDark,
                fontSize = 18.sp
            )
        },
        containerColor = BrandBg,
        text = {
            Column(modifier = Modifier.height(350.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search by code or name...") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ThemePurple,
                        focusedLabelColor = ThemePurple,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                        focusedTextColor = TextDark,
                        unfocusedTextColor = TextDark
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filtered) { curr ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    onSelect(curr)
                                    onDismiss()
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(curr.emoji, fontSize = 28.sp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = curr.code,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = curr.name,
                                    color = TextMedium,
                                    fontSize = 11.sp
                                )
                            }
                            Text(
                                text = curr.symbol,
                                fontWeight = FontWeight.Black,
                                color = ThemePurple,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = TextMedium)
            }
        }
    )
}

// ==========================================
// INTERACTIVE BEZIER CHART
// ==========================================
@Composable
fun InteractiveBezierChart(
    rates: List<Double>,
    dates: List<String>,
    modifier: Modifier = Modifier
) {
    if (rates.isEmpty()) return

    val minRate = rates.minOrNull() ?: 0.0
    val maxRate = rates.maxOrNull() ?: 1.0
    val delta = (maxRate - minRate).let { if (it == 0.0) 1.0 else it }

    // Touch feedback scrubbing index state
    var scrubIndex by remember(rates) { mutableStateOf<Int?>(null) }

    Box(modifier = modifier) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val index = scrubIndex ?: (rates.size - 1)
                val activeRate = rates.getOrNull(index) ?: 0.0
                val activeDate = dates.getOrNull(index) ?: ""

                Column {
                    Text(
                        text = "Historical Rate Fluctuation",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMedium
                    )
                    Text(
                        text = activeDate,
                        fontSize = 11.sp,
                        color = ThemePurple,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = String.format(Locale.US, "%.4f", activeRate),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = ThemePurple
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .border(1.dp, ThemeContainerBorder.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .pointerInput(rates) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val xFraction = offset.x / size.width
                                val idx = (xFraction * (rates.size - 1)).toInt()
                                    .coerceIn(0, rates.size - 1)
                                scrubIndex = idx
                            },
                            onDrag = { change, _ ->
                                val xFraction = change.position.x / size.width
                                val idx = (xFraction * (rates.size - 1)).toInt()
                                    .coerceIn(0, rates.size - 1)
                                scrubIndex = idx
                            },
                            onDragEnd = { scrubIndex = null },
                            onDragCancel = { scrubIndex = null }
                        )
                    }
            ) {
                val width = size.width
                val height = size.height
                val padding = 20f

                // Draw thin background guideline segments
                val gridLines = 3
                for (i in 0..gridLines) {
                    val y = padding + (height - 2 * padding) * i / gridLines
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.25f),
                        start = androidx.compose.ui.geometry.Offset(padding, y),
                        end = androidx.compose.ui.geometry.Offset(width - padding, y),
                        strokeWidth = 1f
                    )
                }

                val points = rates.mapIndexed { idx, rateValue ->
                    val x = padding + (width - 2 * padding) * idx / (rates.size - 1)
                    val y = height - padding - ((rateValue - minRate) / delta * (height - 2 * padding)).toFloat()
                    androidx.compose.ui.geometry.Offset(x, y)
                }

                val path = Path()
                if (points.isNotEmpty()) {
                    path.moveTo(points[0].x, points[0].y)
                    for (i in 0 until points.size - 1) {
                        val p0 = points[i]
                        val p1 = points[i + 1]
                        val cp1 = androidx.compose.ui.geometry.Offset(p0.x + (p1.x - p0.x) / 2f, p0.y)
                        val cp2 = androidx.compose.ui.geometry.Offset(p0.x + (p1.x - p0.x) / 2f, p1.y)
                        path.cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, p1.x, p1.y)
                    }
                }

                // Draw fill under curve
                val fillPath = Path().apply {
                    addPath(path)
                    if (points.isNotEmpty()) {
                        lineTo(points.last().x, height - padding)
                        lineTo(points.first().x, height - padding)
                        close()
                    }
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(ThemeLightPurple.copy(alpha = 0.4f), Color.Transparent),
                        startY = points.map { it.y }.minOrNull() ?: 0f,
                        endY = height - padding
                    )
                )

                // Draw curve stroke line
                drawPath(
                    path = path,
                    color = ThemePurple,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Drag scrubbing indicator
                scrubIndex?.let { index ->
                    val pt = points[index]
                    drawLine(
                        color = ThemePurple.copy(alpha = 0.4f),
                        start = androidx.compose.ui.geometry.Offset(pt.x, padding),
                        end = androidx.compose.ui.geometry.Offset(pt.x, height - padding),
                        strokeWidth = 1.5.dp.toPx(),
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                    drawCircle(color = ThemePurple, radius = 6.dp.toPx(), center = pt)
                    drawCircle(color = Color.White, radius = 3.dp.toPx(), center = pt)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // X Axis date values
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                dates.forEachIndexed { index, dt ->
                    Text(
                        text = dt,
                        fontSize = 9.sp,
                        color = if (scrubIndex == index) ThemePurple else TextMedium.copy(alpha = 0.7f),
                        fontWeight = if (scrubIndex == index) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

// ==========================================
// RATES & HISTORY VIEW COMPONENT
// ==========================================
@Composable
fun RatesHistoryTabContent(
    viewModel: CalculatorViewModel,
    onEditRateClick: () -> Unit
) {
    val history by viewModel.history.collectAsState()
    val rate by viewModel.exchangeRate.collectAsState()
    val sourceCurrency by viewModel.sourceCurrency.collectAsState()
    val targetCurrency by viewModel.targetCurrency.collectAsState()

    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    val soundEnabled by viewModel.soundEnabled.collectAsState()
    val hapticProfile by viewModel.hapticProfile.collectAsState()

    val historicalRates by viewModel.historicalRates.collectAsState()
    val historicalDates by viewModel.historicalDates.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Exchange Rate custom config Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ThemeContainerBorder.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                    .clickable { onEditRateClick() },
                colors = CardDefaults.cardColors(containerColor = DigitBg),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Active Conversion Rate",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Rate",
                            tint = ThemePurple,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(ThemeLightPurple),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CurrencyExchange,
                                contentDescription = "Currency",
                                tint = ThemePurple,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "1 ${sourceCurrency.code} = ${String.format(Locale.US, "%.4f", rate)} ${targetCurrency.code}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF21005D)
                            )
                            Text(
                                text = "Click to set a custom conversion override",
                                fontSize = 11.sp,
                                color = TextMedium
                            )
                        }
                    }
                }
            }
        }

        // Live trend chart card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ThemeContainerBorder.copy(alpha = 0.4f), RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = DigitBg),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    InteractiveBezierChart(rates = historicalRates, dates = historicalDates)
                }
            }
        }

        // SOUND & HAPTIC tactile preferences
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ThemeContainerBorder.copy(alpha = 0.4f), RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = DigitBg),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Sound & Tactile Feedback",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Sound Toggle Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.toggleSound() }
                            .padding(vertical = 10.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = if (soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                                contentDescription = "Audio feedback",
                                tint = ThemePurple,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text("Keypad Clicks", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                                Text("Play click audio on button taps", fontSize = 10.sp, color = TextMedium)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (soundEnabled) ThemeLightPurple else KeypadBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Enabled",
                                tint = if (soundEnabled) ThemePurple else Color.Transparent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Vibration selection labels
                    Text(
                        text = "Vibration Profile Strength",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMedium,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Off", "Soft", "Crisp", "Heavy").forEach { profile ->
                            val isSelected = hapticProfile == profile
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) ThemePurple else KeypadBg)
                                    .clickable {
                                        viewModel.selectHapticProfile(profile)
                                        viewModel.triggerKeypressEffects(context)
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = profile,
                                    color = if (isSelected) Color.White else TextDark,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Calculation History items
        item {
            Text(
                text = "Recent Calculations",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }

        if (history.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(DigitBg)
                        .border(1.dp, ThemeContainerBorder.copy(alpha = 0.2f), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No calculation logs found", color = TextMedium, fontSize = 13.sp)
                }
            }
        } else {
            items(history) { logItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DigitBg)
                        .border(1.dp, ThemeContainerBorder.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                        .clickable {
                            viewModel.triggerKeypressEffects(context)
                            viewModel.onCalcKeyPress("C")
                            logItem.split(" = ").firstOrNull()?.forEach { char ->
                                viewModel.onCalcKeyPress(char.toString())
                            }
                        }
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = logItem,
                        color = TextDark,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Restore Calculation",
                        tint = ThemePurple,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
