package com.example

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardBackspace
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.widthIn
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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import coil.compose.AsyncImage
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Dashboard

import com.example.ui.theme.LocalAppThemeColors

// Dynamic Theme-Aware Palette
private val BrandBg: Color @Composable get() = LocalAppThemeColors.current.brandBg
private val TextDark: Color @Composable get() = LocalAppThemeColors.current.textDark
private val TextMedium: Color @Composable get() = LocalAppThemeColors.current.textMedium
private val ThemePurple: Color @Composable get() = LocalAppThemeColors.current.themePurple
private val ThemeLightPurple: Color @Composable get() = LocalAppThemeColors.current.themeLightPurple
private val ThemeContainerBorder: Color @Composable get() = LocalAppThemeColors.current.themeContainerBorder

private val KeypadBg: Color @Composable get() = LocalAppThemeColors.current.keypadBg
private val DigitBg: Color @Composable get() = LocalAppThemeColors.current.digitBg

enum class ActiveTab {
    CALCULATOR,
    EXCHANGE,
    VAULT
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

    val vaultUnlocked by viewModel.vaultUnlocked.collectAsState()

    // Switch to the private vault screen automatically when unlocked via passcode
    LaunchedEffect(vaultUnlocked) {
        if (vaultUnlocked) {
            activeTab = ActiveTab.VAULT
        } else {
            activeTab = ActiveTab.CALCULATOR
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = BrandBg
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Bar
            if (activeTab != ActiveTab.VAULT) {
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
                                imageVector = if (activeTab == ActiveTab.VAULT) Icons.Default.Lock else Icons.Default.Calculate,
                                contentDescription = "Calculator Icon",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (activeTab == ActiveTab.VAULT) viewModel.t("secure_vault") else viewModel.t("app_title"),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextDark,
                                letterSpacing = (-0.5).sp
                            )
                        }
                    }

                    // Lock button shown only when Vault is unlocked/active
                    if (activeTab == ActiveTab.VAULT) {
                        IconButton(
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                viewModel.lockVault()
                                activeTab = ActiveTab.CALCULATOR
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.Red.copy(alpha = 0.1f))
                                .testTag("header_lock_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock & Exit",
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Main View Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = if (activeTab == ActiveTab.VAULT) 0.dp else 24.dp)
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
                    ActiveTab.VAULT -> {
                        VaultTabContent(
                            viewModel = viewModel,
                            onLockExit = { activeTab = ActiveTab.CALCULATOR }
                        )
                    }
                }
            }

            // Professional Bottom Navigation Bar - only shown if not in vault
            if (activeTab != ActiveTab.VAULT) {
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
                        label = viewModel.t("calculator"),
                        icon = Icons.Default.Calculate,
                        isSelected = activeTab == ActiveTab.CALCULATOR,
                        onClick = {
                            viewModel.triggerKeypressEffects(context)
                            activeTab = ActiveTab.CALCULATOR
                        },
                        modifier = Modifier.testTag("nav_calculator")
                    )

                    BottomNavItem(
                        label = viewModel.t("exchange"),
                        icon = Icons.Default.CurrencyExchange,
                        isSelected = activeTab == ActiveTab.EXCHANGE,
                        onClick = {
                            viewModel.triggerKeypressEffects(context)
                            activeTab = ActiveTab.EXCHANGE
                        },
                        modifier = Modifier.testTag("nav_exchange")
                    )
                }
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

            // Conversion container
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
    val apiStatus by viewModel.apiStatus.collectAsState()
    val lastUpdated by viewModel.lastUpdated.collectAsState()

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
            // Real-Time Sync Status Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, ThemeContainerBorder.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val statusText = when (apiStatus) {
                        ApiStatus.IDLE -> "Auto rates synced"
                        ApiStatus.LOADING -> "Syncing live rates..."
                        ApiStatus.SUCCESS -> "Live rates synced"
                        ApiStatus.ERROR -> "Offline (using cached rates)"
                    }
                    val statusColor = when (apiStatus) {
                        ApiStatus.LOADING -> ThemePurple
                        ApiStatus.SUCCESS -> Color(0xFF2E7D32) // Green
                        ApiStatus.ERROR -> Color(0xFFC62828) // Red
                        else -> TextMedium
                    }
                    
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Column {
                        Text(
                            text = statusText,
                            color = TextDark,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Last updated: $lastUpdated",
                            color = TextMedium.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                
                IconButton(
                    onClick = {
                        viewModel.triggerKeypressEffects(context)
                        viewModel.fetchLatestRates()
                    },
                    modifier = Modifier.size(36.dp),
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

            val drawPurple = ThemePurple
            val drawLightPurple = ThemeLightPurple

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
                        colors = listOf(drawLightPurple.copy(alpha = 0.4f), Color.Transparent),
                        startY = points.map { it.y }.minOrNull() ?: 0f,
                        endY = height - padding
                    )
                )

                // Draw curve stroke line
                drawPath(
                    path = path,
                    color = drawPurple,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Drag scrubbing indicator
                scrubIndex?.let { index ->
                    val pt = points[index]
                    drawLine(
                        color = drawPurple.copy(alpha = 0.4f),
                        start = androidx.compose.ui.geometry.Offset(pt.x, padding),
                        end = androidx.compose.ui.geometry.Offset(pt.x, height - padding),
                        strokeWidth = 1.5.dp.toPx(),
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                    drawCircle(color = drawPurple, radius = 6.dp.toPx(), center = pt)
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
    val apiStatus by viewModel.apiStatus.collectAsState()
    val lastUpdated by viewModel.lastUpdated.collectAsState()

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
                                text = "Last auto-synced: $lastUpdated\nClick to set a custom override",
                                fontSize = 11.sp,
                                color = TextMedium,
                                lineHeight = 14.sp
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

// ==========================================
// OPTION 4: SECRET VAULT VIEW
// ==========================================
@Composable
fun FolderCard(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                icon()
                Column {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = Color(0xFF8B92A5)
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = Color(0xFF8B92A5).copy(alpha = 0.7f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ExploreGridCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bgColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(bgColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = bgColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 9.sp,
                    color = Color(0xFF8B92A5)
                )
            }
        }
    }
}

@Composable
fun SettingsGroup(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8B92A5),
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SettingsActionRow(
    title: String,
    subtitle: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color = Color(0xFF635BFF),
    showWarningBadge: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconTint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 10.sp,
                        color = Color(0xFF8B92A5),
                        lineHeight = 13.sp
                    )
                }
            }
        }
        if (showWarningBadge) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = Color(0xFFFFB300),
                modifier = Modifier.size(18.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Go",
                tint = Color(0xFF8B92A5).copy(alpha = 0.5f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun SettingsSwitchRow(
    title: String,
    subtitle: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color = Color(0xFF635BFF),
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconTint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 10.sp,
                        color = Color(0xFF8B92A5),
                        lineHeight = 13.sp
                    )
                }
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF635BFF),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFF383F56)
            )
        )
    }
}

