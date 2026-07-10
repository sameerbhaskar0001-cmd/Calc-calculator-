package com.example
import androidx.compose.material3.Text
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
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.activity.compose.BackHandler
import androidx.compose.ui.zIndex
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
import androidx.compose.foundation.gestures.*
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
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreHoriz
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.BottomSheetDefaults
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import androidx.compose.material3.TextButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import kotlinx.coroutines.delay
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
import androidx.compose.ui.input.pointer.*
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
import androidx.compose.ui.input.pointer.*
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

fun rememberBackStack(initial: String): androidx.compose.runtime.MutableState<String> {
    return object : androidx.compose.runtime.MutableState<String> {
        var backStack by androidx.compose.runtime.mutableStateOf(listOf(initial))
        override var value: String
            get() = backStack.last()
            set(v) {
                if (v == "Home") {
                    backStack = listOf("Home")
                } else if (v == "__BACK__") {
                    if (backStack.size > 1) {
                        backStack = backStack.dropLast(1)
                    }
                } else if (v != backStack.last()) {
                    backStack = backStack + v
                }
            }
        override fun component1() = value
        override fun component2(): (String) -> Unit = { value = it }
    }
}

enum class ActiveTab {
    CALCULATOR,
    VAULT
}
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf(ActiveTab.CALCULATOR) }
    var showThemeDialog by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val view = androidx.compose.ui.platform.LocalView.current
    var isImporting by remember { mutableStateOf(false) }
    var importProgress by remember { mutableStateOf(0f) }
    var importTotal by remember { mutableStateOf(0) }
    var importCurrent by remember { mutableStateOf(0) }
    var showImportSuccess by remember { mutableStateOf(false) }
    var importSuccessCount by remember { mutableStateOf(0) }
    val vaultUnlocked by viewModel.vaultUnlocked.collectAsState()
    val vaultFiles by viewModel.vaultFiles.collectAsState()
    val vaultNotes by viewModel.vaultNotes.collectAsState()
    // Switch to the private vault screen automatically when unlocked via passcode
    var transitionState by remember { mutableStateOf(0) } // 0=Calc, 1=Authenticating, 2=Transition, 3=Vault
    
    val blurRadius by animateDpAsState(
        targetValue = when (transitionState) {
            0 -> 0.dp
            1 -> 12.dp
            else -> 40.dp
        },
        animationSpec = tween(durationMillis = if (transitionState == 2) 300 else 150)
    )
    val calcAlpha by animateFloatAsState(
        targetValue = if (transitionState >= 2) 0f else 1f,
        animationSpec = tween(300)
    )
    val calcScale by animateFloatAsState(
        targetValue = if (transitionState >= 2) 0.95f else 1f,
        animationSpec = tween(300)
    )
    val vaultScale by animateFloatAsState(
        targetValue = if (transitionState >= 3) 1f else if (transitionState == 2) 0.95f else 0.9f,
        animationSpec = tween(450, easing = FastOutSlowInEasing)
    )
    val welcomeAlpha = remember { androidx.compose.animation.core.Animatable(1f) }
    
    val vaultAlpha by animateFloatAsState(
        targetValue = if (transitionState >= 3) 1f else 0f,
        animationSpec = tween(500)
    )
    
    val vaultBlurRadius by animateDpAsState(
        targetValue = if (transitionState >= 3) 0.dp else 24.dp,
        animationSpec = tween(800)
    )
    
    val authOverlayAlpha by animateFloatAsState(
        targetValue = if (transitionState == 1) 1f else 0f,
        animationSpec = tween(300)
    )
    LaunchedEffect(vaultUnlocked) {
        if (vaultUnlocked) {
            try {
                view.performHapticFeedback(android.view.HapticFeedbackConstants.CONFIRM)
            } catch (e: Exception) {
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
            }
            viewModel.triggerKeypressEffects(context)
            
            // Step A: Show authentication overlay
            transitionState = 1 
            delay(1000)         // Snappy 1 second overlay display
            
            // Step B: Initialize Welcome screen state at full visibility
            welcomeAlpha.snapTo(1f)
            transitionState = 2 
            delay(1200)         // Stay visible for 1.2 seconds so user can read everything clearly
            
            // Step C: Smoothly fade out and blur over 1.2 seconds (1200ms)
            welcomeAlpha.animateTo(
                targetValue = 0f,
                animationSpec = androidx.compose.animation.core.tween(
                    durationMillis = 1200,
                    easing = androidx.compose.animation.core.FastOutSlowInEasing
                )
            )
            
            // Step D: Clean switch to Dashboard after the animation completes
            activeTab = ActiveTab.VAULT
            transitionState = 3 
        } else {
            // Reset states when the vault is locked
            welcomeAlpha.snapTo(1f)
            transitionState = 0
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
        Box(modifier = Modifier.fillMaxSize()) {
            // Calculator Layout
            if (activeTab == ActiveTab.CALCULATOR || welcomeAlpha.value > 0f || authOverlayAlpha > 0f) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = calcScale
                            scaleY = calcScale
                            alpha = calcAlpha
                        }
                        .then(
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                                Modifier.blur(blurRadius)
                            } else {
                                Modifier
                            }
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header Bar
                    // Header Bar
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
                    // Main View Area
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp)
                        ) {
                            CalculatorTabContent(viewModel = viewModel)
                        }
                    }
                }
            }
            if (authOverlayAlpha > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(authOverlayAlpha),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier.size(100.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Concentric circles
                                    val localThemePurple = ThemePurple
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        drawCircle(
                                            color = localThemePurple.copy(alpha = 0.2f),
                                            radius = size.width / 2,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                                        )
                                        drawCircle(
                                            color = localThemePurple.copy(alpha = 0.4f),
                                            radius = size.width / 2.5f,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                        )
                                        // A spinning arc for "authenticating"
                                        drawArc(
                                            color = localThemePurple,
                                            startAngle = -90f,
                                            sweepAngle = 120f,
                                            useCenter = false,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round),
                                            size = androidx.compose.ui.geometry.Size(size.width / 1.25f, size.height / 1.25f),
                                            topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.1f, size.height * 0.1f)
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Authenticating",
                                        tint = ThemePurple,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = "Unlocking Secure Vault...",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                // 1. Dashboard Content: Keep it ready in background
                if (activeTab == ActiveTab.VAULT || transitionState >= 2) {
                    val dashboardAlpha = if (transitionState == 3) 1f else (1f - welcomeAlpha.value)
                    val dashboardScale = if (transitionState == 3) 1f else (0.96f + 0.04f * (1f - welcomeAlpha.value))
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                alpha = dashboardAlpha
                                scaleX = dashboardScale
                                scaleY = dashboardScale
                            }
                            .then(
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                                    Modifier.blur((welcomeAlpha.value * 24f).dp)
                                } else {
                                    Modifier
                                }
                            )
                    ) {
                        VaultTabUnlockedContent(
                            viewModel = viewModel,
                            onLockExit = { activeTab = ActiveTab.CALCULATOR }
                        )
                    }
                }

                // 2. Welcome Screen: Layered directly on top of the Dashboard, fades out smoothly
                if (transitionState == 2 || (transitionState == 3 && welcomeAlpha.value > 0f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                alpha = welcomeAlpha.value // GPU par smooth fade-out
                                scaleX = 1f + (1f - welcomeAlpha.value) * 0.04f
                                scaleY = 1f + (1f - welcomeAlpha.value) * 0.04f
                            }
                            .then(
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                                    Modifier.blur(( (1f - welcomeAlpha.value) * 16f ).dp) // Smooth progressive blur
                                } else {
                                    Modifier
                                }
                            )
                            .background(Color(0xFF0F121C)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Glowing Shield
                            Box(
                                modifier = Modifier
                                    .size(120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                val localThemePurple = ThemePurple
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    // Simple glow effect
                                    drawCircle(
                                        color = localThemePurple.copy(alpha = 0.15f),
                                        radius = size.width / 2,
                                        style = androidx.compose.ui.graphics.drawscope.Fill
                                    )
                                    drawCircle(
                                        color = localThemePurple.copy(alpha = 0.1f),
                                        radius = size.width / 1.5f,
                                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Private",
                                    tint = ThemePurple,
                                    modifier = Modifier.size(64.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Private",
                                    tint = Color(0xFF0F121C),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(32.dp))
                            
                            Text(
                                text = "Your Workspace",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Preparing your private space...",
                                fontSize = 16.sp,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Accent line
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(1.5.dp))
                                    .background(ThemePurple)
                            )
                            
                            Spacer(modifier = Modifier.height(64.dp))
                            
                            // Preview Grid
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.padding(horizontal = 32.dp)
                            ) {
                                VaultFolderCard(
                                    title = "Photos", 
                                    count = let { val c = vaultFiles.count { it.contains("|||image/") }; "$c ${if (c == 1) "Item" else "Items"}" }, 
                                    icon = Icons.Default.Image, 
                                    iconTint = ThemePurple,
                                    modifier = Modifier.weight(1f)
                                ) {}
                                VaultFolderCard(
                                    title = "Videos", 
                                    count = let { val c = vaultFiles.count { it.contains("|||video/") }; "$c ${if (c == 1) "Item" else "Items"}" }, 
                                    icon = Icons.Default.PlayArrow, 
                                    iconTint = ThemePurple,
                                    modifier = Modifier.weight(1f)
                                ) {}
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.padding(horizontal = 32.dp)
                            ) {
                                VaultFolderCard(
                                    title = "Documents", 
                                    count = let { val c = vaultFiles.count { val parts = it.split("|||"); parts.size >= 4 && !parts[3].startsWith("image/") && !parts[3].startsWith("video/") && !parts[3].startsWith("audio/") }; "$c ${if (c == 1) "Item" else "Items"}" }, 
                                    icon = Icons.Default.Description, 
                                    iconTint = ThemePurple,
                                    modifier = Modifier.weight(1f)
                                ) {}
                                VaultFolderCard(
                                    title = "Notes", 
                                    count = let { val c = vaultNotes.size; "$c ${if (c == 1) "Item" else "Items"}" }, 
                                    icon = Icons.Default.List, 
                                    iconTint = ThemePurple,
                                    modifier = Modifier.weight(1f)
                                ) {}
                            }
                        }
                    }
                }
        }
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
    var isImporting by remember { mutableStateOf(false) }
    var importProgress by remember { mutableStateOf(0f) }
    var importTotal by remember { mutableStateOf(0) }
    var importCurrent by remember { mutableStateOf(0) }
    var showImportSuccess by remember { mutableStateOf(false) }
    var importSuccessCount by remember { mutableStateOf(0) }
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
                UnifiedGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("conversion_banner_card"),
                    shape = RoundedCornerShape(24.dp),
                    bgColor = Color.Black.copy(alpha = 0.85f),
                    elevation = 2.dp
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
                                tint = if (ThemePurple == Color(0xFFFFFFFF)) BrandBg else Color.White,
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
    val haptic = LocalHapticFeedback.current
    val view = androidx.compose.ui.platform.LocalView.current
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
        UnifiedGlassCard(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .aspectRatio(1f)
                .graphicsLayer(scaleX = scale, scaleY = scale),
            bgColor = bgColor,
            elevation = if (!isUtility && !isEquals) 4.dp else 0.dp,
            glowAlpha = glowAlpha,
            onClick = {
                try {
                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                } catch (e: Exception) {
                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                }
                onClick()
            },
            interactionSource = interactionSource,
            shape = CircleShape
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
// OPTION 4: SECRET VAULT VIEW
// ==========================================
@Composable
fun FolderCard(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    UnifiedGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
        elevation = 4.dp,
        onClick = onClick
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
    UnifiedGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        shape = RoundedCornerShape(24.dp),
        bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
        elevation = 4.dp,
        onClick = onClick
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
        UnifiedGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
            elevation = 2.dp
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
    val haptic = LocalHapticFeedback.current
    val vaultUnlocked by viewModel.vaultUnlocked.collectAsState()
    val vaultFiles by viewModel.vaultFiles.collectAsState()
    val vaultNotes by viewModel.vaultNotes.collectAsState()
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var isImporting by remember { mutableStateOf(false) }
    var importProgress by remember { mutableStateOf(0f) }
    var importTotal by remember { mutableStateOf(0) }
    var importCurrent by remember { mutableStateOf(0) }
    var showImportSuccess by remember { mutableStateOf(false) }
    var importSuccessCount by remember { mutableStateOf(0) }
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
                .background(Color(0xFF090D1A)) // Force dark background for Secure Vault
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
                    .background(Color(0xFF161B2B).copy(alpha = 0.95f)).border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
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
                    text = "Enter your secret PIN to unlock your private space.",
                    fontSize = 12.sp,
                    color = TextMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                // Custom secure PIN Dots
                var shakeOffset by remember { mutableStateOf(0f) }
                
                LaunchedEffect(pinError) {
                    if (pinError) {
                        try {
                            // Two short vibrations
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as android.os.Vibrator
                                vibrator.vibrate(android.os.VibrationEffect.createWaveform(longArrayOf(0, 50, 100, 50), -1))
                            } else {
                                val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as android.os.Vibrator
                                vibrator.vibrate(longArrayOf(0, 50, 100, 50), -1)
                            }
                        } catch (e: Exception) {}
                        
                        androidx.compose.animation.core.animate(
                            initialValue = 0f,
                            targetValue = 0f,
                            animationSpec = androidx.compose.animation.core.keyframes {
                                durationMillis = 400
                                0f at 0
                                -20f at 50
                                20f at 100
                                -20f at 150
                                20f at 200
                                -10f at 250
                                10f at 300
                                0f at 400
                            }
                        ) { value, _ ->
                            shakeOffset = value
                        }
                        pinError = false
                        pinInput = ""
                    }
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.offset(x = shakeOffset.dp)
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
                        text = "Incorrect PIN! Hint: 7777",
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
                                val pinKeyInteractionSource = remember { MutableInteractionSource() }
                                val isPinKeyPressed by pinKeyInteractionSource.collectIsPressedAsState()
                                val pinKeyScale by animateFloatAsState(
                                    targetValue = if (isPinKeyPressed) 0.92f else 1.0f,
                                    animationSpec = androidx.compose.animation.core.spring(
                                        dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
                                        stiffness = androidx.compose.animation.core.Spring.StiffnessLow
                                    ),
                                    label = "pin_key_scale"
                                )
                                val baseColor = if (isSpecial) Color(0xFF635BFF).copy(alpha = 0.2f) else Color(0xFF1B2031)
                                val pressedColor = if (isSpecial) Color(0xFF635BFF).copy(alpha = 0.4f) else Color(0xFF242B42)
                                val buttonColor = if (isPinKeyPressed) pressedColor else baseColor
                                val contentColor = if (isSpecial) Color(0xFF8C84FF) else Color.White
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp)
                                        .graphicsLayer {
                                            scaleX = pinKeyScale
                                            scaleY = pinKeyScale
                                        }
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(buttonColor)
                                        .clickable(
                                            interactionSource = pinKeyInteractionSource,
                                            indication = null
                                        ) {
                                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
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
    var isEditingNote by remember { mutableStateOf(false) }
    var editedNoteTitle by remember { mutableStateOf("") }
    var editedNoteContent by remember { mutableStateOf("") }
    
    var pendingUnlockAction by remember { mutableStateOf<Pair<String, () -> Unit>?>(null) }
    var activeCameraMode by remember { mutableStateOf<String?>(null) } // null, "camera", "scanner"
    var showMediaAddOptions by remember { mutableStateOf(false) }
    var showDocAddOptions by remember { mutableStateOf(false) }
    var showChangePasscodeDialog by remember { mutableStateOf(false) }
    var realPasscodeInput by remember { mutableStateOf("") }
    var decoyPasscodeInput by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isImporting by remember { mutableStateOf(false) }
    var importProgress by remember { mutableStateOf(0f) }
    var importTotal by remember { mutableStateOf(0) }
    var importCurrent by remember { mutableStateOf(0) }
    var showImportSuccess by remember { mutableStateOf(false) }
    var importSuccessCount by remember { mutableStateOf(0) }
    val biometricEnabled by viewModel.biometricEnabled.collectAsState()
    val panicEnabled by viewModel.panicEnabled.collectAsState()
    val panicAction by viewModel.panicAction.collectAsState()
    val screenDownLock by viewModel.screenDownLock.collectAsState()
    val blurThumbnails by viewModel.blurThumbnails.collectAsState()
    val lockedFolders by viewModel.lockedFolders.collectAsState()
    val tempUnlockedFolders by viewModel.tempUnlockedFolders.collectAsState()
    val activity = context as? androidx.fragment.app.FragmentActivity
        // Photo/Video Picker launcher
        val coroutineScope = rememberCoroutineScope()
        val photoPickerLauncher = rememberLauncherForActivityResult(
            contract = androidx.activity.result.contract.ActivityResultContracts.GetMultipleContents(),
            onResult = { uris ->
                viewModel.isPickingFile = false
                if (uris.isNotEmpty()) {
                    isImporting = true
                    importTotal = uris.size
                    importCurrent = 0
                    coroutineScope.launch {
                        var successCount = 0
                        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                            for (uri in uris) {
                                val success = viewModel.addVaultFile(context, uri, skipDelete = true)
                                if (success) successCount++
                                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                    importCurrent++
                                    importProgress = importCurrent.toFloat() / importTotal
                                }
                            }
                        }
                        isImporting = false
                        if (successCount > 0) {
                            viewModel.batchDeleteOriginalFiles(context, uris)
                            importSuccessCount = successCount
                            showImportSuccess = true
                            kotlinx.coroutines.delay(2000)
                            showImportSuccess = false
                        }
                    }
                }
            }
        )
        // Document/General File Picker launcher
        val documentPickerLauncher = rememberLauncherForActivityResult(
            contract = androidx.activity.result.contract.ActivityResultContracts.GetMultipleContents(),
            onResult = { uris ->
                viewModel.isPickingFile = false
                if (uris.isNotEmpty()) {
                    isImporting = true
                    importTotal = uris.size
                    importCurrent = 0
                    coroutineScope.launch {
                        var successCount = 0
                        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                            for (uri in uris) {
                                val success = viewModel.addVaultFile(context, uri, skipDelete = true)
                                if (success) successCount++
                                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                    importCurrent++
                                    importProgress = importCurrent.toFloat() / importTotal
                                }
                            }
                        }
                        isImporting = false
                        if (successCount > 0) {
                            viewModel.batchDeleteOriginalFiles(context, uris)
                            importSuccessCount = successCount
                            showImportSuccess = true
                            kotlinx.coroutines.delay(2000)
                            showImportSuccess = false
                        }
                    }
                }
            }
        )
        // Audio File Picker launcher
        val audioPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                viewModel.isPickingFile = false
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
            viewModel.isPickingFile = false
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                android.util.Log.d("Vault", "User accepted/cancelled: User accepted")
                android.util.Log.d("Vault", "Delete success/failure: Delete success")
                android.widget.Toast.makeText(context, "Original photo hidden successfully!", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                android.util.Log.d("Vault", "User accepted/cancelled: User cancelled")
                android.util.Log.d("Vault", "Delete success/failure: Delete failure")
                android.widget.Toast.makeText(context, "File secured in vault, but original was not deleted from gallery.", android.widget.Toast.LENGTH_LONG).show()
            }
            viewModel.onOriginalFileDeleted(context)
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
        var activeSection by remember { rememberBackStack("Home") } // "Home", "Notes", "Photos & Videos", "Documents", "Explore", "Settings"
        var selectedFileForDetails by remember { mutableStateOf<String?>(null) }
        var activeDocumentToView by remember { mutableStateOf<String?>(null) }
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
        androidx.activity.compose.BackHandler(enabled = activeSection != "Private Browser") {
            when {
                activeCameraMode != null -> activeCameraMode = null
                showSearchDialog -> showSearchDialog = false
                showCreateFolderDialog -> showCreateFolderDialog = false
                showMoveToFolderDialog != null -> showMoveToFolderDialog = null
                activeViewerIndex >= 0 -> {
                    activeViewerIndex = -1
                    activeViewerFiles = emptyList()
                }
                viewNoteToShow != null -> viewNoteToShow = null
                activeDocumentToView != null -> activeDocumentToView = null
                selectedFileForDetails != null -> selectedFileForDetails = null
                textFileContentToRead != null -> textFileContentToRead = null
                activeSection != "Home" -> activeSection = "__BACK__"
                else -> {
                    viewModel.lockVault()
                    onLockExit()
                }
            }
        }
        LaunchedEffect(pendingDeleteSender) {
            pendingDeleteSender?.let { sender ->
                try {
                    viewModel.isPickingFile = true
                    android.util.Log.d("Vault", "IntentSender launched")
                    deleteSenderLauncher.launch(
                        androidx.activity.result.IntentSenderRequest.Builder(sender).build()
                    )
                } catch (e: Exception) {
                    viewModel.isPickingFile = false
                    android.util.Log.e("Vault", "Any exception with full stack trace", e)
                    viewModel.onOriginalFileDeleted(context)
                    viewModel.clearPendingDelete()
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF090D1A)) // Force dark background for Secure Vault
        ) {
            if (activeSection == "Private Browser") {
                PrivateBrowserSection(
                    modifier = Modifier.fillMaxSize(),
                    onExit = {
                        activeSection = "__BACK__"
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
                if (activeSection == "Home") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { 
                                viewModel.triggerKeypressEffects(context)
                                // Handle menu click
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            IconButton(
                                onClick = { 
                                    viewModel.triggerKeypressEffects(context)
                                    showSearchDialog = true
                                },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            IconButton(
                                onClick = { 
                                    viewModel.triggerKeypressEffects(context)
                                    activeSection = "More"
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(ThemePurple.copy(alpha = 0.2f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = ThemePurple,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    val currentHour = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("Asia/Kolkata")).get(java.util.Calendar.HOUR_OF_DAY)
                    val greeting = when (currentHour) {
                        in 5..11 -> "Good Morning 🌅"
                        in 12..16 -> "Good Afternoon ☀️"
                        in 17..19 -> "Good Evening 🌆"
                        else -> "Good Night 🌙"
                    }
                    
                    val totalVaultItems = vaultFiles.size + vaultNotes.size
                    val vaultStatusText = if (totalVaultItems == 0) {
                        "Ready to organize your private files"
                    } else {
                        "$totalVaultItems items stored privately"
                    }
                    val statusIcon = if (totalVaultItems == 0) Icons.Default.Check else Icons.Default.CheckCircle
                    val statusTint = if (totalVaultItems == 0) ThemePurple else ThemePurple
                    Column(modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 24.dp)) {
                        Text(
                            text = greeting,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(statusIcon, contentDescription = "Status", tint = statusTint, modifier = Modifier.size(16.dp))
                            Text(
                                text = vaultStatusText,
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }
                } else if (activeSection !in listOf("Photos", "Videos", "Documents", "Notes", "Music & Audio")) {
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
                                    activeSection = "__BACK__"
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF161B2B).copy(alpha = 0.95f)).border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
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
                                    text = viewModel.t(
                                        when(activeSection) {
                                            "Notes" -> "notes"
                                            "Photos & Videos" -> "photos_videos"
                                            "Documents" -> "documents"
                                            "Private Browser" -> "private_browser"
                                            "Explore" -> "explore"
                                            "More" -> "more"
                                            "Privacy Settings" -> "security_settings"
                                            "Decoy Space" -> "fake_vault"
                                            "Change PIN" -> "change_pin"
                                            "Export / Import" -> "export_import"
                                            "App Disguise" -> "app_disguise"
                                            "About" -> "about"
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
                                    text = "Private Workspace",
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
                                        .background(Color(0xFF161B2B).copy(alpha = 0.95f)).border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
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
                                    .background(Color(0xFF161B2B).copy(alpha = 0.95f)).border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Global Search",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
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
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Spacer(modifier = Modifier.height(4.dp))
                                             // Status Card
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(Brush.verticalGradient(listOf(Color(0xFF1E2640), Color(0xFF161B2B))))
                                    .border(1.dp, ThemePurple.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                                    .padding(20.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(ThemePurple.copy(alpha = 0.15f))
                                            .border(1.dp, ThemePurple.copy(alpha = 0.4f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Home, contentDescription = null, tint = ThemePurple, modifier = Modifier.size(28.dp))
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "PRIVATE WORKSPACE",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White.copy(alpha = 0.6f),
                                            letterSpacing = 1.sp
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "${vaultFiles.size + vaultNotes.size} items stored",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ThemePurple, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                            
                            Text(
                                text = "FILES & MEDIA",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.5f),
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            ) 
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                EnhancedVaultCard(
                                    title = "Photos", 
                                    count = let { val c = vaultFiles.count { it.contains("|||image/") }; "$c ${if (c == 1) "Item" else "Items"}" }, 
                                    icon = Icons.Default.Image,
                                    modifier = Modifier.weight(1f),
                                    previewContent = {
                                        val photos = vaultFiles.filter { it.contains("|||image/") }.take(3)
                                        if (photos.isEmpty()) {
                                            Row(modifier = Modifier.fillMaxSize().padding(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(4.dp)).background(Color(0xFF1B2236)))
                                                Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(4.dp)).background(Color(0xFF1B2236)))
                                                Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(4.dp)).background(Color(0xFF1B2236)))
                                            }
                                        } else {
                                            Row(modifier = Modifier.fillMaxSize().padding(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                photos.forEach { file ->
                                                    val path = file.split("|||").getOrNull(4) ?: ""
                                                    Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(4.dp)).background(Color(0xFF1B2236))) {
                                                        if (path.isNotEmpty()) {
                                                            coil.compose.AsyncImage(
                                                                model = java.io.File(path),
                                                                contentDescription = null,
                                                                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                                                modifier = Modifier.fillMaxSize()
                                                            )
                                                        }
                                                    }
                                                }
                                                // Fill remaining space if less than 3 photos
                                                repeat(3 - photos.size) {
                                                    Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(4.dp)).background(Color(0xFF1B2236)))
                                                }
                                            }
                                        }
                                    },
                                    onClick = { 
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Photos" 
                                    }
                                )
                                EnhancedVaultCard(
                                    title = "Videos", 
                                    count = let { val c = vaultFiles.count { it.contains("|||video/") }; "$c ${if (c == 1) "Item" else "Items"}" }, 
                                    icon = Icons.Default.PlayArrow,
                                    modifier = Modifier.weight(1f),
                                    previewContent = {
                                        val latestVideo = vaultFiles.firstOrNull { it.contains("|||video/") }
                                        if (latestVideo != null) {
                                            val path = latestVideo.split("|||").getOrNull(4) ?: ""
                                            Box(modifier = Modifier.fillMaxSize().padding(4.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFF1B2236)), contentAlignment = Alignment.Center) {
                                                if (path.isNotEmpty()) {
                                                    val bitmap = remember(path) {
                                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                                            try { android.media.ThumbnailUtils.createVideoThumbnail(java.io.File(path), android.util.Size(200, 200), null) } catch (e: Exception) { null }
                                                        } else {
                                                            @Suppress("DEPRECATION")
                                                            try { android.media.ThumbnailUtils.createVideoThumbnail(path, android.provider.MediaStore.Video.Thumbnails.MINI_KIND) } catch (e: Exception) { null }
                                                        }
                                                    }
                                                    if (bitmap != null) {
                                                        androidx.compose.foundation.Image(
                                                            bitmap = bitmap.asImageBitmap(),
                                                            contentDescription = null,
                                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                                            modifier = Modifier.fillMaxSize().alpha(0.6f)
                                                        )
                                                    }
                                                }
                                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(20.dp))
                                            }
                                        } else {
                                            Box(modifier = Modifier.fillMaxSize().padding(4.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFF1B2236)), contentAlignment = Alignment.Center) {
                                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White.copy(alpha = 0.3f), modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    },
                                    onClick = { 
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Videos" 
                                    }
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                EnhancedVaultCard(
                                    title = "Documents", 
                                    count = let { val c = vaultFiles.count { val parts = it.split("|||"); parts.size >= 4 && !parts[3].startsWith("image/") && !parts[3].startsWith("video/") && !parts[3].startsWith("audio/") }; "$c ${if (c == 1) "Item" else "Items"}" }, 
                                    icon = Icons.Default.Description,
                                    modifier = Modifier.weight(1f),
                                    previewContent = {
                                        val latestDoc = vaultFiles.firstOrNull { val parts = it.split("|||"); parts.size >= 4 && !parts[3].startsWith("image/") && !parts[3].startsWith("video/") && !parts[3].startsWith("audio/") }
                                        if (latestDoc != null) {
                                            val parts = latestDoc.split("|||")
                                            val title = if (parts.size >= 3) parts[2] else latestDoc.split("|||")[0].substringAfterLast('/')
                                            Row(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                val isPdf = latestDoc.lowercase().contains("pdf")
                                                Icon(if (isPdf) Icons.Default.PictureAsPdf else Icons.Default.Description, contentDescription = null, tint = ThemePurple, modifier = Modifier.size(16.dp))
                                                Text(cleanDisplayName(title, "document", parts.getOrNull(0) ?: ""), color = Color.White, fontSize = 10.sp, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                            }
                                        } else {
                                            Column(
                                                modifier = Modifier.fillMaxSize().padding(8.dp),
                                                verticalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Box(modifier = Modifier.fillMaxWidth(0.8f).height(4.dp).clip(CircleShape).background(Color(0xFF262D45)))
                                                Box(modifier = Modifier.fillMaxWidth(0.9f).height(4.dp).clip(CircleShape).background(Color(0xFF262D45)))
                                                Box(modifier = Modifier.fillMaxWidth(0.6f).height(4.dp).clip(CircleShape).background(Color(0xFF262D45)))
                                            }
                                        }
                                    },
                                    onClick = { 
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Documents" 
                                    }
                                )
                                EnhancedVaultCard(
                                    title = "Notes", 
                                    count = let { val c = vaultNotes.size; "$c ${if (c == 1) "Item" else "Items"}" }, 
                                    icon = Icons.Default.List,
                                    modifier = Modifier.weight(1f),
                                    previewContent = {
                                        val latestNote = vaultNotes.firstOrNull()
                                        if (latestNote != null) {
                                            val parts = latestNote.split("|||")
                                            val body = if (parts.size >= 4) parts[3] else ""
                                            Text(body, color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, maxLines = 2, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis, modifier = Modifier.padding(8.dp).fillMaxWidth(), lineHeight = 12.sp)
                                        } else {
                                            Column(
                                                modifier = Modifier.fillMaxSize().padding(8.dp),
                                                verticalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Box(modifier = Modifier.fillMaxWidth(0.5f).height(6.dp).clip(CircleShape).background(ThemePurple.copy(alpha = 0.4f)))
                                                Box(modifier = Modifier.fillMaxWidth(0.9f).height(4.dp).clip(CircleShape).background(Color(0xFF262D45)))
                                                Box(modifier = Modifier.fillMaxWidth(0.8f).height(4.dp).clip(CircleShape).background(Color(0xFF262D45)))
                                            }
                                        }
                                    },
                                    onClick = { 
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Notes" 
                                    }
                                )
                            }
                            
                            // Recent Activity
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "RECENT ACTIVITY",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.5f),
                                    letterSpacing = 2.sp
                                )
                                Text(
                                    text = "View All",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ThemePurple,
                                    modifier = Modifier.clickable { activeSection = "Explore" }
                                )
                            }
                            
                            if (vaultFiles.isEmpty() && vaultNotes.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Your secrets are safe here.", color = Color.White.copy(alpha = 0.4f), fontSize = 14.sp)
                                }
                            } else {
                                val recentFiles = vaultFiles.take(5)
                                recentFiles.forEach { file ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color(0xFF161B2B).copy(alpha = 0.5f))
                                            .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(16.dp))
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        val parts = file.split("|||")
                                        val isMedia = parts.size >= 4 && (parts[3].startsWith("image/") || parts[3].startsWith("video/")) || file.lowercase().endsWith(".jpg") || file.lowercase().endsWith(".png") || file.lowercase().endsWith(".mp4")
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(ThemePurple.copy(alpha = 0.1f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isMedia && parts.size >= 5) {
                                                val ctx = androidx.compose.ui.platform.LocalContext.current
                                                val imageLoader = remember(ctx) {
                                                    coil.ImageLoader.Builder(ctx)
                                                        .components { add(coil.decode.VideoFrameDecoder.Factory()) }
                                                        .build()
                                                }
                                                AsyncImage(
                                                    model = coil.request.ImageRequest.Builder(ctx)
                                                        .data(java.io.File(parts[4]))
                                                        .crossfade(true)
                                                        .build(),
                                                    imageLoader = imageLoader,
                                                    contentDescription = null,
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                                )
                                            } else {
                                                Icon(
                                                    imageVector = if (isMedia) Icons.Default.Image else Icons.Default.Description,
                                                    contentDescription = null,
                                                    tint = ThemePurple
                                                )
                                            }
                                        }
                                        Column(modifier = Modifier.weight(1f)) {
                                            val title = if (parts.size >= 3) parts[2] else file.split("|||")[0].substringAfterLast('/')
                                            val cleanTitle = cleanDisplayName(title)
                                            Text(cleanTitle, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                            val timestamp = parts.getOrNull(0)?.toLongOrNull() ?: 0L
                                            val timeStr = if (timestamp > 0) {
                                                val diff = System.currentTimeMillis() - timestamp
                                                when {
                                                    diff < 60_000 -> "Just now"
                                                    diff < 3600_000 -> "${diff / 60_000} min ago"
                                                    diff < 86400_000 && android.text.format.DateUtils.isToday(timestamp) -> "${diff / 3600_000} ${if (diff / 3600_000 == 1L) "hour" else "hours"} ago"
                                                    android.text.format.DateUtils.isToday(timestamp) -> "Today"
                                                    android.text.format.DateUtils.isToday(timestamp + 86400_000) -> "Yesterday"
                                                    else -> java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date(timestamp))
                                                }
                                            } else ""
                                            Text(timeStr, color = Color.White.copy(alpha = 0.4f), fontSize = 12.sp)
                                        }
                                        Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.size(20.dp))
                                    }
                                }
                            }
                                
                            // Recycle Bin Quick Access
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF161B2B).copy(alpha = 0.5f))
                                    .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(16.dp))
                                    .clickable { activeSection = "Recently Deleted" }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFEF5350).copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Recycle Bin",
                                        tint = Color(0xFFEF5350),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Recycle Bin", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                    val recentlyDeleted by viewModel.recentlyDeletedFiles.collectAsState()
                                    Text(
                                        text = if (recentlyDeleted.isEmpty()) "Empty" else "${recentlyDeleted.size} items", 
                                        color = Color.White.copy(alpha = 0.4f), 
                                        fontSize = 12.sp
                                    )
                                }
                                Icon(Icons.Default.ArrowForward, contentDescription = "Open", tint = Color.White.copy(alpha = 0.3f), modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                "Photos", "Videos", "Documents", "Notes", "Music & Audio" -> {
                    val vaultFiles by viewModel.vaultFiles.collectAsState()
                    val favoriteFiles by viewModel.favoriteFiles.collectAsState()
                    val fileFolders by viewModel.fileFolders.collectAsState()
                    val vaultNotes by viewModel.vaultNotes.collectAsState()
                    val favoriteNotes by viewModel.favoriteNotes.collectAsState()
                    val noteFolders by viewModel.noteFolders.collectAsState()
                    val vaultFolders by viewModel.vaultFolders.collectAsState()
                    val lockedFolders by viewModel.lockedFolders.collectAsState()
                    val tempUnlockedFolders by viewModel.tempUnlockedFolders.collectAsState()
                    val items = remember(activeSection, vaultFiles, vaultNotes, favoriteFiles, favoriteNotes, fileFolders, noteFolders) {
                        when (activeSection) {
                            "Photos" -> vaultFiles.filter { it.contains("|||image/") }.map { fileStr ->
                                val parts = fileStr.split("|||")
                                val duration = if (parts.size >= 7) parts[6].toLongOrNull() ?: 0L else 0L
                                VaultItemData(parts[0], parts[0].toLongOrNull() ?: 0L, parts[2], favoriteFiles.contains(parts[0]), fileFolders[parts[0]] ?: "", "image", parts[4], fileStr, duration)
                            }
                            "Videos" -> vaultFiles.filter { it.contains("|||video/") }.map { fileStr ->
                                val parts = fileStr.split("|||")
                                val duration = if (parts.size >= 7) parts[6].toLongOrNull() ?: 0L else 0L
                                VaultItemData(parts[0], parts[0].toLongOrNull() ?: 0L, parts[2], favoriteFiles.contains(parts[0]), fileFolders[parts[0]] ?: "", "video", parts[4], fileStr, duration)
                            }
                            "Documents" -> vaultFiles.filter { 
                                val parts = it.split("|||")
                                val isMedia = parts.size >= 4 && (parts[3].startsWith("image/") || parts[3].startsWith("video/")) || it.lowercase().endsWith(".jpg") || it.lowercase().endsWith(".png") || it.lowercase().endsWith(".mp4") || it.lowercase().endsWith(".jpeg") || it.lowercase().endsWith(".webp")
                                parts.size >= 4 && !isMedia && !parts[3].startsWith("audio/")
                            }.map { fileStr ->
                                val parts = fileStr.split("|||")
                                VaultItemData(parts[0], parts[0].toLongOrNull() ?: 0L, parts[2], favoriteFiles.contains(parts[0]), fileFolders[parts[0]] ?: "", "document", parts[4], fileStr, 0L)
                            }
                            "Music & Audio" -> vaultFiles.filter { it.contains("|||audio/") }.map { fileStr ->
                                val parts = fileStr.split("|||")
                                VaultItemData(parts[0], parts[0].toLongOrNull() ?: 0L, parts[2], favoriteFiles.contains(parts[0]), fileFolders[parts[0]] ?: "", "audio", parts[4], fileStr, 0L)
                            }
                            "Notes" -> vaultNotes.map { noteStr ->
                                val parts = noteStr.split("|||")
                                val title = if (parts.size > 1) parts[1] else "Note"
                                val dateStr = parts[0]
                                val timeMs = try {
                                    java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault()).parse(dateStr)?.time ?: 0L
                                } catch (e: Exception) { 0L }
                                VaultItemData(dateStr, timeMs, title, favoriteNotes.contains(noteStr), noteFolders[noteStr] ?: "", "note", "", noteStr, 0L)
                            }
                            else -> emptyList()
                        }
                    }
                    VaultContentScreen(
                        viewModel = viewModel,
                        context = context,
                        title = activeSection,
                        emptyIcon = when (activeSection) {
                            "Photos" -> Icons.Default.Image
                            "Videos" -> Icons.Default.PlayArrow
                            "Documents" -> Icons.Default.Description
                            "Notes" -> Icons.Default.Edit
                            else -> Icons.Default.MusicNote
                        },
                        emptyTitle = "No ${activeSection} found",
                        emptySubtitle = "Tap the button below to secure your first item.",
                        addLabel = when (activeSection) {
                            "Photos" -> "Add Photo"
                            "Videos" -> "Add Video"
                            "Documents" -> "Add Document"
                            "Notes" -> "Add Note"
                            else -> "Add Audio"
                        },
                        items = items,
                        folders = vaultFolders.toList(),
                        lockedFolders = lockedFolders,
                        tempUnlockedFolders = tempUnlockedFolders,
                        onNavigateBack = { activeSection = "__BACK__" },
                        onAddClick = { 
                            when (activeSection) {
                                "Notes" -> showAddNoteDialog = true
                                "Photos", "Videos" -> showMediaAddOptions = true
                                "Documents" -> showDocAddOptions = true
                                "Music & Audio" -> {
                                    viewModel.isPickingFile = true
                                    audioPickerLauncher.launch("audio/*")
                                }
                            }
                        },
                        onItemClick = { item, index, allFiltered -> 
                            val isMedia = item.type in listOf("image", "video") || item.rawString.lowercase().endsWith(".jpg") || item.rawString.lowercase().endsWith(".png") || item.rawString.lowercase().endsWith(".mp4") || item.rawString.lowercase().endsWith(".jpeg") || item.rawString.lowercase().endsWith(".webp")
                            if (isMedia) {
                                activeViewerFiles = allFiltered.map { it.rawString }
                                activeViewerIndex = index
                            } else if (item.type == "note") {
                                viewNoteToShow = item.rawString
                            } else if (item.type == "document") {
                                viewModel.recordOpenedItem(item.id, "file", item.title, item.rawString)
                                activeDocumentToView = item.rawString
                            } else {
                                viewModel.recordOpenedItem(item.id, "file", item.title, item.rawString)
                                selectedFileForDetails = item.rawString
                            }
                        },
                        onCreateFolder = { viewModel.addFolder(it) },
                        onDeleteItems = { rawStrings -> 
                            if (activeSection == "Notes") {
                                rawStrings.forEach { viewModel.deleteVaultNote(it) }
                            } else {
                                rawStrings.forEach { viewModel.deleteVaultFile(it) }
                            }
                        },
                        onMoveItems = { rawStrings, folder -> 
                            if (activeSection == "Notes") {
                                rawStrings.forEach { viewModel.setFolderForNote(it, folder) }
                            } else {
                                rawStrings.forEach { viewModel.setFolderForFile(it, folder) }
                            }
                        },
                        onToggleFavorite = { rawString -> 
                            if (activeSection == "Notes") {
                                viewModel.toggleFavoriteNote(rawString)
                            } else {
                                viewModel.toggleFavoriteFile(rawString.split("|||")[0])
                            }
                        }
                    )
                }
                "Private Browser" -> {
                    PrivateBrowserSection(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }
                "App Privacy" -> {
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
                                        activeSection = "Access Logs"
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
                                    title = "App Privacy",
                                    subtitle = "Lock apps",
                                    icon = Icons.Default.Lock,
                                    bgColor = Color(0xFF7C4DFF),
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "App Privacy"
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
                                        activeSection = "More"
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
                                        activeSection = "More"
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
                                                        text = cleanDisplayName(originalName),
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
                                "More" -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SettingsGroup(title = "SECURITY") {
                            SettingsActionRow(
                                title = "App Privacy",
                                subtitle = "Lock apps with Calculator PIN",
                                icon = Icons.Default.Lock,
                                iconTint = Color(0xFF2979FF),
                                onClick = { activeSection = "App Privacy" }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsActionRow(
                                title = "App Disguise",
                                subtitle = "Camouflage app icon",
                                icon = Icons.Default.Palette,
                                iconTint = Color(0xFF00B0FF),
                                onClick = { activeSection = "App Disguise" }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsActionRow(
                                title = "Access Logs",
                                subtitle = "Monitor unauthorized access",
                                icon = Icons.Default.Warning,
                                iconTint = Color(0xFFFF9100),
                                onClick = { activeSection = "Access Logs" }
                            )
                        }
                        SettingsGroup(title = "WORKSPACE SETTINGS") {
                            SettingsActionRow(
                                title = "Privacy Settings",
                                subtitle = "Privacy & discrete options",
                                icon = Icons.Default.Settings,
                                iconTint = Color(0xFFE57373),
                                onClick = { activeSection = "Privacy Settings" }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsActionRow(
                                title = "Decoy Space",
                                subtitle = "Create a decoy space",
                                icon = Icons.Default.Folder,
                                iconTint = Color(0xFFAB47BC),
                                onClick = { activeSection = "Decoy Space" }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsActionRow(
                                title = "Change PIN",
                                subtitle = "Update your workspace PIN",
                                icon = Icons.Default.Calculate,
                                iconTint = Color(0xFF26C6DA),
                                onClick = { activeSection = "Change PIN" }
                            )
                        }
                        
                        SettingsGroup(title = "DATA & ABOUT") {
                            SettingsActionRow(
                                title = "Export / Import",
                                subtitle = "Backup or restore data",
                                icon = Icons.Default.SwapVert,
                                iconTint = Color(0xFFD4E157),
                                onClick = { activeSection = "Export / Import" }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            SettingsActionRow(
                                title = "About",
                                subtitle = "App version & info",
                                icon = Icons.Default.Article,
                                iconTint = Color(0xFF8D6E63),
                                onClick = { activeSection = "About" }
                            )
                        }
                    }
                }
                "Privacy Settings" -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
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
                                title = "Access Logs",
                                subtitle = "Log incorrect PIN attempts & keys entered",
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
                        // GENERAL PREFERENCES
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
                "Change PIN" -> {
                    LaunchedEffect(Unit) {
                        realPasscodeInput = viewModel.getVaultPin()
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
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
                                        text = "WORKSPACE PIN",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2979FF)
                                    )
                                }
                                
                                Text(
                                    text = "Configure your secret numerical PIN. Entering this unlocks your private workspace.",
                                    fontSize = 11.sp,
                                    color = TextMedium,
                                    lineHeight = 15.sp
                                )
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
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
                                Button(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        if (realPasscodeInput.isBlank()) {
                                            android.widget.Toast.makeText(context, "PIN cannot be empty!", android.widget.Toast.LENGTH_SHORT).show()
                                        } else {
                                            viewModel.setVaultPin(realPasscodeInput)
                                            android.widget.Toast.makeText(context, "PIN updated successfully!", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Save PIN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
                "Decoy Space" -> {
                    LaunchedEffect(Unit) {
                        decoyPasscodeInput = viewModel.getDecoyPin()
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
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
                                    Icon(Icons.Default.Folder, contentDescription = "Decoy Space", tint = Color(0xFFAB47BC), modifier = Modifier.size(22.dp))
                                    Text(
                                        text = "DECOY PIN",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFAB47BC)
                                    )
                                }
                                
                                Text(
                                    text = "Entering your decoy PIN opens an empty guest workspace.",
                                    fontSize = 11.sp,
                                    color = TextMedium,
                                    lineHeight = 15.sp
                                )
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
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
                                        if (decoyPasscodeInput.isBlank()) {
                                            android.widget.Toast.makeText(context, "PIN cannot be empty!", android.widget.Toast.LENGTH_SHORT).show()
                                        } else {
                                            viewModel.setDecoyPin(decoyPasscodeInput)
                                            android.widget.Toast.makeText(context, "Decoy PIN updated successfully!", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Save Decoy PIN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
                "App Disguise" -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Icon(Icons.Default.Palette, contentDescription = "App Disguise", tint = Color(0xFF00B0FF), modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Camouflage Icon", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Change how this app looks on your phone's home screen. Disguise it as a Weather, Notes, or Clock app so no one suspects it's a secure vault.", color = TextMedium, fontSize = 13.sp)
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                Text("CHOOSE AN ICON", color = Color.White.copy(alpha=0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { android.widget.Toast.makeText(context, "Icon changed to Calculator", android.widget.Toast.LENGTH_SHORT).show() }) {
                                        Box(modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF263238)), contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.Calculate, contentDescription = "Calculator", tint = Color.White, modifier = Modifier.size(32.dp))
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Calculator", color = Color.White, fontSize = 12.sp)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { android.widget.Toast.makeText(context, "Icon changed to Notes", android.widget.Toast.LENGTH_SHORT).show() }) {
                                        Box(modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFFFB300)), contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.Article, contentDescription = "Notes", tint = Color.White, modifier = Modifier.size(32.dp))
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Notes", color = Color.White, fontSize = 12.sp)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { android.widget.Toast.makeText(context, "Icon changed to Weather", android.widget.Toast.LENGTH_SHORT).show() }) {
                                        Box(modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF03A9F4)), contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.CloudQueue, contentDescription = "Weather", tint = Color.White, modifier = Modifier.size(32.dp))
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Weather", color = Color.White, fontSize = 12.sp)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { android.widget.Toast.makeText(context, "Icon changed to Clock", android.widget.Toast.LENGTH_SHORT).show() }) {
                                        Box(modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF8E24AA)), contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.Timer, contentDescription = "Clock", tint = Color.White, modifier = Modifier.size(32.dp))
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Clock", color = Color.White, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
                "Export / Import" -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.SwapVert, contentDescription = "Export/Import", tint = Color(0xFFD4E157), modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Backup & Restore",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Backup all your vault contents to an encrypted archive, or restore a previous backup.",
                            color = TextMedium,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { android.widget.Toast.makeText(context, "Exporting...", android.widget.Toast.LENGTH_SHORT).show() },
                            colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            Text("Create Backup", color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { android.widget.Toast.makeText(context, "Importing...", android.widget.Toast.LENGTH_SHORT).show() },
                            modifier = Modifier.fillMaxWidth(0.8f),
                            border = BorderStroke(1.dp, ThemePurple),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ThemePurple)
                        ) {
                            Text("Restore Backup")
                        }
                    }
                }
                "About" -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Star, contentDescription = "App Logo", tint = ThemePurple, modifier = Modifier.size(80.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Calculator Vault Pro",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Version 1.0.0",
                            color = TextMedium,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Your privacy is our priority. All files are encrypted and stored locally on your device.",
                            color = TextMedium,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
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
                                Text(text = "Name: ${cleanDisplayName(originalName)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextDark, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
        AnimatedVisibility(
            visible = activeViewerFiles.isNotEmpty() && activeViewerIndex >= 0 && activeViewerIndex < activeViewerFiles.size,
            enter = scaleIn(initialScale = 0.8f, animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f)) + fadeIn(animationSpec = tween(250)),
            exit = scaleOut(targetScale = 0.8f, animationSpec = tween(250)) + fadeOut(animationSpec = tween(250)),
            modifier = Modifier.fillMaxSize().zIndex(100f)
        ) {
            key(activeViewerFiles) {
                val favoriteFiles by viewModel.favoriteFiles.collectAsState()
                val pagerState = rememberPagerState(
                    initialPage = activeViewerIndex.coerceIn(0, maxOf(0, activeViewerFiles.size - 1)),
                    pageCount = { activeViewerFiles.size }
                )
                // Sync activeViewerIndex with pagerState.currentPage
                LaunchedEffect(pagerState.currentPage) {
                    if (pagerState.currentPage < activeViewerFiles.size) {
                        activeViewerIndex = pagerState.currentPage
                    }
                }
                
                var currentScale by remember { mutableStateOf(1f) }
                
                Box(modifier = Modifier.fillMaxSize()) {
                    var isViewerUiVisible by remember { mutableStateOf(true) }
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
                            .background(Color.Black)
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = { isViewerUiVisible = !isViewerUiVisible })
                            }
                    ) {
                        // HorizontalPager for left/right swipe
                        HorizontalPager(
                            state = pagerState,
                            userScrollEnabled = currentScale <= 1.01f,
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
                                    val isImage = mimeType.startsWith("image/") || originalName.lowercase().endsWith(".jpg") || originalName.lowercase().endsWith(".png") || originalName.lowercase().endsWith(".jpeg") || originalName.lowercase().endsWith(".webp")
                                    if (isImage) {
                                        // Dynamic Pinch-to-Zoom
                                        var scale by remember { mutableStateOf(1f) }
                                        LaunchedEffect(scale, pagerState.currentPage) {
                                            if (pagerState.currentPage == page) {
                                                currentScale = scale
                                            }
                                        }
                                        var offset by remember { mutableStateOf(Offset.Zero) }
                                        AsyncImage(
                                            model = java.io.File(path),
                                            contentDescription = cleanDisplayName(originalName),
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .pointerInput(Unit) {
                                                    detectTapGestures(
                                                        onDoubleTap = {
                                                            scale = if (scale > 1f) 1f else 2.5f
                                                            offset = Offset.Zero
                                                        },
                                                        onTap = {
                                                            isViewerUiVisible = !isViewerUiVisible
                                                        }
                                                    )
                                                }
                                                .pointerInput(Unit) {
                                                    awaitEachGesture {
                                                        awaitFirstDown(requireUnconsumed = false)
                                                        do {
                                                            val event = awaitPointerEvent()
                                                            val zoom = event.calculateZoom()
                                                            val pan = event.calculatePan()
                                                            
                                                            val isMultiTouch = event.changes.size > 1
                                                            val isZoomedIn = scale > 1f
                                                            
                                                            if (zoom != 1f && isMultiTouch) {
                                                                scale = (scale * zoom).coerceIn(1f, 5f)
                                                            }
                                                            
                                                            if (isZoomedIn || isMultiTouch) {
                                                                if (pan != Offset.Zero) {
                                                                    offset += pan
                                                                }
                                                                if (scale <= 1f) {
                                                                    offset = Offset.Zero
                                                                }
                                                                event.changes.forEach {
                                                                    if (it.positionChanged()) {
                                                                        it.consume()
                                                                    }
                                                                }
                                                            }
                                                        } while (event.changes.any { it.pressed })
                                                    }
                                                }
                                                .graphicsLayer(
                                                    scaleX = scale,
                                                    scaleY = scale,
                                                    translationX = offset.x,
                                                    translationY = offset.y
                                                ),
                                            contentScale = androidx.compose.ui.layout.ContentScale.Fit
                                        )
                                    } else if (mimeType.startsWith("video/") || originalName.lowercase().endsWith(".mp4")) {
                                        // Video Player
                                        var playbackPosition by androidx.compose.runtime.saveable.rememberSaveable(path) { androidx.compose.runtime.mutableIntStateOf(0) }
                                        var videoViewRef by remember { mutableStateOf<android.widget.VideoView?>(null) }
                                        
                                        LaunchedEffect(videoViewRef) {
                                            while (true) {
                                                kotlinx.coroutines.delay(1000)
                                                videoViewRef?.let {
                                                    if (it.isPlaying) {
                                                        playbackPosition = it.currentPosition
                                                    }
                                                }
                                            }
                                        }

                                        AndroidView(
                                            factory = { ctx ->
                                                android.widget.VideoView(ctx).apply {
                                                    setVideoPath(path)
                                                    val mediaController = android.widget.MediaController(ctx)
                                                    mediaController.setAnchorView(this)
                                                    setMediaController(mediaController)
                                                    setOnPreparedListener { mp ->
                                                        mp.isLooping = true
                                                        seekTo(playbackPosition)
                                                        start()
                                                    }
                                                    videoViewRef = this
                                                }
                                            },
                                            modifier = Modifier.fillMaxSize(),
                                            onRelease = {
                                                playbackPosition = it.currentPosition
                                                videoViewRef = null
                                            }
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
                                                text = cleanDisplayName(originalName),
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
                                            Text(cleanDisplayName(originalName), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                            Text("$sizeStr • $mimeType", color = TextMedium, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                        // Top Action Bar overlaying everything
                        val activeFile = activeViewerFiles.getOrNull(activeViewerIndex)
                        val activeParts = activeFile?.split("|||") ?: emptyList()
                        if (activeParts.size >= 6) {
                            val activeId = activeParts[0]
                            val activeName = activeParts[2]
                            val activeIsFav = favoriteFiles.contains(activeId)
                            AnimatedVisibility(
                                visible = isViewerUiVisible,
                                enter = fadeIn(),
                                exit = fadeOut(),
                                modifier = Modifier.align(Alignment.TopCenter)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Black.copy(alpha = 0.6f))
                                        .statusBarsPadding()
                                        .padding(horizontal = 8.dp, vertical = 12.dp),
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
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back",
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
                                            fileSerialized = activeFile!!,
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
                                        viewModel.deleteVaultFile(activeFile!!)
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
                        Text("Create", color = if (ThemePurple == Color(0xFFFFFFFF)) BrandBg else Color.White)
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
                                                    Text(cleanDisplayName(originalName), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
                
                LaunchedEffect(viewNoteToShow) {
                    editedNoteTitle = title
                    editedNoteContent = body
                    isEditingNote = false
                }
                
                AlertDialog(
                    onDismissRequest = { 
                        viewNoteToShow = null
                        isEditingNote = false
                    },
                    containerColor = BrandBg,
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(28.dp)),
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isEditingNote) {
                                Text(
                                    text = "Edit Note", 
                                    fontWeight = FontWeight.ExtraBold, 
                                    fontSize = 18.sp, 
                                    color = Color.White
                                )
                            } else {
                                Text(
                                    text = title, 
                                    fontWeight = FontWeight.ExtraBold, 
                                    fontSize = 18.sp, 
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            viewModel.triggerKeypressEffects(context)
                                            isEditingNote = true
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit note",
                                            tint = ThemePurple,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    val isFav = viewModel.favoriteNotes.collectAsState().value.contains(noteStr)
                                    IconButton(
                                        onClick = {
                                            viewModel.triggerKeypressEffects(context)
                                            viewModel.toggleFavoriteNote(noteStr)
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isFav) Icons.Default.Star else Icons.Default.StarBorder,
                                            contentDescription = "Toggle favorite",
                                            tint = if (isFav) Color(0xFFFFD600) else Color.White.copy(alpha = 0.6f),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    val isPinned = viewModel.pinnedNotes.collectAsState().value.contains(noteStr)
                                    IconButton(
                                        onClick = {
                                            viewModel.triggerKeypressEffects(context)
                                            viewModel.togglePinnedNote(noteStr)
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PushPin,
                                            contentDescription = "Toggle pin",
                                            tint = if (isPinned) ThemePurple else Color.White.copy(alpha = 0.6f),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isEditingNote) {
                                OutlinedTextField(
                                    value = editedNoteTitle,
                                    onValueChange = { editedNoteTitle = it },
                                    label = { Text("Title", fontSize = 12.sp) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = ThemePurple,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                                        focusedLabelColor = ThemePurple,
                                        unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = editedNoteContent,
                                    onValueChange = { editedNoteContent = it },
                                    label = { Text("Note content", fontSize = 12.sp) },
                                    minLines = 4,
                                    maxLines = 8,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = ThemePurple,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                                        focusedLabelColor = ThemePurple,
                                        unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                                )
                            } else {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 260.dp)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Text(
                                        text = body, 
                                        fontSize = 14.sp, 
                                        color = Color(0xFFF1F5F9), 
                                        lineHeight = 22.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Last updated: $timestamp", 
                                    fontSize = 11.sp, 
                                    color = TextMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    },
                    confirmButton = {
                        if (isEditingNote) {
                            Button(
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    if (editedNoteTitle.isNotBlank() && editedNoteContent.isNotBlank()) {
                                        viewModel.editVaultNote(noteStr, editedNoteTitle, editedNoteContent)
                                        viewNoteToShow = null
                                        isEditingNote = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Save", color = if (ThemePurple == Color(0xFFFFFFFF)) BrandBg else Color.White, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    viewNoteToShow = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Close", color = if (ThemePurple == Color(0xFFFFFFFF)) BrandBg else Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    },
                    dismissButton = {
                        if (isEditingNote) {
                            TextButton(
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    isEditingNote = false
                                }
                            ) {
                                Text("Cancel", color = ThemePurple, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            TextButton(
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    viewModel.deleteVaultNote(noteStr)
                                    viewNoteToShow = null
                                }
                            ) {
                                Text("Delete", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                )
            }
        }
        
        if (activeSection in listOf("Home", "Private Browser", "More")) {
        // Custom Bottom Navigation
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(72.dp)
                    .clip(RoundedCornerShape(36.dp))
                    .background(Color(0xFF161B2B).copy(alpha = 0.95f))
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(36.dp))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Vault (Home)
                Box(
                    modifier = Modifier.height(56.dp).clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { activeSection = "Home" }
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(if (activeSection == "Home") ThemePurple.copy(alpha = 0.15f) else Color.Transparent)
                            .padding(horizontal = if (activeSection == "Home") 12.dp else 0.dp, vertical = 10.dp)
                            .animateContentSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Dashboard,
                            contentDescription = "Vault",
                            tint = if (activeSection == "Home") ThemePurple else Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(22.dp)
                        )
                        if (activeSection == "Home") {
                            Text("Vault", color = ThemePurple, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                        }
                    }
                }
                
                // Browser
                Box(
                    modifier = Modifier.height(56.dp).clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { activeSection = "Private Browser" }
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(if (activeSection == "Private Browser") ThemePurple.copy(alpha = 0.15f) else Color.Transparent)
                            .padding(horizontal = if (activeSection == "Private Browser") 12.dp else 0.dp, vertical = 10.dp)
                            .animateContentSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Browser",
                            tint = if (activeSection == "Private Browser") ThemePurple else Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(22.dp)
                        )
                        if (activeSection == "Private Browser") {
                            Text("Browser", color = ThemePurple, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                        }
                    }
                }
                
                // Settings
                Box(
                    modifier = Modifier.height(56.dp).clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { activeSection = "More" }
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(if (activeSection == "More") ThemePurple.copy(alpha = 0.15f) else Color.Transparent)
                            .padding(horizontal = if (activeSection == "More") 12.dp else 0.dp, vertical = 10.dp)
                            .animateContentSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = if (activeSection == "More") ThemePurple else Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(22.dp)
                        )
                        if (activeSection == "More") {
                            Text("Settings", color = ThemePurple, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
        }
        
        @OptIn(ExperimentalMaterial3Api::class)
        if (showMediaAddOptions || showDocAddOptions) {
            ModalBottomSheet(
                onDismissRequest = { 
                    showMediaAddOptions = false
                    showDocAddOptions = false 
                },
                containerColor = Color(0xFF161B2B),
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.White.copy(alpha=0.3f)) }
            ) {
                Column(modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 32.dp).fillMaxWidth()) {
                    Text(
                        text = if (showDocAddOptions) "Secure Documents" else "Secure Media", 
                        color = Color.White, 
                        fontSize = 20.sp, 
                        fontWeight = FontWeight.Bold, 
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    
                    if (showMediaAddOptions) {
                        // Gallery
                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).clickable {
                                showMediaAddOptions = false
                                val isPhoto = activeSection == "Photos"
                                viewModel.isPickingFile = true
                                photoPickerLauncher.launch(
                                    if (isPhoto) "image/*" else "video/*"
                                )
                            }.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(ThemePurple.copy(alpha=0.2f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.PhotoLibrary, contentDescription = "Gallery", tint = ThemePurple)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Import from Gallery", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                Text(if (activeSection == "Photos") "Select multiple photos" else "Select multiple videos", color = TextMedium, fontSize = 13.sp)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        // Camera
                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).clickable {
                                showMediaAddOptions = false
                                activeCameraMode = "camera"
                            }.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(ThemePurple.copy(alpha=0.2f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = ThemePurple)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Use Built-in Camera", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                Text("Capture directly to vault", color = TextMedium, fontSize = 13.sp)
                            }
                        }
                    } else if (showDocAddOptions) {
                        // Documents
                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).clickable {
                                showDocAddOptions = false
                                viewModel.isPickingFile = true
                                documentPickerLauncher.launch("application/*")
                            }.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(ThemePurple.copy(alpha=0.2f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.UploadFile, contentDescription = "Documents", tint = ThemePurple)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Import Document Files", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                Text("Select PDFs, Word, etc.", color = TextMedium, fontSize = 13.sp)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        // Scanner
                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).clickable {
                                showDocAddOptions = false
                                activeCameraMode = "scanner"
                            }.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(ThemePurple.copy(alpha=0.2f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.DocumentScanner, contentDescription = "Scanner", tint = ThemePurple)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Use Document Scanner", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                Text("Scan physical documents", color = TextMedium, fontSize = 13.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
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
    // Settings Dialog
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
                        text = "Customize PINs, biometrics, and dynamic launcher disguises below.",
                        fontSize = 11.sp,
                        color = TextMedium,
                        lineHeight = 15.sp
                    )
                    Text(
                        text = "SECURITY PINs",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ThemePurple
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Workspace PIN",
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
                            text = "Decoy / Guest PIN",
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
                            android.widget.Toast.makeText(context, "PINs cannot be empty!", android.widget.Toast.LENGTH_SHORT).show()
                        } else if (realPasscodeInput == decoyPasscodeInput) {
                            android.widget.Toast.makeText(context, "Workspace and decoy PINs must be different!", android.widget.Toast.LENGTH_SHORT).show()
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
    if (activeDocumentToView != null) {
        SecureDocumentViewer(
            fileStr = activeDocumentToView!!,
            viewModel = viewModel,
            onDismiss = { activeDocumentToView = null }
        )
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
    var isImporting by remember { mutableStateOf(false) }
    var importProgress by remember { mutableStateOf(0f) }
    var importTotal by remember { mutableStateOf(0) }
    var importCurrent by remember { mutableStateOf(0) }
    var showImportSuccess by remember { mutableStateOf(false) }
    var importSuccessCount by remember { mutableStateOf(0) }
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
                                imageVector = Icons.Default.VisibilityOff,
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
                        imageVector = Icons.Default.CheckCircle,
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
                                Icon(Icons.Default.Add, contentDescription = "Open New Tab", modifier = Modifier.size(14.dp), tint = if (ThemePurple == Color(0xFFFFFFFF)) BrandBg else Color.White)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("New Private Tab", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (ThemePurple == Color(0xFFFFFFFF)) BrandBg else Color.White)
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
    var isImporting by remember { mutableStateOf(false) }
    var importProgress by remember { mutableStateOf(0f) }
    var importTotal by remember { mutableStateOf(0) }
    var importCurrent by remember { mutableStateOf(0) }
    var showImportSuccess by remember { mutableStateOf(false) }
    var importSuccessCount by remember { mutableStateOf(0) }
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
@Composable
fun UnifiedGlassCard(
    modifier: Modifier = Modifier,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(24.dp),
    bgColor: Color,
    elevation: androidx.compose.ui.unit.Dp = 4.dp,
    glowAlpha: Float = 0f,
    onClick: (() -> Unit)? = null,
    interactionSource: androidx.compose.foundation.interaction.MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable androidx.compose.foundation.layout.BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .drawBehind {
                if (glowAlpha > 0f) {
                    val outerGlowRadius = size.width * 1.5f
                    drawCircle(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.25f * glowAlpha),
                                Color.White.copy(alpha = 0.05f * glowAlpha),
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
                elevation = elevation,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.8f),
                spotColor = Color.Black.copy(alpha = 0.8f)
            )
            .clip(shape)
            .background(bgColor)
            .drawWithContent {
                drawContent()
                if (glowAlpha > 0f) {
                    drawRect(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.2f * glowAlpha),
                                Color.Transparent
                            ),
                            center = center,
                            radius = size.width / 2f
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
                shape = shape
            )
            .then(
                if (onClick != null) Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ) else Modifier
            ),
        contentAlignment = Alignment.Center,
        content = content
    )
}
@Composable
fun VaultFolderCard(title: String, count: String, icon: androidx.compose.ui.graphics.vector.ImageVector, iconTint: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    UnifiedGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        bgColor = Color(0xFF161B2B).copy(alpha = 0.95f),
        elevation = 0.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(count, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
            }
        }
    }
}
@Composable
fun EnhancedVaultCard(title: String, count: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier, previewContent: @Composable () -> Unit = {}, onClick: () -> Unit) {
    val themePurple = LocalAppThemeColors.current.themePurple
    UnifiedGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        bgColor = Color(0xFF161B2B).copy(alpha = 0.95f),
        elevation = 4.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(themePurple.copy(alpha = 0.1f))
                        .border(1.dp, themePurple.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = themePurple, modifier = Modifier.size(18.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(count, color = Color.White.copy(alpha = 0.4f), fontSize = 11.sp)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Preview content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0F121C))
                    .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                previewContent()
            }
        }
    }
}
