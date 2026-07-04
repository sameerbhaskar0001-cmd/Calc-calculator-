
package com.example
import androidx.compose.foundation.layout.wrapContentSize


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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.key
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.blur
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
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
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.Canvas

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
    var showThemeDialog by remember { mutableStateOf(false) }

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
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent(androidx.compose.ui.input.pointer.PointerEventPass.Initial)
                        viewModel.updateLastInteraction()
                    }
                }
            },
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
                        .padding(start = 24.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        
                        Text(
                            text = "Calculator",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Box {
                        var showHeaderMenu by remember { mutableStateOf(false) }
                        IconButton(
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                showHeaderMenu = true
                            },
                            modifier = Modifier.testTag("overflow_menu_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = TextDark
                            )
                        }
                        androidx.compose.material3.DropdownMenu(
                            expanded = showHeaderMenu,
                            onDismissRequest = { showHeaderMenu = false },
                            modifier = Modifier.background(KeypadBg)
                        ) {
                            androidx.compose.material3.DropdownMenuItem(
                                text = { Text("Theme", color = TextDark) },
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    showHeaderMenu = false
                                    showThemeDialog = true
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Palette,
                                        contentDescription = null,
                                        tint = TextDark
                                    )
                                }
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
                        val vaultUnlocked by viewModel.vaultUnlocked.collectAsState()
                        if (!vaultUnlocked) {
                            VaultTabLockedContent(
                                viewModel = viewModel,
                                onLockExit = { activeTab = ActiveTab.CALCULATOR }
                            )
                        } else {
                            VaultTabUnlockedContent(
                                viewModel = viewModel,
                                onLockExit = { activeTab = ActiveTab.CALCULATOR }
                            )
                        }
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


    // Dynamic Theme Selection Dialog
    if (showThemeDialog) {
        val selectedTheme by viewModel.selectedTheme.collectAsState()
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = {
                Text(
                    text = "Theme",
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
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth().heightIn(max = 350.dp)
                ) {
                    items(com.example.ui.theme.AppTheme.values()) { theme ->
                        val isSelected = selectedTheme == theme
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) ThemePurple.copy(alpha = 0.1f) else Color.Transparent)
                                .clickable {
                                    viewModel.triggerKeypressEffects(context)
                                    viewModel.setSelectedTheme(theme)
                                    showThemeDialog = false
                                }
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(theme.previewColor)
                                    .border(1.dp, Color.Gray.copy(alpha = 0.2f), CircleShape)
                            )
                            Text(
                                text = theme.displayName,
                                color = if (isSelected) ThemePurple else TextDark,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 16.sp,
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
    val isEvaluated by viewModel.isEvaluated.collectAsState()
    val rate by viewModel.exchangeRate.collectAsState()
    val sourceCurrency by viewModel.sourceCurrency.collectAsState()
    val targetCurrency by viewModel.targetCurrency.collectAsState()

    val scrollState = rememberScrollState()

    // Determine numerical value for live split calculation
    val numericResult = calcResult.toDoubleOrNull() ?: expression.toDoubleOrNull() ?: 0.0
    val convertedTargetVal = numericResult * rate
    val df = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))

    val context = LocalContext.current

    // Smart presentation engine
    val formulaDisplay = when {
        isEvaluated -> "$expression ="
        calcResult.isNotEmpty() -> "= $calcResult"
        else -> ""
    }

    val mainDisplay = when {
        isEvaluated -> calcResult
        expression.isEmpty() -> "0"
        else -> expression
    }

    val mainColor = when {
        isEvaluated -> ThemePurple
        else -> TextDark
    }

    val mainFontSize = when {
        isEvaluated -> 64.sp
        else -> 54.sp
    }

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
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Bottom
            ) {
            // Expression
            if (formulaDisplay.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = formulaDisplay,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isEvaluated) TextMedium.copy(alpha = 0.65f) else ThemePurple.copy(alpha = 0.75f),
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.End,
                        modifier = Modifier.testTag("expression_display")
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Main output
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = mainDisplay,
                    fontSize = mainFontSize,
                    fontWeight = FontWeight.Bold,
                    color = mainColor,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.End,
                    modifier = Modifier.testTag("calc_result_display")
                )
            }

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
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black.copy(alpha = 0.4f))
                        .border(
                            width = 1.dp,
                            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(ThemePurple.copy(alpha = 0.3f), Color.Transparent),
                                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                end = androidx.compose.ui.geometry.Offset(0f, 300f)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .testTag("conversion_banner_card"),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
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
                                    color = TextDark
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
                .weight(1.5f)
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (row in buttons) {
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (char in row) {
                        val isOperator = char == "÷" || char == "×" || char == "-" || char == "+"
                        val isUtility = char == "C" || char == "+/-" || char == "%" || char == "⌫"
                        val isEquals = char == "="

                        GlassCalculatorKey(
                            char = char,
                            isOperator = isOperator,
                            isUtility = isUtility,
                            isEquals = isEquals,
                            themePurple = ThemePurple,
                            themeLightPurple = ThemeLightPurple,
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                viewModel.onCalcKeyPress(char)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("key_$char")
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GlassCalculatorKey(
    char: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isOperator: Boolean = false,
    isUtility: Boolean = false,
    isEquals: Boolean = false,
    themePurple: Color,
    themeLightPurple: Color
) {
    val themeColors = com.example.ui.theme.LocalAppThemeColors.current
    val interactionSource = remember { MutableInteractionSource() }

    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_scale"
    )

    val glowAlpha by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        animationSpec = androidx.compose.animation.core.tween(150),
        label = "glow_alpha"
    )

    val bgColor = when {
        isEquals -> Color(0xFFE3AB79) // Lighter beige/orange
        isUtility -> themeColors.keypadBg
        isOperator -> themeColors.keypadBg
        else -> themeColors.digitBg
    }
    
    val contentColor = when {
        isEquals -> Color.Black
        isUtility -> themePurple
        isOperator -> themePurple
        else -> themeColors.textDark
    }

    Box(
        modifier = modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .aspectRatio(1f)
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .drawBehind {
                    if (glowAlpha > 0f) {
                        val outerGlowRadius = size.width * 2.5f
                        drawCircle(
                            brush = androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.5f * glowAlpha),
                                    Color.White.copy(alpha = 0.1f * glowAlpha),
                                    Color.Transparent
                                ),
                                center = center,
                                radius = outerGlowRadius
                            ),
                            radius = outerGlowRadius,
                            center = center
                        )
                    }
                }
                .shadow(
                    elevation = if (!isUtility && !isEquals) 4.dp else 0.dp,
                    shape = CircleShape,
                    ambientColor = Color.Black.copy(alpha = 0.8f),
                    spotColor = Color.Black.copy(alpha = 0.8f)
                )
                .clip(CircleShape)
                .background(bgColor)
                .drawWithContent {
                    drawContent()
                    if (glowAlpha > 0f) {
                        drawRect(
                            brush = androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.4f * glowAlpha),
                                    Color.Transparent
                                ),
                                center = center,
                                radius = size.width / 1.2f
                            )
                        )
                    }
                }
                .background(
                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                        colors = listOf(Color.White.copy(alpha = 0.20f), Color.Transparent),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(0f, Float.POSITIVE_INFINITY)
                    )
                )
                .border(
                    width = 1.dp,
                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.4f), 
                            Color.White.copy(alpha = 0.05f), 
                            Color.Black.copy(alpha = 0.8f)
                        ),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(0f, Float.POSITIVE_INFINITY)
                    ),
                    shape = CircleShape
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            if (char == "⌫") {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Backspace",
                    tint = contentColor,
                    modifier = Modifier.size(28.dp)
                )
            } else if (char == "+/-") {
                 Text(
                    text = char,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    color = contentColor
                )
            } else {
                Text(
                    text = char,
                    fontSize = if (isOperator || isEquals) 32.sp else 32.sp,
                    fontWeight = FontWeight.Normal,
                    color = contentColor
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
                .padding(top = 12.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Minimal Rate Display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "1 ${sourceCurrency.code} = ${String.format(java.util.Locale.US, "%.4f", rate)} ${targetCurrency.code}",
                    color = TextMedium.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Normal
                )
            }
            // Cards container with reduced gap and swap button
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // SOURCE INPUT CARD
                    val isSourceActive = activeField == CurrencyField.USD
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
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
                            .weight(1f)
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
                .weight(1.5f)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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

            // Custom Numeric Keypad for Exchange Flow (3 Columns to match main calculator perfectly)
            val keys = listOf(
                listOf("C", " ", "⌫"),
                listOf("7", "8", "9"),
                listOf("4", "5", "6"),
                listOf("1", "2", "3"),
                listOf(".", "0", "00")
            )

            Column(
                modifier = Modifier.fillMaxWidth(0.75f).weight(1f).align(Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (row in keys) {
                    Row(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (char in row) {
                            if (char.isBlank()) {
                                Spacer(modifier = Modifier.weight(1f))
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
        CurrencySelectDialog(
            currencies = currencies,
            onDismiss = { showSourceSelector = false },
            onSelect = {
                viewModel.selectSourceCurrency(it)
                showSourceSelector = false
            }
        )
    }

    if (showTargetSelector) {
        CurrencySelectDialog(
            currencies = currencies,
            onDismiss = { showTargetSelector = false },
            onSelect = {
                viewModel.selectTargetCurrency(it)
                showTargetSelector = false
            }
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
                                color = TextDark
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
                        color = TextMedium
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
                    color = TextMedium
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
            color = TextMedium,
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
                        color = TextMedium,
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
                        color = TextMedium,
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

fun VaultTabLockedContent(
    viewModel: CalculatorViewModel,
    onLockExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vaultUnlocked by viewModel.vaultUnlocked.collectAsState()
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val biometricEnabled by viewModel.biometricEnabled.collectAsState()
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
                        android.widget.Toast.makeText(context, "Vault Unlocked via Biometrics!", android.widget.Toast.LENGTH_SHORT).show()
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
                    imageVector = Icons.Default.ArrowBack,
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
                    color = TextMedium,
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
                                                    }
                                                }
                                                else -> {
                                                    if (pinInput.length < 8) pinInput += key
                                                }
                                            }
                                        }
                                ) {
                                    Text(
                                        text = key,
                                        color = contentColor,
                                        fontSize = if (isSpecial) 16.sp else 24.sp,
                                        fontWeight = if (isSpecial) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

@Composable
fun VaultTabUnlockedContent(
    viewModel: CalculatorViewModel,
    onLockExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vaultNotes by viewModel.vaultNotes.collectAsState()
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    var showLanguageDialog by remember { mutableStateOf(false) }

    var showAddNoteDialog by remember { mutableStateOf(false) }
    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
    
    var pendingUnlockAction by remember { mutableStateOf<Pair<String, () -> Unit>?>(null) }

    var activeCameraMode by remember { mutableStateOf<String?>(null) } // null, "camera", "scanner"
    var showMediaAddOptions by remember { mutableStateOf(false) }
    var showDocAddOptions by remember { mutableStateOf(false) }

    var showChangePasscodeDialog by remember { mutableStateOf(false) }
    var realPasscodeInput by remember { mutableStateOf("") }
    var decoyPasscodeInput by remember { mutableStateOf("") }

    val context = LocalContext.current

    val biometricEnabled by viewModel.biometricEnabled.collectAsState()
    val panicEnabled by viewModel.panicEnabled.collectAsState()
    val panicAction by viewModel.panicAction.collectAsState()
    val screenDownLock by viewModel.screenDownLock.collectAsState()
    val blurThumbnails by viewModel.blurThumbnails.collectAsState()
    val lockedFolders by viewModel.lockedFolders.collectAsState()
    val tempUnlockedFolders by viewModel.tempUnlockedFolders.collectAsState()
    val activity = context as? androidx.fragment.app.FragmentActivity

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

        // Audio File Picker launcher
        val audioPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                if (uri != null) {
                    val success = viewModel.addVaultFile(context, uri)
                    if (success) {
                        android.widget.Toast.makeText(context, "Audio secured in Vault!", android.widget.Toast.LENGTH_SHORT).show()
                    } else {
                        android.widget.Toast.makeText(context, "Failed to secure audio", android.widget.Toast.LENGTH_SHORT).show()
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
    // Unified sensor detector for Panic Gesture (Shake and Face Down)
    if ((panicEnabled || screenDownLock) ) {
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
        // Vault Unlocked Content: Advanced Private Media Hub
        val vaultFiles by viewModel.vaultFiles.collectAsState()
        var activeSection by remember { mutableStateOf("Home") } // "Home", "Notes", "Photos & Videos", "Documents", "Explore", "Settings"
        var selectedFileForDetails by remember { mutableStateOf<String?>(null) }
        var textFileContentToRead by remember { mutableStateOf<Pair<String, String>?>(null) } // Pair(Name, Content)

        var selectedMediaFolder by remember { mutableStateOf("All") }
        var selectedDocFolder by remember { mutableStateOf("All") }
        var selectedNotesFolder by remember { mutableStateOf("All") }
        var selectedMusicFolder by remember { mutableStateOf("All") }
        
        var isMediaGridView by remember { mutableStateOf(true) }
        var isDocGridView by remember { mutableStateOf(false) }
        var isMusicGridView by remember { mutableStateOf(false) }
        
        var activeViewerFiles by remember { mutableStateOf<List<String>>(emptyList()) }
        var activeViewerIndex by remember { mutableStateOf<Int>(-1) }

        var showCreateFolderDialog by remember { mutableStateOf(false) }
        var newFolderName by remember { mutableStateOf("") }
        var showMoveToFolderDialog by remember { mutableStateOf<Pair<String, String>?>(null) } // Pair(itemId, type: "file"/"note")
        var showSearchDialog by remember { mutableStateOf(false) }
        var viewNoteToShow by remember { mutableStateOf<String?>(null) }



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
            if (activeSection == "Private Browser") {
                PrivateBrowserSection(
                    modifier = Modifier.fillMaxSize(),
                    onExit = {
                        activeSection = "Home"
                    },
                    onPanic = {
                        viewModel.lockVault()
                        onLockExit()
                    }
                )
            } else {
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
                                imageVector = Icons.Default.ArrowBack,
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
                        if (activeSection == "Photos & Videos" || activeSection == "Documents" || activeSection == "Music & Audio") {
                            val isCurrentGrid = when (activeSection) {
                                "Photos & Videos" -> isMediaGridView
                                "Documents" -> isDocGridView
                                "Music & Audio" -> isMusicGridView
                                else -> true
                            }
                            IconButton(
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    when (activeSection) {
                                        "Photos & Videos" -> isMediaGridView = !isMediaGridView
                                        "Documents" -> isDocGridView = !isDocGridView
                                        "Music & Audio" -> isMusicGridView = !isMusicGridView
                                    }
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1B2031))
                            ) {
                                Icon(
                                    imageVector = if (isCurrentGrid) Icons.Default.List else Icons.Default.GridView,
                                    contentDescription = "Toggle Grid/List View",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                showSearchDialog = true
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1B2031))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Global Search",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

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
                listOf("Home", "Timeline", "Notes", "Photos & Videos", "Documents", "Private Browser", "Recently Deleted", "Settings")
            } else {
                listOf("Home", "Timeline", "Notes", "Photos & Videos", "Documents", "Private Browser", "Explore", "Recently Deleted", "Settings")
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
                                "Recently Deleted" -> viewModel.t("recently_deleted")
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

            // Section Contents with Crossfade Animation
            androidx.compose.animation.Crossfade(
                targetState = activeSection,
                animationSpec = androidx.compose.animation.core.tween(300),
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) { section ->
                when (section) {
                    "Home" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
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
                                            color = TextMedium
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
                                            color = TextMedium
                                        )
                                    }
                                }
                            }
                        }

                        // 1. Vault Storage Analyzer Card
                        val storageDetails = viewModel.getVaultStorageDetails()
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = ThemeContainerBorder.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Vault Storage Analyzer",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Total: ${viewModel.formatFileSize(storageDetails.totalSize)}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ThemePurple
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                
                                val totalBytes = storageDetails.totalSize.coerceAtLeast(1L)
                                val photoRatio = storageDetails.photosSize.toFloat() / totalBytes
                                val videoRatio = storageDetails.videosSize.toFloat() / totalBytes
                                val docRatio = storageDetails.documentsSize.toFloat() / totalBytes
                                val noteRatio = storageDetails.notesSize.toFloat() / totalBytes
                                
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF2C3145))
                                ) {
                                    if (photoRatio > 0) {
                                        Box(modifier = Modifier.weight(photoRatio.coerceAtLeast(0.01f)).fillMaxHeight().background(Color(0xFF2979FF)))
                                    }
                                    if (videoRatio > 0) {
                                        Box(modifier = Modifier.weight(videoRatio.coerceAtLeast(0.01f)).fillMaxHeight().background(Color(0xFFFF9100)))
                                    }
                                    if (docRatio > 0) {
                                        Box(modifier = Modifier.weight(docRatio.coerceAtLeast(0.01f)).fillMaxHeight().background(Color(0xFF00E676)))
                                    }
                                    if (noteRatio > 0) {
                                        Box(modifier = Modifier.weight(noteRatio.coerceAtLeast(0.01f)).fillMaxHeight().background(Color(0xFFFFD600)))
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    StorageCategoryItem(
                                        color = Color(0xFF2979FF),
                                        label = "Photos",
                                        sizeStr = viewModel.formatFileSize(storageDetails.photosSize),
                                        count = storageDetails.photosCount
                                    )
                                    StorageCategoryItem(
                                        color = Color(0xFFFF9100),
                                        label = "Videos",
                                        sizeStr = viewModel.formatFileSize(storageDetails.videosSize),
                                        count = storageDetails.videosCount
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    StorageCategoryItem(
                                        color = Color(0xFF00E676),
                                        label = "Docs",
                                        sizeStr = viewModel.formatFileSize(storageDetails.documentsSize),
                                        count = storageDetails.documentsCount
                                    )
                                    StorageCategoryItem(
                                        color = Color(0xFFFFD600),
                                        label = "Notes",
                                        sizeStr = viewModel.formatFileSize(storageDetails.notesSize),
                                        count = storageDetails.notesCount
                                    )
                                }
                            }
                        }

                        // 2. Recently Opened Row (Horizontal Scroll)
                        val recentlyOpened by viewModel.recentlyOpened.collectAsState()
                        if (recentlyOpened.isNotEmpty()) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "Recently Opened",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMedium,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(recentlyOpened) { item ->
                                        Card(
                                            modifier = Modifier
                                                .width(130.dp)
                                                .height(70.dp)
                                                .clickable {
                                                    viewModel.triggerKeypressEffects(context)
                                                    if (item.type == "note") {
                                                        activeSection = "Notes"
                                                        viewNoteToShow = item.extra
                                                    } else {
                                                        val fileStr = item.extra
                                                        if (fileStr.isNotEmpty()) {
                                                            val parts = fileStr.split("|||")
                                                            if (parts.size >= 4) {
                                                                if (parts[3].startsWith("image/") || parts[3].startsWith("video/")) {
                                                                    selectedFileForDetails = fileStr
                                                                } else if (parts[3].startsWith("text/plain")) {
                                                                    try {
                                                                        val txt = java.io.File(parts[4]).readText()
                                                                        textFileContentToRead = Pair(parts[2], txt)
                                                                    } catch (e: Exception) {}
                                                                } else {
                                                                    selectedFileForDetails = fileStr
                                                                }
                                                            }
                                                        }
                                                    }
                                                },
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031)),
                                            shape = RoundedCornerShape(10.dp),
                                            border = BorderStroke(1.dp, ThemeContainerBorder.copy(alpha = 0.15f))
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxSize().padding(8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(32.dp)
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(
                                                            when (item.type) {
                                                                "note" -> Color(0xFFFFD600).copy(alpha = 0.15f)
                                                                else -> {
                                                                    val parts = item.extra.split("|||")
                                                                    val mime = if (parts.size >= 4) parts[3] else ""
                                                                    if (mime.startsWith("image/")) Color(0xFF2979FF).copy(alpha = 0.15f)
                                                                    else if (mime.startsWith("video/")) Color(0xFFFF9100).copy(alpha = 0.15f)
                                                                    else Color(0xFF00E676).copy(alpha = 0.15f)
                                                                }
                                                            }
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = when (item.type) {
                                                            "note" -> Icons.Default.Article
                                                            else -> {
                                                                val parts = item.extra.split("|||")
                                                                val mime = if (parts.size >= 4) parts[3] else ""
                                                                if (mime.startsWith("image/")) Icons.Default.Image
                                                                else if (mime.startsWith("video/")) Icons.Default.PlayArrow
                                                                else Icons.Default.Description
                                                            }
                                                        },
                                                        contentDescription = null,
                                                        tint = when (item.type) {
                                                            "note" -> Color(0xFFFFD600)
                                                            else -> {
                                                                val parts = item.extra.split("|||")
                                                                val mime = if (parts.size >= 4) parts[3] else ""
                                                                if (mime.startsWith("image/")) Color(0xFF2979FF)
                                                                else if (mime.startsWith("video/")) Color(0xFFFF9100)
                                                                else Color(0xFF00E676)
                                                            }
                                                        },
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                                Column(verticalArrangement = Arrangement.Center) {
                                                    Text(
                                                        text = item.name,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                    Text(
                                                        text = if (item.type == "note") "Note" else "File",
                                                        fontSize = 8.sp,
                                                        color = TextMedium
                                                    )
                                                }
                                            }
                                        }
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
                                color = TextMedium
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
                            title = "Secure Camera",
                            subtitle = "Take private photos",
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFE53935).copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.CameraAlt, "Camera", tint = Color(0xFFE53935), modifier = Modifier.size(20.dp))
                                }
                            },
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                activeCameraMode = "camera"
                            }
                        )

                        FolderCard(
                            title = "Private Documents",
                            subtitle = "${vaultFiles.filter { !it.contains("image/") && !it.contains("video/") && !it.contains("audio/") && !it.contains("music/") }.size} locked assets",
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
                            title = "Private Music & Audio",
                            subtitle = "${vaultFiles.filter { it.contains("audio/") || it.contains("music/") }.size} secure audio tracks",
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFE040FB).copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.MusicNote, "Music", tint = Color(0xFFE040FB), modifier = Modifier.size(20.dp))
                                }
                            },
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                activeSection = "Music & Audio"
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

                        val recentlyDeletedFiles by viewModel.recentlyDeletedFiles.collectAsState()
                        FolderCard(
                            title = "Recently Deleted",
                            subtitle = "${recentlyDeletedFiles.size} temporary items",
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFE53935).copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Delete, "Recently Deleted", tint = Color(0xFFE53935), modifier = Modifier.size(20.dp))
                                }
                            },
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                activeSection = "Recently Deleted"
                            }
                        )
                    }
                }
                "Timeline" -> {
                    val allItems = mutableListOf<Triple<String, String, String>>() // Timestamp, Type, Content/ID
                    
                    vaultFiles.forEach { fileStr ->
                        val parts = fileStr.split("|||")
                        if (parts.size >= 5) {
                            val timestamp = parts[4]
                            val mime = parts[3]
                            val type = if (mime.startsWith("image/") || mime.startsWith("video/")) "Media" else "Document"
                            allItems.add(Triple(timestamp, type, fileStr))
                        }
                    }
                    
                    vaultNotes.forEach { noteStr ->
                        val parts = noteStr.split("|||")
                        if (parts.size >= 3) {
                            val timestamp = parts[0]
                            allItems.add(Triple(timestamp, "Note", noteStr))
                        }
                    }
                    
                    // Simple chronological sort assuming YYYY-MM-DD HH:MM:SS format 
                    // (Note: For robust timelines, you'd parse to dates, but string sort works for ISO formats)
                    val sortedItems = allItems.sortedByDescending { it.first }

                    if (sortedItems.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.History, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(64.dp))
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("No Activity", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Items added to your vault will appear here", color = TextMedium, fontSize = 14.sp)
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 80.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(sortedItems) { (timestamp, type, data) ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .background(
                                                    when (type) {
                                                        "Media" -> Color(0xFF2979FF).copy(alpha = 0.2f)
                                                        "Document" -> Color(0xFF00E676).copy(alpha = 0.2f)
                                                        else -> Color(0xFFFFD600).copy(alpha = 0.2f)
                                                    },
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = when (type) {
                                                    "Media" -> Icons.Default.Image
                                                    "Document" -> Icons.Default.Description
                                                    else -> Icons.Default.Article
                                                },
                                                contentDescription = null,
                                                tint = when (type) {
                                                    "Media" -> Color(0xFF2979FF)
                                                    "Document" -> Color(0xFF00E676)
                                                    else -> Color(0xFFFFD600)
                                                },
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        
                                        Spacer(modifier = Modifier.width(16.dp))
                                        
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = when(type) {
                                                    "Note" -> data.split("|||").getOrNull(1) ?: "Secure Note"
                                                    else -> data.split("|||").getOrNull(2) ?: "Secure File"
                                                },
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = timestamp,
                                                color = TextMedium,
                                                fontSize = 12.sp
                                            )
                                        }
                                        
                                        IconButton(onClick = {
                                            if (type == "Note") {
                                                viewNoteToShow = data
                                            } else {
                                                selectedFileForDetails = data
                                            }
                                        }) {
                                            Icon(Icons.Default.ArrowForward, contentDescription = "View", tint = Color.Gray, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                "Notes" -> {
                    val favoriteNotes by viewModel.favoriteNotes.collectAsState()
                    val noteFolders by viewModel.noteFolders.collectAsState()
                    val vaultFolders by viewModel.vaultFolders.collectAsState()

                    val filteredNotes = vaultNotes.filter { noteStr ->
                        val parts = noteStr.split("|||")
                        val isFav = favoriteNotes.contains(noteStr)
                        val assocFolder = noteFolders[noteStr] ?: ""
                        
                        when (selectedNotesFolder) {
                            "All" -> true
                            "Favorites" -> isFav
                            "Default" -> assocFolder.isEmpty()
                            else -> assocFolder == selectedNotesFolder
                        }
                    }

                    Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        // Custom folder chip row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomFolderChip(
                                selected = selectedNotesFolder == "All",
                                label = "All",
                                onClick = { selectedNotesFolder = "All" }
                            )
                            CustomFolderChip(
                                selected = selectedNotesFolder == "Favorites",
                                label = "Favorites ⭐",
                                onClick = { selectedNotesFolder = "Favorites" }
                            )
                            CustomFolderChip(
                                selected = selectedNotesFolder == "Default",
                                label = "Uncategorized",
                                onClick = { selectedNotesFolder = "Default" }
                            )
                            vaultFolders.forEach { folderName ->
                                CustomFolderChip(
                                    selected = selectedNotesFolder == folderName,
                                    label = folderName,
                                    isLocked = lockedFolders.contains(folderName),
                                    onClick = { 
                                        if (lockedFolders.contains(folderName) && !tempUnlockedFolders.contains(folderName)) {
                                            pendingUnlockAction = Pair(folderName) { 
                                                viewModel.tempUnlockFolder(folderName)
                                                selectedNotesFolder = folderName 
                                            }
                                        } else {
                                            selectedNotesFolder = folderName 
                                        }
                                    },
                                    onLongClick = {
                                        if (lockedFolders.contains(folderName)) {
                                            pendingUnlockAction = Pair(folderName) { viewModel.toggleFolderLock(folderName) }
                                        } else {
                                            viewModel.toggleFolderLock(folderName)
                                        }
                                    },
                                    onDelete = {
                                        viewModel.deleteFolder(folderName)
                                        if (selectedNotesFolder == folderName) {
                                            selectedNotesFolder = "All"
                                        }
                                    }
                                )
                            }
                            TextButton(onClick = { showCreateFolderDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Folder", modifier = Modifier.size(16.dp), tint = ThemePurple)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("New Folder", fontSize = 12.sp, color = ThemePurple)
                            }
                        }

                        if (filteredNotes.isEmpty()) {
                            EmptyVaultSectionState(
                                title = "No Secure Notes",
                                description = "Tap 'Add Note' to save password credentials or personal journals safely.",
                                actionLabel = "Add Note",
                                onActionClick = { showAddNoteDialog = true }
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(filteredNotes) { noteStr ->
                                    val parts = noteStr.split("|||")
                                    if (parts.size == 3) {
                                        val timestamp = parts[0]
                                        val title = parts[1]
                                        val body = parts[2]
                                        val isFav = favoriteNotes.contains(noteStr)
                                        val assocFolder = noteFolders[noteStr] ?: ""

                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .border(
                                                    width = 1.dp,
                                                    color = ThemeContainerBorder.copy(alpha = 0.2f),
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .clickable {
                                                    viewModel.triggerKeypressEffects(context)
                                                    viewModel.recordOpenedItem(noteStr, "note", title, noteStr)
                                                    viewNoteToShow = noteStr
                                                },
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031)),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(14.dp)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.Top
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                        modifier = Modifier.weight(1f)
                                                    ) {
                                                        Text(
                                                            text = title,
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 14.sp,
                                                            color = Color.White,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis,
                                                            modifier = Modifier.weight(1f, fill = false)
                                                        )
                                                        if (isFav) {
                                                            Icon(
                                                                imageVector = Icons.Default.Star,
                                                                contentDescription = "Favorite",
                                                                tint = Color(0xFFFFD600),
                                                                modifier = Modifier.size(14.dp)
                                                            )
                                                        }
                                                        if (assocFolder.isNotEmpty()) {
                                                            Surface(
                                                                color = ThemePurple.copy(alpha = 0.15f),
                                                                shape = RoundedCornerShape(4.dp),
                                                                modifier = Modifier.padding(horizontal = 4.dp)
                                                            ) {
                                                                Text(
                                                                    text = assocFolder,
                                                                    color = ThemePurple,
                                                                    fontSize = 8.sp,
                                                                    fontWeight = FontWeight.Bold,
                                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                                )
                                                            }
                                                        }
                                                    }
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
                                                    color = TextMedium,
                                                    lineHeight = 16.sp,
                                                    maxLines = 3,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = timestamp,
                                                        fontSize = 9.sp,
                                                        color = TextMedium.copy(alpha = 0.6f),
                                                        fontWeight = FontWeight.Medium,
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                    
                                                    IconButton(
                                                        onClick = {
                                                            viewModel.triggerKeypressEffects(context)
                                                            viewModel.toggleFavoriteNote(noteStr)
                                                        },
                                                        modifier = Modifier.size(24.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = if (isFav) Icons.Default.Star else Icons.Default.StarBorder,
                                                            contentDescription = "Toggle Favorite",
                                                            tint = if (isFav) Color(0xFFFFD600) else Color(0xFF8B92A5),
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                    }

                                                    IconButton(
                                                        onClick = {
                                                            viewModel.triggerKeypressEffects(context)
                                                            showMoveToFolderDialog = Pair(noteStr, "note")
                                                        },
                                                        modifier = Modifier.size(24.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Folder,
                                                            contentDescription = "Move to Folder",
                                                            tint = Color(0xFF2979FF),
                                                            modifier = Modifier.size(16.dp)
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
                }

                "Photos & Videos" -> {
                    val favoriteFiles by viewModel.favoriteFiles.collectAsState()
                    val fileFolders by viewModel.fileFolders.collectAsState()
                    val vaultFolders by viewModel.vaultFolders.collectAsState()

                    val allMediaFiles = vaultFiles.filter {
                        val parts = it.split("|||")
                        parts.size >= 4 && (parts[3].startsWith("image/") || parts[3].startsWith("video/"))
                    }

                    val filteredMediaFiles = allMediaFiles.filter { fileStr ->
                        val parts = fileStr.split("|||")
                        val id = parts[0]
                        val isFav = favoriteFiles.contains(id)
                        val assocFolder = fileFolders[id] ?: ""
                        
                        when (selectedMediaFolder) {
                            "All" -> true
                            "Favorites" -> isFav
                            "Default" -> assocFolder.isEmpty()
                            else -> assocFolder == selectedMediaFolder
                        }
                    }

                    Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        // Custom folder chip row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomFolderChip(
                                selected = selectedMediaFolder == "All",
                                label = "All",
                                onClick = { selectedMediaFolder = "All" }
                            )
                            CustomFolderChip(
                                selected = selectedMediaFolder == "Favorites",
                                label = "Favorites ⭐",
                                onClick = { selectedMediaFolder = "Favorites" }
                            )
                            CustomFolderChip(
                                selected = selectedMediaFolder == "Default",
                                label = "Uncategorized",
                                onClick = { selectedMediaFolder = "Default" }
                            )
                            vaultFolders.forEach { folderName ->
                                CustomFolderChip(
                                    selected = selectedMediaFolder == folderName,
                                    label = folderName,
                                    isLocked = lockedFolders.contains(folderName),
                                    onClick = { 
                                        if (lockedFolders.contains(folderName) && !tempUnlockedFolders.contains(folderName)) {
                                            pendingUnlockAction = Pair(folderName) { 
                                                viewModel.tempUnlockFolder(folderName)
                                                selectedMediaFolder = folderName 
                                            }
                                        } else {
                                            selectedMediaFolder = folderName 
                                        }
                                    },
                                    onLongClick = {
                                        if (lockedFolders.contains(folderName)) {
                                            pendingUnlockAction = Pair(folderName) { viewModel.toggleFolderLock(folderName) }
                                        } else {
                                            viewModel.toggleFolderLock(folderName)
                                        }
                                    },
                                    onDelete = {
                                        viewModel.deleteFolder(folderName)
                                        if (selectedMediaFolder == folderName) {
                                            selectedMediaFolder = "All"
                                        }
                                    }
                                )
                            }
                            TextButton(onClick = { showCreateFolderDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Folder", modifier = Modifier.size(16.dp), tint = ThemePurple)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("New Folder", fontSize = 12.sp, color = ThemePurple)
                            }
                        }

                        if (filteredMediaFiles.isEmpty()) {
                            EmptyVaultSectionState(
                                title = "No Secure Photos or Videos",
                                description = "Tap 'Import Photo' to transfer visual media into this secure sandboxed directory."
                            )
                        } else {
                            if (isMediaGridView) {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(3),
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(filteredMediaFiles) { fileStr ->
                                        val parts = fileStr.split("|||")
                                        if (parts.size >= 6) {
                                            val id = parts[0]
                                            val originalName = parts[2]
                                            val mimeType = parts[3]
                                            val path = parts[4]
                                            val isFav = favoriteFiles.contains(id)

                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(1f)
                                                    .clickable {
                                                        viewModel.triggerKeypressEffects(context)
                                                        viewModel.recordOpenedItem(id, "file", originalName, fileStr)
                                                        activeViewerFiles = filteredMediaFiles
                                                        activeViewerIndex = filteredMediaFiles.indexOf(fileStr)
                                                    },
                                                shape = RoundedCornerShape(12.dp),
                                                colors = CardDefaults.cardColors(containerColor = KeypadBg)
                                            ) {
                                                Box(modifier = Modifier.fillMaxSize()) {
                                                    if (mimeType.startsWith("image/")) {
                                                        AsyncImage(
                                                            model = java.io.File(path),
                                                            contentDescription = originalName,
                                                            modifier = Modifier.fillMaxSize().then(if (blurThumbnails) Modifier.blur(16.dp) else Modifier),
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
                                                    if (blurThumbnails) {
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxSize()
                                                                .background(Color.Black.copy(alpha = 0.4f)),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.BlurOn,
                                                                contentDescription = "Blurred",
                                                                tint = Color.White.copy(alpha = 0.8f),
                                                                modifier = Modifier.size(24.dp)
                                                            )
                                                        }
                                                    }

                                                    // Favorite star in top right corner
                                                    if (isFav) {
                                                        Icon(
                                                            imageVector = Icons.Default.Star,
                                                            contentDescription = "Favorite",
                                                            tint = Color(0xFFFFD600),
                                                            modifier = Modifier
                                                                .size(18.dp)
                                                                .align(Alignment.TopEnd)
                                                                .padding(4.dp)
                                                        )
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
                            } else {
                                LazyColumn(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(filteredMediaFiles) { fileStr ->
                                        val parts = fileStr.split("|||")
                                        if (parts.size >= 6) {
                                            val id = parts[0]
                                            val timestamp = parts[1]
                                            val originalName = parts[2]
                                            val mimeType = parts[3]
                                            val path = parts[4]
                                            val sizeStr = parts[5]
                                            val isFav = favoriteFiles.contains(id)
                                            val assocFolder = fileFolders[id] ?: ""

                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .border(
                                                        width = 1.dp,
                                                        color = ThemeContainerBorder.copy(alpha = 0.2f),
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                    .clickable {
                                                        viewModel.triggerKeypressEffects(context)
                                                        viewModel.recordOpenedItem(id, "file", originalName, fileStr)
                                                        activeViewerFiles = filteredMediaFiles
                                                        activeViewerIndex = filteredMediaFiles.indexOf(fileStr)
                                                    },
                                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031)),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(8.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(50.dp)
                                                            .clip(RoundedCornerShape(8.dp))
                                                            .background(Color.Black),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        if (mimeType.startsWith("image/")) {
                                                            AsyncImage(
                                                                model = java.io.File(path),
                                                                contentDescription = originalName,
                                                                modifier = Modifier.fillMaxSize().then(if (blurThumbnails) Modifier.blur(16.dp) else Modifier),
                                                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                                            )
                                                        } else {
                                                            Icon(
                                                                imageVector = Icons.Default.Visibility,
                                                                contentDescription = "Video",
                                                                tint = Color.White.copy(alpha = 0.8f),
                                                                modifier = Modifier.size(24.dp)
                                                            )
                                                        }
                                                        if (blurThumbnails) {
                                                            Box(
                                                                modifier = Modifier
                                                                    .fillMaxSize()
                                                                    .background(Color.Black.copy(alpha = 0.4f)),
                                                                contentAlignment = Alignment.Center
                                                            ) {
                                                                Icon(
                                                                    imageVector = Icons.Default.BlurOn,
                                                                    contentDescription = "Blurred",
                                                                    tint = Color.White.copy(alpha = 0.8f),
                                                                    modifier = Modifier.size(16.dp)
                                                                )
                                                            }
                                                        }
                                                    }

                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                        ) {
                                                            Text(
                                                                text = originalName,
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color.White,
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis,
                                                                modifier = Modifier.weight(1f, fill = false)
                                                            )
                                                            if (isFav) {
                                                                Icon(
                                                                    imageVector = Icons.Default.Star,
                                                                    contentDescription = "Favorite",
                                                                    tint = Color(0xFFFFD600),
                                                                    modifier = Modifier.size(14.dp)
                                                                )
                                                            }
                                                            if (assocFolder.isNotEmpty()) {
                                                                Surface(
                                                                    color = ThemePurple.copy(alpha = 0.15f),
                                                                    shape = RoundedCornerShape(4.dp),
                                                                    modifier = Modifier.padding(horizontal = 4.dp)
                                                                ) {
                                                                    Text(
                                                                        text = assocFolder,
                                                                        color = ThemePurple,
                                                                        fontSize = 8.sp,
                                                                        fontWeight = FontWeight.Bold,
                                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                                    )
                                                                }
                                                            }
                                                        }
                                                        Text(
                                                            text = "$sizeStr • $timestamp",
                                                            fontSize = 10.sp,
                                                            color = TextMedium
                                                        )
                                                    }

                                                    Row(
                                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        IconButton(
                                                            onClick = {
                                                                viewModel.triggerKeypressEffects(context)
                                                                viewModel.toggleFavoriteFile(id)
                                                            },
                                                            modifier = Modifier.size(32.dp)
                                                        ) {
                                                            Icon(
                                                                imageVector = if (isFav) Icons.Default.Star else Icons.Default.StarBorder,
                                                                contentDescription = "Toggle Favorite",
                                                                tint = if (isFav) Color(0xFFFFD600) else Color(0xFF8B92A5),
                                                                modifier = Modifier.size(18.dp)
                                                            )
                                                        }

                                                        IconButton(
                                                            onClick = {
                                                                viewModel.triggerKeypressEffects(context)
                                                                showMoveToFolderDialog = Pair(id, "file")
                                                            },
                                                            modifier = Modifier.size(32.dp)
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Folder,
                                                                contentDescription = "Move to Folder",
                                                                tint = Color(0xFF2979FF),
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
                    }
                }

                "Documents" -> {
                    val favoriteFiles by viewModel.favoriteFiles.collectAsState()
                    val fileFolders by viewModel.fileFolders.collectAsState()
                    val vaultFolders by viewModel.vaultFolders.collectAsState()

                    val allDocFiles = vaultFiles.filter {
                        val parts = it.split("|||")
                        parts.size >= 4 && !parts[3].startsWith("image/") && !parts[3].startsWith("video/")
                    }

                    val filteredDocFiles = allDocFiles.filter { fileStr ->
                        val parts = fileStr.split("|||")
                        val id = parts[0]
                        val isFav = favoriteFiles.contains(id)
                        val assocFolder = fileFolders[id] ?: ""
                        
                        when (selectedDocFolder) {
                            "All" -> true
                            "Favorites" -> isFav
                            "Default" -> assocFolder.isEmpty()
                            else -> assocFolder == selectedDocFolder
                        }
                    }

                    Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        // Custom folder chip row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomFolderChip(
                                selected = selectedDocFolder == "All",
                                label = "All",
                                onClick = { selectedDocFolder = "All" }
                            )
                            CustomFolderChip(
                                selected = selectedDocFolder == "Favorites",
                                label = "Favorites ⭐",
                                onClick = { selectedDocFolder = "Favorites" }
                            )
                            CustomFolderChip(
                                selected = selectedDocFolder == "Default",
                                label = "Uncategorized",
                                onClick = { selectedDocFolder = "Default" }
                            )
                            vaultFolders.forEach { folderName ->
                                CustomFolderChip(
                                    selected = selectedDocFolder == folderName,
                                    label = folderName,
                                    isLocked = lockedFolders.contains(folderName),
                                    onClick = { 
                                        if (lockedFolders.contains(folderName) && !tempUnlockedFolders.contains(folderName)) {
                                            pendingUnlockAction = Pair(folderName) { 
                                                viewModel.tempUnlockFolder(folderName)
                                                selectedDocFolder = folderName 
                                            }
                                        } else {
                                            selectedDocFolder = folderName 
                                        }
                                    },
                                    onLongClick = {
                                        if (lockedFolders.contains(folderName)) {
                                            pendingUnlockAction = Pair(folderName) { viewModel.toggleFolderLock(folderName) }
                                        } else {
                                            viewModel.toggleFolderLock(folderName)
                                        }
                                    },
                                    onDelete = {
                                        viewModel.deleteFolder(folderName)
                                        if (selectedDocFolder == folderName) {
                                            selectedDocFolder = "All"
                                        }
                                    }
                                )
                            }
                            TextButton(onClick = { showCreateFolderDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Folder", modifier = Modifier.size(16.dp), tint = ThemePurple)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("New Folder", fontSize = 12.sp, color = ThemePurple)
                            }
                        }

                        if (filteredDocFiles.isEmpty()) {
                            EmptyVaultSectionState(
                                title = "No Private Documents",
                                description = "Tap 'Import File' to secure any PDF, TXT, or binary document from local storage."
                            )
                        } else {
                            if (isDocGridView) {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(3),
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(filteredDocFiles) { fileStr ->
                                        val parts = fileStr.split("|||")
                                        if (parts.size >= 6) {
                                            val id = parts[0]
                                            val originalName = parts[2]
                                            val mimeType = parts[3]
                                            val path = parts[4]
                                            val sizeStr = parts[5]
                                            val isFav = favoriteFiles.contains(id)

                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(1f)
                                                    .clickable {
                                                        viewModel.triggerKeypressEffects(context)
                                                        viewModel.recordOpenedItem(id, "file", originalName, fileStr)
                                                        activeViewerFiles = filteredDocFiles
                                                        activeViewerIndex = filteredDocFiles.indexOf(fileStr)
                                                    },
                                                shape = RoundedCornerShape(12.dp),
                                                colors = CardDefaults.cardColors(containerColor = KeypadBg)
                                            ) {
                                                Box(modifier = Modifier.fillMaxSize()) {
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(8.dp),
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.Center
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Description,
                                                            contentDescription = "Document",
                                                            tint = ThemePurple,
                                                            modifier = Modifier.size(32.dp)
                                                        )
                                                        Spacer(modifier = Modifier.height(4.dp))
                                                        Text(
                                                            text = originalName,
                                                            color = Color.White,
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis,
                                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                                        )
                                                        Text(
                                                            text = sizeStr,
                                                            color = TextMedium,
                                                            fontSize = 9.sp
                                                        )
                                                    }

                                                    if (isFav) {
                                                        Icon(
                                                            imageVector = Icons.Default.Star,
                                                            contentDescription = "Favorite",
                                                            tint = Color(0xFFFFD600),
                                                            modifier = Modifier
                                                                .size(16.dp)
                                                                .align(Alignment.TopEnd)
                                                                .padding(4.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(filteredDocFiles) { fileStr ->
                                        val parts = fileStr.split("|||")
                                        if (parts.size >= 6) {
                                            val id = parts[0]
                                            val timestamp = parts[1]
                                            val originalName = parts[2]
                                            val mimeType = parts[3]
                                            val path = parts[4]
                                            val sizeStr = parts[5]
                                            val isFav = favoriteFiles.contains(id)
                                            val assocFolder = fileFolders[id] ?: ""

                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .border(
                                                        width = 1.dp,
                                                        color = ThemeContainerBorder.copy(alpha = 0.2f),
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                    .clickable {
                                                        viewModel.triggerKeypressEffects(context)
                                                        viewModel.recordOpenedItem(id, "file", originalName, fileStr)
                                                        activeViewerFiles = filteredDocFiles
                                                        activeViewerIndex = filteredDocFiles.indexOf(fileStr)
                                                    },
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
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                        ) {
                                                            Text(
                                                                text = originalName,
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color.White,
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis,
                                                                modifier = Modifier.weight(1f, fill = false)
                                                            )
                                                            if (isFav) {
                                                                Icon(
                                                                    imageVector = Icons.Default.Star,
                                                                    contentDescription = "Favorite",
                                                                    tint = Color(0xFFFFD600),
                                                                    modifier = Modifier.size(14.dp)
                                                                )
                                                            }
                                                            if (assocFolder.isNotEmpty()) {
                                                                Surface(
                                                                    color = ThemePurple.copy(alpha = 0.15f),
                                                                    shape = RoundedCornerShape(4.dp),
                                                                    modifier = Modifier.padding(horizontal = 4.dp)
                                                                ) {
                                                                    Text(
                                                                        text = assocFolder,
                                                                        color = ThemePurple,
                                                                        fontSize = 8.sp,
                                                                        fontWeight = FontWeight.Bold,
                                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                                    )
                                                                }
                                                            }
                                                        }
                                                        Text(
                                                            text = "$sizeStr • $timestamp",
                                                            fontSize = 10.sp,
                                                            color = TextMedium
                                                        )
                                                    }

                                                    Row(
                                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        // Favorite Toggle Icon Button
                                                        IconButton(
                                                            onClick = {
                                                                viewModel.triggerKeypressEffects(context)
                                                                viewModel.toggleFavoriteFile(id)
                                                            },
                                                            modifier = Modifier.size(32.dp)
                                                        ) {
                                                            Icon(
                                                                imageVector = if (isFav) Icons.Default.Star else Icons.Default.StarBorder,
                                                                contentDescription = "Toggle Favorite",
                                                                tint = if (isFav) Color(0xFFFFD600) else Color(0xFF8B92A5),
                                                                modifier = Modifier.size(18.dp)
                                                            )
                                                        }

                                                        // Folder Association Icon Button
                                                        IconButton(
                                                            onClick = {
                                                                viewModel.triggerKeypressEffects(context)
                                                                showMoveToFolderDialog = Pair(id, "file")
                                                            },
                                                            modifier = Modifier.size(32.dp)
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Folder,
                                                                contentDescription = "Move to Folder",
                                                                tint = Color(0xFF2979FF),
                                                                modifier = Modifier.size(18.dp)
                                                            )
                                                        }

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
                                                                android.widget.Toast.makeText(context, "Moved to Recently Deleted!", android.widget.Toast.LENGTH_SHORT).show()
                                                            },
                                                            modifier = Modifier.size(32.dp)
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Delete,
                                                                contentDescription = "Delete File",
                                                                tint = Color(0xFFEF5350),
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
                    }
                }
                "Music & Audio" -> {
                    val favoriteFiles by viewModel.favoriteFiles.collectAsState()
                    val fileFolders by viewModel.fileFolders.collectAsState()
                    val vaultFolders by viewModel.vaultFolders.collectAsState()

                    val allMusicFiles = vaultFiles.filter {
                        val parts = it.split("|||")
                        parts.size >= 4 && (parts[3].startsWith("audio/") || parts[3].startsWith("music/"))
                    }

                    val filteredMusicFiles = allMusicFiles.filter { fileStr ->
                        val parts = fileStr.split("|||")
                        val id = parts[0]
                        val isFav = favoriteFiles.contains(id)
                        val assocFolder = fileFolders[id] ?: ""

                        when (selectedMusicFolder) {
                            "All" -> true
                            "Favorites" -> isFav
                            "Default" -> assocFolder.isEmpty()
                            else -> assocFolder == selectedMusicFolder
                        }
                    }

                    Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        // Custom folder chip row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomFolderChip(
                                selected = selectedMusicFolder == "All",
                                label = "All",
                                onClick = { selectedMusicFolder = "All" }
                            )
                            CustomFolderChip(
                                selected = selectedMusicFolder == "Favorites",
                                label = "Favorites ⭐",
                                onClick = { selectedMusicFolder = "Favorites" }
                            )
                            CustomFolderChip(
                                selected = selectedMusicFolder == "Default",
                                label = "Uncategorized",
                                onClick = { selectedMusicFolder = "Default" }
                            )
                            vaultFolders.forEach { folderName ->
                                CustomFolderChip(
                                    selected = selectedMusicFolder == folderName,
                                    label = folderName,
                                    isLocked = lockedFolders.contains(folderName),
                                    onClick = { 
                                        if (lockedFolders.contains(folderName) && !tempUnlockedFolders.contains(folderName)) {
                                            pendingUnlockAction = Pair(folderName) { 
                                                viewModel.tempUnlockFolder(folderName)
                                                selectedMusicFolder = folderName 
                                            }
                                        } else {
                                            selectedMusicFolder = folderName 
                                        }
                                    },
                                    onLongClick = {
                                        if (lockedFolders.contains(folderName)) {
                                            pendingUnlockAction = Pair(folderName) { viewModel.toggleFolderLock(folderName) }
                                        } else {
                                            viewModel.toggleFolderLock(folderName)
                                        }
                                    },
                                    onDelete = {
                                        viewModel.deleteFolder(folderName)
                                        if (selectedMusicFolder == folderName) {
                                            selectedMusicFolder = "All"
                                        }
                                    }
                                )
                            }
                            TextButton(onClick = { showCreateFolderDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Folder", modifier = Modifier.size(16.dp), tint = ThemePurple)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("New Folder", fontSize = 12.sp, color = ThemePurple)
                            }
                        }

                        if (filteredMusicFiles.isEmpty()) {
                            EmptyVaultSectionState(
                                title = "No Private Music & Audio",
                                description = "Tap 'Import Audio' to transfer your sensitive voice records or audio files into the vault."
                            )
                        } else {
                            if (isMusicGridView) {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(3),
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(filteredMusicFiles) { fileStr ->
                                        val parts = fileStr.split("|||")
                                        if (parts.size >= 6) {
                                            val id = parts[0]
                                            val originalName = parts[2]
                                            val mimeType = parts[3]
                                            val path = parts[4]
                                            val sizeStr = parts[5]
                                            val isFav = favoriteFiles.contains(id)

                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(1f)
                                                    .clickable {
                                                        viewModel.triggerKeypressEffects(context)
                                                        viewModel.recordOpenedItem(id, "file", originalName, fileStr)
                                                        activeViewerFiles = filteredMusicFiles
                                                        activeViewerIndex = filteredMusicFiles.indexOf(fileStr)
                                                    },
                                                shape = RoundedCornerShape(12.dp),
                                                colors = CardDefaults.cardColors(containerColor = KeypadBg)
                                            ) {
                                                Box(modifier = Modifier.fillMaxSize()) {
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(8.dp),
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.Center
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.MusicNote,
                                                            contentDescription = "Audio file",
                                                            tint = ThemePurple,
                                                            modifier = Modifier.size(32.dp)
                                                        )
                                                        Spacer(modifier = Modifier.height(4.dp))
                                                        Text(
                                                            text = originalName,
                                                            color = Color.White,
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis,
                                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                                        )
                                                        Text(
                                                            text = sizeStr,
                                                            color = TextMedium,
                                                            fontSize = 9.sp
                                                        )
                                                    }

                                                    if (isFav) {
                                                        Icon(
                                                            imageVector = Icons.Default.Star,
                                                            contentDescription = "Favorite",
                                                            tint = Color(0xFFFFD600),
                                                            modifier = Modifier
                                                                .size(16.dp)
                                                                .align(Alignment.TopEnd)
                                                                .padding(4.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(filteredMusicFiles) { fileStr ->
                                        val parts = fileStr.split("|||")
                                        if (parts.size >= 6) {
                                            val id = parts[0]
                                            val timestamp = parts[1]
                                            val originalName = parts[2]
                                            val mimeType = parts[3]
                                            val path = parts[4]
                                            val sizeStr = parts[5]
                                            val isFav = favoriteFiles.contains(id)
                                            val assocFolder = fileFolders[id] ?: ""

                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .border(
                                                        width = 1.dp,
                                                        color = ThemeContainerBorder.copy(alpha = 0.2f),
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                    .clickable {
                                                        viewModel.triggerKeypressEffects(context)
                                                        viewModel.recordOpenedItem(id, "file", originalName, fileStr)
                                                        activeViewerFiles = filteredMusicFiles
                                                        activeViewerIndex = filteredMusicFiles.indexOf(fileStr)
                                                    },
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
                                                            imageVector = Icons.Default.MusicNote,
                                                            contentDescription = "Audio file",
                                                            tint = ThemePurple,
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                    }

                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                        ) {
                                                            Text(
                                                                text = originalName,
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color.White,
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis,
                                                                modifier = Modifier.weight(1f, fill = false)
                                                            )
                                                            if (isFav) {
                                                                Icon(
                                                                    imageVector = Icons.Default.Star,
                                                                    contentDescription = "Favorite",
                                                                    tint = Color(0xFFFFD600),
                                                                    modifier = Modifier.size(14.dp)
                                                                )
                                                            }
                                                            if (assocFolder.isNotEmpty()) {
                                                                Surface(
                                                                    color = ThemePurple.copy(alpha = 0.15f),
                                                                    shape = RoundedCornerShape(4.dp),
                                                                    modifier = Modifier.padding(horizontal = 4.dp)
                                                                ) {
                                                                    Text(
                                                                        text = assocFolder,
                                                                        color = ThemePurple,
                                                                        fontSize = 8.sp,
                                                                        fontWeight = FontWeight.Bold,
                                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                                    )
                                                                }
                                                            }
                                                        }
                                                        Text(
                                                            text = "$sizeStr • $timestamp",
                                                            fontSize = 10.sp,
                                                            color = TextMedium
                                                        )
                                                    }

                                                    Row(
                                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        IconButton(
                                                            onClick = {
                                                                viewModel.triggerKeypressEffects(context)
                                                                viewModel.toggleFavoriteFile(id)
                                                            },
                                                            modifier = Modifier.size(32.dp)
                                                        ) {
                                                            Icon(
                                                                imageVector = if (isFav) Icons.Default.Star else Icons.Default.StarBorder,
                                                                contentDescription = "Toggle Favorite",
                                                                tint = if (isFav) Color(0xFFFFD600) else Color(0xFF8B92A5),
                                                                modifier = Modifier.size(18.dp)
                                                            )
                                                        }

                                                        IconButton(
                                                            onClick = {
                                                                viewModel.triggerKeypressEffects(context)
                                                                showMoveToFolderDialog = Pair(id, "file")
                                                            },
                                                            modifier = Modifier.size(32.dp)
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Folder,
                                                                contentDescription = "Move to Folder",
                                                                tint = Color(0xFF2979FF),
                                                                modifier = Modifier.size(18.dp)
                                                            )
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
                                                                android.widget.Toast.makeText(context, "Moved to Recently Deleted!", android.widget.Toast.LENGTH_SHORT).show()
                                                            },
                                                            modifier = Modifier.size(32.dp)
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Delete,
                                                                contentDescription = "Delete File",
                                                                tint = Color(0xFFEF5350),
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
                            color = TextMedium
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
                                    title = "Secure Camera",
                                    subtitle = "Private photos",
                                    icon = Icons.Default.CameraAlt,
                                    bgColor = Color(0xFFE53935),
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeCameraMode = "camera"
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
                "Recently Deleted" -> {
                    val recentlyDeletedFiles by viewModel.recentlyDeletedFiles.collectAsState()

                    if (recentlyDeletedFiles.isEmpty()) {
                        EmptyVaultSectionState(
                            title = "No Recently Deleted Files",
                            description = "Files you delete from the vault will be kept here for 30 days before being permanently removed."
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(recentlyDeletedFiles) { recentStr ->
                                val parts = recentStr.split("|||")
                                if (parts.size >= 7) {
                                    val originalName = parts[2]
                                    val mimeType = parts[3]
                                    val path = parts[4]
                                    val sizeStr = parts[5]
                                    val deletedTimeMs = parts[6].toLongOrNull() ?: System.currentTimeMillis()
                                    val deletedDate = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date(deletedTimeMs))
                                    
                                    val elapsedMillis = System.currentTimeMillis() - deletedTimeMs
                                    val remainingMillis = (30L * 24 * 60 * 60 * 1000) - elapsedMillis
                                    val remainingDays = (remainingMillis / (1000 * 60 * 60 * 24)).coerceAtLeast(0L)

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
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(50.dp)
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(ThemeLightPurple),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    if (mimeType.startsWith("image/")) {
                                                        AsyncImage(
                                                            model = java.io.File(path),
                                                            contentDescription = originalName,
                                                            modifier = Modifier.fillMaxSize(),
                                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                                        )
                                                    } else if (mimeType.startsWith("video/")) {
                                                        Icon(
                                                            imageVector = Icons.Default.Visibility,
                                                            contentDescription = "Video",
                                                            tint = ThemePurple,
                                                            modifier = Modifier.size(24.dp)
                                                        )
                                                    } else {
                                                        Icon(
                                                            imageVector = Icons.Default.Description,
                                                            contentDescription = "Document",
                                                            tint = ThemePurple,
                                                            modifier = Modifier.size(24.dp)
                                                        )
                                                    }
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
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(
                                                        text = "Deleted: $deletedDate • $sizeStr",
                                                        fontSize = 10.sp,
                                                        color = TextMedium
                                                    )
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(
                                                        text = "$remainingDays days remaining before permanent deletion",
                                                        fontSize = 9.sp,
                                                        color = Color(0xFFFF8A80),
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(10.dp))

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                Button(
                                                    onClick = {
                                                        viewModel.triggerKeypressEffects(context)
                                                        val restored = viewModel.restoreFromRecent(recentStr)
                                                        if (restored) {
                                                            android.widget.Toast.makeText(context, "Restored to Vault", android.widget.Toast.LENGTH_SHORT).show()
                                                        } else {
                                                            android.widget.Toast.makeText(context, "Failed to restore", android.widget.Toast.LENGTH_SHORT).show()
                                                        }
                                                    },
                                                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(8.dp),
                                                    contentPadding = PaddingValues(vertical = 4.dp)
                                                ) {
                                                    Icon(Icons.Default.Restore, contentDescription = "Restore", modifier = Modifier.size(14.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Restore", fontSize = 11.sp, color = Color.White)
                                                }

                                                Button(
                                                    onClick = {
                                                        viewModel.triggerKeypressEffects(context)
                                                        val deleted = viewModel.deletePermanentlyFromRecent(recentStr)
                                                        if (deleted) {
                                                            android.widget.Toast.makeText(context, "Permanently Deleted", android.widget.Toast.LENGTH_SHORT).show()
                                                        } else {
                                                            android.widget.Toast.makeText(context, "Failed to delete", android.widget.Toast.LENGTH_SHORT).show()
                                                        }
                                                    },
                                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(8.dp),
                                                    contentPadding = PaddingValues(vertical = 4.dp)
                                                ) {
                                                    Icon(Icons.Default.DeleteForever, contentDescription = "Delete Permanently", modifier = Modifier.size(14.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Delete Permanently", fontSize = 11.sp, color = Color.White)
                                                }
                                            }
                                        }
                                    }
                                }
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
                                    color = TextMedium,
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
                                        placeholder = { Text("e.g. 7777", fontSize = 12.sp, color = TextMedium) },
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
                                        placeholder = { Text("e.g. 1111", fontSize = 12.sp, color = TextMedium) },
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
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))

                            val intruderDetectionEnabled by viewModel.intruderDetectionEnabled.collectAsState()
                            SettingsSwitchRow(
                                title = "Intruder Detection",
                                subtitle = "Log incorrect passcode attempts & keys entered",
                                icon = Icons.Default.Warning,
                                iconTint = Color(0xFFFF9100),
                                checked = intruderDetectionEnabled,
                                onCheckedChange = { viewModel.setIntruderDetectionEnabled(it) }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))

                            val blurThumbnails by viewModel.blurThumbnails.collectAsState()
                            SettingsSwitchRow(
                                title = "Blur Thumbnails",
                                subtitle = "Blurs media previews to prevent shoulder-surfing",
                                icon = Icons.Default.BlurOn,
                                iconTint = Color(0xFF00E5FF),
                                checked = blurThumbnails,
                                onCheckedChange = { viewModel.setBlurThumbnails(it) }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))

                            val autoLockDuration by viewModel.autoLockDuration.collectAsState()
                            var showAutoLockDialog by remember { mutableStateOf(false) }
                            val currentDurationLabel = when (autoLockDuration) {
                                30 -> "30 seconds"
                                60 -> "1 minute"
                                120 -> "2 minutes"
                                300 -> "5 minutes"
                                600 -> "10 minutes"
                                else -> "Never"
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showAutoLockDialog = true }
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = "Auto Lock Timer",
                                    tint = Color(0xFFFFD600),
                                    modifier = Modifier.size(24.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Auto Lock Timer", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text("Instantly locks vault after inactivity", color = TextMedium, fontSize = 11.sp)
                                }
                                Surface(
                                    color = ThemePurple.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = currentDurationLabel,
                                        color = ThemePurple,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            if (showAutoLockDialog) {
                                AlertDialog(
                                    onDismissRequest = { showAutoLockDialog = false },
                                    title = { Text("Select Auto Lock Timer", color = TextDark) },
                                    text = {
                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            listOf(
                                                -1 to "Never (Disable)",
                                                30 to "30 seconds",
                                                60 to "1 minute",
                                                120 to "2 minutes",
                                                300 to "5 minutes",
                                                600 to "10 minutes"
                                            ).forEach { (seconds, label) ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            viewModel.setAutoLockDuration(seconds)
                                                            showAutoLockDialog = false
                                                        }
                                                        .padding(vertical = 12.dp, horizontal = 8.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(label, color = TextDark, fontSize = 14.sp)
                                                    if (autoLockDuration == seconds) {
                                                        Icon(Icons.Default.Check, contentDescription = "Selected", tint = ThemePurple, modifier = Modifier.size(18.dp))
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    confirmButton = {
                                        TextButton(onClick = { showAutoLockDialog = false }) {
                                            Text("Close", color = ThemePurple)
                                        }
                                    },
                                    containerColor = Color.White,
                                    shape = RoundedCornerShape(16.dp)
                                )
                            }
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
                        }
                    }
                }
            }
        } // closes when (section)
        } // closes Crossfade
    } // closes Column
    } // closes else

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
                                    viewModel.unhideVaultFile(
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
                                Icon(Icons.Default.FileDownload, contentDescription = "Unhide", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(viewModel.t("unhide"), fontSize = 11.sp)
                            }

                            Button(
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    viewModel.deleteVaultFile(fileStr)
                                    android.widget.Toast.makeText(context, "Moved to Recently Deleted!", android.widget.Toast.LENGTH_SHORT).show()
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

        // Full-Screen Interactive Media & Document Viewer
        if (activeViewerFiles.isNotEmpty() && activeViewerIndex >= 0 && activeViewerIndex < activeViewerFiles.size) {
            key(activeViewerFiles) {
                val favoriteFiles by viewModel.favoriteFiles.collectAsState()
                val pagerState = rememberPagerState(
                    initialPage = activeViewerIndex,
                    pageCount = { activeViewerFiles.size }
                )
                // Sync activeViewerIndex with pagerState.currentPage
                LaunchedEffect(pagerState.currentPage) {
                    activeViewerIndex = pagerState.currentPage
                }

                androidx.compose.ui.window.Dialog(
                    onDismissRequest = {
                        activeViewerFiles = emptyList()
                        activeViewerIndex = -1
                    },
                    properties = androidx.compose.ui.window.DialogProperties(
                        usePlatformDefaultWidth = false,
                        dismissOnBackPress = true,
                        dismissOnClickOutside = false
                    )
                ) {
                    val formatDuration = remember {
                        { ms: Int ->
                            val sec = (ms / 1000) % 60
                            val min = (ms / 1000) / 60
                            String.format("%02d:%02d", min, sec)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF0A0C16))
                    ) {
                        // HorizontalPager for left/right swipe
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            val currentFileStr = activeViewerFiles[page]
                            val parts = currentFileStr.split("|||")
                            if (parts.size >= 6) {
                                val id = parts[0]
                                val timestamp = parts[1]
                                val originalName = parts[2]
                                val mimeType = parts[3]
                                val path = parts[4]
                                val sizeStr = parts[5]

                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (mimeType.startsWith("image/")) {
                                        // Dynamic Pinch-to-Zoom
                                        var scale by remember { mutableStateOf(1f) }
                                        var offset by remember { mutableStateOf(Offset.Zero) }
                                        val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
                                            scale = (scale * zoomChange).coerceIn(1f, 5f)
                                            offset += offsetChange
                                        }

                                        AsyncImage(
                                            model = java.io.File(path),
                                            contentDescription = originalName,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .transformable(state = transformState)
                                                .graphicsLayer(
                                                    scaleX = scale,
                                                    scaleY = scale,
                                                    translationX = offset.x,
                                                    translationY = offset.y
                                                ),
                                            contentScale = androidx.compose.ui.layout.ContentScale.Fit
                                        )
                                    } else if (mimeType.startsWith("video/")) {
                                        // Video Player
                                        AndroidView(
                                            factory = { ctx ->
                                                android.widget.VideoView(ctx).apply {
                                                    setVideoPath(path)
                                                    val mediaController = android.widget.MediaController(ctx)
                                                    mediaController.setAnchorView(this)
                                                    setMediaController(mediaController)
                                                    setOnPreparedListener { mp ->
                                                        mp.isLooping = true
                                                        start()
                                                    }
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(16 / 9f)
                                        )
                                    } else if (mimeType.equals("application/pdf", ignoreCase = true) || originalName.endsWith(".pdf", ignoreCase = true)) {
                                        // PDF Viewer
                                        val bitmaps = remember(path) {
                                            val list = mutableListOf<android.graphics.Bitmap>()
                                            try {
                                                val file = java.io.File(path)
                                                val pfd = android.os.ParcelFileDescriptor.open(file, android.os.ParcelFileDescriptor.MODE_READ_ONLY)
                                                val renderer = android.graphics.pdf.PdfRenderer(pfd)
                                                val count = minOf(renderer.pageCount, 15) // Renders up to 15 pages safely
                                                for (i in 0 until count) {
                                                    val page = renderer.openPage(i)
                                                    val bitmap = android.graphics.Bitmap.createBitmap(page.width * 2, page.height * 2, android.graphics.Bitmap.Config.ARGB_8888)
                                                    val canvas = android.graphics.Canvas(bitmap)
                                                    canvas.drawColor(android.graphics.Color.WHITE)
                                                    page.render(bitmap, null, null, android.graphics.pdf.PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                                                    list.add(bitmap)
                                                    page.close()
                                                }
                                                renderer.close()
                                                pfd.close()
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                            list
                                        }

                                        if (bitmaps.isNotEmpty()) {
                                            LazyColumn(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(top = 80.dp, bottom = 80.dp),
                                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                items(bitmaps) { bitmap ->
                                                    Card(
                                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                                        shape = RoundedCornerShape(8.dp),
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 16.dp)
                                                    ) {
                                                        androidx.compose.foundation.Image(
                                                            bitmap = bitmap.asImageBitmap(),
                                                            contentDescription = "PDF Page",
                                                            modifier = Modifier.fillMaxWidth(),
                                                            contentScale = androidx.compose.ui.layout.ContentScale.FillWidth
                                                        )
                                                    }
                                                }
                                            }
                                        } else {
                                            Column(
                                                modifier = Modifier.fillMaxSize(),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.PictureAsPdf,
                                                    contentDescription = null,
                                                    tint = ThemePurple,
                                                    modifier = Modifier.size(64.dp)
                                                )
                                                Spacer(modifier = Modifier.height(16.dp))
                                                Text("Secure PDF Document", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                                Text("$sizeStr • $timestamp", color = TextMedium, fontSize = 12.sp)
                                            }
                                        }
                                    } else if (mimeType.startsWith("audio/") || mimeType.startsWith("music/")) {
                                        // Audio Player with beautiful Vinyl Record Layout
                                        var isPlaying by remember { mutableStateOf(false) }
                                        var currentPosition by remember { mutableStateOf(0f) }
                                        var duration by remember { mutableStateOf(1f) }
                                        val mediaPlayer = remember(path) {
                                            android.media.MediaPlayer().apply {
                                                setDataSource(path)
                                                prepare()
                                                duration = this.duration.toFloat()
                                            }
                                        }

                                        LaunchedEffect(isPlaying) {
                                            while (isPlaying) {
                                                currentPosition = mediaPlayer.currentPosition.toFloat()
                                                kotlinx.coroutines.delay(250)
                                            }
                                        }

                                        DisposableEffect(path) {
                                            onDispose {
                                                mediaPlayer.release()
                                            }
                                        }

                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(24.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            var rotationAngle by remember { mutableStateOf(0f) }
                                            LaunchedEffect(isPlaying) {
                                                while (isPlaying) {
                                                    rotationAngle = (rotationAngle + 2f) % 360f
                                                    kotlinx.coroutines.delay(16)
                                                }
                                            }

                                            // Visual Vinyl disc representation
                                            Box(
                                                modifier = Modifier
                                                    .size(200.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFF101424))
                                                    .border(4.dp, ThemePurple.copy(alpha = 0.5f), CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(180.dp)
                                                        .graphicsLayer(rotationZ = rotationAngle)
                                                        .background(Color.Black, CircleShape)
                                                        .border(8.dp, Color(0xFF1A1A1A), CircleShape),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(60.dp)
                                                            .background(ThemePurple, CircleShape),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.MusicNote,
                                                            contentDescription = null,
                                                            tint = Color.White,
                                                            modifier = Modifier.size(28.dp)
                                                        )
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(32.dp))

                                            Text(
                                                text = originalName,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Spacer(modifier = Modifier.height(24.dp))

                                            Slider(
                                                value = currentPosition,
                                                onValueChange = {
                                                    currentPosition = it
                                                    mediaPlayer.seekTo(it.toInt())
                                                },
                                                valueRange = 0f..duration,
                                                colors = SliderDefaults.colors(
                                                    thumbColor = ThemePurple,
                                                    activeTrackColor = ThemePurple,
                                                    inactiveTrackColor = ThemePurple.copy(alpha = 0.24f)
                                                ),
                                                modifier = Modifier.fillMaxWidth()
                                            )

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = formatDuration(currentPosition.toInt()),
                                                    color = TextMedium,
                                                    fontSize = 12.sp
                                                )
                                                Text(
                                                    text = formatDuration(duration.toInt()),
                                                    color = TextMedium,
                                                    fontSize = 12.sp
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(24.dp))

                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(24.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                IconButton(
                                                    onClick = {
                                                        viewModel.triggerKeypressEffects(context)
                                                        val prev = activeViewerIndex - 1
                                                        if (prev >= 0) {
                                                            activeViewerIndex = prev
                                                        }
                                                    },
                                                    modifier = Modifier.size(48.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.ArrowBack,
                                                        contentDescription = "Previous",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(32.dp)
                                                    )
                                                }

                                                IconButton(
                                                    onClick = {
                                                        viewModel.triggerKeypressEffects(context)
                                                        if (isPlaying) {
                                                            mediaPlayer.pause()
                                                            isPlaying = false
                                                        } else {
                                                            mediaPlayer.start()
                                                            isPlaying = true
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .size(64.dp)
                                                        .background(ThemePurple, CircleShape)
                                                ) {
                                                    Icon(
                                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                        contentDescription = if (isPlaying) "Pause" else "Play",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(36.dp)
                                                    )
                                                }

                                                IconButton(
                                                    onClick = {
                                                        viewModel.triggerKeypressEffects(context)
                                                        val next = activeViewerIndex + 1
                                                        if (next < activeViewerFiles.size) {
                                                            activeViewerIndex = next
                                                        }
                                                    },
                                                    modifier = Modifier.size(48.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.ArrowForward,
                                                        contentDescription = "Next",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(32.dp)
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        // Generic file presentation
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Description,
                                                contentDescription = null,
                                                tint = ThemePurple,
                                                modifier = Modifier.size(64.dp)
                                            )
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Text(originalName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                            Text("$sizeStr • $mimeType", color = TextMedium, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // Top Action Bar overlaying everything
                        val activeFile = activeViewerFiles[activeViewerIndex]
                        val activeParts = activeFile.split("|||")
                        if (activeParts.size >= 6) {
                            val activeId = activeParts[0]
                            val activeName = activeParts[2]
                            val activeIsFav = favoriteFiles.contains(activeId)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black.copy(alpha = 0.6f))
                                    .statusBarsPadding()
                                    .padding(horizontal = 8.dp, vertical = 12.dp)
                                    .align(Alignment.TopCenter),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeViewerFiles = emptyList()
                                        activeViewerIndex = -1
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close Viewer",
                                        tint = Color.White
                                    )
                                }

                                Text(
                                    text = activeName,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                IconButton(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        viewModel.toggleFavoriteFile(activeId)
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (activeIsFav) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = "Toggle Favorite",
                                        tint = if (activeIsFav) Color(0xFFFFD600) else Color.White
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        showMoveToFolderDialog = Pair(activeId, "file")
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Folder,
                                        contentDescription = "Move to Folder",
                                        tint = Color(0xFF2979FF)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        viewModel.exportVaultFile(
                                            context = context,
                                            fileSerialized = activeFile,
                                            onSuccess = { msg -> android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_LONG).show() },
                                            onFailure = { err -> android.widget.Toast.makeText(context, err, android.widget.Toast.LENGTH_LONG).show() }
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FileDownload,
                                        contentDescription = "Export File",
                                        tint = Color(0xFF4CAF50)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        viewModel.deleteVaultFile(activeFile)
                                        android.widget.Toast.makeText(context, "Moved to Recently Deleted!", android.widget.Toast.LENGTH_SHORT).show()
                                        val nextIndex = if (activeViewerIndex < activeViewerFiles.size - 1) activeViewerIndex else activeViewerIndex - 1
                                        val remainingList = activeViewerFiles.toMutableList().apply { removeAt(activeViewerIndex) }
                                        if (remainingList.isNotEmpty() && nextIndex >= 0) {
                                            activeViewerFiles = remainingList
                                            activeViewerIndex = nextIndex
                                        } else {
                                            activeViewerFiles = emptyList()
                                            activeViewerIndex = -1
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete File",
                                        tint = Color(0xFFEF5350)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

            // 1. Create Folder Dialog
            if (showCreateFolderDialog) {
                AlertDialog(
                onDismissRequest = { showCreateFolderDialog = false },
                containerColor = BrandBg,
                title = { Text("Create New Folder", color = TextDark, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Enter a unique folder name to organize your vault content:", color = TextMedium, fontSize = 12.sp)
                        OutlinedTextField(
                            value = newFolderName,
                            onValueChange = { newFolderName = it },
                            placeholder = { Text("Folder Name", color = TextMedium) },
                            textStyle = androidx.compose.ui.text.TextStyle(color = TextDark),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ThemePurple,
                                unfocusedBorderColor = ThemeContainerBorder.copy(alpha = 0.2f),
                                focusedContainerColor = KeypadBg,
                                unfocusedContainerColor = KeypadBg,
                                focusedTextColor = TextDark,
                                unfocusedTextColor = TextDark
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.triggerKeypressEffects(context)
                            if (newFolderName.trim().isNotEmpty()) {
                                viewModel.addFolder(newFolderName.trim())
                                newFolderName = ""
                                showCreateFolderDialog = false
                            } else {
                                android.widget.Toast.makeText(context, "Please enter a folder name", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ThemePurple)
                    ) {
                        Text("Create", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        viewModel.triggerKeypressEffects(context)
                        showCreateFolderDialog = false
                        newFolderName = ""
                    }) {
                        Text("Cancel", color = TextMedium)
                    }
                }
            )
        }

        // 2. Move To Folder Dialog
        if (showMoveToFolderDialog != null) {
            val (itemId, type) = showMoveToFolderDialog!!
            val vaultFolders by viewModel.vaultFolders.collectAsState()
            
            AlertDialog(
                onDismissRequest = { showMoveToFolderDialog = null },
                containerColor = BrandBg,
                title = { Text("Move to Folder", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Select a destination folder or remove from current folder:", color = TextMedium, fontSize = 12.sp)
                        
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.heightIn(max = 240.dp)) {
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.triggerKeypressEffects(context)
                                            if (type == "file") {
                                                viewModel.setFolderForFile(itemId, "")
                                            } else {
                                                viewModel.setFolderForNote(itemId, "")
                                            }
                                            showMoveToFolderDialog = null
                                            android.widget.Toast.makeText(context, "Removed from folder", android.widget.Toast.LENGTH_SHORT).show()
                                        },
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.FolderOpen, contentDescription = null, tint = Color.Gray)
                                        Text("[ Uncategorized / Default ]", color = Color.White, fontSize = 13.sp)
                                    }
                                }
                            }
                            
                            items(vaultFolders) { fName ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.triggerKeypressEffects(context)
                                            if (type == "file") {
                                                viewModel.setFolderForFile(itemId, fName)
                                            } else {
                                                viewModel.setFolderForNote(itemId, fName)
                                            }
                                            showMoveToFolderDialog = null
                                            android.widget.Toast.makeText(context, "Moved to $fName", android.widget.Toast.LENGTH_SHORT).show()
                                        },
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Folder, contentDescription = null, tint = ThemePurple)
                                        Text(fName, color = Color.White, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = {
                        viewModel.triggerKeypressEffects(context)
                        showMoveToFolderDialog = null
                    }) {
                        Text("Close", color = TextMedium)
                    }
                }
            )
        }

        // 3. Global Search Dialog
        if (showSearchDialog) {
            var searchQuery by remember { mutableStateOf("") }
            val favoriteNotes by viewModel.favoriteNotes.collectAsState()
            val favoriteFiles by viewModel.favoriteFiles.collectAsState()
            
            val matchedNotes = if (searchQuery.trim().isEmpty()) emptyList() else vaultNotes.filter {
                val parts = it.split("|||")
                parts.size == 3 && (parts[1].contains(searchQuery, ignoreCase = true) || parts[2].contains(searchQuery, ignoreCase = true))
            }
            val matchedFiles = if (searchQuery.trim().isEmpty()) emptyList() else vaultFiles.filter {
                val parts = it.split("|||")
                parts.size >= 6 && parts[2].contains(searchQuery, ignoreCase = true)
            }
            
            AlertDialog(
                onDismissRequest = { showSearchDialog = false },
                containerColor = BrandBg,
                title = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Global Vault Search", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Locate any photo, video, document, or note instantly", color = TextMedium, fontSize = 11.sp)
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search title, body, original filename...", color = TextMedium, fontSize = 12.sp) },
                            textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 13.sp),
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ThemePurple,
                                unfocusedBorderColor = ThemeContainerBorder.copy(alpha = 0.2f),
                                focusedContainerColor = KeypadBg,
                                unfocusedContainerColor = KeypadBg,
                                focusedTextColor = TextDark,
                                unfocusedTextColor = TextDark
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        if (searchQuery.trim().isNotEmpty() && matchedNotes.isEmpty() && matchedFiles.isEmpty()) {
                            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                Text("No matching records found", color = TextMedium, fontSize = 12.sp)
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.heightIn(max = 300.dp)
                            ) {
                                if (matchedNotes.isNotEmpty()) {
                                    item {
                                        Text("Matched Notes (${matchedNotes.size})", color = ThemePurple, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    }
                                    items(matchedNotes) { noteStr ->
                                        val parts = noteStr.split("|||")
                                        val title = parts[1]
                                        val body = parts[2]
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    viewModel.triggerKeypressEffects(context)
                                                    viewModel.recordOpenedItem(noteStr, "note", title, noteStr)
                                                    showSearchDialog = false
                                                    activeSection = "Notes"
                                                    viewNoteToShow = noteStr
                                                },
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031))
                                        ) {
                                            Column(modifier = Modifier.padding(10.dp)) {
                                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.Default.Article, contentDescription = null, tint = Color(0xFFFFD600), modifier = Modifier.size(14.dp))
                                                    Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                                }
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(body, color = TextMedium, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                            }
                                        }
                                    }
                                }
                                
                                if (matchedFiles.isNotEmpty()) {
                                    item {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Matched Media & Files (${matchedFiles.size})", color = ThemePurple, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    }
                                    items(matchedFiles) { fileStr ->
                                        val parts = fileStr.split("|||")
                                        val id = parts[0]
                                        val originalName = parts[2]
                                        val mime = parts[3]
                                        val sizeStr = parts[5]
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    viewModel.triggerKeypressEffects(context)
                                                    viewModel.recordOpenedItem(id, "file", originalName, fileStr)
                                                    showSearchDialog = false
                                                    if (mime.startsWith("image/") || mime.startsWith("video/")) {
                                                        activeSection = "Photos & Videos"
                                                    } else {
                                                        activeSection = "Documents"
                                                    }
                                                    selectedFileForDetails = fileStr
                                                },
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(10.dp),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = if (mime.startsWith("image/")) Icons.Default.Image else if (mime.startsWith("video/")) Icons.Default.PlayArrow else Icons.Default.Description,
                                                    contentDescription = null,
                                                    tint = if (mime.startsWith("image/")) Color(0xFF2979FF) else if (mime.startsWith("video/")) Color(0xFFFF9100) else Color(0xFF00E676),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(originalName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                                    Text(sizeStr, color = TextMedium, fontSize = 10.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = {
                        viewModel.triggerKeypressEffects(context)
                        showSearchDialog = false
                    }) {
                        Text("Close", color = TextMedium)
                    }
                }
            )
        }

        // 4. View Note Dialog
        if (viewNoteToShow != null) {
            val noteStr = viewNoteToShow!!
            val parts = noteStr.split("|||")
            if (parts.size == 3) {
                val timestamp = parts[0]
                val title = parts[1]
                val body = parts[2]
                
                AlertDialog(
                    onDismissRequest = { viewNoteToShow = null },
                    containerColor = BrandBg,
                    title = {
                        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
                            Text(text = body, fontSize = 13.sp, color = Color(0xFFE2E4E9), lineHeight = 18.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Created At: $timestamp", fontSize = 10.sp, color = TextMedium)
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.triggerKeypressEffects(context)
                                viewNoteToShow = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ThemePurple)
                        ) {
                            Text("OK", color = Color.White)
                        }
                    }
                )
            }
        }

        // Floating Action Button for active media/notes/documents sections!
        if (activeSection == "Notes" || activeSection == "Photos & Videos" || activeSection == "Documents" || activeSection == "Music & Audio") {
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
                                showMediaAddOptions = true
                            }
                            "Documents" -> {
                                showDocAddOptions = true
                            }
                            "Music & Audio" -> {
                                audioPickerLauncher.launch("audio/*")
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
                                "Photos & Videos" -> "Add Media"
                                "Documents" -> "Add Document"
                                else -> "Add Audio"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        if (showMediaAddOptions) {
            AlertDialog(
                onDismissRequest = { showMediaAddOptions = false },
                title = { Text("Secure Media", color = Color.White, fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Choose how you want to secure media into your vault:", color = TextMedium, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        // Option 1: Built-in Camera
                        Button(
                            onClick = {
                                showMediaAddOptions = false
                                activeCameraMode = "camera"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "Camera")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Use Built-in Camera", fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        // Option 2: Gallery import
                        OutlinedButton(
                            onClick = {
                                showMediaAddOptions = false
                                val intent = android.content.Intent(
                                    android.content.Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                ).apply {
                                    type = "image/* video/*"
                                    putExtra(android.content.Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
                                }
                                photoPickerLauncher.launch(intent)
                            },
                            border = BorderStroke(1.dp, ThemePurple),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = "Gallery", tint = ThemePurple)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Import from Gallery", fontWeight = FontWeight.Bold, color = ThemePurple)
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showMediaAddOptions = false }) {
                        Text("Cancel", color = TextMedium)
                    }
                },
                containerColor = Color(0xFF0F1322),
                shape = RoundedCornerShape(16.dp)
            )
        }

        if (showDocAddOptions) {
            AlertDialog(
                onDismissRequest = { showDocAddOptions = false },
                title = { Text("Secure Document", color = Color.White, fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Choose how you want to secure documents into your vault:", color = TextMedium, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        // Option 1: Document Scanner
                        Button(
                            onClick = {
                                showDocAddOptions = false
                                activeCameraMode = "scanner"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Icon(Icons.Default.DocumentScanner, contentDescription = "Scanner")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Use Document Scanner", fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        // Option 2: Document file import
                        OutlinedButton(
                            onClick = {
                                showDocAddOptions = false
                                documentPickerLauncher.launch("*/*")
                            },
                            border = BorderStroke(1.dp, ThemePurple),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Icon(Icons.Default.UploadFile, contentDescription = "Import", tint = ThemePurple)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Import Document File", fontWeight = FontWeight.Bold, color = ThemePurple)
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showDocAddOptions = false }) {
                        Text("Cancel", color = TextMedium)
                    }
                },
                containerColor = Color(0xFF0F1322),
                shape = RoundedCornerShape(16.dp)
            )
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



    if (pendingUnlockAction != null) {
        var folderPin by remember { mutableStateOf("") }
        var folderPinError by remember { mutableStateOf(false) }
        val folderName = pendingUnlockAction!!.first
        val action = pendingUnlockAction!!.second
        AlertDialog(
            onDismissRequest = { pendingUnlockAction = null },
            title = { Text("Unlock Folder", color = TextDark) },
            text = {
                Column {
                    Text("Enter vault passcode to access '$folderName'", color = TextMedium, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = folderPin,
                        onValueChange = { folderPin = it; folderPinError = false },
                        isError = folderPinError,
                        keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.NumberPassword),
                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextDark,
                            unfocusedTextColor = TextDark,
                            focusedBorderColor = ThemePurple,
                            cursorColor = ThemePurple
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (viewModel.verifyFolderPin(folderPin)) {
                            action()
                            pendingUnlockAction = null
                        } else {
                            folderPinError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple)
                ) {
                    Text("Unlock")
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingUnlockAction = null }) {
                    Text("Cancel", color = ThemePurple)
                }
            },
            containerColor = Color.White
        )
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

    when (activeCameraMode) {
        "camera" -> {
            SecureCameraView(
                viewModel = viewModel,
                onDismiss = { activeCameraMode = null }
            )
        }
        "scanner" -> {
            SecureScannerView(
                viewModel = viewModel,
                onDismiss = { activeCameraMode = null }
            )
        }
    }
}

@Composable
fun EmptyVaultSectionState(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
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

data class TabState(
    val id: String,
    val title: String = "New Tab",
    val url: String = "https://duckduckgo.com",
    val progress: Int = 0,
    val isLoading: Boolean = false,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false
)

fun createPrivateWebView(
    ctx: android.content.Context,
    tabId: String,
    initialUrl: String,
    onDownloadRequested: (url: String, userAgent: String, contentDisposition: String, mimeType: String, contentLength: Long) -> Unit,
    onUpdate: ((TabState) -> TabState) -> Unit
): android.webkit.WebView {
    return android.webkit.WebView(ctx).apply {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            android.webkit.WebView.setWebContentsDebuggingEnabled(
                (ctx.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0
            )
        }

        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = false
            savePassword = false
            saveFormData = false
            cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE
            useWideViewPort = true
            loadWithOverviewMode = true
            builtInZoomControls = true
            displayZoomControls = false
        }

        setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            onDownloadRequested(url, userAgent, contentDisposition, mimetype, contentLength)
        }

        val cookieManager = android.webkit.CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(this, false)

        webViewClient = object : android.webkit.WebViewClient() {
            override fun onPageStarted(view: android.webkit.WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                onUpdate { tab ->
                    tab.copy(
                        url = url ?: tab.url,
                        isLoading = true,
                        canGoBack = view?.canGoBack() ?: false,
                        canGoForward = view?.canGoForward() ?: false
                    )
                }
            }

            override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                super.onPageFinished(view, url)
                onUpdate { tab ->
                    tab.copy(
                        url = url ?: tab.url,
                        title = view?.title?.ifEmpty { null } ?: "Private Tab",
                        isLoading = false,
                        canGoBack = view?.canGoBack() ?: false,
                        canGoForward = view?.canGoForward() ?: false
                    )
                }
            }

            override fun shouldOverrideUrlLoading(view: android.webkit.WebView?, request: android.webkit.WebResourceRequest?): Boolean {
                return false
            }
        }

        webChromeClient = object : android.webkit.WebChromeClient() {
            override fun onProgressChanged(view: android.webkit.WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                onUpdate { tab ->
                    tab.copy(progress = newProgress)
                }
            }

            override fun onReceivedTitle(view: android.webkit.WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                onUpdate { tab ->
                    tab.copy(title = title ?: tab.title)
                }
            }
        }

        clearCache(true)
        clearHistory()
        loadUrl(initialUrl)
    }
}

fun clearAllBrowsingData(
    context: android.content.Context,
    tabs: androidx.compose.runtime.snapshots.SnapshotStateList<TabState>,
    webViews: MutableMap<String, android.webkit.WebView>
) {
    webViews.values.forEach { webView ->
        try {
            webView.stopLoading()
            webView.clearHistory()
            webView.clearCache(true)
            webView.loadUrl("about:blank")
            webView.destroy()
        } catch (e: Exception) {}
    }
    webViews.clear()
    tabs.clear()

    try {
        val cookieManager = android.webkit.CookieManager.getInstance()
        cookieManager.removeAllCookies(null)
        cookieManager.flush()
        android.webkit.WebStorage.getInstance().deleteAllData()
    } catch (e: Exception) {}

    try {
        if (!context.cacheDir.exists()) {
            context.cacheDir.mkdirs()
        }
        if (!context.codeCacheDir.exists()) {
            context.codeCacheDir.mkdirs()
        }

        fun deleteCacheContents(dir: java.io.File) {
            dir.listFiles()?.forEach { file ->
                if (!file.name.equals("WebView", ignoreCase = true) && 
                    !file.name.contains("webview", ignoreCase = true)) {
                    file.deleteRecursively()
                }
            }
        }

        deleteCacheContents(context.cacheDir)
        deleteCacheContents(context.codeCacheDir)
    } catch (e: Exception) {}
}

@Composable
fun PrivateBrowserSection(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onExit: () -> Unit = {},
    onPanic: () -> Unit = {}
) {
    val context = LocalContext.current
    val tabs = remember { mutableStateListOf<TabState>() }
    val webViews = remember { mutableStateMapOf<String, android.webkit.WebView>() }
    var activeTabId by remember { mutableStateOf<String?>(null) }
    var safeSearchOff by remember { mutableStateOf(true) }
    var showTabSwitcher by remember { mutableStateOf(false) }
    var showDownloadsDialog by remember { mutableStateOf(false) }

    val openNewTab: (String) -> Unit = { url ->
        val tabId = java.util.UUID.randomUUID().toString()
        val newTab = TabState(id = tabId, url = url, title = "New Tab")
        tabs.add(newTab)
        
        val webView = createPrivateWebView(
            ctx = context,
            tabId = tabId,
            initialUrl = url,
            onDownloadRequested = { downloadUrl, userAgent, contentDisposition, mimeType, contentLength ->
                viewModel.startVaultDownload(context, downloadUrl, userAgent, contentDisposition, mimeType, contentLength)
                showDownloadsDialog = true
            }
        ) { transform ->
            val index = tabs.indexOfFirst { it.id == tabId }
            if (index != -1) {
                tabs[index] = transform(tabs[index])
            }
        }
        webViews[tabId] = webView
        activeTabId = tabId
    }

    LaunchedEffect(Unit) {
        try {
            val cookieManager = android.webkit.CookieManager.getInstance()
            cookieManager.removeAllCookies(null)
            cookieManager.flush()
            android.webkit.WebStorage.getInstance().deleteAllData()
        } catch (e: Exception) {}
        if (tabs.isEmpty()) {
            openNewTab("https://duckduckgo.com")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            webViews.values.forEach { webView ->
                try {
                    webView.stopLoading()
                    webView.clearHistory()
                    webView.clearCache(true)
                    webView.loadUrl("about:blank")
                    webView.destroy()
                } catch (e: Exception) {}
            }
            webViews.clear()
            tabs.clear()
            
            try {
                val cookieManager = android.webkit.CookieManager.getInstance()
                cookieManager.removeAllCookies(null)
                cookieManager.flush()
                android.webkit.WebStorage.getInstance().deleteAllData()
            } catch (e: Exception) {}
        }
    }

    val activeTab = tabs.find { it.id == activeTabId }
    val activeWebView = webViews[activeTabId]

    androidx.activity.compose.BackHandler {
        if (showTabSwitcher) {
            showTabSwitcher = false
        } else if (activeWebView != null && activeWebView.canGoBack()) {
            activeWebView.goBack()
        } else {
            onExit()
        }
    }

    var urlInput by remember(activeTabId, activeTab?.url) {
        mutableStateOf(activeTab?.url ?: "https://duckduckgo.com")
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF030304))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0C0C10))
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { onExit() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Vault",
                            tint = Color.White
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Incognito Mode Active",
                                tint = ThemePurple,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Ghost Browser",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = "Zero-Trace Sandbox",
                            fontSize = 9.sp,
                            color = TextMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    IconButton(
                        onClick = {
                            clearAllBrowsingData(context, tabs, webViews)
                            openNewTab("https://duckduckgo.com")
                            android.widget.Toast.makeText(context, "All cookies and browsing cache wiped!", android.widget.Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color(0xFF1F1F26), RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear All Browsing Data",
                            tint = Color.Red.copy(alpha = 0.85f),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Button(
                        onClick = {
                            clearAllBrowsingData(context, tabs, webViews)
                            onPanic()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Panic Lock",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "PANIC",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0C0C10))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                IconButton(
                    onClick = { activeWebView?.goBack() },
                    enabled = activeTab?.canGoBack == true,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Web Back",
                        tint = if (activeTab?.canGoBack == true) ThemePurple else Color.Gray
                    )
                }

                IconButton(
                    onClick = { activeWebView?.goForward() },
                    enabled = activeTab?.canGoForward == true,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Web Forward",
                        tint = if (activeTab?.canGoForward == true) ThemePurple else Color.Gray
                    )
                }

                IconButton(
                    onClick = { activeWebView?.reload() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reload Webpage",
                        tint = ThemePurple
                    )
                }

                OutlinedTextField(
                    value = urlInput,
                    onValueChange = { urlInput = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 11.sp, color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF13131A),
                        unfocusedContainerColor = Color(0xFF13131A),
                        focusedBorderColor = ThemePurple,
                        unfocusedBorderColor = ThemeContainerBorder,
                        cursorColor = ThemePurple
                    ),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            var target = urlInput.trim()
                            if (target.isNotEmpty()) {
                                if (!target.startsWith("http://") && !target.startsWith("https://")) {
                                    if (target.contains(".") && !target.contains(" ")) {
                                        target = "https://$target"
                                    } else {
                                        val safeParam = if (safeSearchOff) "&kp=-2" else "&kp=1"
                                        target = "https://duckduckgo.com/?q=${java.net.URLEncoder.encode(target, "UTF-8")}$safeParam"
                                    }
                                }
                                activeWebView?.loadUrl(target)
                            }
                        }
                    )
                )

                IconButton(
                    onClick = {
                        safeSearchOff = !safeSearchOff
                        android.widget.Toast.makeText(
                            context,
                            if (safeSearchOff) "SafeSearch: OFF (Default)" else "SafeSearch: ON",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            if (safeSearchOff) Color(0xFFE65100).copy(alpha = 0.15f) else Color(0xFF2E7D32).copy(alpha = 0.15f),
                            RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Safe Search",
                        tint = if (safeSearchOff) Color(0xFFFF9800) else Color(0xFF4CAF50),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Secure Downloads Manager Badge Button
                val downloadsList by viewModel.downloads.collectAsState()
                val activeDownloadsCount = downloadsList.count { it.status == "Downloading" }
                
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF1F1F26), RoundedCornerShape(8.dp))
                        .clickable { showDownloadsDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = "Downloads Manager",
                        tint = if (activeDownloadsCount > 0) ThemePurple else Color.LightGray,
                        modifier = Modifier.size(18.dp)
                    )
                    
                    if (downloadsList.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 2.dp, y = (-2).dp)
                                .size(14.dp)
                                .background(
                                    if (activeDownloadsCount > 0) ThemePurple else Color(0xFF4CAF50),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = downloadsList.size.toString(),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { showTabSwitcher = true },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .border(1.5.dp, ThemePurple, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tabs.size.toString(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ThemePurple
                        )
                    }
                }
            }

            if (activeTab?.isLoading == true) {
                LinearProgressIndicator(
                    progress = { (activeTab.progress) / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = ThemePurple,
                    trackColor = Color.Transparent
                )
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                key(activeTabId) {
                    if (activeWebView != null) {
                        AndroidView(
                            factory = { activeWebView },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        if (showTabSwitcher) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .clickable { showTabSwitcher = false }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .clickable(enabled = false) {},
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F0F13)),
                    border = BorderStroke(1.dp, ThemeContainerBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Active Browser Tabs (${tabs.size})",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            IconButton(
                                onClick = { showTabSwitcher = false },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Done",
                                    tint = ThemePurple
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyColumn(
                            modifier = Modifier
                                .heightIn(max = 250.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(tabs) { tab ->
                                val isActive = tab.id == activeTabId
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            activeTabId = tab.id
                                            showTabSwitcher = false
                                        },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isActive) Color(0xFF230D35) else Color(0xFF1A1A22)
                                    ),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isActive) ThemePurple else ThemeContainerBorder
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = tab.title,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = tab.url,
                                                fontSize = 10.sp,
                                                color = TextMedium,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                val wv = webViews[tab.id]
                                                if (wv != null) {
                                                    try {
                                                        wv.stopLoading()
                                                        wv.clearHistory()
                                                        wv.clearCache(true)
                                                        wv.loadUrl("about:blank")
                                                        wv.destroy()
                                                    } catch (e: Exception) {}
                                                    webViews.remove(tab.id)
                                                }
                                                
                                                val tabIndex = tabs.indexOf(tab)
                                                tabs.remove(tab)

                                                if (activeTabId == tab.id) {
                                                    if (tabs.isNotEmpty()) {
                                                        activeTabId = tabs[tabIndex.coerceAtMost(tabs.size - 1)].id
                                                    } else {
                                                        openNewTab("https://duckduckgo.com")
                                                    }
                                                }
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.DeleteForever,
                                                contentDescription = "Close Tab",
                                                tint = Color.Red.copy(alpha = 0.7f),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    clearAllBrowsingData(context, tabs, webViews)
                                    openNewTab("https://duckduckgo.com")
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
                            ) {
                                Icon(Icons.Default.DeleteForever, contentDescription = "Close All Tabs", modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Close All Tabs", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            Button(
                                onClick = {
                                    openNewTab("https://duckduckgo.com")
                                    showTabSwitcher = false
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ThemePurple)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Open New Tab", modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("New Private Tab", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // --- SECURE DOWNLOADS MANAGER DIALOG ---
        if (showDownloadsDialog) {
            val downloadsList by viewModel.downloads.collectAsState()
            
            AlertDialog(
                onDismissRequest = { showDownloadsDialog = false },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileDownload,
                            contentDescription = "Downloads",
                            tint = ThemePurple
                        )
                        Text(
                            text = "Private Vault Downloads",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "All files are downloaded directly into your encrypted Vault. No trace is left in your device's public download directories.",
                            fontSize = 11.sp,
                            color = TextMedium
                        )
                        
                        if (downloadsList.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudQueue,
                                        contentDescription = "No downloads",
                                        tint = Color.Gray.copy(alpha = 0.5f),
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Text(
                                        text = "No downloads in this session",
                                        fontSize = 12.sp,
                                        color = TextMedium
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .heightIn(max = 280.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(downloadsList) { task ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF141419)),
                                        border = BorderStroke(1.dp, ThemeContainerBorder)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = task.filename,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                    Text(
                                                        text = "${task.sizeString} • ${task.status}",
                                                        fontSize = 10.sp,
                                                        color = if (task.status == "Completed") Color(0xFF4CAF50) else if (task.status == "Failed") Color.Red else TextMedium
                                                    )
                                                }
                                                
                                                IconButton(
                                                    onClick = { viewModel.removeDownload(task.id) },
                                                    modifier = Modifier.size(24.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = "Remove task",
                                                        tint = Color.Gray,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                            }
                                            
                                            if (task.status == "Downloading") {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    LinearProgressIndicator(
                                                        progress = { task.progress },
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .height(4.dp)
                                                            .clip(RoundedCornerShape(2.dp)),
                                                        color = ThemePurple,
                                                        trackColor = Color(0xFF22222A)
                                                    )
                                                    Text(
                                                        text = "${(task.progress * 100).toInt()}%",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = ThemePurple
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (downloadsList.isNotEmpty()) {
                            Button(
                                onClick = { viewModel.clearDownloads() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.15f), contentColor = Color.Red),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Clear All", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Button(
                            onClick = { showDownloadsDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Dismiss", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                },
                containerColor = Color(0xFF0C0C10),
                shape = RoundedCornerShape(16.dp)
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

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun CustomFolderChip(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    isLocked: Boolean = false,
    onDelete: (() -> Unit)? = null
) {
    Surface(
        color = if (selected) ThemePurple else Color(0xFF1B2031),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, if (selected) ThemePurple else ThemeContainerBorder.copy(alpha = 0.2f)),
        modifier = Modifier.combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (isLocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color(0xFFFFD600),
                    modifier = Modifier.size(11.dp)
                )
            }
            Text(
                text = label,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
            if (onDelete != null) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete",
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier
                        .size(14.dp)
                        .clickable { onDelete() }
                )
            }
        }
    }
}

@Composable
fun StorageCategoryItem(color: Color, label: String, sizeStr: String, count: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Column {
            Text(text = label, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Text(text = "$sizeStr ($count items)", fontSize = 9.sp, color = TextMedium)
        }
    }
}