@Composable
fun VaultTabContent(
    viewModel: CalculatorViewModel,
    onLockExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vaultUnlocked by viewModel.vaultUnlocked.collectAsState()
    val vaultNotes by viewModel.vaultNotes.collectAsState()
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    var showAddNoteDialog by remember { mutableStateOf(false) }
    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }

    var showChangePasscodeDialog by remember { mutableStateOf(false) }
    var realPasscodeInput by remember { mutableStateOf("") }
    var decoyPasscodeInput by remember { mutableStateOf("") }

    val context = LocalContext.current

    val biometricEnabled by viewModel.biometricEnabled.collectAsState()
    val panicEnabled by viewModel.panicEnabled.collectAsState()
    val panicAction by viewModel.panicAction.collectAsState()
    val screenDownLock by viewModel.screenDownLock.collectAsState()
    val activity = context as? androidx.fragment.app.FragmentActivity

    fun triggerBiometric() {
        if (activity != null && biometricEnabled) {
            val executor = androidx.core.content.ContextCompat.getMainExecutor(activity)
            val biometricPrompt = androidx.biometric.BiometricPrompt(
                activity,
                executor,
                object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                    }

                    override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        viewModel.unlockVault(isDecoy = false)
                        Toast.makeText(context, "Vault Unlocked via Biometrics!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                    }
                }
            )

            val promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("Unlock Vault")
                .setSubtitle("Authenticate using fingerprint or face recognition")
                .setNegativeButtonText("Use PIN Pad")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }

    if (!vaultUnlocked && biometricEnabled) {
        LaunchedEffect(Unit) {
            triggerBiometric()
        }
    }

    // Unified sensor detector for Panic Gesture (Shake and Face Down)
    if ((panicEnabled || screenDownLock) && vaultUnlocked) {
        val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
        val accelerometer = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

        DisposableEffect(sensorManager, accelerometer) {
            var lastUpdate = 0L
            var lastX = 0f
            var lastY = 0f
            var lastZ = 0f

            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    val curTime = System.currentTimeMillis()
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    // 1. Screen Down Lock detection (Z-axis negative gravity)
                    if (screenDownLock && z < -8.5f) {
                        viewModel.triggerKeypressEffects(context)
                        viewModel.lockVault()
                        Toast.makeText(context, "Vault locked: Face down detected!", Toast.LENGTH_SHORT).show()
                        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                            addCategory(Intent.CATEGORY_HOME)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(homeIntent)
                        return
                    }

                    // 2. Shake detection
                    if (panicEnabled && (curTime - lastUpdate) > 150) {
                        val diffTime = (curTime - lastUpdate)
                        lastUpdate = curTime

                        val speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000

                        if (speed > 1000) { // Shake detected
                            viewModel.triggerKeypressEffects(context)
                            viewModel.lockVault()
                            if (panicAction == "lock") {
                                Toast.makeText(context, "Vault locked via shake gesture!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Emergency lock initiated!", Toast.LENGTH_SHORT).show()
                                val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                                    addCategory(Intent.CATEGORY_HOME)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                context.startActivity(homeIntent)
                            }
                        }

                        lastX = x
                        lastY = y
                        lastZ = z
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
            }

            if (accelerometer != null) {
                sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            }
            onDispose {
                sensorManager.unregisterListener(listener)
            }
        }
    }

    if (!vaultUnlocked) {
        // Vault Lock Screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0B0F19)) // Immersive premium high-security dark canvas
        ) {
            // Back Button to exit to Calculator
            IconButton(
                onClick = {
                    viewModel.triggerKeypressEffects(context)
                    onLockExit()
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1B2031))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Calculator",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF635BFF).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Secret Vault Lock",
                        tint = Color(0xFF635BFF),
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Secret Calculator Vault",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Enter the secret passcode to unlock your private space.",
                    fontSize = 12.sp,
                    color = Color(0xFF8B92A5),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Custom secure PIN Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until 4) {
                        val isFilled = pinInput.length > i
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(if (isFilled) Color(0xFF635BFF) else Color(0xFF1B2031))
                                .border(width = 1.dp, color = Color(0xFF635BFF), shape = CircleShape)
                        )
                    }
                }

                if (pinError) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Incorrect Passcode! Hint: 7777",
                        color = Color.Red,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Grid PIN Pad
                val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "Clear", "0", "Unlock")
                Column(
                    modifier = Modifier.width(280.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    keys.chunked(3).forEach { rowKeys ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowKeys.forEach { key ->
                                val isSpecial = key == "Clear" || key == "Unlock"
                                val buttonColor = if (isSpecial) Color(0xFF635BFF).copy(alpha = 0.2f) else Color(0xFF1B2031)
                                val contentColor = if (isSpecial) Color(0xFF8C84FF) else Color.White

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(buttonColor)
                                        .clickable {
                                            viewModel.triggerKeypressEffects(context)
                                            pinError = false
                                            when (key) {
                                                "Clear" -> {
                                                    if (pinInput.isNotEmpty()) pinInput = pinInput.dropLast(1)
                                                }
                                                "Unlock" -> {
                                                    if (viewModel.tryUnlockVault(pinInput)) {
                                                        pinInput = ""
                                                    } else {
                                                        pinError = true
                                                        pinInput = ""
                                                    }
                                                }
                                                else -> {
                                                    if (pinInput.length < 4) {
                                                        pinInput += key
                                                        if (pinInput.length == 4) {
                                                            // Auto submit when 4 digits are typed
                                                            if (viewModel.tryUnlockVault(pinInput)) {
                                                                pinInput = ""
                                                            } else {
                                                                pinError = true
                                                                pinInput = ""
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = key,
                                        fontSize = if (isSpecial) 12.sp else 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = contentColor
                                    )
                                }
                            }
                        }
                    }
                }

                if (biometricEnabled && activity != null) {
                    Spacer(modifier = Modifier.height(20.dp))
                    IconButton(
                        onClick = {
                            viewModel.triggerKeypressEffects(context)
                            triggerBiometric()
                        },
                        modifier = Modifier
                            .size(54.dp)
                            .background(Color(0xFF635BFF).copy(alpha = 0.15f), CircleShape)
                            .border(1.dp, Color(0xFF635BFF).copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "Unlock with Biometrics",
                            tint = Color(0xFF635BFF),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    } else {
        // Vault Unlocked Content: Advanced Private Media Hub
        val vaultFiles by viewModel.vaultFiles.collectAsState()
        var activeSection by remember { mutableStateOf("Home") } // "Home", "Notes", "Photos & Videos", "Documents", "Explore", "Settings"
        var selectedFileForDetails by remember { mutableStateOf<String?>(null) }
        var textFileContentToRead by remember { mutableStateOf<Pair<String, String>?>(null) } // Pair(Name, Content)

        // Photo/Video Picker launcher
        val photoPickerLauncher = rememberLauncherForActivityResult(
            contract = androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult(),
            onResult = { result ->
                if (result.resultCode == android.app.Activity.RESULT_OK) {
                    val uri = result.data?.data
                    if (uri != null) {
                        val success = viewModel.addVaultFile(context, uri)
                        if (success) {
                            android.widget.Toast.makeText(context, "Photo/Video secured in Vault!", android.widget.Toast.LENGTH_SHORT).show()
                        } else {
                            android.widget.Toast.makeText(context, "Failed to secure photo/video", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        )

        // Document/General File Picker launcher
        val documentPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                if (uri != null) {
                    val success = viewModel.addVaultFile(context, uri)
                    if (success) {
                        android.widget.Toast.makeText(context, "Document secured in Vault!", android.widget.Toast.LENGTH_SHORT).show()
                    } else {
                        android.widget.Toast.makeText(context, "Failed to secure document", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        // Secure Original File Gallery Deletion confirmation flow
        val pendingDeleteSender by viewModel.pendingDeleteSender.collectAsState()
        val deleteSenderLauncher = rememberLauncherForActivityResult(
            contract = androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                android.util.Log.d("Vault", "User accepted/cancelled: User accepted")
                android.util.Log.d("Vault", "Delete success/failure: Delete success")
                android.widget.Toast.makeText(context, "Original photo hidden successfully!", android.widget.Toast.LENGTH_SHORT).show()
                viewModel.onOriginalFileDeleted(context)
            } else {
                android.util.Log.d("Vault", "User accepted/cancelled: User cancelled")
                android.util.Log.d("Vault", "Delete success/failure: Delete failure")
            }
            viewModel.clearPendingDelete()
        }

        LaunchedEffect(pendingDeleteSender) {
            pendingDeleteSender?.let { sender ->
                try {
                    android.util.Log.d("Vault", "IntentSender launched")
                    deleteSenderLauncher.launch(
                        androidx.activity.result.IntentSenderRequest.Builder(sender).build()
                    )
                } catch (e: Exception) {
                    android.util.Log.e("Vault", "Any exception with full stack trace", e)
                    viewModel.clearPendingDelete()
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0B0F19)) // Premium high-security slate dark background
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Clean and spacious Unlocked Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                if (activeSection == "Home") {
                                    viewModel.lockVault()
                                    onLockExit()
                                } else {
                                    activeSection = "Home"
                                }
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1B2031))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = if (activeSection == "Home") viewModel.t("secure_vault") else viewModel.t(
                                    when(activeSection) {
                                        "Notes" -> "notes"
                                        "Photos & Videos" -> "photos_videos"
                                        "Documents" -> "documents"
                                        "Private Browser" -> "private_browser"
                                        "Explore" -> "explore"
                                        "Settings" -> "settings"
                                        else -> "secure_vault"
                                    }
                                ),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                            Text(
                                text = "AES Passcode Secured",
                                fontSize = 10.sp,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                activeSection = "Settings"
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1B2031))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Change Passcode",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                viewModel.lockVault()
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1B2031))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock Vault",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

            // Material 3 Filter Chips for Sections
            val decoyActive by viewModel.decoyActive.collectAsState()
            val sections = if (decoyActive) {
                listOf("Home", "Notes", "Photos & Videos", "Documents", "Private Browser", "Settings")
            } else {
                listOf("Home", "Notes", "Photos & Videos", "Documents", "Private Browser", "Explore", "Settings")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                sections.forEach { label ->
                    val isSelected = activeSection == label
                    val chipBg = if (isSelected) ThemePurple else Color(0xFF1B2031)
                    val chipText = if (isSelected) Color.White else Color(0xFF8B92A5)
                    val chipBorder = if (isSelected) Color.Transparent else Color(0xFF383F56)

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(chipBg)
                            .border(1.dp, chipBorder, RoundedCornerShape(12.dp))
                            .clickable {
                                viewModel.triggerKeypressEffects(context)
                                activeSection = label
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when(label) {
                                "Home" -> viewModel.t("vault")
                                "Notes" -> viewModel.t("notes")
                                "Photos & Videos" -> viewModel.t("photos_videos")
                                "Documents" -> viewModel.t("documents")
                                "Private Browser" -> viewModel.t("private_browser")
                                "Explore" -> viewModel.t("explore")
                                "Settings" -> viewModel.t("settings")
                                else -> label
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = chipText,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Section Contents
            when (activeSection) {
                "Home" -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Header Cards like App Lock and Explore
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Card 1: App Lock
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(100.dp)
                                    .clickable {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "App Lock"
                                    },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF635BFF).copy(alpha = 0.15f))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFF635BFF)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Shield,
                                            contentDescription = "App Lock",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "App Lock",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Protect apps",
                                            fontSize = 10.sp,
                                            color = Color(0xFF8B92A5)
                                        )
                                    }
                                }
                            }

                            // Card 2: Explore Toolbox
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(100.dp)
                                    .clickable {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Explore"
                                    },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF00C853).copy(alpha = 0.15f))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFF00C853)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.TrendingUp,
                                            contentDescription = "Explore",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Explore",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Secure features",
                                            fontSize = 10.sp,
                                            color = Color(0xFF8B92A5)
                                        )
                                    }
                                }
                            }
                        }

                        // Folder items header
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "All Folders",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8B92A5)
                            )
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = "Sort",
                                tint = Color(0xFF8B92A5),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Folders list like in Screenshot 1
                        FolderCard(
                            title = "Recent Files",
                            subtitle = "${vaultFiles.size + vaultNotes.size} locked items",
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFFF9100).copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.History, "Recent", tint = Color(0xFFFF9100), modifier = Modifier.size(20.dp))
                                }
                            },
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                activeSection = "Photos & Videos"
                            }
                        )

                        FolderCard(
                            title = "Photos & Videos",
                            subtitle = "${vaultFiles.filter { it.contains("image/") || it.contains("video/") }.size} media objects",
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF2979FF).copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Image, "Photos", tint = Color(0xFF2979FF), modifier = Modifier.size(20.dp))
                                }
                            },
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                activeSection = "Photos & Videos"
                            }
                        )

                        FolderCard(
                            title = "Private Notes",
                            subtitle = "${vaultNotes.size} secret logs",
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFFFD600).copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Article, "Notes", tint = Color(0xFFFFD600), modifier = Modifier.size(20.dp))
                                }
                            },
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                activeSection = "Notes"
                            }
                        )

                        FolderCard(
                            title = "Private Documents",
                            subtitle = "${vaultFiles.filter { !it.contains("image/") && !it.contains("video/") }.size} locked assets",
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF00E676).copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Description, "Documents", tint = Color(0xFF00E676), modifier = Modifier.size(20.dp))
                                }
                            },
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                activeSection = "Documents"
                            }
                        )

                        FolderCard(
                            title = "Private Web Browser",
                            subtitle = "Incognito browsing workspace",
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF00B0FF).copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Language, "Browser", tint = Color(0xFF00B0FF), modifier = Modifier.size(20.dp))
                                }
                            },
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                activeSection = "Private Browser"
                            }
                        )
                    }
                }
                "Notes" -> {
                    if (vaultNotes.isEmpty()) {
                        EmptyVaultSectionState(
                            title = "No Secure Notes",
                            description = "Tap 'Add Note' to save password credentials or personal journals safely."
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(vaultNotes) { noteStr ->
                                val parts = noteStr.split("|||")
                                if (parts.size == 3) {
                                    val timestamp = parts[0]
                                    val title = parts[1]
                                    val body = parts[2]

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(
                                                width = 1.dp,
                                                color = ThemeContainerBorder.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(12.dp)
                                            ),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031)),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.Top
                                            ) {
                                                Text(
                                                    text = title,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp,
                                                    color = Color.White,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                IconButton(
                                                    onClick = {
                                                        viewModel.triggerKeypressEffects(context)
                                                        viewModel.deleteVaultNote(noteStr)
                                                    },
                                                    modifier = Modifier.size(24.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Delete Note",
                                                        tint = Color.Red.copy(alpha = 0.7f),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = body,
                                                fontSize = 12.sp,
                                                color = Color(0xFF8B92A5),
                                                lineHeight = 16.sp
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = timestamp,
                                                fontSize = 9.sp,
                                                color = TextMedium.copy(alpha = 0.6f),
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "Photos & Videos" -> {
                    val mediaFiles = vaultFiles.filter {
                        val parts = it.split("|||")
                        parts.size >= 4 && (parts[3].startsWith("image/") || parts[3].startsWith("video/"))
                    }

                    if (mediaFiles.isEmpty()) {
                        EmptyVaultSectionState(
                            title = "No Secure Photos or Videos",
                            description = "Tap 'Import Photo' to transfer visual media into this secure sandboxed directory."
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(mediaFiles) { fileStr ->
                                val parts = fileStr.split("|||")
                                if (parts.size >= 6) {
                                    val originalName = parts[2]
                                    val mimeType = parts[3]
                                    val path = parts[4]

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(1f)
                                            .clickable {
                                                viewModel.triggerKeypressEffects(context)
                                                selectedFileForDetails = fileStr
                                            },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = KeypadBg)
                                    ) {
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            if (mimeType.startsWith("image/")) {
                                                AsyncImage(
                                                    model = java.io.File(path),
                                                    contentDescription = originalName,
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                                )
                                            } else {
                                                // Video Placeholder thumbnail
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .background(Color.Black.copy(alpha = 0.7f)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Visibility,
                                                        contentDescription = "Video",
                                                        tint = Color.White.copy(alpha = 0.8f),
                                                        modifier = Modifier.size(28.dp)
                                                    )
                                                }
                                            }

                                            // Tiny tag or details
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(Color.Black.copy(alpha = 0.4f))
                                                    .padding(4.dp)
                                                    .align(Alignment.BottomStart)
                                            ) {
                                                Text(
                                                    text = if (mimeType.startsWith("video/")) "VIDEO" else "IMAGE",
                                                    color = Color.White,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.align(Alignment.Center)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "Documents" -> {
                    val docFiles = vaultFiles.filter {
                        val parts = it.split("|||")
                        parts.size >= 4 && !parts[3].startsWith("image/") && !parts[3].startsWith("video/")
                    }

                    if (docFiles.isEmpty()) {
                        EmptyVaultSectionState(
                            title = "No Private Documents",
                            description = "Tap 'Import File' to secure any PDF, TXT, or binary document from local storage."
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(docFiles) { fileStr ->
                                val parts = fileStr.split("|||")
                                if (parts.size >= 6) {
                                    val timestamp = parts[1]
                                    val originalName = parts[2]
                                    val mimeType = parts[3]
                                    val path = parts[4]
                                    val sizeStr = parts[5]

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(
                                                width = 1.dp,
                                                color = ThemeContainerBorder.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(12.dp)
                                            ),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031)),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(ThemeLightPurple),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Description,
                                                    contentDescription = "Document",
                                                    tint = ThemePurple,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }

                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = originalName,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = "$sizeStr • $timestamp",
                                                    fontSize = 10.sp,
                                                    color = Color(0xFF8B92A5)
                                                )
                                            }

                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                if (mimeType.startsWith("text/plain")) {
                                                    IconButton(
                                                        onClick = {
                                                            viewModel.triggerKeypressEffects(context)
                                                            try {
                                                                val txt = java.io.File(path).readText()
                                                                textFileContentToRead = Pair(originalName, txt)
                                                            } catch (e: Exception) {
                                                                android.widget.Toast.makeText(context, "Cannot read text file", android.widget.Toast.LENGTH_SHORT).show()
                                                            }
                                                        },
                                                        modifier = Modifier.size(32.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Visibility,
                                                            contentDescription = "Read File",
                                                            tint = ThemePurple,
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                    }
                                                }

                                                IconButton(
                                                    onClick = {
                                                        viewModel.triggerKeypressEffects(context)
                                                        viewModel.exportVaultFile(
                                                            context = context,
                                                            fileSerialized = fileStr,
                                                            onSuccess = { msg -> android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_LONG).show() },
                                                            onFailure = { err -> android.widget.Toast.makeText(context, err, android.widget.Toast.LENGTH_LONG).show() }
                                                        )
                                                    },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.FileDownload,
                                                        contentDescription = "Export File",
                                                        tint = Color(0xFF4CAF50),
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }

                                                IconButton(
                                                    onClick = {
                                                        viewModel.triggerKeypressEffects(context)
                                                        viewModel.deleteVaultFile(fileStr)
                                                        android.widget.Toast.makeText(context, "File deleted", android.widget.Toast.LENGTH_SHORT).show()
                                                    },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Delete File",
                                                        tint = Color.Red.copy(alpha = 0.7f),
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                "Private Browser" -> {
                    PrivateBrowserSection(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }
                "App Lock" -> {
                    Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        AppLockSection(viewModel = viewModel)
                    }
                }
                "Intruder Alerts" -> {
                    val intruderAttempts by viewModel.intruderAttempts.collectAsState()
                    if (intruderAttempts.isEmpty()) {
                        EmptyVaultSectionState(
                            title = "No Break-In Attempts",
                            description = "Your vault is completely safe. Any failed unlock attempts (using incorrect PINs) will be logged here with timestamps and entered keys."
                        )
                    } else {
                        Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${intruderAttempts.size} Attempted Break-ins",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red
                                )
                                TextButton(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        viewModel.clearIntruderAttempts()
                                        android.widget.Toast.makeText(context, "Logs cleared!", android.widget.Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Text("Clear All", color = ThemePurple, fontSize = 12.sp)
                                }
                            }
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(intruderAttempts) { attemptStr ->
                                    val parts = attemptStr.split("|||")
                                    if (parts.size >= 2) {
                                        val timestamp = parts[0]
                                        val enteredPin = parts[1]
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .border(
                                                    width = 1.dp,
                                                    color = Color.Red.copy(alpha = 0.2f),
                                                    shape = RoundedCornerShape(12.dp)
                                                ),
                                            colors = CardDefaults.cardColors(containerColor = Color.White),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(14.dp).fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column {
                                                    Text(
                                                        text = "Suspicious Access Attempt",
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 13.sp,
                                                        color = Color.Red
                                                    )
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = "Date/Time: $timestamp",
                                                        fontSize = 11.sp,
                                                        color = TextMedium
                                                    )
                                                }
                                                Column(horizontalAlignment = Alignment.End) {
                                                    Text(
                                                        text = "Entered Code",
                                                        fontSize = 9.sp,
                                                        color = TextMedium.copy(alpha = 0.7f),
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        text = enteredPin,
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = TextDark
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                "Explore" -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Security Toolbox",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8B92A5)
                        )

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .height(380.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            userScrollEnabled = false
                        ) {
                            item {
                                ExploreGridCard(
                                    title = "Intruder Selfie",
                                    subtitle = "Logs break-ins",
                                    icon = Icons.Default.Warning,
                                    bgColor = Color(0xFFFF9100),
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Intruder Alerts"
                                    }
                                )
                            }
                            item {
                                ExploreGridCard(
                                    title = "App Lock",
                                    subtitle = "Lock apps",
                                    icon = Icons.Default.Shield,
                                    bgColor = Color(0xFF7C4DFF),
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "App Lock"
                                    }
                                )
                            }
                            item {
                                ExploreGridCard(
                                    title = "Private Browser",
                                    subtitle = "Incognito search",
                                    icon = Icons.Default.Language,
                                    bgColor = Color(0xFF2979FF),
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Private Browser"
                                    }
                                )
                            }
                            item {
                                ExploreGridCard(
                                    title = "Private Notes",
                                    subtitle = "Encrypted logs",
                                    icon = Icons.Default.Article,
                                    bgColor = Color(0xFFFFD600),
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Notes"
                                    }
                                )
                            }
                            item {
                                ExploreGridCard(
                                    title = "Prevent Screenshot",
                                    subtitle = "Blocks recordings",
                                    icon = Icons.Default.Visibility,
                                    bgColor = Color(0xFF00E676),
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Settings"
                                    }
                                )
                            }
                            item {
                                ExploreGridCard(
                                    title = "Decoy Vault PIN",
                                    subtitle = "Fake password space",
                                    icon = Icons.Default.Lock,
                                    bgColor = Color(0xFFEC407A),
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Settings"
                                    }
                                )
                            }
                        }
                    }
                }
                "Settings" -> {
                    // Initialize inputs on Settings entry
                    LaunchedEffect(Unit) {
                        realPasscodeInput = viewModel.getVaultPin()
                        decoyPasscodeInput = viewModel.getDecoyPin()
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Card 1: SECURITY PASSCODES
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = "Passcode security", tint = Color(0xFF2979FF), modifier = Modifier.size(22.dp))
                                    Text(
                                        text = "PASSCODE MANAGEMENT",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2979FF)
                                    )
                                }
                                
                                Text(
                                    text = "Configure your secret numerical passcodes. Entering your real passcode unlocks your private vault. Entering your decoy passcode opens a completely empty guest vault.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF8B92A5),
                                    lineHeight = 15.sp
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "Real Vault Passcode",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    OutlinedTextField(
                                        value = realPasscodeInput,
                                        onValueChange = { input ->
                                            if (input.all { it.isDigit() } && input.length <= 8) {
                                                realPasscodeInput = input
                                            }
                                        },
                                        placeholder = { Text("e.g. 7777", fontSize = 12.sp, color = Color(0xFF8B92A5)) },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White,
                                            focusedBorderColor = ThemePurple,
                                            unfocusedBorderColor = Color(0xFF383F56)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "Decoy / Guest Passcode (Plausible Deniability)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFE57373)
                                    )
                                    OutlinedTextField(
                                        value = decoyPasscodeInput,
                                        onValueChange = { input ->
                                            if (input.all { it.isDigit() } && input.length <= 8) {
                                                decoyPasscodeInput = input
                                            }
                                        },
                                        placeholder = { Text("e.g. 1111", fontSize = 12.sp, color = Color(0xFF8B92A5)) },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White,
                                            focusedBorderColor = Color(0xFFE57373),
                                            unfocusedBorderColor = Color(0xFF383F56)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                Button(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        if (realPasscodeInput.isBlank() || decoyPasscodeInput.isBlank()) {
                                            android.widget.Toast.makeText(context, "Passcodes cannot be empty!", android.widget.Toast.LENGTH_SHORT).show()
                                        } else if (realPasscodeInput == decoyPasscodeInput) {
                                            android.widget.Toast.makeText(context, "Real and decoy passcodes must be different!", android.widget.Toast.LENGTH_SHORT).show()
                                        } else {
                                            viewModel.setVaultPin(realPasscodeInput)
                                            viewModel.setDecoyPin(decoyPasscodeInput)
                                            android.widget.Toast.makeText(context, "Passcodes updated successfully!", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Save Passcodes", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }

                        // Card 2: STEALTH & PANIC MODES
                        SettingsGroup(title = "STEALTH & SECURITY OPTIONS") {
                            val preventScreenshots by viewModel.preventScreenshots.collectAsState()
                            val screenDownLock by viewModel.screenDownLock.collectAsState()
                            val panicEnabled by viewModel.panicEnabled.collectAsState()
                            val biometricEnabled by viewModel.biometricEnabled.collectAsState()

                            SettingsSwitchRow(
                                title = "Biometric Unlock",
                                subtitle = "Unlock using fingerprint or face scanning",
                                icon = Icons.Default.Fingerprint,
                                iconTint = Color(0xFF2979FF),
                                checked = biometricEnabled,
                                onCheckedChange = { viewModel.setBiometricEnabled(it) }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsSwitchRow(
                                title = "Prevent screenshots",
                                subtitle = "Blocks Android screen capture & recorders",
                                icon = Icons.Default.Visibility,
                                iconTint = Color(0xFF00E676),
                                checked = preventScreenshots,
                                onCheckedChange = { viewModel.setPreventScreenshots(it) }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsSwitchRow(
                                title = "Screen down lock",
                                subtitle = "Instantly closes and locks vault face-down",
                                icon = Icons.Default.ScreenRotation,
                                iconTint = Color(0xFFFFD600),
                                checked = screenDownLock,
                                onCheckedChange = { viewModel.setScreenDownLock(it) }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsSwitchRow(
                                title = "Shake panic gesture",
                                subtitle = "Shake phone hard to quickly lock vault",
                                icon = Icons.Default.Refresh,
                                iconTint = Color(0xFFEC407A),
                                checked = panicEnabled,
                                onCheckedChange = { viewModel.setPanicEnabled(it) }
                            )
                        }

                        // Panic actions directly visible if panic is enabled
                        val panicEnabled by viewModel.panicEnabled.collectAsState()
                        if (panicEnabled) {
                            val panicAction by viewModel.panicAction.collectAsState()
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031))
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = "PANIC ACTION CONFIGURATION",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFEC407A)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        listOf("lock" to "Lock Vault Only", "home" to "Lock & Go Home").forEach { (actionKey, labelText) ->
                                            val isSelected = panicAction == actionKey
                                            Card(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clickable {
                                                        viewModel.triggerKeypressEffects(context)
                                                        viewModel.setPanicAction(actionKey)
                                                    },
                                                shape = RoundedCornerShape(10.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = if (isSelected) ThemePurple.copy(alpha = 0.2f) else Color(0xFF161C2C)
                                                ),
                                                border = BorderStroke(
                                                    1.dp,
                                                    if (isSelected) ThemePurple else Color(0xFF383F56)
                                                )
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(12.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = labelText,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isSelected) ThemePurple else Color(0xFF8B92A5)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    
                                    Button(
                                        onClick = {
                                            viewModel.triggerKeypressEffects(context)
                                            viewModel.lockVault()
                                            if (panicAction == "lock") {
                                                android.widget.Toast.makeText(context, "Virtual Panic: Vault locked successfully!", android.widget.Toast.LENGTH_LONG).show()
                                            } else {
                                                android.widget.Toast.makeText(context, "Virtual Panic: Vault locked & returned Home!", android.widget.Toast.LENGTH_LONG).show()
                                                val homeIntent = android.content.Intent(android.content.Intent.ACTION_MAIN).apply {
                                                    addCategory(android.content.Intent.CATEGORY_HOME)
                                                    flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                                                }
                                                context.startActivity(homeIntent)
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Test Panic Trigger (Simulator)", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // Card 4: GENERAL PREFERENCES
                        SettingsGroup(title = "GENERAL PREFERENCES") {
                            val currentLangCode by viewModel.selectedLanguage.collectAsState()
                            val currentLang = TranslationProvider.languages.find { it.code == currentLangCode }
                            val currentLangDisplay = currentLang?.let { "${it.name} ${it.flag}" } ?: "English 🇺🇸"
                            SettingsActionRow(
                                title = "App Language",
                                subtitle = currentLangDisplay,
                                icon = Icons.Default.Language,
                                iconTint = Color(0xFF00B0FF),
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    showLanguageDialog = true
                                }
                            )

                            val currentTheme by viewModel.selectedTheme.collectAsState()
                            val themeDisplay = "${currentTheme.displayName} ${currentTheme.flag}"
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsActionRow(
                                title = viewModel.t("app_theme"),
                                subtitle = themeDisplay,
                                icon = Icons.Default.Palette,
                                iconTint = Color(0xFFFF4081),
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    showThemeDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

        // Secure File Detail Modal Dialog (Photos/Videos)
        if (selectedFileForDetails != null) {
            val fileStr = selectedFileForDetails!!
            val parts = fileStr.split("|||")
            if (parts.size >= 6) {
                val timestamp = parts[1]
                val originalName = parts[2]
                val mimeType = parts[3]
                val path = parts[4]
                val sizeStr = parts[5]

                AlertDialog(
                    onDismissRequest = { selectedFileForDetails = null },
                    containerColor = BrandBg,
                    title = {
                        Text(
                            text = "Secure File Preview",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextDark
                        )
                    },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (mimeType.startsWith("image/")) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(240.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    AsyncImage(
                                        model = java.io.File(path),
                                        contentDescription = originalName,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = androidx.compose.ui.layout.ContentScale.Fit
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(ThemeLightPurple),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Default.Visibility, contentDescription = "Video file", tint = ThemePurple, modifier = Modifier.size(48.dp))
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Secure Video Content", color = TextDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(text = "Name: $originalName", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextDark, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(text = "Type: $mimeType", fontSize = 11.sp, color = TextMedium)
                                Text(text = "Size: $sizeStr", fontSize = 11.sp, color = TextMedium)
                                Text(text = "Secured on: $timestamp", fontSize = 11.sp, color = TextMedium)
                            }
                        }
                    },
                    confirmButton = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    viewModel.exportVaultFile(
                                        context = context,
                                        fileSerialized = fileStr,
                                        onSuccess = { msg ->
                                            android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_LONG).show()
                                            selectedFileForDetails = null
                                        },
                                        onFailure = { err ->
                                            android.widget.Toast.makeText(context, err, android.widget.Toast.LENGTH_LONG).show()
                                        }
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.FileDownload, contentDescription = "Export", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Decrypt Export", fontSize = 11.sp)
                            }

                            Button(
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    viewModel.deleteVaultFile(fileStr)
                                    android.widget.Toast.makeText(context, "Permanently Deleted!", android.widget.Toast.LENGTH_SHORT).show()
                                    selectedFileForDetails = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Delete", fontSize = 11.sp)
                            }
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            viewModel.triggerKeypressEffects(context)
                            selectedFileForDetails = null
                        }) {
                            Text("Cancel", color = ThemePurple)
                        }
                    }
                )
            }
        }

        // Floating Action Button for active media/notes/documents sections!
        if (activeSection == "Notes" || activeSection == "Photos & Videos" || activeSection == "Documents") {
            Box(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                FloatingActionButton(
                    onClick = {
                        viewModel.triggerKeypressEffects(context)
                        when (activeSection) {
                            "Notes" -> {
                                showAddNoteDialog = true
                            }
                            "Photos & Videos" -> {
                                val intent = android.content.Intent(
                                    android.content.Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                ).apply {
                                    type = "image/* video/*"
                                    putExtra(android.content.Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
                                }
                                photoPickerLauncher.launch(intent)
                            }
                            "Documents" -> {
                                documentPickerLauncher.launch("*/*")
                            }
                        }
                    },
                    containerColor = ThemePurple,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("vault_fab")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Item"
                        )
                        Text(
                            text = when (activeSection) {
                                "Notes" -> "Add Note"
                                "Photos & Videos" -> "Import Media"
                                else -> "Import File"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

    // Private Secure Plain Text Viewer Dialog
        if (textFileContentToRead != null) {
            val (name, content) = textFileContentToRead!!
            AlertDialog(
                onDismissRequest = { textFileContentToRead = null },
                containerColor = BrandBg,
                title = {
                    Text(
                        text = name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .border(1.dp, ThemeContainerBorder.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = content,
                            fontSize = 12.sp,
                            color = TextDark,
                            lineHeight = 16.sp
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.triggerKeypressEffects(context)
                            textFileContentToRead = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ThemePurple)
                    ) {
                        Text("Close")
                    }
                }
            )
        }

    }

    // New Note Dialog// New Note Dialog
    if (showAddNoteDialog) {
        AlertDialog(
            onDismissRequest = { showAddNoteDialog = false },
            title = {
                Text(
                    text = "Add Secret Note",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextDark
                )
            },
            containerColor = BrandBg,
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = noteTitle,
                        onValueChange = { noteTitle = it },
                        label = { Text("Title", fontSize = 12.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ThemePurple,
                            unfocusedBorderColor = ThemeContainerBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = noteContent,
                        onValueChange = { noteContent = it },
                        label = { Text("Secret Content", fontSize = 12.sp) },
                        minLines = 3,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ThemePurple,
                            unfocusedBorderColor = ThemeContainerBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.triggerKeypressEffects(context)
                        if (noteTitle.isNotBlank() && noteContent.isNotBlank()) {
                            viewModel.addVaultNote(noteTitle, noteContent)
                            noteTitle = ""
                            noteContent = ""
                            showAddNoteDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.triggerKeypressEffects(context)
                    showAddNoteDialog = false
                }) {
                    Text("Cancel", color = ThemePurple)
                }
            }
        )
    }

    // Change Passcode / Vault Settings Dialog
    if (showChangePasscodeDialog) {
        AlertDialog(
            onDismissRequest = { showChangePasscodeDialog = false },
            title = {
                Text(
                    text = "Vault Settings & Stealth",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextDark
                )
            },
            containerColor = BrandBg,
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Customize passcodes, biometric triggers, instant shake panic actions, and dynamic launcher disguises below.",
                        fontSize = 11.sp,
                        color = TextMedium,
                        lineHeight = 15.sp
                    )

                    Text(
                        text = "SECURITY PASSCODES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ThemePurple
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Real Vault Passcode",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ThemePurple
                        )
                        OutlinedTextField(
                            value = realPasscodeInput,
                            onValueChange = { input ->
                                if (input.all { it.isDigit() } && input.length <= 8) {
                                    realPasscodeInput = input
                                }
                            },
                            placeholder = { Text("e.g. 7777", fontSize = 12.sp) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ThemePurple,
                                unfocusedBorderColor = ThemeContainerBorder
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Decoy / Guest Passcode (Plausible Deniability)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE57373)
                        )
                        OutlinedTextField(
                            value = decoyPasscodeInput,
                            onValueChange = { input ->
                                if (input.all { it.isDigit() } && input.length <= 8) {
                                    decoyPasscodeInput = input
                                }
                            },
                            placeholder = { Text("e.g. 1111", fontSize = 12.sp) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFE57373),
                                unfocusedBorderColor = ThemeContainerBorder
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "BIOMETRIC AUTHENTICATION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ThemePurple
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Biometric Unlock",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = TextDark
                            )
                            Text(
                                text = "Unlock your secure vault using fingerprint or face scanning.",
                                fontSize = 10.sp,
                                color = TextMedium,
                                lineHeight = 13.sp
                            )
                        }
                        Switch(
                            checked = biometricEnabled,
                            onCheckedChange = {
                                viewModel.triggerKeypressEffects(context)
                                viewModel.setBiometricEnabled(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ThemePurple
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "PANIC GESTURE (SHAKE TO LOCK)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ThemePurple
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Enable Panic Shake",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = TextDark
                            )
                            Text(
                                text = "Shaking your phone instantly locks the vault or hides it.",
                                fontSize = 10.sp,
                                color = TextMedium,
                                lineHeight = 13.sp
                            )
                        }
                        Switch(
                            checked = panicEnabled,
                            onCheckedChange = {
                                viewModel.triggerKeypressEffects(context)
                                viewModel.setPanicEnabled(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ThemePurple
                            )
                        )
                    }

                    if (panicEnabled) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Panic Action Trigger",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("lock" to "Lock Vault Only", "home" to "Lock & Go Home").forEach { (actionKey, labelText) ->
                                    val isSelected = panicAction == actionKey
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                viewModel.triggerKeypressEffects(context)
                                                viewModel.setPanicAction(actionKey)
                                            },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSelected) ThemePurple.copy(alpha = 0.15f) else Color.White
                                        ),
                                        border = BorderStroke(
                                            1.dp,
                                            if (isSelected) ThemePurple else ThemeContainerBorder
                                        )
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = labelText,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) ThemePurple else TextMedium
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Button(
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    viewModel.lockVault()
                                    if (panicAction == "lock") {
                                        Toast.makeText(context, "Virtual Panic: Vault locked successfully!", Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(context, "Virtual Panic: Vault locked & returned Home!", Toast.LENGTH_LONG).show()
                                        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                                            addCategory(Intent.CATEGORY_HOME)
                                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        }
                                        context.startActivity(homeIntent)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Test Panic Trigger (Simulator)", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }


                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.triggerKeypressEffects(context)
                        if (realPasscodeInput.isBlank() || decoyPasscodeInput.isBlank()) {
                            android.widget.Toast.makeText(context, "Passcodes cannot be empty!", android.widget.Toast.LENGTH_SHORT).show()
                        } else if (realPasscodeInput == decoyPasscodeInput) {
                            android.widget.Toast.makeText(context, "Real and decoy passcodes must be different!", android.widget.Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.setVaultPin(realPasscodeInput)
                            viewModel.setDecoyPin(decoyPasscodeInput)
                            android.widget.Toast.makeText(context, "Settings saved successfully!", android.widget.Toast.LENGTH_LONG).show()
                            showChangePasscodeDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple)
                ) {
                    Text("Save Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.triggerKeypressEffects(context)
                    showChangePasscodeDialog = false
                }) {
                    Text("Cancel", color = ThemePurple)
                }
            }
        )
    }

    // Dynamic Multi-Language Selection Dialog
    if (showLanguageDialog) {
        val selectedLang by viewModel.selectedLanguage.collectAsState()
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = {
                Text(
                    text = viewModel.t("language"),
                    color = TextDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            containerColor = BrandBg,
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("OK", color = ThemePurple, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().heightIn(max = 350.dp)
                ) {
                    items(TranslationProvider.languages) { lang ->
                        val isSelected = selectedLang == lang.code
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) ThemePurple.copy(alpha = 0.15f) else Color.Transparent)
                                .clickable {
                                    viewModel.triggerKeypressEffects(context)
                                    viewModel.setSelectedLanguage(lang.code)
                                    showLanguageDialog = false
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(lang.flag, fontSize = 22.sp)
                            Text(
                                text = lang.name,
                                color = if (isSelected) ThemePurple else TextDark,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = ThemePurple,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        )
    }

    // Dynamic Theme Selection Dialog
    if (showThemeDialog) {
        val selectedTheme by viewModel.selectedTheme.collectAsState()
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = {
                Text(
                    text = viewModel.t("app_theme"),
                    color = TextDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            containerColor = BrandBg,
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("OK", color = ThemePurple, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth().heightIn(max = 350.dp)
                ) {
                    items(com.example.ui.theme.AppTheme.values()) { theme ->
                        val isSelected = selectedTheme == theme
                        val themeColors = when (theme) {
                            com.example.ui.theme.AppTheme.CLASSIC_LAVENDER -> com.example.ui.theme.ClassicLavenderColors
                            com.example.ui.theme.AppTheme.SUNSET_ROSE -> com.example.ui.theme.SunsetRoseColors
                            com.example.ui.theme.AppTheme.NORDIC_EMERALD -> com.example.ui.theme.NordicEmeraldColors
                            com.example.ui.theme.AppTheme.OCEAN_BREEZE -> com.example.ui.theme.OceanBreezeColors
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) ThemePurple.copy(alpha = 0.15f) else Color.Transparent)
                                .border(
                                    1.dp,
                                    if (isSelected) ThemePurple else Color.Gray.copy(alpha = 0.2f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    viewModel.triggerKeypressEffects(context)
                                    viewModel.setSelectedTheme(theme)
                                    showThemeDialog = false
                                }
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(theme.flag, fontSize = 24.sp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = theme.displayName,
                                    color = if (isSelected) ThemePurple else TextDark,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                // Swatch circles representing colors
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(themeColors.themePurple)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(themeColors.themeLightPurple)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(themeColors.brandBg)
                                            .border(1.dp, Color.Gray.copy(alpha = 0.3f), CircleShape)
                                    )
                                }
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = ThemePurple,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun EmptyVaultSectionState(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Description,
            contentDescription = null,
            tint = ThemePurple.copy(alpha = 0.5f),
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = TextDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = description,
            fontSize = 11.sp,
            color = TextMedium.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun PrivateBrowserSection(modifier: Modifier = Modifier) {
    var urlInput by remember { mutableStateOf("https://www.google.com") }
    var webViewRef by remember { mutableStateOf<android.webkit.WebView?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize()) {
        // Address Bar & Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IconButton(
                onClick = { webViewRef?.goBack() },
                enabled = canGoBack,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = if (canGoBack) ThemePurple else Color.Gray
                )
            }
            IconButton(
                onClick = { webViewRef?.goForward() },
                enabled = canGoForward,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Forward",
                    tint = if (canGoForward) ThemePurple else Color.Gray
                )
            }
            IconButton(
                onClick = { webViewRef?.reload() },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = ThemePurple)
            }

            OutlinedTextField(
                value = urlInput,
                onValueChange = { urlInput = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = TextStyle(fontSize = 11.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ThemePurple,
                    unfocusedBorderColor = ThemeContainerBorder
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        var targetUrl = urlInput.trim()
                        if (targetUrl.isNotEmpty()) {
                            if (!targetUrl.startsWith("http://") && !targetUrl.startsWith("https://")) {
                                targetUrl = "https://$targetUrl"
                            }
                            webViewRef?.loadUrl(targetUrl)
                        }
                    }
                )
            )

            IconButton(
                onClick = {
                    webViewRef?.loadUrl("https://www.google.com")
                    urlInput = "https://www.google.com"
                },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Default.Home, contentDescription = "Home", tint = ThemePurple)
            }
        }

        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().height(2.dp),
                color = ThemePurple
            )
        }

        // WebView Holder
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, ThemeContainerBorder, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            AndroidView(
                factory = { ctx ->
                    android.webkit.WebView(ctx).apply {
                        webViewClient = object : android.webkit.WebViewClient() {
                            override fun onPageStarted(view: android.webkit.WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                                isLoading = true
                                url?.let { urlInput = it }
                                view?.let {
                                    canGoBack = it.canGoBack()
                                    canGoForward = it.canGoForward()
                                }
                            }

                            override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                                isLoading = false
                                view?.let {
                                    canGoBack = it.canGoBack()
                                    canGoForward = it.canGoForward()
                                }
                            }
                        }
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE
                        clearCache(true)
                        clearHistory()
                        loadUrl("https://www.google.com")
                        webViewRef = this
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { webView ->
                    webViewRef = webView
                }
            )
        }
    }
}

@Composable
fun AppLockSection(viewModel: CalculatorViewModel) {
    val context = LocalContext.current
    val pm = remember { context.packageManager }
    val lockedApps by viewModel.lockedApps.collectAsState()

    val appsList = remember {
        try {
            pm.getInstalledApplications(PackageManager.GET_META_DATA)
                .filter { pm.getLaunchIntentForPackage(it.packageName) != null && it.packageName != context.packageName }
                .map { appInfo ->
                    val label = pm.getApplicationLabel(appInfo).toString()
                    val icon = pm.getApplicationIcon(appInfo)
                    val pkgName = appInfo.packageName
                    Triple(label, pkgName, icon)
                }
                .sortedBy { it.first }
        } catch (e: Exception) {
            emptyList()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Simulated App Lock Registry",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = TextDark
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Select which social or system apps you want to monitor inside this vault. When locked apps are launched, the calculator overlay is simulated.",
            fontSize = 11.sp,
            color = TextMedium,
            lineHeight = 15.sp
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${appsList.size} Apps Found",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ThemePurple
            )
            Text(
                text = "${lockedApps.size} Locked",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (appsList.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No dynamic launcher apps found on device", fontSize = 11.sp, color = TextMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(appsList) { (label, pkgName, icon) ->
                    val isLocked = lockedApps.contains(pkgName)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, if (isLocked) Color.Red.copy(alpha = 0.3f) else ThemeContainerBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                AndroidView(
                                    factory = { ctx ->
                                        android.widget.ImageView(ctx).apply {
                                            setImageDrawable(icon)
                                        }
                                    },
                                    modifier = Modifier.size(36.dp)
                                )
                                Column {
                                    Text(
                                        text = label,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = TextDark
                                    )
                                    Text(
                                        text = pkgName,
                                        fontSize = 10.sp,
                                        color = TextMedium
                                    )
                                }
                            }

                            Switch(
                                checked = isLocked,
                                onCheckedChange = {
                                    viewModel.triggerKeypressEffects(context)
                                    viewModel.toggleAppLock(pkgName)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color.Red,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = Color.LightGray
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
