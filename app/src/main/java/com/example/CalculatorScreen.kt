package com.example
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.Text
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.Brush
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.filled.DesktopMac
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Clear
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

import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.ExitToApp
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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardBackspace
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.WorkspacePremium
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
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Mic
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Surface
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
class OpenMultipleDocumentsWithWrite : androidx.activity.result.contract.ActivityResultContract<Array<String>, List<android.net.Uri>>() {
    override fun createIntent(context: android.content.Context, input: Array<String>): android.content.Intent {
        return android.content.Intent(android.content.Intent.ACTION_OPEN_DOCUMENT)
            .addCategory(android.content.Intent.CATEGORY_OPENABLE)
            .putExtra(android.content.Intent.EXTRA_ALLOW_MULTIPLE, true)
            .addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .addFlags(android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            .addFlags(android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            .setType("*/*")
            .putExtra(android.content.Intent.EXTRA_MIME_TYPES, input)
    }

    override fun parseResult(resultCode: Int, intent: android.content.Intent?): List<android.net.Uri> {
        if (resultCode != android.app.Activity.RESULT_OK || intent == null) {
            return emptyList()
        }
        val uris = mutableListOf<android.net.Uri>()
        if (intent.data != null) {
            uris.add(intent.data!!)
        } else if (intent.clipData != null) {
            val clipData = intent.clipData!!
            for (i in 0 until clipData.itemCount) {
                uris.add(clipData.getItemAt(i).uri)
            }
        }
        return uris
    }
}

class OpenDocumentWithWrite : androidx.activity.result.contract.ActivityResultContract<Array<String>, android.net.Uri?>() {
    override fun createIntent(context: android.content.Context, input: Array<String>): android.content.Intent {
        return android.content.Intent(android.content.Intent.ACTION_OPEN_DOCUMENT)
            .addCategory(android.content.Intent.CATEGORY_OPENABLE)
            .addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .addFlags(android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            .addFlags(android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            .setType("*/*")
            .putExtra(android.content.Intent.EXTRA_MIME_TYPES, input)
    }

    override fun parseResult(resultCode: Int, intent: android.content.Intent?): android.net.Uri? {
        return if (resultCode == android.app.Activity.RESULT_OK) intent?.data else null
    }
}

// Dynamic Theme-Aware Palette
private val BrandBg: Color @Composable get() = LocalAppThemeColors.current.brandBg
private val TextDark: Color @Composable get() = LocalAppThemeColors.current.textDark
private val TextMedium: Color @Composable get() = LocalAppThemeColors.current.textMedium
private val ThemePurple: Color @Composable get() = LocalAppThemeColors.current.themePurple
private val ThemeLightPurple: Color @Composable get() = LocalAppThemeColors.current.themeLightPurple
private val ThemeContainerBorder: Color @Composable get() = LocalAppThemeColors.current.themeContainerBorder
private val KeypadBg: Color @Composable get() = LocalAppThemeColors.current.keypadBg
private val DigitBg: Color @Composable get() = LocalAppThemeColors.current.digitBg
private val IsWhiteTheme: Boolean @Composable get() = ThemePurple.red > 0.9f && ThemePurple.green > 0.9f && ThemePurple.blue > 0.9f
private val IsLightColor: Boolean @Composable get() = (ThemePurple.red * 0.299f + ThemePurple.green * 0.587f + ThemePurple.blue * 0.114f) > 0.6f

@Composable
private fun dynamicSwitchColors() = androidx.compose.material3.SwitchDefaults.colors(
    checkedThumbColor = if (IsLightColor) Color(0xFF1B2031) else Color.White,
    checkedTrackColor = ThemePurple,
    uncheckedThumbColor = Color.White.copy(alpha = 0.6f),
    uncheckedTrackColor = Color.White.copy(alpha = 0.12f)
)

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
@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf(ActiveTab.CALCULATOR) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLocalRecoveryDialog by remember { mutableStateOf(false) }
    val showRecoveryTrigger by viewModel.showRecoveryTrigger.collectAsStateWithLifecycle()
    val showRecoveryDialog = showLocalRecoveryDialog || showRecoveryTrigger
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val view = androidx.compose.ui.platform.LocalView.current
    var isImporting by remember { mutableStateOf(false) }
    var importProgress by remember { mutableStateOf(0f) }
    var importTotal by remember { mutableStateOf(0) }
    var importCurrent by remember { mutableStateOf(0) }
    var showImportSuccess by remember { mutableStateOf(false) }
    var importSuccessCount by remember { mutableStateOf(0) }
    val vaultUnlocked by viewModel.vaultUnlocked.collectAsStateWithLifecycle()
    val vaultFiles by viewModel.vaultFiles.collectAsStateWithLifecycle()
    val vaultNotes by viewModel.vaultNotes.collectAsStateWithLifecycle()
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
    val welcomeAlpha = remember { androidx.compose.animation.core.Animatable(0f) }
    
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

    // Orbiting ring and pulsing infinite transition animations
    val welcomeInfiniteTransition = rememberInfiniteTransition(label = "welcome_infinite")
    val orbitRotationAngle by welcomeInfiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit_angle"
    )
    val lockPulseScale by welcomeInfiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lock_pulse"
    )

    // Staggered layout entry values for the preview folder grid
    val item1Alpha by animateFloatAsState(
        targetValue = if (transitionState >= 2) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = 0, easing = FastOutSlowInEasing),
        label = "stagger_1_alpha"
    )
    val item1OffsetY by animateDpAsState(
        targetValue = if (transitionState >= 2) 0.dp else 16.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "stagger_1_offset"
    )
    
    val item2Alpha by animateFloatAsState(
        targetValue = if (transitionState >= 2) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = 120, easing = FastOutSlowInEasing),
        label = "stagger_2_alpha"
    )
    val item2OffsetY by animateDpAsState(
        targetValue = if (transitionState >= 2) 0.dp else 16.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "stagger_2_offset"
    )
    
    val item3Alpha by animateFloatAsState(
        targetValue = if (transitionState >= 2) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = 240, easing = FastOutSlowInEasing),
        label = "stagger_3_alpha"
    )
    val item3OffsetY by animateDpAsState(
        targetValue = if (transitionState >= 2) 0.dp else 16.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "stagger_3_offset"
    )
    
    val item4Alpha by animateFloatAsState(
        targetValue = if (transitionState >= 2) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = 360, easing = FastOutSlowInEasing),
        label = "stagger_4_alpha"
    )
    val item4OffsetY by animateDpAsState(
        targetValue = if (transitionState >= 2) 0.dp else 16.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "stagger_4_offset"
    )
    LaunchedEffect(vaultUnlocked) {
        if (vaultUnlocked) {
            try {
                view.performHapticFeedback(android.view.HapticFeedbackConstants.CONFIRM)
            } catch (e: Exception) {
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
            }
            viewModel.triggerKeypressEffects(context)
            transitionState = 1 
            activeTab = ActiveTab.VAULT 
        } else {
            welcomeAlpha.snapTo(0f)
            transitionState = 0
            activeTab = ActiveTab.CALCULATOR
        }
    }
    
    fun triggerBiometricUnlock() {
        val activity = context as? androidx.fragment.app.FragmentActivity
        val isBiometricEnabled = viewModel.biometricEnabled.value
        if (activity != null && isBiometricEnabled) {
            val executor = androidx.core.content.ContextCompat.getMainExecutor(activity)
            val biometricPrompt = androidx.biometric.BiometricPrompt(
                activity,
                executor,
                object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        viewModel.unlockVault(isDecoy = false)
                        android.widget.Toast.makeText(context, "Vault Unlocked via Biometrics!", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
            )
            val promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("Unlock Vault")
                .setSubtitle("Authenticate using fingerprint or face recognition")
                .setNegativeButtonText("Cancel")
                .build()
            biometricPrompt.authenticate(promptInfo)
        }
    }
    Surface(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(androidx.compose.ui.input.pointer.PointerEventPass.Main)
                        if (event.type == androidx.compose.ui.input.pointer.PointerEventType.Press) {
                            viewModel.updateLastInteraction()
                        }
                    }
                }
            },
        color = BrandBg
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Calculator Layout
            if (activeTab == ActiveTab.CALCULATOR || welcomeAlpha.value > 0f || authOverlayAlpha > 0f) {
                val calcGlowColor = ThemePurple
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawBehind {
                            // Elegant dynamic ambient glow pool
                            val glowColor = calcGlowColor.copy(alpha = 0.08f)
                            drawCircle(
                                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                                    colors = listOf(glowColor, Color.Transparent),
                                    center = Offset(0f, 0f),
                                    radius = size.width * 1.5f
                                ),
                                radius = size.width * 1.5f,
                                center = Offset(0f, 0f)
                            )
                            drawCircle(
                                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                                    colors = listOf(glowColor, Color.Transparent),
                                    center = Offset(size.width, size.height * 0.8f),
                                    radius = size.width * 1.5f
                                ),
                                radius = size.width * 1.5f,
                                center = Offset(size.width, size.height * 0.8f)
                            )
                        }
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
                                color = TextDark,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.combinedClickable(
                                    onClick = {},
                                    onLongClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        showLocalRecoveryDialog = true
                                    }
                                )
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
                            modifier = Modifier.background(KeypadBg).widthIn(max = 240.dp)
                        ) {
                            val selectedTheme by viewModel.selectedTheme.collectAsStateWithLifecycle()
                            
                            androidx.compose.material3.DropdownMenuItem(
                                text = { 
                                    Text(
                                        text = "CHOOSE THEME", 
                                        color = TextDark.copy(alpha = 0.5f), 
                                        fontSize = 11.sp, 
                                        fontWeight = FontWeight.Bold
                                    ) 
                                },
                                onClick = {},
                                enabled = false
                            )

                            com.example.ui.theme.AppTheme.values().forEach { theme ->
                                val isSelected = selectedTheme == theme
                                androidx.compose.material3.DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = theme.displayName,
                                            color = if (isSelected) ThemePurple else TextDark,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 14.sp
                                        )
                                    },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        viewModel.setSelectedTheme(theme)
                                        showHeaderMenu = false
                                    },
                                    leadingIcon = {
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clip(CircleShape)
                                                .background(theme.previewColor)
                                                .border(1.dp, Color.Gray.copy(alpha = 0.3f), CircleShape)
                                        )
                                    },
                                    trailingIcon = {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = ThemePurple,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
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
                    ) {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp)
                        ) {
                            CalculatorTabContent(
                                viewModel = viewModel,
                                onTriggerBiometric = { triggerBiometricUnlock() }
                            )
                        }
                    }
                }
            }
            // 1. Dashboard Content when unlocked
            if (activeTab == ActiveTab.VAULT || transitionState == 3) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    VaultTabUnlockedContent(
                        viewModel = viewModel,
                        onLockExit = { activeTab = ActiveTab.CALCULATOR }
                    )
                }
            }

            // 2. Secret Vault Unlocking Animation
            if (transitionState == 1) {
                SecretVaultUnlockingAnimation(
                    onAnimationComplete = {
                        activeTab = ActiveTab.VAULT
                        transitionState = 3
                    }
                )
            }
        }
    }
    // Dynamic Theme Selection Dialog
    if (showThemeDialog) {
        val selectedTheme by viewModel.selectedTheme.collectAsStateWithLifecycle()
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

    // Secure PIN Recovery Dialog
    if (showRecoveryDialog) {
        val securityQuestion by viewModel.securityQuestion.collectAsStateWithLifecycle()
        val securityAnswer by viewModel.securityAnswer.collectAsStateWithLifecycle()
        val recoveryCode by viewModel.recoveryCode.collectAsStateWithLifecycle()
        
        var selectedRecoveryOption by remember { mutableStateOf(0) } // 0 = Question, 1 = Recovery Code
        var answerInput by remember { mutableStateOf("") }
        var codeInput by remember { mutableStateOf("") }
        var newPinInput by remember { mutableStateOf("") }
        var isRecoverySuccessful by remember { mutableStateOf(false) }

        androidx.compose.material3.AlertDialog(
            onDismissRequest = {
                showLocalRecoveryDialog = false
                viewModel.setShowRecoveryTrigger(false)
            },
            containerColor = Color(0xFF1B2031),
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    text = "Reset Vault PIN",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            confirmButton = {
                if (isRecoverySuccessful) {
                    androidx.compose.material3.TextButton(
                        onClick = {
                            if (newPinInput.length >= 4) {
                                viewModel.setVaultPin(newPinInput)
                                showLocalRecoveryDialog = false
                                viewModel.setShowRecoveryTrigger(false)
                                android.widget.Toast.makeText(context, "PIN successfully reset!", android.widget.Toast.LENGTH_SHORT).show()
                            } else {
                                android.widget.Toast.makeText(context, "PIN must be at least 4 digits.", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Text("Reset PIN", color = ThemePurple, fontWeight = FontWeight.Bold)
                    }
                } else {
                    androidx.compose.material3.TextButton(
                        onClick = {
                            if (selectedRecoveryOption == 0) {
                                val trimmedInput = answerInput.trim().lowercase()
                                if (securityAnswer.isNotEmpty() && trimmedInput == securityAnswer) {
                                    isRecoverySuccessful = true
                                    android.widget.Toast.makeText(context, "Authentication successful! Enter your new PIN.", android.widget.Toast.LENGTH_SHORT).show()
                                } else {
                                    android.widget.Toast.makeText(context, "Incorrect answer. Please try again.", android.widget.Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                val trimmedInput = codeInput.trim().uppercase()
                                val trimmedCode = recoveryCode.trim().uppercase()
                                if (recoveryCode.isNotEmpty() && (trimmedInput == trimmedCode || trimmedInput.replace("-", "") == trimmedCode.replace("-", ""))) {
                                    isRecoverySuccessful = true
                                    android.widget.Toast.makeText(context, "Authentication successful! Enter your new PIN.", android.widget.Toast.LENGTH_SHORT).show()
                                } else {
                                    android.widget.Toast.makeText(context, "Incorrect recovery key. Please try again.", android.widget.Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    ) {
                        Text("Verify", color = ThemePurple, fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        showLocalRecoveryDialog = false
                        viewModel.setShowRecoveryTrigger(false)
                    }
                ) {
                    Text("Cancel", color = Color.White.copy(alpha = 0.6f))
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (!isRecoverySuccessful) {
                        Text(
                            text = "Choose a recovery option to unlock your vault and set a new PIN.",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        
                        // Option tabs
                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(Color.White.copy(alpha = 0.05f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selectedRecoveryOption == 0) ThemePurple.copy(alpha = 0.2f) else Color.Transparent)
                                    .clickable { selectedRecoveryOption = 0 }
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Security Q",
                                    color = if (selectedRecoveryOption == 0) ThemePurple else Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selectedRecoveryOption == 1) ThemePurple.copy(alpha = 0.2f) else Color.Transparent)
                                    .clickable { selectedRecoveryOption = 1 }
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Master Key",
                                    color = if (selectedRecoveryOption == 1) ThemePurple else Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        if (selectedRecoveryOption == 0) {
                            // Question recovery
                            if (securityQuestion.isEmpty()) {
                                Text(
                                    text = "⚠️ You have not set up a security question yet. Please set it up in Vault Settings under Authentication.",
                                    color = Color(0xFFFF9100),
                                    fontSize = 12.sp
                                )
                            } else {
                                Text(
                                    text = "Question: $securityQuestion",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.sp
                                )
                                androidx.compose.material3.OutlinedTextField(
                                    value = answerInput,
                                    onValueChange = { answerInput = it },
                                    placeholder = { Text("Answer", color = Color.White.copy(alpha = 0.4f)) },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth().testTag("recovery_answer_input"),
                                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedBorderColor = ThemePurple,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.25f)
                                    )
                                )
                            }
                        } else {
                            // Recovery code recovery
                            Text(
                                text = "Enter your 16-character Master Recovery Code to unlock.",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            androidx.compose.material3.OutlinedTextField(
                                value = codeInput,
                                onValueChange = { codeInput = it },
                                placeholder = { Text("e.g. XXXX-XXXX-XXXX-XXXX", color = Color.White.copy(alpha = 0.4f)) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("recovery_code_input"),
                                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedBorderColor = ThemePurple,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.25f)
                                )
                            )
                        }
                    } else {
                        // Recover success: let them write new PIN
                        Text(
                            text = "Choose your new vault access PIN. Minimum 4 digits.",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        androidx.compose.material3.OutlinedTextField(
                            value = newPinInput,
                            onValueChange = { if (it.all { c -> c.isDigit() }) newPinInput = it },
                            placeholder = { Text("Enter New Access PIN", color = Color.White.copy(alpha = 0.4f)) },
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                            ),
                            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth().testTag("new_pin_input"),
                            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = ThemePurple,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.25f)
                            )
                        )
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
    viewModel: CalculatorViewModel,
    onTriggerBiometric: () -> Unit
) {
    val expression by viewModel.expression.collectAsStateWithLifecycle()
    val calcResult by viewModel.calcResult.collectAsStateWithLifecycle()
    val isEvaluated by viewModel.isEvaluated.collectAsStateWithLifecycle()
    val rate by viewModel.exchangeRate.collectAsStateWithLifecycle()
    val sourceCurrency by viewModel.sourceCurrency.collectAsStateWithLifecycle()
    val targetCurrency by viewModel.targetCurrency.collectAsStateWithLifecycle()
    val biometricEnabled by viewModel.biometricEnabled.collectAsStateWithLifecycle()
    val biometricMode by viewModel.biometricMode.collectAsStateWithLifecycle()
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
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (biometricEnabled) {
                                    onTriggerBiometric()
                                }
                            }
                        )
                    },
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
                                tint = if (ThemePurple.red > 0.95f && ThemePurple.green > 0.95f && ThemePurple.blue > 0.95f) BrandBg else Color.White,
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
                            onLongClick = if (isEquals && biometricEnabled) {
                                {
                                    onTriggerBiometric()
                                }
                            } else null,
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
    themeLightPurple: Color,
    onLongClick: (() -> Unit)? = null
) {
    val haptic = LocalHapticFeedback.current
    val view = androidx.compose.ui.platform.LocalView.current
    val themeColors = com.example.ui.theme.LocalAppThemeColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "button_scale"
    )
    val glowAlpha by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        animationSpec = androidx.compose.animation.core.tween(150),
        label = "glow_alpha"
    )
    val baseBgColor = when {
        isEquals -> Color(0xFFE3AB79) // Lighter beige/orange
        isUtility -> themeColors.keypadBg
        isOperator -> themeColors.keypadBg
        else -> themeColors.digitBg
    }
    val bgColor = if (isPressed) {
        when {
            isEquals -> Color(0xFFEDC5A1) // Glowing warm warm orange/beige when pressed
            isUtility -> baseBgColor.copy(alpha = 0.85f)
            isOperator -> themePurple.copy(alpha = 0.25f) // Operator keys glow purple
            else -> themePurple.copy(alpha = 0.15f) // Digit keys glow with a glass purple tint when pressed
        }
    } else {
        baseBgColor
    }
    
    val baseContentColor = when {
        isEquals -> Color.Black
        isUtility -> themePurple
        isOperator -> themePurple
        else -> themeColors.textDark
    }
    val contentColor = if (isPressed) {
        when {
            isEquals -> Color.Black
            isUtility -> themePurple
            isOperator -> Color.White // Operator text lights up white when pressed
            else -> themePurple // Digit text turns glowing themePurple when pressed
        }
    } else {
        baseContentColor
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
            onLongClick = onLongClick,
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
fun CompactSettingsCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color = ThemePurple,
    onClick: () -> Unit
) {
    UnifiedGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
        elevation = 2.dp,
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
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(iconColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.5f),
                        lineHeight = 14.sp
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = Color.White.copy(alpha = 0.3f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun PrivacyToolGridCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color = ThemePurple,
    onClick: () -> Unit
) {
    UnifiedGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp),
        shape = RoundedCornerShape(18.dp),
        bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
        elevation = 2.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
fun PrivacyToolWideCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color = ThemePurple,
    onClick: () -> Unit
) {
    UnifiedGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp),
        shape = RoundedCornerShape(18.dp),
        bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
        elevation = 2.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.5f),
                    lineHeight = 13.sp
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = Color.White.copy(alpha = 0.3f),
                modifier = Modifier.size(18.dp)
            )
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
    val vaultUnlocked by viewModel.vaultUnlocked.collectAsStateWithLifecycle()
    val vaultFiles by viewModel.vaultFiles.collectAsStateWithLifecycle()
    val vaultNotes by viewModel.vaultNotes.collectAsStateWithLifecycle()
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var isImporting by remember { mutableStateOf(false) }
    var importProgress by remember { mutableStateOf(0f) }
    var importTotal by remember { mutableStateOf(0) }
    var importCurrent by remember { mutableStateOf(0) }
    var showImportSuccess by remember { mutableStateOf(false) }
    var importSuccessCount by remember { mutableStateOf(0) }
    val biometricEnabled by viewModel.biometricEnabled.collectAsStateWithLifecycle()
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
    // No automatic biometric prompt on startup to avoid disclosing vault app.
    // Biometric is only triggered manually (e.g. by long pressing the equals key).
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
                    text = "Secret Vault",
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
                                    targetValue = if (isPinKeyPressed) 0.95f else 1.0f,
                                    animationSpec = androidx.compose.animation.core.spring(
                                        dampingRatio = androidx.compose.animation.core.Spring.DampingRatioLowBouncy,
                                        stiffness = androidx.compose.animation.core.Spring.StiffnessMedium
                                    ),
                                    label = "pin_key_scale"
                                )
                                val baseColor = if (isSpecial) Color(0xFF635BFF).copy(alpha = 0.2f) else Color(0xFF1B2031)
                                val pressedColor = if (isSpecial) Color(0xFF635BFF).copy(alpha = 0.4f) else Color(0xFF635BFF).copy(alpha = 0.25f)
                                val buttonColor = if (isPinKeyPressed) pressedColor else baseColor
                                val baseContentColor = if (isSpecial) Color(0xFF8C84FF) else Color.White
                                val contentColor = if (isPinKeyPressed) {
                                    if (isSpecial) Color.White else Color(0xFF8C84FF)
                                } else {
                                    baseContentColor
                                }
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
                                            indication = androidx.compose.material3.ripple(
                                                bounded = true,
                                                color = Color(0xFF635BFF).copy(alpha = 0.3f)
                                            )
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
    val vaultNotes by viewModel.vaultNotes.collectAsStateWithLifecycle()

    // --- Google Drive Cloud Backup State Flows ---
    val googleDriveConnected by viewModel.googleDriveConnected.collectAsStateWithLifecycle()
    val googleDriveEmail by viewModel.googleDriveEmail.collectAsStateWithLifecycle()
    val googleDriveName by viewModel.googleDriveName.collectAsStateWithLifecycle()
    val cloudBackupInfo by viewModel.cloudBackupInfo.collectAsStateWithLifecycle()

    // --- Modern Backup & Restore State & Launchers ---
    var isBackupRestoreProcessing by remember { androidx.compose.runtime.mutableStateOf(false) }
    val backupRestoreScope = androidx.compose.runtime.rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current

    val googleDriveAuthLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        val data = result.data
        if (data != null) {
            viewModel.handleGoogleDriveAuthResultIntent(
                data = data,
                onSuccess = {
                    android.widget.Toast.makeText(context, "Google Drive connected successfully!", android.widget.Toast.LENGTH_SHORT).show()
                },
                onFailure = { error ->
                    android.widget.Toast.makeText(context, error, android.widget.Toast.LENGTH_LONG).show()
                }
            )
        } else {
            android.widget.Toast.makeText(context, "Connection failed. Result Code: ${result.resultCode}", android.widget.Toast.LENGTH_LONG).show()
        }
    }

    val exportBackupLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.CreateDocument("application/zip")
    ) { uri ->
        if (uri != null) {
            isBackupRestoreProcessing = true
            backupRestoreScope.launch {
                val success = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                    try {
                        context.contentResolver.openOutputStream(uri)?.use { outStream ->
                            viewModel.exportBackupToZip(context, outStream)
                        } ?: false
                    } catch (e: Exception) {
                        e.printStackTrace()
                        false
                    }
                }
                isBackupRestoreProcessing = false
                if (success) {
                    android.widget.Toast.makeText(context, "Backup created successfully!", android.widget.Toast.LENGTH_LONG).show()
                } else {
                    android.widget.Toast.makeText(context, "Failed to create backup", android.widget.Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    val importBackupLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            isBackupRestoreProcessing = true
            backupRestoreScope.launch {
                val success = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                    try {
                        context.contentResolver.openInputStream(uri)?.use { inStream ->
                            viewModel.importBackupFromZip(context, inStream)
                        } ?: false
                    } catch (e: Exception) {
                        e.printStackTrace()
                        false
                    }
                }
                isBackupRestoreProcessing = false
                if (success) {
                    android.widget.Toast.makeText(context, "Backup restored successfully!", android.widget.Toast.LENGTH_LONG).show()
                } else {
                    android.widget.Toast.makeText(context, "Failed to restore backup", android.widget.Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    var showDrawerMenu by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showTimezoneDialog by remember { mutableStateOf(false) }
    var showAddNoteDialog by remember { mutableStateOf(false) }
    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
    var isEditingNote by remember { mutableStateOf(false) }
    var editedNoteTitle by remember { mutableStateOf("") }
    var editedNoteContent by remember { mutableStateOf("") }
    var editedNoteContentValue by remember { mutableStateOf(TextFieldValue("")) }
    var isNewDraftNote by remember { mutableStateOf(false) }
    
    var pendingUnlockAction by remember { mutableStateOf<Pair<String, () -> Unit>?>(null) }
    var activeCameraMode by remember { mutableStateOf<String?>(null) } // null, "camera", "scanner"
    var showMediaAddOptions by remember { mutableStateOf(false) }
    var showDocAddOptions by remember { mutableStateOf(false) }
    var showChangePasscodeDialog by remember { mutableStateOf(false) }
    var pendingDisguiseTarget by remember { mutableStateOf<Pair<String, String>?>(null) }
    var realPasscodeInput by remember { mutableStateOf("") }
    var decoyPasscodeInput by remember { mutableStateOf("") }
    var isImporting by remember { mutableStateOf(false) }
    var importProgress by remember { mutableStateOf(0f) }
    var importTotal by remember { mutableStateOf(0) }
    var importCurrent by remember { mutableStateOf(0) }
    var showImportSuccess by remember { mutableStateOf(false) }
    var importSuccessCount by remember { mutableStateOf(0) }
    val activeAppIcon by viewModel.activeAppIcon.collectAsStateWithLifecycle()
    val biometricEnabled by viewModel.biometricEnabled.collectAsStateWithLifecycle()
    val panicEnabled by viewModel.panicEnabled.collectAsStateWithLifecycle()
    val panicAction by viewModel.panicAction.collectAsStateWithLifecycle()
    val panicExitActionVal by viewModel.panicExitAction.collectAsStateWithLifecycle()
    val screenDownLock by viewModel.screenDownLock.collectAsStateWithLifecycle()
    val blurThumbnails by viewModel.blurThumbnails.collectAsStateWithLifecycle()
    val lockedFolders by viewModel.lockedFolders.collectAsStateWithLifecycle()
    val tempUnlockedFolders by viewModel.tempUnlockedFolders.collectAsStateWithLifecycle()
    val ownerName by viewModel.ownerName.collectAsStateWithLifecycle()
    val ownerAvatarUri by viewModel.ownerAvatarUri.collectAsStateWithLifecycle()
    val ownerAvatarScale by viewModel.ownerAvatarScale.collectAsStateWithLifecycle()
    val ownerAvatarOffsetX by viewModel.ownerAvatarOffsetX.collectAsStateWithLifecycle()
    val ownerAvatarOffsetY by viewModel.ownerAvatarOffsetY.collectAsStateWithLifecycle()
    val premiumState by viewModel.premiumState.collectAsStateWithLifecycle()
    val vaultId by viewModel.vaultId.collectAsStateWithLifecycle()
    val overallSecurityRating by viewModel.overallSecurityRating.collectAsStateWithLifecycle()
    val securityItems by viewModel.securityItems.collectAsStateWithLifecycle()

    val avatarGalleryLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                try {
                    val takeFlags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                    context.contentResolver.takePersistableUriPermission(uri, takeFlags)
                } catch (e: Exception) {
                    android.util.Log.e("Profile", "Failed to take persistable URI permission", e)
                }
                viewModel.setOwnerAvatarUri(uri.toString())
                android.widget.Toast.makeText(context, "Photo selected! Scroll down to adjust crop & position.", android.widget.Toast.LENGTH_LONG).show()
            }
        }
    )

    val activity = context as? androidx.fragment.app.FragmentActivity
        // Photo/Video Picker launcher
        val coroutineScope = rememberCoroutineScope()
        val photoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(),
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
                                try {
                                    val takeFlags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                    context.contentResolver.takePersistableUriPermission(uri, takeFlags)
                                } catch (e: Exception) {
                                    try {
                                        val takeFlags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        context.contentResolver.takePersistableUriPermission(uri, takeFlags)
                                        android.util.Log.d("Vault", "Took persistable READ URI permission for $uri after write failed")
                                    } catch (e2: Exception) {
                                        android.util.Log.e("Vault", "Failed to take any persistable URI permission for $uri", e2)
                                    }
                                }
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
                            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                viewModel.onOriginalFileDeleted(context)
                            }
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
            contract = OpenMultipleDocumentsWithWrite(),
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
                                try {
                                    val takeFlags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                    context.contentResolver.takePersistableUriPermission(uri, takeFlags)
                                } catch (e: Exception) {
                                    try {
                                        val takeFlags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        context.contentResolver.takePersistableUriPermission(uri, takeFlags)
                                        android.util.Log.d("Vault", "Took persistable READ URI permission for $uri after write failed")
                                    } catch (e2: Exception) {
                                        android.util.Log.e("Vault", "Failed to take any persistable URI permission for $uri", e2)
                                    }
                                }
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
                            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                viewModel.onOriginalFileDeleted(context)
                            }
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
            contract = OpenDocumentWithWrite(),
            onResult = { uri ->
                viewModel.isPickingFile = false
                if (uri != null) {
                    try {
                        val takeFlags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        context.contentResolver.takePersistableUriPermission(uri, takeFlags)
                    } catch (e: Exception) {
                        try {
                            val takeFlags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                            context.contentResolver.takePersistableUriPermission(uri, takeFlags)
                            android.util.Log.d("Vault", "Took persistable READ URI permission for $uri after write failed")
                        } catch (e2: Exception) {
                            android.util.Log.e("Vault", "Failed to take any persistable URI permission for $uri", e2)
                        }
                    }
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
        val pendingDeleteSender by viewModel.pendingDeleteSender.collectAsStateWithLifecycle()
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
            var faceDownStartTime = 0L
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    val curTime = System.currentTimeMillis()
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    
                    // 1. Screen Down Lock detection (Z-axis negative gravity)
                    if (screenDownLock) {
                        if (z < -9.5f) {
                            if (faceDownStartTime == 0L) {
                                faceDownStartTime = curTime
                            } else if (curTime - faceDownStartTime >= 1500L) {
                                viewModel.triggerKeypressEffects(context)
                                viewModel.lockVault()
                                Toast.makeText(context, "Vault locked: Face down detected!", Toast.LENGTH_SHORT).show()
                                faceDownStartTime = 0L
                                return
                            }
                        } else {
                            faceDownStartTime = 0L
                        }
                    } else {
                        faceDownStartTime = 0L
                    }
                    
                    // 2. Shake detection
                    if (panicEnabled) {
                        if (lastUpdate == 0L) {
                            lastUpdate = curTime
                            lastX = x
                            lastY = y
                            lastZ = z
                            return
                        }
                        val diffTime = curTime - lastUpdate
                        if (diffTime > 150) {
                            val deltaX = x - lastX
                            val deltaY = y - lastY
                            val deltaZ = z - lastZ
                            val acceleration = Math.sqrt((deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ).toDouble()).toFloat()
                            
                            if (acceleration > 25.0f) { // Intentional shake
                                viewModel.triggerKeypressEffects(context)
                                viewModel.lockVault()
                                Toast.makeText(context, "Shake to Exit: Vault locked!", Toast.LENGTH_SHORT).show()
                            }
                            lastUpdate = curTime
                            lastX = x
                            lastY = y
                            lastZ = z
                        }
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
        val vaultFiles by viewModel.vaultFiles.collectAsStateWithLifecycle()
        var activeSection by remember { rememberBackStack("Home") } // "Home", "Notes", "Photos & Videos", "Documents", "Explore", "Settings"
        LaunchedEffect(Unit) {
            activeSection = "Home"
        }
        var selectedFileForDetails by remember { mutableStateOf<String?>(null) }
        var secureShareFileSerialized by remember { mutableStateOf<String?>(null) }
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
        var showDeleteConfirm by remember { mutableStateOf<String?>(null) }
        var showMoveToFolderDialog by remember { mutableStateOf<Pair<String, String>?>(null) } // Pair(itemId, type: "file"/"note")
        var showSearchDialog by remember { mutableStateOf(false) }
        var viewNoteToShow by remember { mutableStateOf<String?>(null) }
        var lastNonNullNote by remember { mutableStateOf<String?>(null) }
        val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }
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
                viewNoteToShow != null -> {
                    viewModel.triggerKeypressEffects(context)
                    val noteStr = lastNonNullNote
                    if (noteStr != null && isEditingNote) {
                        if (editedNoteTitle.isNotBlank() || editedNoteContentValue.text.isNotBlank()) {
                            viewModel.editVaultNote(noteStr, editedNoteTitle, editedNoteContentValue.text)
                        } else if (isNewDraftNote) {
                            viewModel.deleteVaultNote(noteStr)
                        }
                    }
                    viewNoteToShow = null
                    isEditingNote = false
                    isNewDraftNote = false
                }
                activeDocumentToView != null -> activeDocumentToView = null
                selectedFileForDetails != null -> selectedFileForDetails = null
                secureShareFileSerialized != null -> secureShareFileSerialized = null
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                // Clean and spacious Unlocked Header
                if (activeSection == "Home") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box {
                            IconButton(
                                onClick = { 
                                    viewModel.triggerKeypressEffects(context)
                                    showDrawerMenu = true
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

                            DropdownMenu(
                                expanded = showDrawerMenu,
                                onDismissRequest = { showDrawerMenu = false },
                                modifier = Modifier
                                    .background(Color(0xFF1E293B))
                                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                                    .padding(vertical = 4.dp)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("🏠 Home Dashboard", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Home"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("🖼️ Photos Vault", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Photos"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("🎥 Videos Vault", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Videos"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("🎵 Music & Audio", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Music & Audio"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("📄 Documents", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Documents"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("📝 Secure Notes", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Notes"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("🌐 Private Browser", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Private Browser"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("🔑 Password Generator", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Password_Generator"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("🎙️ Secure Voice Note", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Secure_Voice_Note"
                                        showDrawerMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("⚙️ Vault Settings", color = Color.White, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "More"
                                        showDrawerMenu = false
                                    }
                                )
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF334155)))
                                DropdownMenuItem(
                                    text = { Text("🔒 Lock Vault Now", color = Color(0xFFEF4444), style = MaterialTheme.typography.bodyMedium) },
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        viewModel.lockVault()
                                        showDrawerMenu = false
                                    }
                                )
                            }
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
                            
                            val homeAvatarModel = remember(ownerAvatarUri) {
                                if (ownerAvatarUri.isEmpty()) {
                                    null
                                } else if (ownerAvatarUri.startsWith("res_name:")) {
                                    val name = ownerAvatarUri.removePrefix("res_name:")
                                    val resId = context.resources.getIdentifier(name, "drawable", context.packageName)
                                    if (resId != 0) resId else null
                                } else {
                                    ownerAvatarUri
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(ThemePurple.copy(alpha = 0.2f))
                                    .border(1.dp, ThemePurple.copy(alpha = 0.4f), CircleShape)
                                    .clickable {
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Profile"
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (homeAvatarModel != null) {
                                    coil.compose.AsyncImage(
                                        model = homeAvatarModel,
                                        contentDescription = "Profile",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .scale(ownerAvatarScale)
                                            .offset(
                                                x = (ownerAvatarOffsetX * 40f / 112f).dp,
                                                y = (ownerAvatarOffsetY * 40f / 112f).dp
                                            ),
                                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Profile",
                                        tint = ThemePurple,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                } else if (activeSection !in listOf("Photos", "Videos", "Documents", "Notes", "Music & Audio", "Password_Generator", "Secure_Voice_Note", "Metadata_Cleaner", "About")) {
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
                                    .background(Color(0xFF161B2B).copy(alpha = 0.95f))
                                    .border(
                                        width = 1.dp,
                                        color = if (activeSection == "Profile") ThemePurple.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.05f),
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = if (activeSection == "Profile") ThemePurple else Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                if (activeSection == "Profile") {
                                    Text(
                                        text = "VIP Vault Studio",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        style = androidx.compose.ui.text.TextStyle(
                                            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                                colors = listOf(
                                                    Color.White,
                                                    ThemePurple,
                                                    Color(0xFFFFB300)
                                                )
                                            )
                                        ),
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                } else {
                                    Text(
                                        text = if (activeSection.startsWith("Placeholder_")) {
                                            activeSection.removePrefix("Placeholder_").replace("_", " ")
                                        } else if (activeSection == "Security") {
                                            "Security"
                                        } else if (activeSection == "Authentication") {
                                            "Authentication"
                                        } else if (activeSection == "Protection") {
                                            "Protection"
                                        } else if (activeSection == "Shake to Exit") {
                                            "Shake to Exit"
                                        } else if (activeSection == "Monitoring") {
                                            "Monitoring"
                                        } else {
                                            viewModel.t(
                                                when(activeSection) {
                                                    "Notes" -> "notes"
                                                    "Photos & Videos" -> "photos_videos"
                                                    "Documents" -> "documents"
                                                    "Private Browser" -> "private_browser"
                                                    "Explore" -> "explore"
                                                    "More" -> "settings"
                                                    "Privacy Settings" -> "security_settings"
                                                    "Decoy Space" -> "fake_vault"
                                                    "Change PIN" -> "change_pin"
                                                    "Backup" -> "backup"
                                                    "App Disguise" -> "app_disguise"
                                                    "About" -> "about"
                                                    else -> "secure_vault"
                                                }
                                            )
                                        },
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
                            if (activeSection == "Recently Deleted") {
                                Text(
                                    text = "Deleted items are automatically removed after 30 days.",
                                    fontSize = 10.sp,
                                    color = TextMedium.copy(alpha = 0.8f),
                                    modifier = Modifier.padding(top = 2.dp)
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
                            if (activeSection != "Recently Deleted") {
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
                            val totalVaultItems = vaultFiles.size + vaultNotes.size
                            val currentHour = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("Asia/Kolkata")).get(java.util.Calendar.HOUR_OF_DAY)
                            val greeting = when (currentHour) {
                                in 5..11 -> "Good Morning 🌅"
                                in 12..16 -> "Good Afternoon ☀️"
                                in 17..19 -> "Good Evening 🌆"
                                else -> "Good Night 🌙"
                            }
                            
                            val vaultStatusText = if (totalVaultItems == 0) {
                                "Ready to organize your private files"
                            } else {
                                "$totalVaultItems items stored privately"
                            }
                            
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = greeting,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = 0.5.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = vaultStatusText,
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.5f)
                                )
                            }
                                             // Status Card
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Brush.verticalGradient(listOf(Color(0xFF1E2640), Color(0xFF161B2B))))
                                    .border(1.dp, ThemePurple.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                                    .padding(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(ThemePurple.copy(alpha = 0.15f))
                                            .border(1.dp, ThemePurple.copy(alpha = 0.3f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Home, contentDescription = null, tint = ThemePurple, modifier = Modifier.size(20.dp))
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "PRIVATE WORKSPACE",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White.copy(alpha = 0.6f),
                                            letterSpacing = 1.sp
                                        )
                                        Spacer(modifier = Modifier.height(1.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "${vaultFiles.size + vaultNotes.size} items stored",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ThemePurple, modifier = Modifier.size(13.dp))
                                        }
                                    }
                                }
                            }

                            Text(
                                text = "STORAGE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.5f),
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { 
                                         viewModel.triggerKeypressEffects(context)
                                         activeSection = "Storage" 
                                    },
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B2B).copy(alpha = 0.95f)),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Folder, contentDescription = "Storage", tint = ThemePurple, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Vault Storage", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("View Details", color = ThemePurple, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            Icon(Icons.Default.ChevronRight, contentDescription = "View Details", tint = ThemePurple, modifier = Modifier.size(12.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    val storageInfo by viewModel.storageInfo.collectAsStateWithLifecycle()
                                    val maxStorage = 15L * 1024 * 1024 * 1024 // 15 GB
                                    val progress = if (maxStorage > 0) (storageInfo.totalBytes.toFloat() / maxStorage.toFloat()).coerceIn(0f, 1f) else 0f
                                    LinearProgressIndicator(
                                        progress = { progress },
                                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                                        color = ThemePurple,
                                        trackColor = Color(0xFF090D1A),
                                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("${storageInfo.totalUsedFormatted} Used", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                        val freeBytes = maxStorage - storageInfo.totalBytes
                                        val freeFormatted = if (freeBytes <= 0) "0 B" else {
                                            val units = arrayOf("B", "KB", "MB", "GB", "TB")
                                            val digitGroups = (Math.log10(freeBytes.toDouble()) / Math.log10(1024.0)).toInt()
                                            val index = if (digitGroups > 4) 4 else digitGroups
                                            val num = freeBytes / Math.pow(1024.0, index.toDouble())
                                            String.format(java.util.Locale.US, "%.1f %s", num, units[index])
                                        }
                                        Text("$freeFormatted Free", color = TextMedium, fontSize = 11.sp)
                                    }
                                }
                            }
                            
                            Text(
                                text = "FILES & MEDIA",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.5f),
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(top = 16.dp)
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
                                    title = "Audio",
                                     count = let { val c = vaultFiles.count { it.contains("|||audio/") }; "$c ${if (c == 1) "Item" else "Items"}" },
                                     icon = Icons.Default.AudioFile,
                                    modifier = Modifier.weight(1f),
                                    previewContent = {
                                        val latestAudio = vaultFiles.firstOrNull { it.contains("|||audio/") }
                                        if (latestAudio != null) {
                                            val parts = latestAudio.split("|||")
                                            val title = if (parts.size >= 3) parts[2] else latestAudio.split("|||")[0].substringAfterLast('/')
                                            Row(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Icon(Icons.Default.AudioFile, contentDescription = null, tint = ThemePurple, modifier = Modifier.size(16.dp))
                                                Text(cleanDisplayName(title, "audio", parts.getOrNull(0) ?: ""), color = Color.White, fontSize = 10.sp, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                            }
                                        } else {
                                            Row(
                                                modifier = Modifier.fillMaxSize().padding(8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Box(modifier = Modifier.width(4.dp).height(12.dp).clip(CircleShape).background(Color(0xFF262D45)))
                                                Box(modifier = Modifier.width(4.dp).height(20.dp).clip(CircleShape).background(ThemePurple.copy(alpha=0.5f)))
                                                Box(modifier = Modifier.width(4.dp).height(12.dp).clip(CircleShape).background(Color(0xFF262D45)))
                                                Box(modifier = Modifier.width(4.dp).height(8.dp).clip(CircleShape).background(Color(0xFF262D45)))
                                            }
                                        }
                                    },
                                    onClick = { 
                                        viewModel.triggerKeypressEffects(context)
                                        activeSection = "Music & Audio" 
                                    }
                                )
                            }
                            
                            // Notes full width
                            EnhancedVaultCard(
                                title = "Notes",
                                 count = let { val c = vaultNotes.size; "$c ${if (c == 1) "Item" else "Items"}" },
                                 icon = Icons.Default.List,
                                modifier = Modifier.fillMaxWidth(),
                                previewContent = {
                                    val latestNote = vaultNotes.firstOrNull()
                                    if (latestNote != null) {
                                        val parts = latestNote.split("|||")
                                        val body = if (parts.size >= 3) parts[2] else ""
                                        Text(parseRichTextToAnnotatedString(body), color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, maxLines = 2, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis, modifier = Modifier.padding(8.dp).fillMaxWidth(), lineHeight = 12.sp)
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
                            
                            // Recent Activity
                            Text(
                                text = "RECENT ACTIVITY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.5f),
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                            )
                            
                            if (vaultFiles.isEmpty() && vaultNotes.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Your secrets are safe here.", color = Color.White.copy(alpha = 0.4f), fontSize = 14.sp)
                                }
                            } else {
                                val recentFiles = vaultFiles.take(5)
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
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
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                                
                            // Recycle Bin Section Header
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "RECYCLE BIN",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.5f),
                                    letterSpacing = 2.sp
                                )
                            }

                            // Recycle Bin Quick Access Card
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
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
                                    val recentlyDeleted by viewModel.recentlyDeletedFiles.collectAsStateWithLifecycle()
                                    Text(
                                        text = if (recentlyDeleted.isEmpty()) "Empty" else "${recentlyDeleted.size} items", 
                                        color = Color.White.copy(alpha = 0.4f), 
                                        fontSize = 12.sp
                                    )
                                }
                                Icon(Icons.Default.ArrowForward, contentDescription = "Open", tint = Color.White.copy(alpha = 0.3f), modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                "Photos", "Videos", "Documents", "Notes", "Music & Audio" -> {
                    val vaultFiles by viewModel.vaultFiles.collectAsStateWithLifecycle()
                    val favoriteFiles by viewModel.favoriteFiles.collectAsStateWithLifecycle()
                    val fileFolders by viewModel.fileFolders.collectAsStateWithLifecycle()
                    val vaultNotes by viewModel.vaultNotes.collectAsStateWithLifecycle()
                    val favoriteNotes by viewModel.favoriteNotes.collectAsStateWithLifecycle()
                    val noteFolders by viewModel.noteFolders.collectAsStateWithLifecycle()
                    val vaultFolders by viewModel.vaultFolders.collectAsStateWithLifecycle()
                    val lockedFolders by viewModel.lockedFolders.collectAsStateWithLifecycle()
                    val tempUnlockedFolders by viewModel.tempUnlockedFolders.collectAsStateWithLifecycle()
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
                                val parts = noteStr.split("|||", limit = 3)
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
                                "Notes" -> {
                                    val newNoteStr = viewModel.addVaultNote("Untitled Note", " ")
                                    viewNoteToShow = newNoteStr
                                    isEditingNote = true
                                    isNewDraftNote = true
                                }
                                "Photos", "Videos" -> showMediaAddOptions = true
                                "Documents" -> showDocAddOptions = true
                                "Music & Audio" -> {
                                    viewModel.isPickingFile = true
                                    audioPickerLauncher.launch(arrayOf("audio/*"))
                                }
                            }
                        },
                        onItemClick = { item, index, allFiltered -> 
                            if (item.type == "note") {
                                viewNoteToShow = item.rawString
                            } else {
                                secureShareFileSerialized = item.rawString
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
                            .fillMaxWidth(),
                        onExit = {
                            activeSection = "__BACK__"
                        },
                        onPanic = {
                            viewModel.lockVault()
                            onLockExit()
                        }
                    )
                }
                "App Privacy" -> {
                    Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        AppLockSection(viewModel = viewModel)
                    }
                }
                "Intruder Alerts", "Access Logs", "Monitoring" -> {
                    Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        MonitoringSection(
                            viewModel = viewModel,
                            onNavigateBack = { activeSection = "__BACK__" }
                        )
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
                    val recentlyDeletedFiles by viewModel.recentlyDeletedFiles.collectAsStateWithLifecycle()
                    if (recentlyDeletedFiles.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "🗑️",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Recently Deleted is empty.",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Deleted items remain here for 30 days before being permanently removed.",
                                fontSize = 14.sp,
                                color = TextMedium.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }
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
                                                val ctx = androidx.compose.ui.platform.LocalContext.current
                                                val imageLoader = remember(ctx) {
                                                    coil.ImageLoader.Builder(ctx)
                                                        .components { add(coil.decode.VideoFrameDecoder.Factory()) }
                                                        .build()
                                                }
                                                Box(
                                                    modifier = Modifier
                                                        .size(50.dp)
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(ThemeLightPurple),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    if (mimeType.startsWith("image/") || mimeType.startsWith("video/")) {
                                                        AsyncImage(
                                                            model = java.io.File(path),
                                                            imageLoader = imageLoader,
                                                            contentDescription = originalName,
                                                            modifier = Modifier.fillMaxSize(),
                                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                                            error = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_gallery)
                                                        )
                                                        if (mimeType.startsWith("video/")) {
                                                            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)), contentAlignment = Alignment.Center) {
                                                                Icon(
                                                                    imageVector = Icons.Default.PlayArrow,
                                                                    contentDescription = "Video",
                                                                    tint = Color.White,
                                                                    modifier = Modifier.size(24.dp)
                                                                )
                                                            }
                                                        }
                                                    } else if (mimeType.startsWith("audio/")) {
                                                        Icon(
                                                            imageVector = Icons.Default.MusicNote,
                                                            contentDescription = "Audio",
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
                                                        text = "Auto deletes in $remainingDays days",
                                                        fontSize = 11.sp,
                                                        color = Color(0xFFFF6B6B).copy(alpha = 0.8f),
                                                        fontWeight = FontWeight.Medium
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
                                                            coroutineScope.launch { snackbarHostState.showSnackbar("File restored successfully.") }
                                                        } else {
                                                            coroutineScope.launch { snackbarHostState.showSnackbar("Failed to restore.") }
                                                        }
                                                    },
                                                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(8.dp),
                                                    
                                                ) {
                                                    Icon(Icons.Default.Restore, contentDescription = "Restore", tint = if (IsWhiteTheme) Color(0xFF1E2235) else Color.White, modifier = Modifier.size(14.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Restore", fontSize = 11.sp, color = if (IsWhiteTheme) Color(0xFF1E2235) else Color.White)
                                                }
                                                Button(
                                                    onClick = {
                                                        viewModel.triggerKeypressEffects(context)
                                                        showDeleteConfirm = recentStr
                                                    },
                                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(8.dp),
                                                    
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
                        if (showDeleteConfirm != null) {
                            androidx.compose.material3.AlertDialog(
                                onDismissRequest = { showDeleteConfirm = null },
                                title = { Text("Delete Permanently?", fontWeight = FontWeight.Bold, color = Color.White) },
                                text = { Text("This action cannot be undone.", color = Color.White.copy(alpha = 0.7f)) },
                                containerColor = Color(0xFF1B2031),
                                titleContentColor = Color.White,
                                textContentColor = Color.White.copy(alpha = 0.8f),
                                confirmButton = {
                                    androidx.compose.material3.TextButton(
                                        onClick = {
                                            viewModel.triggerKeypressEffects(context)
                                            val deleted = viewModel.deletePermanentlyFromRecent(showDeleteConfirm!!)
                                            if (deleted) {
                                                coroutineScope.launch { snackbarHostState.showSnackbar("Permanently Deleted") }
                                            }
                                            showDeleteConfirm = null
                                        },
                                        colors = androidx.compose.material3.ButtonDefaults.textButtonColors(contentColor = Color(0xFFFF6B6B))
                                    ) {
                                        Text("Delete", fontWeight = FontWeight.Bold)
                                    }
                                },
                                dismissButton = {
                                    androidx.compose.material3.TextButton(onClick = { showDeleteConfirm = null }) {
                                        Text("Cancel", color = Color.White.copy(alpha = 0.7f))
                                    }
                                }
                            )
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
                        // 1. Top Status Card
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(ThemePurple.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Shield,
                                            contentDescription = null,
                                            tint = ThemePurple,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Secret Vault",
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Private Workspace",
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                                
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(100.dp))
                                        .background(Color(0xFF00E676).copy(alpha = 0.12f))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF00E676))
                                    )
                                    Text(
                                        text = "Your vault is protected.",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF00E676)
                                    )
                                }
                            }
                        }

                        // 2. Main Settings Sections Group
                        Text(
                            text = "SYSTEM SETTINGS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMedium,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 2.dp)
                        )

                        CompactSettingsCard(
                            title = "Security",
                            subtitle = "Workspace lock, biometrics, and auto-lock",
                            icon = Icons.Default.Lock,
                            iconColor = Color(0xFF2979FF),
                            onClick = { activeSection = "Security" }
                        )

                        CompactSettingsCard(
                            title = "App Disguise",
                            subtitle = "Camouflage icon, stealth layout, and decoy space",
                            icon = Icons.Default.Palette,
                            iconColor = Color(0xFF00B0FF),
                            onClick = { activeSection = "App Disguise" }
                        )

                        CompactSettingsCard(
                            title = "Backup",
                            subtitle = "Secure cloud backup, recovery, and offline local storage",
                            icon = Icons.Default.CloudQueue,
                            iconColor = Color(0xFFD4E157),
                            onClick = { activeSection = "Backup" }
                        )

                        // 3. Privacy Tools Section
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "PRIVACY TOOLS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMedium,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 2.dp)
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    PrivacyToolGridCard(
                                        title = "Secure Camera",
                                        icon = Icons.Default.CameraAlt,
                                        iconColor = Color(0xFFEC407A),
                                        onClick = { activeCameraMode = "camera" }
                                    )
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    PrivacyToolGridCard(
                                        title = "QR Scanner",
                                        icon = Icons.Default.QrCode,
                                        iconColor = Color(0xFF00E676),
                                        onClick = { activeCameraMode = "scanner" }
                                    )
                                }
                            }
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    PrivacyToolGridCard(
                                        title = "Password Generator",
                                        icon = Icons.Default.VpnKey,
                                        iconColor = Color(0xFFFFD600),
                                        onClick = { activeSection = "Password_Generator" }
                                    )
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    PrivacyToolGridCard(
                                        title = "Metadata Cleaner",
                                        icon = Icons.Default.CleaningServices,
                                        iconColor = Color(0xFF26C6DA),
                                        onClick = { activeSection = "Metadata_Cleaner" }
                                    )
                                }
                            }
                            
                            PrivacyToolWideCard(
                                title = "Secure Voice Note",
                                subtitle = "Record and save encrypted audio notes safely",
                                icon = Icons.Default.Mic,
                                iconColor = Color(0xFFEF5350),
                                onClick = { activeSection = "Secure_Voice_Note" }
                            )
                        }

                        // 4. About Section
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "ABOUT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMedium,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 2.dp)
                        )

                        CompactSettingsCard(
                            title = "About",
                            subtitle = "Secret Vault version, licenses, and info",
                            icon = Icons.Default.Info,
                            iconColor = Color(0xFF26A69A),
                            onClick = { activeSection = "About" }
                        )

                        Spacer(modifier = Modifier.height(100.dp))
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
                            val preventScreenshots by viewModel.preventScreenshots.collectAsStateWithLifecycle()
                            val screenDownLock by viewModel.screenDownLock.collectAsStateWithLifecycle()
                            val panicEnabled by viewModel.panicEnabled.collectAsStateWithLifecycle()
                            val biometricEnabled by viewModel.biometricEnabled.collectAsStateWithLifecycle()
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
                                title = "Shake to Exit",
                                subtitle = "Shake phone hard to quickly exit your vault",
                                icon = Icons.Default.Refresh,
                                iconTint = Color(0xFFEC407A),
                                checked = panicEnabled,
                                onCheckedChange = { viewModel.setPanicEnabled(it) }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            val intruderDetectionEnabled by viewModel.intruderDetectionEnabled.collectAsStateWithLifecycle()
                            SettingsSwitchRow(
                                title = "Access Logs",
                                subtitle = "Log incorrect PIN attempts & keys entered",
                                icon = Icons.Default.Warning,
                                iconTint = Color(0xFFFF9100),
                                checked = intruderDetectionEnabled,
                                onCheckedChange = { viewModel.setIntruderDetectionEnabled(it) }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            val blurThumbnails by viewModel.blurThumbnails.collectAsStateWithLifecycle()
                            SettingsSwitchRow(
                                title = "Blur Thumbnails",
                                subtitle = "Blurs media previews to prevent shoulder-surfing",
                                icon = Icons.Default.BlurOn,
                                iconTint = Color(0xFF00E5FF),
                                checked = blurThumbnails,
                                onCheckedChange = { viewModel.setBlurThumbnails(it) }
                            )
                            Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                            val autoLockDuration by viewModel.autoLockDuration.collectAsStateWithLifecycle()
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
                                    shape = RoundedCornerShape(8.dp),
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
                        val panicEnabled by viewModel.panicEnabled.collectAsStateWithLifecycle()
                        if (panicEnabled) {
                            val panicAction by viewModel.panicAction.collectAsStateWithLifecycle()
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
                            val currentLangCode by viewModel.selectedLanguage.collectAsStateWithLifecycle()
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
                            val currentTimezone by viewModel.preferredTimezone.collectAsStateWithLifecycle()
                            val timezoneDisplay = when (currentTimezone) {
                                "Asia/Kolkata" -> "India Standard Time (IST)"
                                "America/Los_Angeles" -> "US Pacific Time (PST/PDT)"
                                "UTC" -> "Universal Time (UTC)"
                                "System" -> "Device Default"
                                else -> currentTimezone
                            }
                            SettingsActionRow(
                                title = "Preferred Time Zone",
                                subtitle = timezoneDisplay,
                                icon = Icons.Default.Timer,
                                iconTint = Color(0xFF00E676),
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    showTimezoneDialog = true
                                }
                            )
                        }
                    }
                }
                "Change PIN" -> {
                    LaunchedEffect(activeSection) {
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
                                            focusedContainerColor = Color(0xFF121622),
                                            unfocusedContainerColor = Color(0xFF121622),
                                            focusedBorderColor = ThemePurple,
                                            unfocusedBorderColor = Color(0xFF383F56),
                                            cursorColor = ThemePurple,
                                            focusedLabelColor = ThemePurple,
                                            unfocusedLabelColor = TextMedium
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
                                    val btnContentColor = if (IsWhiteTheme) Color(0xFF1E2235) else Color.White
                                    Text("Save PIN", color = btnContentColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
                "Decoy Space" -> {
                    LaunchedEffect(activeSection) {
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
                                            focusedContainerColor = Color(0xFF121622),
                                            unfocusedContainerColor = Color(0xFF121622),
                                            focusedBorderColor = Color(0xFFE57373),
                                            unfocusedBorderColor = Color(0xFF383F56),
                                            cursorColor = Color(0xFFE57373),
                                            focusedLabelColor = Color(0xFFE57373),
                                            unfocusedLabelColor = TextMedium
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
                                    val btnContentColor = if (IsWhiteTheme) Color(0xFF1E2235) else Color.White
                                    Text("Save Decoy PIN", color = btnContentColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
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
                        // Polished Mock Device Preview Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF131722))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "HOME SCREEN PREVIEW",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.Start)
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                // Render Mock Launcher Icon
                                val activeOptionName = when (activeAppIcon) {
                                    "LauncherCalculator" -> "Calculator"
                                    "LauncherCalcClassic" -> "Classic Calc"
                                    "LauncherCalcRetro" -> "Retro Calc"
                                    "LauncherCalcNeon" -> "Neon Calc"
                                    "LauncherNotes" -> "Notes"
                                    "LauncherCompass" -> "Compass"
                                    "LauncherVoice" -> "Recorder"
                                    "LauncherSudoku" -> "Sudoku"
                                    "LauncherWeather" -> "Weather"
                                    else -> "Calculator"
                                }

                                val activeOptionColor = when (activeAppIcon) {
                                    "LauncherCalculator" -> Color(0xFF2196F3)
                                    "LauncherCalcClassic" -> Color(0xFF607D8B)
                                    "LauncherCalcRetro" -> Color(0xFF8D6E63)
                                    "LauncherCalcNeon" -> Color(0xFF00E5FF)
                                    "LauncherNotes" -> Color(0xFFFFB300)
                                    "LauncherCompass" -> Color(0xFF009688)
                                    "LauncherVoice" -> Color(0xFFE53935)
                                    "LauncherSudoku" -> Color(0xFF3F51B5)
                                    "LauncherWeather" -> Color(0xFF03A9F4)
                                    else -> Color(0xFF2196F3)
                                }

                                val activeOptionIcon = when (activeAppIcon) {
                                    "LauncherCalculator", "LauncherCalcClassic", "LauncherCalcRetro", "LauncherCalcNeon" -> Icons.Default.Calculate
                                    "LauncherNotes" -> Icons.Default.Article
                                    "LauncherCompass" -> Icons.Default.ScreenRotation
                                    "LauncherVoice" -> Icons.Default.MusicNote
                                    "LauncherSudoku" -> Icons.Default.GridView
                                    "LauncherWeather" -> Icons.Default.CloudQueue
                                    else -> Icons.Default.Calculate
                                }

                                AppDisguiseIconPreview(
                                    id = activeAppIcon,
                                    modifier = Modifier.size(72.dp)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = activeOptionName,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "This is how the app appears on your phone's home screen. Click any disguise below to change it.",
                                    color = TextMedium,
                                    fontSize = 12.sp,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }

                        // Single active disguise selector layout
                        Text(
                            text = "ACTIVE DISGUISE",
                            color = TextMedium,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(2.dp, ThemePurple),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                AppDisguiseIconPreview(
                                    id = "LauncherCalculator",
                                    modifier = Modifier.size(64.dp)
                                )
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Calculator Vault (Default)",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Premium luxury vault interface seamlessly disguised as a fully functional standard scientific calculator.",
                                        color = TextMedium,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(ThemePurple, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Polished Upcoming Disguises Information block
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF242B3F))
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ThemePurple.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "UPCOMING DISGUISES",
                                        color = ThemePurple,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                Text(
                                    text = "New Invisibility Themes Coming Soon",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "To guarantee absolute stealth on your home screen, we are currently designing new icon and app name disguise packages that will bypass advanced system scanner frameworks:\n\n" +
                                            "• Notes App Disguise (Stylized yellow notepad wrapper)\n" +
                                            "• Utility Tools (Compass navigation & Voice Recorder templates)\n" +
                                            "• Gaming / Puzzle Launchers (Sudoku classic board simulation)\n" +
                                            "• Live Climate Forecast Disguise (Weather radar mockups)\n" +
                                            "• Retro/Neon Calculator Themes (80s hardware & Cyberpunk aesthetics)\n\n" +
                                            "All upcoming themes will arrive in the next major update, keeping your privacy secure and 100% offline.",
                                    color = TextMedium,
                                    fontSize = 13.sp,
                                    lineHeight = 19.sp
                                )
                            }
                        }
                    }
                }
                "Backup" -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Icon(
                                imageVector = Icons.Default.SwapVert, 
                                contentDescription = "Export/Import", 
                                tint = Color(0xFFD4E157), 
                                modifier = Modifier.size(72.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Offline Local Backup",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Create a fully offline backup of all your hidden files, secret notes, folders, browser data, and settings into a single secure file on your storage.",
                                color = TextMedium,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )

                            Spacer(modifier = Modifier.height(28.dp))

                            Button(
                                onClick = { 
                                    try {
                                        exportBackupLauncher.launch("vault_backup.zip")
                                    } catch (e: Exception) {
                                        android.widget.Toast.makeText(context, "Failed to launch export dialog", android.widget.Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .height(52.dp)
                                    .testTag("create_backup_button")
                            ) {
                                val btnContentColor = if (IsWhiteTheme) Color(0xFF1E2235) else Color.White
                                Icon(Icons.Default.Lock, contentDescription = null, tint = btnContentColor, modifier = Modifier.padding(end = 8.dp))
                                Text("Create Offline Backup", color = btnContentColor, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedButton(
                                onClick = { 
                                    try {
                                        importBackupLauncher.launch(arrayOf("application/zip", "application/octet-stream", "*/*"))
                                    } catch (e: Exception) {
                                        android.widget.Toast.makeText(context, "Failed to launch import dialog", android.widget.Toast.LENGTH_SHORT).show()
                                    }
                                },
                                border = BorderStroke(1.5.dp, ThemePurple),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ThemePurple),
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .height(52.dp)
                                    .testTag("restore_backup_button")
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, tint = ThemePurple, modifier = Modifier.padding(end = 8.dp))
                                Text("Restore Previous Backup", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Local Backup Guide Card
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF242B3F)),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(18.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "💾 Offline Local Backup Guide",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "How it works:",
                                            color = Color.White.copy(alpha = 0.9f),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "1. Tap 'Create Offline Backup'.\n2. Use the system folder picker to select a safe location (e.g., Download folder, SD card, or USB) and save the backup file ('vault_backup.zip').\n3. To restore, tap 'Restore Previous Backup' and choose that zip file.",
                                            color = TextMedium,
                                            fontSize = 12.sp,
                                            lineHeight = 18.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "Key Benefits:",
                                            color = Color.White.copy(alpha = 0.9f),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "• 100% Offline: Absolutely no internet required. Complete control over your backup files.\n• Portability: You can copy, transfer, or email the zip file to your PC, SD card, or secondary devices.",
                                            color = TextMedium,
                                            fontSize = 12.sp,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Google Drive Cloud Backup Upcoming Card
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2235)),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF4285F4).copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CloudQueue,
                                            contentDescription = "Cloud Backup",
                                            tint = Color(0xFF4285F4),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = "Google Drive Cloud Sync",
                                                color = Color.White,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(Color(0xFFFFCC00).copy(alpha = 0.15f))
                                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = "SOON",
                                                    color = Color(0xFFFFCC00),
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.ExtraBold
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Automatic cloud backup & restore synchronization via Google Drive is undergoing final cryptographic security audits to guarantee 100% data isolation. Coming in the next release.",
                                            color = TextMedium,
                                            fontSize = 12.sp,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Progress Dialog overlay
                    if (isBackupRestoreProcessing) {
                        androidx.compose.ui.window.Dialog(
                            onDismissRequest = {}
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2235)),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .testTag("backup_progress_dialog")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = ThemePurple,
                                        strokeWidth = 4.dp,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(
                                        text = "Syncing with Cloud",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Please do not close the app or lock your screen...",
                                        color = TextMedium,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
                "Storage" -> {
                    StorageScreenSection(
                        onBack = { activeSection = "__BACK__" },
                        onNavigateToRecentlyDeleted = { activeSection = "Recently Deleted" }
                    )
                }
                "Security" -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Top Hero Card
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(ThemePurple.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Shield,
                                            contentDescription = null,
                                            tint = ThemePurple,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Vault Security",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            modifier = Modifier
                                                .padding(top = 4.dp)
                                                .clip(RoundedCornerShape(100.dp))
                                                .background(Color(0xFF00E676).copy(alpha = 0.12f))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFF00E676))
                                            )
                                            Text(
                                                text = "Protected",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF00E676)
                                            )
                                        }
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(2.dp))
                                
                                Text(
                                    text = "Manage your vault authentication, protection and security preferences.",
                                    fontSize = 13.sp,
                                    color = Color.White.copy(alpha = 0.6f),
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        // Main Security Sections
                        Text(
                            text = "SECURITY SECTIONS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMedium,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 2.dp)
                        )

                        CompactSettingsCard(
                            title = "Authentication",
                            subtitle = "PIN, Fake PIN and Biometric",
                            icon = Icons.Default.Fingerprint,
                            iconColor = Color(0xFF2979FF),
                            onClick = { activeSection = "Authentication" }
                        )

                        CompactSettingsCard(
                            title = "Protection",
                            subtitle = "Vault protection and security controls",
                            icon = Icons.Default.Shield,
                            iconColor = Color(0xFF00E676),
                            onClick = { activeSection = "Protection" }
                        )

                        CompactSettingsCard(
                            title = "Shake to Exit",
                            subtitle = "Instantly exit the vault by shaking your phone",
                            icon = Icons.Default.ExitToApp,
                            iconColor = Color(0xFFEF5350),
                            onClick = { activeSection = "Shake to Exit" }
                        )

                        CompactSettingsCard(
                            title = "Monitoring",
                            subtitle = "Intruder detection and security activity",
                            icon = Icons.Default.History,
                            iconColor = Color(0xFFFF9100),
                            onClick = { activeSection = "Monitoring" }
                        )

                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
                "Authentication" -> {
                    var showAppLockDialog by remember { mutableStateOf(false) }
                    var appLockMethod by remember { mutableStateOf("PIN Only") }
                    var isRecoveryInstructionEnglish by remember { mutableStateOf(false) }
                    val biometricEnabledState by viewModel.biometricEnabled.collectAsStateWithLifecycle()
                    val biometricModeState by viewModel.biometricMode.collectAsStateWithLifecycle()
                    val securityQuestionState by viewModel.securityQuestion.collectAsStateWithLifecycle()
                    val securityAnswerState by viewModel.securityAnswer.collectAsStateWithLifecycle()
                    val recoveryCodeState by viewModel.recoveryCode.collectAsStateWithLifecycle()

                    var tempQuestion by remember(securityQuestionState) { mutableStateOf(securityQuestionState) }
                    var tempAnswer by remember(securityAnswerState) { mutableStateOf(securityAnswerState) }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Top Hero Card
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(ThemePurple.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LockOpen,
                                            contentDescription = null,
                                            tint = ThemePurple,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Authentication",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Security Controls",
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(2.dp))
                                
                                Text(
                                    text = "Manage how you securely access your private vault.",
                                    fontSize = 13.sp,
                                    color = Color.White.copy(alpha = 0.6f),
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        // Options Section Title
                        Text(
                            text = "AUTHENTICATION PREFERENCES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMedium,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 2.dp)
                        )

                        // 1. Change PIN
                        CompactSettingsCard(
                            title = "Change PIN",
                            subtitle = "Update your current vault PIN.",
                            icon = Icons.Default.VpnKey,
                            iconColor = Color(0xFF2979FF),
                            onClick = { activeSection = "Change PIN" }
                        )

                        // 2. Change Fake PIN
                        CompactSettingsCard(
                            title = "Change Fake PIN",
                            subtitle = "Update the PIN used for the fake vault.",
                            icon = Icons.Default.Lock,
                            iconColor = Color(0xFFEF5350),
                            onClick = { activeSection = "Decoy Space" }
                        )

                        // 3. Biometric Unlock
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 2.dp,
                            onClick = {
                                viewModel.setBiometricEnabled(!biometricEnabledState)
                            }
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
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFF00E676).copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Fingerprint,
                                            contentDescription = "Biometric Unlock",
                                            tint = Color(0xFF00E676),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Biometric Unlock",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Use fingerprint or face authentication.",
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.5f),
                                            lineHeight = 14.sp
                                        )
                                        if (biometricEnabledState) {
                                            Text(
                                                text = "🔑 Tip: Long-press the '=' button on the calculator screen to trigger biometric unlock.",
                                                fontSize = 11.sp,
                                                color = Color(0xFF00E676),
                                                lineHeight = 14.sp,
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }
                                    }
                                }
                                androidx.compose.material3.Switch(
                                    checked = biometricEnabledState,
                                    onCheckedChange = { enabled ->
                                        viewModel.setBiometricEnabled(enabled)
                                    },
                                    colors = dynamicSwitchColors()
                                )
                            }
                        }

                        // 4. App Lock Method
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 2.dp,
                            onClick = { showAppLockDialog = true }
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
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFFF9100).copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Security,
                                            contentDescription = "App Lock Method",
                                            tint = Color(0xFFFF9100),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "App Lock Method",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Choose how the vault should authenticate access.",
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.5f),
                                            lineHeight = 14.sp
                                        )
                                        Text(
                                            text = "Current: $appLockMethod",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ThemePurple,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Open",
                                    tint = Color.White.copy(alpha = 0.3f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // PIN Recovery Options Section
                        Text(
                            text = "PIN RECOVERY & SECURITY BACKUPS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMedium,
                            modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 2.dp)
                        )
                        
                        // Instructional Attention-grabbing Alert
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            bgColor = Color(0xFFFF3D00).copy(alpha = 0.08f),
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Crucial",
                                    tint = Color(0xFFFF3D00),
                                    modifier = Modifier.size(24.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Configure Recovery Settings (Recommended)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Enabling recovery triggers is critical. If you ever forget your vault access PIN, these parameters act as the only bypass to recover access to your private files safely.",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.7f),
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }

                        // How to Trigger Recovery Instruction
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            bgColor = Color(0xFF00E676).copy(alpha = 0.08f),
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.Info,
                                    contentDescription = "Instruction",
                                    tint = Color(0xFF00E676),
                                    modifier = Modifier.size(24.dp).padding(top = 2.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (isRecoveryInstructionEnglish) "💡 How to use recovery?" else "💡 Recovery bypass kaise use karein?",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.weight(1f)
                                        )
                                        
                                        Row(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.White.copy(alpha = 0.08f))
                                                .border(0.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(if (!isRecoveryInstructionEnglish) Color(0xFF00E676).copy(alpha = 0.25f) else Color.Transparent)
                                                    .clickable { isRecoveryInstructionEnglish = false }
                                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("HI", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (!isRecoveryInstructionEnglish) Color(0xFF00E676) else Color.White.copy(alpha = 0.6f))
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(if (isRecoveryInstructionEnglish) Color(0xFF00E676).copy(alpha = 0.25f) else Color.Transparent)
                                                    .clickable { isRecoveryInstructionEnglish = true }
                                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("EN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isRecoveryInstructionEnglish) Color(0xFF00E676) else Color.White.copy(alpha = 0.6f))
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = if (isRecoveryInstructionEnglish) {
                                            "If you ever forget your PIN, simply long-press (press and hold for 2-3 seconds) the 'Calculator' title text on the top-left bar of the main Calculator screen. This will trigger the bypass window where you can enter your Security Answer or Master Recovery Code to reset your PIN safely."
                                        } else {
                                            "Agar aap kabhi apna PIN bhul jate hain, toh main Calculator screen ke top-left bar par likhe 'Calculator' title text ko 2-3 seconds ke liye long-press (dabaye rakhein) karein. Isse bypass window open ho jayega jahan aap apna Security Answer ya Master Recovery Code enter karke naya PIN set kar sakte hain."
                                        },
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.85f),
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }

                        // Security Question and Answer
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 2.dp
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFF2979FF).copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.QuestionAnswer,
                                            contentDescription = null,
                                            tint = Color(0xFF2979FF),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Security Question & Answer",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Recover by answering a pre-set secret question.",
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                                
                                androidx.compose.material3.OutlinedTextField(
                                    value = tempQuestion,
                                    onValueChange = { tempQuestion = it },
                                    label = { Text("Secret Question", color = Color.White.copy(alpha = 0.6f)) },
                                    placeholder = { Text("e.g. What was your first pet's name?", color = Color.White.copy(alpha = 0.3f)) },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth().testTag("security_question_input"),
                                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedLabelColor = ThemePurple,
                                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                                        focusedBorderColor = ThemePurple,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                                        cursorColor = ThemePurple
                                    )
                                )
                                
                                androidx.compose.material3.OutlinedTextField(
                                    value = tempAnswer,
                                    onValueChange = { tempAnswer = it },
                                    label = { Text("Your Secret Answer", color = Color.White.copy(alpha = 0.6f)) },
                                    placeholder = { Text("Case-insensitive, kept strictly private", color = Color.White.copy(alpha = 0.3f)) },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth().testTag("security_answer_input"),
                                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedLabelColor = ThemePurple,
                                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                                        focusedBorderColor = ThemePurple,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                                        cursorColor = ThemePurple
                                    )
                                )
                                
                                androidx.compose.material3.Button(
                                    onClick = {
                                        if (tempQuestion.isNotBlank() && tempAnswer.isNotBlank()) {
                                            viewModel.setSecurityQuestionAndAnswer(tempQuestion, tempAnswer)
                                            android.widget.Toast.makeText(context, "Security Question saved successfully!", android.widget.Toast.LENGTH_SHORT).show()
                                        } else {
                                            android.widget.Toast.makeText(context, "Please fill out both question and answer.", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier.align(Alignment.End).testTag("save_security_question_button"),
                                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                        containerColor = ThemePurple,
                                        contentColor = if (ThemePurple.red > 0.95f && ThemePurple.green > 0.95f && ThemePurple.blue > 0.95f) BrandBg else Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Save Question")
                                }
                            }
                        }

                        // Master Recovery Key / File Backup
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 2.dp
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFF00E676).copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Key,
                                            contentDescription = null,
                                            tint = Color(0xFF00E676),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Master Recovery Code & File Backup",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "A secure cryptographic recovery code and file backup.",
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                                
                                Text(
                                    text = "Your Master Recovery Code:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                                
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.Black.copy(alpha = 0.35f))
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = recoveryCodeState.ifEmpty { "Generating..." },
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF00E676),
                                        letterSpacing = 1.2.sp
                                    )
                                    
                                    IconButton(
                                        onClick = {
                                            viewModel.copyToClipboard(context, "Recovery Code", recoveryCodeState)
                                            android.widget.Toast.makeText(context, "Recovery code copied to clipboard!", android.widget.Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Copy Recovery Code",
                                            tint = Color.White.copy(alpha = 0.7f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    androidx.compose.material3.OutlinedButton(
                                        onClick = {
                                            viewModel.exportRecoveryKeyFile(
                                                onSuccess = { msg ->
                                                    android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_LONG).show()
                                                },
                                                onFailure = { err ->
                                                    android.widget.Toast.makeText(context, err, android.widget.Toast.LENGTH_LONG).show()
                                                }
                                            )
                                        },
                                        modifier = Modifier.weight(1f).testTag("export_recovery_file_button"),
                                        shape = RoundedCornerShape(12.dp),
                                        border = BorderStroke(1.dp, ThemePurple),
                                        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                                            contentColor = ThemePurple
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Save,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Save Backup File", fontSize = 11.sp)
                                    }
                                    
                                    androidx.compose.material3.Button(
                                        onClick = {
                                            // Trigger copy
                                            viewModel.copyToClipboard(context, "Recovery Code", recoveryCodeState)
                                            android.widget.Toast.makeText(context, "Recovery code copied!", android.widget.Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                            containerColor = ThemePurple,
                                            contentColor = if (ThemePurple.red > 0.95f && ThemePurple.green > 0.95f && ThemePurple.blue > 0.95f) BrandBg else Color.White
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Copy Code Only", fontSize = 11.sp)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(100.dp))
                    }

                    // Selection Dialog for App Lock Method
                    if (showAppLockDialog) {
                        androidx.compose.material3.AlertDialog(
                            onDismissRequest = { showAppLockDialog = false },
                            containerColor = Color(0xFF1B2031),
                            titleContentColor = Color.White,
                            textContentColor = Color.White.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(24.dp),
                            title = {
                                Text(
                                    text = "Select App Lock Method",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                            },
                            text = {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf("PIN Only", "Biometric Only", "PIN + Biometric").forEach { method ->
                                        val isSelected = appLockMethod == method
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (isSelected) ThemePurple.copy(alpha = 0.15f) else Color.Transparent)
                                                .clickable {
                                                    viewModel.triggerKeypressEffects(context)
                                                    appLockMethod = method
                                                    showAppLockDialog = false
                                                }
                                                .padding(horizontal = 16.dp, vertical = 12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = method,
                                                color = if (isSelected) ThemePurple else Color.White,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 14.sp
                                            )
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = ThemePurple,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                androidx.compose.material3.TextButton(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        showAppLockDialog = false
                                    }
                                ) {
                                    Text("Cancel", color = Color.White.copy(alpha = 0.6f))
                                }
                            }
                        )
                    }
                }
                "Protection" -> {
                    var showAutoLockDialog by remember { mutableStateOf(false) }
                    val lockOnBackground by viewModel.lockOnBackground.collectAsStateWithLifecycle()
                    val hideNotifications by viewModel.hideNotifications.collectAsStateWithLifecycle()
                    val clipboardProtection by viewModel.clipboardProtection.collectAsStateWithLifecycle()
                    val stealthMode by viewModel.stealthMode.collectAsStateWithLifecycle()
                    val secureShareBranding by viewModel.secureShareBranding.collectAsStateWithLifecycle()
                    
                    val autoLockDurationVal by viewModel.autoLockDuration.collectAsStateWithLifecycle()
                    val preventScreenshotsVal by viewModel.preventScreenshots.collectAsStateWithLifecycle()

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Top Hero Card
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(ThemePurple.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Shield,
                                            contentDescription = null,
                                            tint = ThemePurple,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Protection",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Security Controls",
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(2.dp))
                                
                                Text(
                                    text = "Configure how your vault protects itself.",
                                    fontSize = 13.sp,
                                    color = Color.White.copy(alpha = 0.6f),
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        // Options Section Title
                        Text(
                            text = "VAULT PROTECTION OPTIONS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMedium,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 2.dp)
                        )

                        // 1. Auto Lock
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 2.dp,
                            onClick = { showAutoLockDialog = true }
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
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFF0EA5E9).copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Timer,
                                            contentDescription = "Auto Lock",
                                            tint = Color(0xFF0EA5E9),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Auto Lock",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Automatically lock the vault after inactivity.",
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.5f),
                                            lineHeight = 14.sp
                                        )
                                        val currentLabel = when (autoLockDurationVal) {
                                            30 -> "30 Seconds"
                                            60 -> "1 Minute"
                                            300 -> "5 Minutes"
                                            else -> "Never"
                                        }
                                        Text(
                                            text = "Current: $currentLabel",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ThemePurple,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Open",
                                    tint = Color.White.copy(alpha = 0.3f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // 2. Lock on Background
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 2.dp
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
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFF3B82F6).copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Home,
                                            contentDescription = "Lock on Background",
                                            tint = Color(0xFF3B82F6),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Lock on Background",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Lock the vault when the app goes to background.",
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.5f),
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                                androidx.compose.material3.Switch(
                                    checked = lockOnBackground,
                                    onCheckedChange = { enabled ->
                                        viewModel.triggerKeypressEffects(context)
                                        viewModel.setLockOnBackground(enabled)
                                    },
                                    colors = dynamicSwitchColors()
                                )
                            }
                        }

                        // 3. Hide Notifications
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 2.dp
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
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFEF5350).copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.VolumeOff,
                                            contentDescription = "Hide Notifications",
                                            tint = Color(0xFFEF5350),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Hide Notifications",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Hide sensitive notifications.",
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.5f),
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                                androidx.compose.material3.Switch(
                                    checked = hideNotifications,
                                    onCheckedChange = { enabled ->
                                        viewModel.triggerKeypressEffects(context)
                                        viewModel.setHideNotifications(enabled)
                                    },
                                    colors = dynamicSwitchColors()
                                )
                            }
                        }

                        // 4. Screenshot Protection
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 2.dp
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
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFFF9100).copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CameraAlt,
                                            contentDescription = "Screenshot Protection",
                                            tint = Color(0xFFFF9100),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Screenshot Protection",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Prevent screenshots inside the vault.",
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.5f),
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                                androidx.compose.material3.Switch(
                                    checked = preventScreenshotsVal,
                                    onCheckedChange = { enabled ->
                                        viewModel.triggerKeypressEffects(context)
                                        viewModel.setPreventScreenshots(enabled)
                                    },
                                    colors = dynamicSwitchColors()
                                )
                            }
                        }

                        // 5. Clipboard Protection
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 2.dp
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
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFE5C158).copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Description,
                                            contentDescription = "Clipboard Protection",
                                            tint = Color(0xFFE5C158),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Clipboard Protection",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Automatically clear copied sensitive data.",
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.5f),
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                                androidx.compose.material3.Switch(
                                    checked = clipboardProtection,
                                    onCheckedChange = { enabled ->
                                        viewModel.triggerKeypressEffects(context)
                                        viewModel.setClipboardProtection(enabled)
                                    },
                                    colors = dynamicSwitchColors()
                                )
                            }
                        }

                        // 6. Stealth Mode
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 2.dp
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
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFF8B5CF6).copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.VisibilityOff,
                                            contentDescription = "Stealth Mode",
                                            tint = Color(0xFF8B5CF6),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Stealth Mode",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Reduce visible traces while using the vault.",
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.5f),
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                                androidx.compose.material3.Switch(
                                    checked = stealthMode,
                                    onCheckedChange = { enabled ->
                                        viewModel.triggerKeypressEffects(context)
                                        viewModel.setStealthMode(enabled)
                                    },
                                    colors = dynamicSwitchColors()
                                )
                            }
                        }

                        // 7. Secure Share Branding
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 2.dp
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
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFF3B82F6).copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Security,
                                            contentDescription = "Secure Share Branding",
                                            tint = Color(0xFF3B82F6),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Secure Share Branding",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Add a subtle Secret Vault watermark when sharing supported images.",
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.5f),
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                                androidx.compose.material3.Switch(
                                    checked = secureShareBranding,
                                    onCheckedChange = { enabled ->
                                        viewModel.triggerKeypressEffects(context)
                                        viewModel.setSecureShareBranding(enabled)
                                    },
                                    colors = dynamicSwitchColors()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(100.dp))
                    }

                    // Selection Dialog for Auto Lock
                    if (showAutoLockDialog) {
                        androidx.compose.material3.AlertDialog(
                            onDismissRequest = { showAutoLockDialog = false },
                            containerColor = Color(0xFF1B2031),
                            titleContentColor = Color.White,
                            textContentColor = Color.White.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(24.dp),
                            title = {
                                Text(
                                    text = "Select Auto Lock Duration",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                            },
                            text = {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val options = listOf(
                                        "30 Seconds" to 30,
                                        "1 Minute" to 60,
                                        "5 Minutes" to 300,
                                        "Never" to -1
                                    )
                                    options.forEach { (label, durationSec) ->
                                        val isSelected = autoLockDurationVal == durationSec
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (isSelected) ThemePurple.copy(alpha = 0.15f) else Color.Transparent)
                                                .clickable {
                                                    viewModel.triggerKeypressEffects(context)
                                                    viewModel.setAutoLockDuration(durationSec)
                                                    showAutoLockDialog = false
                                                }
                                                .padding(horizontal = 16.dp, vertical = 12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = label,
                                                color = if (isSelected) ThemePurple else Color.White,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 14.sp
                                            )
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = ThemePurple,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                androidx.compose.material3.TextButton(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        showAutoLockDialog = false
                                    }
                                ) {
                                    Text("Cancel", color = Color.White.copy(alpha = 0.6f))
                                }
                            }
                        )
                    }
                }
                "Shake to Exit" -> {
                    var showPanicExitDialog by remember { mutableStateOf(false) }
                    val panicExitActionVal by viewModel.panicExitAction.collectAsStateWithLifecycle()
                    val panicExitLabel = when (panicExitActionVal) {
                        "close" -> "Close Vault Immediately"
                        "calculator" -> "Return to Calculator Screen"
                        "home" -> "Return to Home Screen"
                        else -> "Close Vault Immediately"
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Top Hero Card
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth().testTag("shake_to_exit_hero_card"),
                            shape = RoundedCornerShape(24.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(ThemePurple.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ExitToApp,
                                            contentDescription = null,
                                            tint = ThemePurple,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Shake to Exit",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Emergency Shake Controls",
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(2.dp))
                                
                                Text(
                                    text = "Instantly leave and secure your private vault during emergency situations by shaking your phone hard.",
                                    fontSize = 13.sp,
                                    color = Color.White.copy(alpha = 0.6f),
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        // Enable Shake to Exit Switch Card
                        UnifiedGlassCard(
                            modifier = Modifier.fillMaxWidth().testTag("shake_toggle_card"),
                            shape = RoundedCornerShape(20.dp),
                            bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                            elevation = 2.dp
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
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(ThemePurple.copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = "Shake Gesture",
                                            tint = ThemePurple,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Enable Shake to Exit",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Detect hard shaking to trigger emergency exit.",
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.5f),
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                                Switch(
                                    checked = panicEnabled,
                                    onCheckedChange = {
                                        viewModel.triggerKeypressEffects(context)
                                        viewModel.setPanicEnabled(it)
                                    },
                                    colors = dynamicSwitchColors()
                                )
                            }
                        }

                        if (panicEnabled) {
                            // Options Section Title
                            Text(
                                text = "SHAKE ACTION CONFIGURATION",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMedium,
                                modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 2.dp)
                            )

                            // 1. Shake Action Option Setting Card
                            UnifiedGlassCard(
                                modifier = Modifier.fillMaxWidth().testTag("shake_action_card"),
                                shape = RoundedCornerShape(20.dp),
                                bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                                elevation = 2.dp,
                                onClick = { showPanicExitDialog = true }
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
                                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(Color(0xFFEF5350).copy(alpha = 0.12f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ExitToApp,
                                                contentDescription = "Shake Action",
                                                tint = Color(0xFFEF5350),
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = "Shake Action",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                            Text(
                                                text = "Action performed when shaking is detected.",
                                                fontSize = 11.sp,
                                                color = Color.White.copy(alpha = 0.5f),
                                                lineHeight = 14.sp
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = panicExitLabel,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ThemePurple
                                            )
                                        }
                                    }
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = "Select",
                                        tint = Color.White.copy(alpha = 0.4f),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            // Test Shake Exit Button
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Button(
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    viewModel.lockVault()
                                    Toast.makeText(context, "Emergency exit triggered: Vault locked!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("test_shake_exit_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFEF5350).copy(alpha = 0.2f),
                                    contentColor = Color(0xFFEF5350)
                                ),
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = 1.dp,
                                    color = Color(0xFFEF5350).copy(alpha = 0.4f)
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Test",
                                        tint = Color(0xFFEF5350),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Test Shake Action",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFEF5350)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(100.dp))
                    }

                    // Selection Dialog for Shake Action
                    if (showPanicExitDialog) {
                        androidx.compose.material3.AlertDialog(
                            onDismissRequest = { showPanicExitDialog = false },
                            containerColor = Color(0xFF1B2031),
                            titleContentColor = Color.White,
                            textContentColor = Color.White.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(24.dp),
                            title = {
                                Text(
                                    text = "Select Shake Exit Action",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            },
                            text = {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf(
                                        "close" to "Close Vault Immediately",
                                        "calculator" to "Return to Calculator Screen",
                                        "home" to "Return to Home Screen"
                                    ).forEach { (actionVal, actionLabel) ->
                                        val isSelected = panicExitActionVal == actionVal
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (isSelected) ThemePurple.copy(alpha = 0.15f) else Color.Transparent)
                                                .clickable {
                                                    viewModel.triggerKeypressEffects(context)
                                                    viewModel.setPanicExitAction(actionVal)
                                                    showPanicExitDialog = false
                                                }
                                                .padding(horizontal = 16.dp, vertical = 12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = actionLabel,
                                                color = if (isSelected) ThemePurple else Color.White,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 14.sp
                                            )
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = ThemePurple,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                androidx.compose.material3.TextButton(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        showPanicExitDialog = false
                                    }
                                ) {
                                    Text("Cancel", color = Color.White.copy(alpha = 0.6f))
                                }
                            }
                        )
                    }
                }
                "Password_Generator" -> {
                    PasswordGeneratorScreen(
                        viewModel = viewModel,
                        onBack = { activeSection = "__BACK__" }
                    )
                }
                "Metadata_Cleaner" -> {
                    MetadataCleanerScreen(
                        viewModel = viewModel,
                        onBack = { activeSection = "__BACK__" }
                    )
                }
                "Secure_Voice_Note" -> {
                    SecureVoiceNoteScreen(
                        onBack = { activeSection = "__BACK__" }
                    )
                }
                "About" -> {
                    AboutScreen(
                        onBack = { activeSection = "__BACK__" }
                    )
                }
                "Profile" -> {
                    var showAvatarSheet by remember { mutableStateOf(false) }
                    var showBuiltInGrid by remember { mutableStateOf(false) }
                    var showNameDialog by remember { mutableStateOf(false) }
                    var showPremiumDialog by remember { mutableStateOf(false) }
                    
                    val displayName = if (ownerName == "Vault Owner" || ownerName.isEmpty()) "Sameer" else ownerName
                    var nameInput by remember { mutableStateOf(displayName) }
                    
                    val context = androidx.compose.ui.platform.LocalContext.current
                    
                    // Discovered built-in avatars starts with "ic_avatar_" from drawable
                    val builtInAvatars = remember {
                        val list = mutableListOf<String>()
                        try {
                            val packageName = context.packageName
                            val rDrawableClass = Class.forName("$packageName.R\$drawable")
                            for (field in rDrawableClass.fields) {
                                val name = field.name
                                if (name.startsWith("ic_avatar_")) {
                                    list.add(name)
                                }
                            }
                        } catch (e: Exception) {
                            // Fallback
                            list.add("ic_avatar_1")
                            list.add("ic_avatar_2")
                            list.add("ic_avatar_3")
                            list.add("ic_avatar_4")
                            list.add("ic_avatar_5")
                            list.add("ic_avatar_6")
                            list.add("ic_avatar_7")
                            list.add("ic_avatar_8")
                        }
                        list.sorted()
                    }
                    
                    // Resolve ownerAvatarUri to model (either drawable Int resource id or Uri String)
                    val avatarModel: Any? = remember(ownerAvatarUri) {
                        if (ownerAvatarUri.isEmpty()) {
                            null
                        } else if (ownerAvatarUri.startsWith("res_name:")) {
                            val name = ownerAvatarUri.removePrefix("res_name:")
                            val resId = context.resources.getIdentifier(name, "drawable", context.packageName)
                            if (resId != 0) resId else null
                        } else {
                            ownerAvatarUri
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // --- Hero Section ---
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Avatar container with Camera icon at bottom-right (no clipping on outer box so camera icon doesn't cut)
                            Box(
                                modifier = Modifier
                                    .size(126.dp)
                                    .clickable(
                                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                        indication = null
                                    ) { showAvatarSheet = true }
                            ) {
                                // Centered profile avatar
                                Box(
                                    modifier = Modifier
                                        .size(112.dp)
                                        .align(Alignment.Center)
                                        .clip(CircleShape)
                                        .background(
                                            androidx.compose.ui.graphics.Brush.radialGradient(
                                                colors = listOf(
                                                    ThemePurple.copy(alpha = 0.35f),
                                                    Color(0xFF161B2B)
                                                )
                                            )
                                        )
                                        .border(2.5.dp, ThemePurple.copy(alpha = 0.8f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (avatarModel != null) {
                                        coil.compose.AsyncImage(
                                            model = avatarModel,
                                            contentDescription = "Profile Picture",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .scale(ownerAvatarScale)
                                                .offset(x = ownerAvatarOffsetX.dp, y = ownerAvatarOffsetY.dp),
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                        )
                                    } else {
                                        // Fallback elegant initial
                                        val initialText = displayName.take(1).uppercase()
                                        Text(
                                            text = initialText,
                                            fontSize = 44.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            letterSpacing = 0.sp
                                        )
                                    }
                                }

                                // Small camera icon attached to the bottom-right of the avatar
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .align(Alignment.Center)
                                        .offset(x = 38.dp, y = 38.dp) // Perfect offset from center to place it exactly on the boundary of the 112dp avatar circle
                                        .clip(CircleShape)
                                        .background(Color(0xFF161B2B)) // Dark luxury background
                                        .border(1.5.dp, ThemePurple, CircleShape), // Beautiful purple border ring
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "Change avatar",
                                        tint = Color.White, // Crispy white camera icon inside
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }

                            // Display Name below the avatar - clearly editable premium pill
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(alpha = 0.04f)) // Subtle glass overlay
                                    .border(
                                        width = 1.dp,
                                        color = ThemePurple.copy(alpha = 0.35f), // Glowing purple border
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        nameInput = displayName
                                        showNameDialog = true
                                    }
                                    .padding(horizontal = 18.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = displayName,
                                    fontSize = 24.sp, // Premium display typography
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = 0.5.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Display Name",
                                    tint = ThemePurple,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Subtitle and Premium Badge on the same row below the name
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp) // Premium spacing
                            ) {
                                Text(
                                    text = "Vault Owner",
                                    fontSize = 15.sp,
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontWeight = FontWeight.Medium
                                )
                                
                                // Premium Badge supporting Free / Premium / Lifetime states in Glassmorphism style
                                val badgeBgColor = when (premiumState) {
                                    "Premium" -> ThemePurple.copy(alpha = 0.25f)
                                    "Lifetime" -> Color(0xFFFFA000).copy(alpha = 0.25f)
                                    else -> Color.White.copy(alpha = 0.08f) // elegant glass for Free
                                }
                                
                                val badgeBorderColor = when (premiumState) {
                                    "Premium" -> ThemePurple.copy(alpha = 0.5f)
                                    "Lifetime" -> Color(0xFFFFA000).copy(alpha = 0.6f)
                                    else -> Color.White.copy(alpha = 0.25f) // clean white glass border
                                }
                                
                                val badgeTextColor = when (premiumState) {
                                    "Premium" -> ThemePurple
                                    "Lifetime" -> Color(0xFFFFB300)
                                    else -> Color.White.copy(alpha = 0.9f)
                                }
                                
                                val badgeText = premiumState.uppercase()
                                
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape) // Rounded capsule design
                                        .background(badgeBgColor)
                                        .border(1.dp, badgeBorderColor, CircleShape)
                                        .clickable { showPremiumDialog = true }
                                        .padding(horizontal = 12.dp, vertical = 6.dp), // Premium spacing
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Badge star",
                                            tint = badgeTextColor,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = badgeText,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = badgeTextColor,
                                            letterSpacing = 1.sp
                                        )
                                    }
                                }
                            }
                        }

                        // --- Vault Identity Card Section (Phase 9B) ---
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(22.dp))
                                .background(
                                    androidx.compose.ui.graphics.Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF111422), // Luxury deep obsidian-navy
                                            Color(0xFF07090F)  // Pure deep black-blue
                                        )
                                    )
                                )
                                .border(
                                    width = 1.dp,
                                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.08f),
                                            Color.White.copy(alpha = 0.02f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(22.dp)
                                )
                                .padding(horizontal = 24.dp, vertical = 28.dp),
                            verticalArrangement = Arrangement.spacedBy(22.dp)
                        ) {
                            // Section 1: Vault Rank & Aura
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = "VAULT RANK",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White.copy(alpha = 0.4f),
                                        letterSpacing = 1.8.sp
                                    )
                                    val rankText = when (premiumState) {
                                        "Premium" -> "Guardian"
                                        "Lifetime" -> "Legend"
                                        else -> "Explorer"
                                    }
                                    Text(
                                        text = rankText,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "AURA",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White.copy(alpha = 0.4f),
                                        letterSpacing = 1.8.sp
                                    )
                                    val auraText = when (premiumState) {
                                        "Premium" -> "999"
                                        "Lifetime" -> "∞"
                                        else -> "99"
                                    }
                                    Text(
                                        text = auraText,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Black,
                                        color = ThemePurple,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }

                            androidx.compose.material3.Divider(
                                color = Color.White.copy(alpha = 0.06f),
                                thickness = 1.dp
                            )

                            // Section 2: Vault ID
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "VAULT ID",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.4f),
                                    letterSpacing = 1.8.sp
                                )
                                Text(
                                    text = vaultId,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    letterSpacing = 1.2.sp
                                )
                            }

                            androidx.compose.material3.Divider(
                                color = Color.White.copy(alpha = 0.06f),
                                thickness = 1.dp
                            )

                            // Section 3: Protected Locally & Zero Cloud Storage
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White.copy(alpha = 0.03f))
                                        .border(0.5.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = ThemePurple,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Text(
                                        text = "Protected Locally",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Zero Cloud Storage",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.White.copy(alpha = 0.5f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Section 4: Premium CTA Button
                            val ctaText = when (premiumState) {
                                "Premium" -> "Manage Plan"
                                "Lifetime" -> "Lifetime Activated"
                                else -> "Upgrade Vault"
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        androidx.compose.ui.graphics.Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFF4C1D95),
                                                Color(0xFF6D28D9)
                                            )
                                        )
                                    )
                                    .clickable {
                                        if (premiumState == "Lifetime") {
                                            android.widget.Toast.makeText(context, "Lifetime Vault status is fully activated!", android.widget.Toast.LENGTH_SHORT).show()
                                        } else {
                                            showPremiumDialog = true
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = ctaText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        // --- Security Status Section (Phase 9C) ---
                        SecurityStatusSection(
                            overallSecurityRating = overallSecurityRating,
                            securityItems = securityItems
                        )

                        // --- Your Journey Section (Phase 9D) ---
                        val journeyTimeline by viewModel.journeyTimeline.collectAsStateWithLifecycle()
                        YourJourneySection(
                            timelineItems = journeyTimeline
                        )
                    }

                    // --- Material 3 Bottom Sheet for Avatar ---
                    @OptIn(ExperimentalMaterial3Api::class)
                    if (showAvatarSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { 
                                showAvatarSheet = false
                                showBuiltInGrid = false
                            },
                            containerColor = Color(0xFF161B2B),
                            dragHandle = { BottomSheetDefaults.DragHandle(color = Color.White.copy(alpha=0.3f)) }
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 24.dp)
                                    .padding(bottom = 40.dp)
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                if (!showBuiltInGrid) {
                                    // Main Sheet Options
                                    Text(
                                        text = "Profile Avatar",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 24.dp)
                                    )

                                    // Option 1: Choose from Gallery
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(16.dp))
                                            .clickable {
                                                avatarGalleryLauncher.launch(
                                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                                )
                                            }
                                            .padding(vertical = 16.dp, horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(ThemePurple.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PhotoLibrary,
                                                contentDescription = null,
                                                tint = ThemePurple,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Text(
                                            text = "Choose from Gallery",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    // Option 2: Built-in Avatars
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(16.dp))
                                            .clickable {
                                                showBuiltInGrid = true
                                            }
                                            .padding(vertical = 16.dp, horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(ThemePurple.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = null,
                                                tint = ThemePurple,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Text(
                                            text = "Built-in Avatars",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    // Option 3: Remove Photo
                                    if (ownerAvatarUri.isNotEmpty()) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(16.dp))
                                                .clickable {
                                                    viewModel.setOwnerAvatarUri("")
                                                    showAvatarSheet = false
                                                    android.widget.Toast.makeText(context, "Avatar removed", android.widget.Toast.LENGTH_SHORT).show()
                                                }
                                                .padding(vertical = 16.dp, horizontal = 12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFFFF3333).copy(alpha = 0.15f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = null,
                                                    tint = Color(0xFFFF3333),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Text(
                                                text = "Remove Current Photo",
                                                color = Color(0xFFFF5555),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }

                                    if (ownerAvatarUri.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        androidx.compose.material3.Divider(color = Color.White.copy(alpha = 0.12f), thickness = 1.dp)
                                        Spacer(modifier = Modifier.height(16.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Crop & Alignment",
                                                color = Color.White,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            TextButton(
                                                onClick = {
                                                    viewModel.setOwnerAvatarScale(1.0f)
                                                    viewModel.setOwnerAvatarOffsetX(0f)
                                                    viewModel.setOwnerAvatarOffsetY(0f)
                                                }
                                            ) {
                                                Text("Reset", color = ThemePurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Help/Notice Box explaining alignment sliders
                                         Row(
                                             modifier = Modifier
                                                 .fillMaxWidth()
                                                 .clip(RoundedCornerShape(12.dp))
                                                 .background(ThemePurple.copy(alpha = 0.08f))
                                                 .border(0.5.dp, ThemePurple.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                                                 .padding(12.dp),
                                             horizontalArrangement = Arrangement.spacedBy(10.dp),
                                             verticalAlignment = Alignment.CenterVertically
                                         ) {
                                             Icon(
                                                 imageVector = Icons.Default.Info,
                                                 contentDescription = null,
                                                 tint = ThemePurple,
                                                 modifier = Modifier.size(16.dp)
                                             )
                                             Text(
                                                 text = "Drag the sliders below to adjust, zoom, and align your photo perfectly inside your Identity Card!",
                                                 color = Color.White.copy(alpha = 0.85f),
                                                 fontSize = 12.sp,
                                                 lineHeight = 16.sp,
                                                 fontWeight = FontWeight.Medium
                                             )
                                         }

                                         Spacer(modifier = Modifier.height(16.dp))

                                         // Real-time Preview Box
                                        Box(
                                            modifier = Modifier
                                                .size(80.dp)
                                                .align(Alignment.CenterHorizontally)
                                                .clip(CircleShape)
                                                .background(Color(0xFF090D1A))
                                                .border(2.dp, ThemePurple.copy(alpha = 0.8f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            val previewModel = remember(ownerAvatarUri) {
                                                if (ownerAvatarUri.startsWith("res_name:")) {
                                                    val name = ownerAvatarUri.removePrefix("res_name:")
                                                    val resId = context.resources.getIdentifier(name, "drawable", context.packageName)
                                                    if (resId != 0) resId else null
                                                } else {
                                                    ownerAvatarUri
                                                }
                                            }
                                            if (previewModel != null) {
                                                coil.compose.AsyncImage(
                                                    model = previewModel,
                                                    contentDescription = "Preview",
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .scale(ownerAvatarScale)
                                                        .offset(
                                                            x = (ownerAvatarOffsetX * 80f / 112f).dp,
                                                            y = (ownerAvatarOffsetY * 80f / 112f).dp
                                                        ),
                                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Zoom Scale Slider
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("Zoom / Scale", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                                                Text(String.format("%.2fx", ownerAvatarScale), color = ThemePurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            }
                                            Slider(
                                                value = ownerAvatarScale,
                                                onValueChange = { viewModel.setOwnerAvatarScale(it) },
                                                valueRange = 0.8f..2.5f,
                                                colors = SliderDefaults.colors(
                                                    thumbColor = ThemePurple,
                                                    activeTrackColor = ThemePurple,
                                                    inactiveTrackColor = Color.White.copy(alpha = 0.12f)
                                                )
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Horizontal Offset
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("Horizontal Position (Left / Right)", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                                                Text(String.format("%d dp", ownerAvatarOffsetX.toInt()), color = ThemePurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            }
                                            Slider(
                                                value = ownerAvatarOffsetX,
                                                onValueChange = { viewModel.setOwnerAvatarOffsetX(it) },
                                                valueRange = -80f..80f,
                                                colors = SliderDefaults.colors(
                                                    thumbColor = ThemePurple,
                                                    activeTrackColor = ThemePurple,
                                                    inactiveTrackColor = Color.White.copy(alpha = 0.12f)
                                                )
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Vertical Offset
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("Vertical Position (Up / Down)", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                                                Text(String.format("%d dp", ownerAvatarOffsetY.toInt()), color = ThemePurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            }
                                            Slider(
                                                value = ownerAvatarOffsetY,
                                                onValueChange = { viewModel.setOwnerAvatarOffsetY(it) },
                                                valueRange = -80f..80f,
                                                colors = SliderDefaults.colors(
                                                    thumbColor = ThemePurple,
                                                    activeTrackColor = ThemePurple,
                                                    inactiveTrackColor = Color.White.copy(alpha = 0.12f)
                                                )
                                            )
                                        }
                                    }
                                } else {
                                    // Built-in Avatar Selection Grid Panel
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 24.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        IconButton(
                                            onClick = { showBuiltInGrid = false }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowBack,
                                                contentDescription = "Back",
                                                tint = Color.White
                                            )
                                        }
                                        Text(
                                            text = "Built-in Avatars",
                                            color = Color.White,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    if (builtInAvatars.isEmpty()) {
                                        Text(
                                            text = "No built-in avatars found.",
                                            color = Color.White.copy(alpha = 0.6f),
                                            modifier = Modifier.padding(vertical = 16.dp)
                                        )
                                    } else {
                                        val rows = builtInAvatars.chunked(4)
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalArrangement = Arrangement.spacedBy(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            rows.forEach { rowItems ->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                                                ) {
                                                    rowItems.forEach { avatarName ->
                                                        val resId = context.resources.getIdentifier(avatarName, "drawable", context.packageName)
                                                        if (resId != 0) {
                                                            Box(
                                                                modifier = Modifier
                                                                    .size(68.dp)
                                                                    .clip(CircleShape)
                                                                    .background(Color(0xFF090D1A))
                                                                    .border(
                                                                        width = if (ownerAvatarUri == "res_name:$avatarName") 2.5.dp else 1.dp,
                                                                        color = if (ownerAvatarUri == "res_name:$avatarName") ThemePurple else Color.White.copy(alpha = 0.15f),
                                                                        shape = CircleShape
                                                                    )
                                                                    .clickable {
                                                                        viewModel.setOwnerAvatarUri("res_name:$avatarName")
                                                                        showBuiltInGrid = false
                                                                        android.widget.Toast.makeText(context, "Avatar updated! Adjust its position below.", android.widget.Toast.LENGTH_SHORT).show()
                                                                    },
                                                                contentAlignment = Alignment.Center
                                                            ) {
                                                                coil.compose.AsyncImage(
                                                                    model = resId,
                                                                    contentDescription = "Avatar $avatarName",
                                                                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                                                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
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

                    // --- Material 3 Dialog for Name Editing ---
                    if (showNameDialog) {
                        androidx.compose.ui.window.Dialog(
                            onDismissRequest = { showNameDialog = false }
                        ) {
                            androidx.compose.material3.Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                shape = RoundedCornerShape(24.dp),
                                color = Color(0xFF161B2B), // Explicitly force our premium dark color
                                border = androidx.compose.foundation.BorderStroke(1.dp, ThemePurple.copy(alpha = 0.3f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text(
                                        text = "Edit Display Name",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = nameInput,
                                            onValueChange = {
                                                if (it.length <= 25) {
                                                    nameInput = it
                                                }
                                            },
                                            singleLine = true,
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White,
                                                focusedContainerColor = Color(0xFF0C0F1A),
                                                unfocusedContainerColor = Color(0xFF0C0F1A),
                                                focusedBorderColor = ThemePurple,
                                                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                                cursorColor = ThemePurple
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Text(
                                                text = "${nameInput.length}/25",
                                                color = Color.White.copy(alpha = 0.4f),
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        TextButton(
                                            onClick = { showNameDialog = false }
                                        ) {
                                            Text("Cancel", color = Color.White.copy(alpha = 0.6f))
                                        }
                                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(8.dp))
                                        Button(
                                            onClick = {
                                                val trimmed = nameInput.trim()
                                                if (trimmed.isNotEmpty()) {
                                                    viewModel.setOwnerName(trimmed)
                                                    showNameDialog = false
                                                } else {
                                                    android.widget.Toast.makeText(context, "Name cannot be empty", android.widget.Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text("Save", color = if (IsWhiteTheme) Color(0xFF161B2B) else Color.White, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // --- Material 3 Dialog for Premium Upgrading ---
                    if (showPremiumDialog) {
                        AlertDialog(
                            onDismissRequest = { showPremiumDialog = false },
                            containerColor = Color(0xFF161B2B),
                            title = {
                                Text(
                                    text = "Upgrade Premium Status",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            text = {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Experience advanced vault styles and exclusive benefits.",
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 14.sp
                                    )
                                    
                                    // Option 1: Free
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                if (premiumState == "Free") Color.White.copy(alpha = 0.12f)
                                                else Color.White.copy(alpha = 0.04f)
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = if (premiumState == "Free") Color.White.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .clickable {
                                                viewModel.setPremiumState("Free")
                                                showPremiumDialog = false
                                                android.widget.Toast.makeText(context, "Status set to Free Vault!", android.widget.Toast.LENGTH_SHORT).show()
                                            }
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color.White.copy(alpha = 0.5f),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Column {
                                            Text("Free Standard Vault", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                            Text("Basic security style essentials", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                                        }
                                    }

                                    // Option 2: Premium
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                if (premiumState == "Premium") ThemePurple.copy(alpha = 0.15f)
                                                else Color.White.copy(alpha = 0.04f)
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = if (premiumState == "Premium") ThemePurple else Color.White.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .clickable {
                                                viewModel.setPremiumState("Premium")
                                                showPremiumDialog = false
                                                android.widget.Toast.makeText(context, "Upgraded to Premium Vault!", android.widget.Toast.LENGTH_SHORT).show()
                                            }
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = ThemePurple,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Column {
                                            Text("Premium Vault Pro", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                            Text("Aesthetic Glassmorphism theme style", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                                        }
                                    }

                                    // Option 3: Lifetime
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                if (premiumState == "Lifetime") Color(0xFFFFA000).copy(alpha = 0.15f)
                                                else Color.White.copy(alpha = 0.04f)
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = if (premiumState == "Lifetime") Color(0xFFFFA000) else Color.White.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .clickable {
                                                viewModel.setPremiumState("Lifetime")
                                                showPremiumDialog = false
                                                android.widget.Toast.makeText(context, "Upgraded to Lifetime Gold VIP!", android.widget.Toast.LENGTH_SHORT).show()
                                            }
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFFFB300),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Column {
                                            Text("Lifetime Gold VIP", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                            Text("Full luxury gold accent VIP badge style", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = { showPremiumDialog = false }
                                ) {
                                    Text("Dismiss", color = ThemePurple, fontWeight = FontWeight.Bold)
                                }
                            }
                        )
                    }
                }
                else -> {
                    if (activeSection.startsWith("Placeholder_")) {
                        val title = activeSection.removePrefix("Placeholder_").replace("_", " ")
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(88.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(ThemePurple.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                val placeholderIcon = when {
                                    title.contains("Security") -> Icons.Default.Shield
                                    title.contains("Disguise") -> Icons.Default.Palette
                                    title.contains("Privacy") -> Icons.Default.Security
                                    title.contains("Data") -> Icons.Default.Storage
                                    title.contains("About") -> Icons.Default.Info
                                    title.contains("Camera") -> Icons.Default.CameraAlt
                                    title.contains("Scanner") -> Icons.Default.QrCode
                                    title.contains("Generator") -> Icons.Default.VpnKey
                                    title.contains("Cleaner") -> Icons.Default.CleaningServices
                                    title.contains("Shredder") -> Icons.Default.DeleteForever
                                    else -> Icons.Default.Extension
                                }
                                Icon(
                                    imageVector = placeholderIcon,
                                    contentDescription = null,
                                    tint = ThemePurple,
                                    modifier = Modifier.size(44.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = title,
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Feature Under Construction",
                                color = ThemePurple,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            UnifiedGlassCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                                elevation = 2.dp
                            ) {
                                Text(
                                    text = "This component is registered in the Calculator Vault settings control center architecture. Underlying databases, hardware permissions, and encryption pipelines are being developed to preserve military-grade offline security on your device.",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(18.dp),
                                    lineHeight = 18.sp
                                )
                            }
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
                                shape = RoundedCornerShape(8.dp),
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
                                shape = RoundedCornerShape(8.dp),
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
                val favoriteFiles by viewModel.favoriteFiles.collectAsStateWithLifecycle()
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
                                        val context = LocalContext.current
                                        val mediaPlayer = remember(path) {
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                                android.media.MediaPlayer(context)
                                            } else {
                                                android.media.MediaPlayer()
                                            }.apply {
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
                        Text("Create", color = if (ThemePurple.red > 0.95f && ThemePurple.green > 0.95f && ThemePurple.blue > 0.95f) BrandBg else Color.White)
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
            val vaultFolders by viewModel.vaultFolders.collectAsStateWithLifecycle()
            
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
            val favoriteNotes by viewModel.favoriteNotes.collectAsStateWithLifecycle()
            val favoriteFiles by viewModel.favoriteFiles.collectAsStateWithLifecycle()
            
            val matchedNotes = if (searchQuery.trim().isEmpty()) emptyList() else vaultNotes.filter {
                val parts = it.split("|||")
                parts.size >= 3 && (parts[1].contains(searchQuery, ignoreCase = true) || parts[2].contains(searchQuery, ignoreCase = true))
            }
            val matchedFiles = if (searchQuery.trim().isEmpty()) emptyList() else vaultFiles.filter {
                val parts = it.split("|||")
                parts.size >= 3 && parts[2].contains(searchQuery, ignoreCase = true)
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
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
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
                                        val parts = noteStr.split("|||", limit = 3)
                                        val title = parts.getOrNull(1) ?: "Note"
                                        val body = parts.getOrNull(2) ?: ""
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
        // 4. View/Edit Note Screen (Fullscreen Overlay with Premium Transition)
        if (viewNoteToShow != null) {
            lastNonNullNote = viewNoteToShow
        }
        
        AnimatedVisibility(
            visible = viewNoteToShow != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(animationSpec = tween(300)),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(animationSpec = tween(300)),
            modifier = Modifier.zIndex(100f)
        ) {
            val noteStr = lastNonNullNote
            if (noteStr != null) {
                val parts = noteStr.split("|||", limit = 3)
                if (parts.size == 3) {
                    val timestamp = parts[0]
                    val title = parts[1]
                    val body = parts[2]
                    
                    val noteId = remember(noteStr) { noteStr.split("|||").firstOrNull() ?: "" }
                    LaunchedEffect(noteId) {
                        editedNoteTitle = title
                        editedNoteContentValue = TextFieldValue(
                            text = body,
                            selection = TextRange(body.length)
                        )
                    }
                    
                    LaunchedEffect(editedNoteTitle, editedNoteContentValue.text) {
                        if (isEditingNote && (editedNoteTitle != title || editedNoteContentValue.text != body)) {
                            delay(1000)
                            if (isEditingNote && viewNoteToShow != null) {
                                viewModel.editVaultNote(noteStr, editedNoteTitle, editedNoteContentValue.text)
                                val newNoteStr = "$timestamp|||$editedNoteTitle|||${editedNoteContentValue.text}"
                                lastNonNullNote = newNoteStr
                                viewNoteToShow = newNoteStr
                            }
                        }
                    }
                    
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFF070A14)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color(0xFF0C1020),
                                            Color(0xFF05070E)
                                        )
                                    )
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .statusBarsPadding()
                            ) {
                            // Header Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        if (isEditingNote) {
                                            if (editedNoteTitle.isNotBlank() || editedNoteContentValue.text.isNotBlank()) {
                                                viewModel.editVaultNote(noteStr, editedNoteTitle, editedNoteContentValue.text)
                                            } else if (isNewDraftNote) {
                                                viewModel.deleteVaultNote(noteStr)
                                            }
                                        }
                                        viewNoteToShow = null
                                        isEditingNote = false
                                        isNewDraftNote = false
                                    },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color.White.copy(alpha = 0.04f), CircleShape)
                                        .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Go Back & Save",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Text(
                                    text = if (isEditingNote) "Editing Note" else "Secret Note",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.8f),
                                    letterSpacing = 0.5.sp
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val isFav = viewModel.favoriteNotes.collectAsStateWithLifecycle().value.contains(noteStr)
                                    IconButton(
                                        onClick = {
                                            viewModel.triggerKeypressEffects(context)
                                            viewModel.toggleFavoriteNote(noteStr)
                                        },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color.White.copy(alpha = 0.04f), CircleShape)
                                            .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = if (isFav) Icons.Default.Star else Icons.Default.StarBorder,
                                            contentDescription = "Favorite",
                                            tint = if (isFav) Color(0xFFFFD600) else Color.White.copy(alpha = 0.6f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    val isPinned = viewModel.pinnedNotes.collectAsStateWithLifecycle().value.contains(noteStr)
                                    IconButton(
                                        onClick = {
                                            viewModel.triggerKeypressEffects(context)
                                            viewModel.togglePinnedNote(noteStr)
                                        },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color.White.copy(alpha = 0.04f), CircleShape)
                                            .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PushPin,
                                            contentDescription = "Pin",
                                            tint = if (isPinned) ThemePurple else Color.White.copy(alpha = 0.6f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            viewModel.triggerKeypressEffects(context)
                                            viewModel.deleteVaultNote(noteStr)
                                            viewNoteToShow = null
                                            isEditingNote = false
                                            isNewDraftNote = false
                                        },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color.White.copy(alpha = 0.04f), CircleShape)
                                            .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Note",
                                            tint = Color(0xFFEF4444).copy(alpha = 0.8f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            viewModel.triggerKeypressEffects(context)
                                            if (isEditingNote) {
                                                if (editedNoteTitle.isNotBlank() || editedNoteContentValue.text.isNotBlank()) {
                                                    val newNoteStr = "$timestamp|||$editedNoteTitle|||${editedNoteContentValue.text}"
                                                    viewModel.editVaultNote(noteStr, editedNoteTitle, editedNoteContentValue.text)
                                                    viewNoteToShow = newNoteStr
                                                    lastNonNullNote = newNoteStr
                                                }
                                                isEditingNote = false
                                            } else {
                                                isEditingNote = true
                                            }
                                        },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color.White.copy(alpha = 0.04f), CircleShape)
                                            .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = if (isEditingNote) Icons.Default.Check else Icons.Default.Edit,
                                            contentDescription = if (isEditingNote) "Done" else "Edit",
                                            tint = ThemePurple,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }

                            // Custom Divider
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.08f)))

                            // Content Area
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            ) {
                                if (isEditingNote) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        // Title Input
                                        OutlinedTextField(
                                            value = editedNoteTitle,
                                            onValueChange = { editedNoteTitle = it; viewModel.updateLastInteraction() },
                                            placeholder = { Text("Title", fontSize = 22.sp, color = Color.White.copy(alpha = 0.4f), fontWeight = FontWeight.Bold) },
                                            textStyle = TextStyle(
                                                fontSize = 22.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            ),
                                            singleLine = true,
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = Color.Transparent,
                                                unfocusedBorderColor = Color.Transparent,
                                                focusedContainerColor = Color.Transparent,
                                                unfocusedContainerColor = Color.Transparent
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        // Rich Text Toolbar
                                        var showFontOptions by remember { mutableStateOf(false) }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .horizontalScroll(rememberScrollState())
                                                .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(16.dp))
                                                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                                                .padding(horizontal = 8.dp, vertical = 6.dp),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            IconButton(
                                                onClick = { 
                                                    viewModel.triggerKeypressEffects(context)
                                                    editedNoteContentValue = toggleTag(editedNoteContentValue, "<b>", "</b>") 
                                                    viewModel.updateLastInteraction()
                                                },
                                                modifier = Modifier.size(36.dp).background(if (isTagActive(editedNoteContentValue, "<b>", "</b>")) Color.White.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(8.dp))
                                            ) {
                                                Text("B", fontWeight = FontWeight.ExtraBold, color = Color.White, fontSize = 16.sp)
                                            }
                                            IconButton(
                                                onClick = { 
                                                    viewModel.triggerKeypressEffects(context)
                                                    editedNoteContentValue = toggleTag(editedNoteContentValue, "<i>", "</i>") 
                                                    viewModel.updateLastInteraction()
                                                },
                                                modifier = Modifier.size(36.dp).background(if (isTagActive(editedNoteContentValue, "<i>", "</i>")) Color.White.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(8.dp))
                                            ) {
                                                Text("I", fontStyle = FontStyle.Italic, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                            }
                                            IconButton(
                                                onClick = { 
                                                    viewModel.triggerKeypressEffects(context)
                                                    editedNoteContentValue = toggleTag(editedNoteContentValue, "<u>", "</u>") 
                                                    viewModel.updateLastInteraction()
                                                },
                                                modifier = Modifier.size(36.dp).background(if (isTagActive(editedNoteContentValue, "<u>", "</u>")) Color.White.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(8.dp))
                                            ) {
                                                Text("U", textDecoration = TextDecoration.Underline, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                            }
                                            
                                            Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color.White.copy(alpha = 0.1f)))
                                            
                                            
                                            
                                            IconButton(
                                                onClick = { 
                                                    viewModel.triggerKeypressEffects(context)
                                                    editedNoteContentValue = toggleLinePrefix(editedNoteContentValue, "• ") 
                                                    viewModel.updateLastInteraction()
                                                },
                                                modifier = Modifier.size(36.dp).background(if (isPrefixActive(editedNoteContentValue, "• ")) Color.White.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(8.dp))
                                            ) {
                                                Icon(Icons.Default.List, contentDescription = "Bullet List", tint = Color.White, modifier = Modifier.size(18.dp))
                                            }
                                            
                                            IconButton(
                                                onClick = { 
                                                    viewModel.triggerKeypressEffects(context)
                                                    editedNoteContentValue = toggleLinePrefix(editedNoteContentValue, "1. ") 
                                                    viewModel.updateLastInteraction()
                                                },
                                                modifier = Modifier.size(36.dp).background(if (isPrefixActive(editedNoteContentValue, "1. ")) Color.White.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(8.dp))
                                            ) {
                                                Text("1.", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                                            }
                                            
                                            IconButton(
                                                onClick = { 
                                                    viewModel.triggerKeypressEffects(context)
                                                    editedNoteContentValue = toggleLinePrefix(editedNoteContentValue, "[ ] ") 
                                                    viewModel.updateLastInteraction()
                                                },
                                                modifier = Modifier.size(36.dp).background(if (isPrefixActive(editedNoteContentValue, "[ ] ")) Color.White.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(8.dp))
                                            ) {
                                                Icon(Icons.Default.CheckBox, contentDescription = "Checklist", tint = Color.White, modifier = Modifier.size(18.dp))
                                            }
                                        }


                                        // Note Body Input with real-time visual transformation
                                        OutlinedTextField(
                                            value = editedNoteContentValue,
                                            onValueChange = { editedNoteContentValue = it; viewModel.updateLastInteraction() },
                                            placeholder = { Text("Start typing your secret notes...", fontSize = 16.sp, color = Color.White.copy(alpha = 0.3f)) },
                                            textStyle = TextStyle(
                                                fontSize = 16.sp,
                                                color = Color.White,
                                                lineHeight = 26.sp
                                            ),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = Color.Transparent,
                                                unfocusedBorderColor = Color.Transparent,
                                                focusedContainerColor = Color.Transparent,
                                                unfocusedContainerColor = Color.Transparent
                                            ),
                                            visualTransformation = RichTextVisualTransformation(ThemePurple),
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                                .verticalScroll(rememberScrollState())
                                        )
                                    }
                                } else {
                                    // Read-only view mode
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .verticalScroll(rememberScrollState())
                                            .padding(24.dp),
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Text(
                                            text = title,
                                            fontSize = 26.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            lineHeight = 34.sp,
                                            letterSpacing = (-0.5).sp
                                        )

                                        Text(
                                            text = "Last updated: $timestamp",
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.4f),
                                            fontWeight = FontWeight.Medium
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        RenderNoteContent(
                                            content = body,
                                            themePurple = ThemePurple,
                                            onToggleChecklist = { lineIndex, checked ->
                                                viewModel.triggerKeypressEffects(context)
                                                val lines = body.split("\n").toMutableList()
                                                if (lines.size > lineIndex) {
                                                    val currentLine = lines[lineIndex]
                                                    if (checked) {
                                                        lines[lineIndex] = currentLine.replaceFirst("[ ] ", "[x] ")
                                                    } else {
                                                        lines[lineIndex] = currentLine.replaceFirst("[x] ", "[ ] ")
                                                    }
                                                    val newBody = lines.joinToString("\n")
                                                    val newNoteStr = "$timestamp|||$title|||$newBody"
                                                    viewModel.editVaultNote(noteStr, title, newBody)
                                                    viewNoteToShow = newNoteStr
                                                    lastNonNullNote = newNoteStr
                                                }
                                            }
                                        )
                                    }
                                }
                            }

                            // Auto save / actions bar at bottom in editing mode with premium dark footer
                            if (isEditingNote) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White.copy(alpha = 0.015f))
                                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                        .padding(horizontal = 24.dp, vertical = 16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .clip(CircleShape)
                                                    .background(ThemePurple)
                                            )
                                            Text(
                                                text = "Auto-saved locally",
                                                fontSize = 12.sp,
                                                color = Color.White.copy(alpha = 0.4f),
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        
                                        Button(
                                            onClick = {
                                                viewModel.triggerKeypressEffects(context)
                                                if (editedNoteTitle.isNotBlank() || editedNoteContentValue.text.isNotBlank()) {
                                                    val newNoteStr = "$timestamp|||$editedNoteTitle|||${editedNoteContentValue.text}"
                                                    viewModel.editVaultNote(noteStr, editedNoteTitle, editedNoteContentValue.text)
                                                    viewNoteToShow = null
                                                    lastNonNullNote = newNoteStr
                                                } else {
                                                    viewNoteToShow = null
                                                }
                                                isEditingNote = false
                                                isNewDraftNote = false
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                                            shape = RoundedCornerShape(16.dp),
                                            modifier = Modifier.height(44.dp)
                                        ) {
                                            Text(
                                                "Save & Close",
                                                color = if (ThemePurple.red > 0.95f && ThemePurple.green > 0.95f && ThemePurple.blue > 0.95f) BrandBg else Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
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
        
        if (activeSection in listOf("Home", "More")) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF161B2B).copy(alpha = 0.95f))
                        .height(56.dp)
                        .drawBehind {
                            // High-end glassmorphic top hairline divider
                            drawLine(
                                color = Color.White.copy(alpha = 0.08f),
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 1.dp.toPx()
                            )
                        },
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isHomeSelected = activeSection == "Home"
                    val isBrowserSelected = activeSection == "Private Browser"
                    val isMoreSelected = activeSection == "More"

                    // Vault Item
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                                viewModel.triggerKeypressEffects(context)
                                activeSection = "Home"
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Dashboard,
                            contentDescription = "Vault",
                            tint = if (isHomeSelected) ThemePurple else Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Vault",
                            fontSize = 10.sp,
                            fontWeight = if (isHomeSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isHomeSelected) ThemePurple else Color.White.copy(alpha = 0.4f)
                        )
                    }

                    // Browser Item
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                                viewModel.triggerKeypressEffects(context)
                                activeSection = "Private Browser"
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Browser",
                            tint = if (isBrowserSelected) ThemePurple else Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Browser",
                            fontSize = 10.sp,
                            fontWeight = if (isBrowserSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isBrowserSelected) ThemePurple else Color.White.copy(alpha = 0.4f)
                        )
                    }

                    // Settings Item
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                                viewModel.triggerKeypressEffects(context)
                                activeSection = "More"
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = if (isMoreSelected) ThemePurple else Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Settings",
                            fontSize = 10.sp,
                            fontWeight = if (isMoreSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isMoreSelected) ThemePurple else Color.White.copy(alpha = 0.4f)
                        )
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
                                    PickVisualMediaRequest(
                                        if (isPhoto) ActivityResultContracts.PickVisualMedia.ImageOnly
                                        else ActivityResultContracts.PickVisualMedia.VideoOnly
                                    )
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
                                documentPickerLauncher.launch(arrayOf("*/*"))
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
                                Icon(Icons.Default.QrCode, contentDescription = "QR Scanner", tint = ThemePurple)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Private QR Scanner", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                Text("Scan and decode QR codes", color = TextMedium, fontSize = 13.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Premium Secure Share Bottom Sheet
        @OptIn(ExperimentalMaterial3Api::class)
        if (secureShareFileSerialized != null) {
            val fileStr = secureShareFileSerialized!!
            val parts = fileStr.split("|||")
            if (parts.size >= 5) {
                val originalName = parts[2]
                val mimeType = parts[3]
                val path = parts[4]
                val sizeStr = if (parts.size >= 6) parts[5] else "Unknown Size"
                
                var isSharingInProgress by remember { mutableStateOf(false) }
                var isUnhidingInProgress by remember { mutableStateOf(false) }
                var showPermanentDeleteConfirm by remember { mutableStateOf(false) }

                ModalBottomSheet(
                    onDismissRequest = { 
                        if (!isSharingInProgress && !isUnhidingInProgress) {
                            secureShareFileSerialized = null 
                        }
                    },
                    containerColor = Color(0xFF161B2B),
                    dragHandle = { BottomSheetDefaults.DragHandle(color = Color.White.copy(alpha=0.3f)) }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Title / Header
                        Text(
                            text = "Secure Share Options",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        // File Preview / Info Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 180.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2436))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Dynamic File Type Preview Icon or Mini-Image
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White.copy(alpha = 0.05f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (mimeType.startsWith("image/")) {
                                        coil.compose.AsyncImage(
                                            model = java.io.File(path),
                                            contentDescription = originalName,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                        )
                                    } else {
                                        Icon(
                                            imageVector = when {
                                                mimeType.startsWith("video/") -> Icons.Default.PlayArrow
                                                mimeType.startsWith("audio/") -> Icons.Default.MusicNote
                                                else -> Icons.Default.Description
                                            },
                                            contentDescription = null,
                                            tint = ThemePurple,
                                            modifier = Modifier.size(36.dp)
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = cleanDisplayName(originalName),
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "$sizeStr • $mimeType",
                                        color = Color.White.copy(alpha = 0.5f),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        // Options / Actions list
                        if (isSharingInProgress || isUnhidingInProgress) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CircularProgressIndicator(color = ThemePurple)
                                Text(
                                    text = if (isSharingInProgress) "Preparing secure temporary share..." else "Unhiding file...",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 14.sp
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // 1. Share Securely
                                Surface(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        isSharingInProgress = true
                                        viewModel.shareVaultFile(
                                            context = context,
                                            fileSerialized = fileStr,
                                            onSuccess = {
                                                isSharingInProgress = false
                                                secureShareFileSerialized = null
                                            },
                                            onFailure = { error ->
                                                isSharingInProgress = false
                                                android.widget.Toast.makeText(context, error, android.widget.Toast.LENGTH_LONG).show()
                                            }
                                        )
                                    },
                                    color = ThemePurple.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(ThemePurple.copy(alpha = 0.2f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Send, contentDescription = "Share", tint = ThemePurple)
                                        }
                                        Column {
                                            Text("Share File Securely", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                            Text("Share safely without permanently unhiding", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                                        }
                                    }
                                }

                                // 2. Unhide (Restore)
                                Surface(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        isUnhidingInProgress = true
                                        viewModel.unhideVaultFile(
                                            context = context,
                                            fileSerialized = fileStr,
                                            onSuccess = { msg ->
                                                isUnhidingInProgress = false
                                                secureShareFileSerialized = null
                                                android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_LONG).show()
                                            },
                                            onFailure = { err ->
                                                isUnhidingInProgress = false
                                                android.widget.Toast.makeText(context, err, android.widget.Toast.LENGTH_LONG).show()
                                            }
                                        )
                                    },
                                    color = Color(0xFF10B981).copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF10B981).copy(alpha = 0.2f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.LockOpen, contentDescription = "Unhide", tint = Color(0xFF10B981))
                                        }
                                        Column {
                                            Text("Unhide & Restore", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                            Text("Restore file back to original device folders", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                                        }
                                    }
                                }

                                // 3. Delete Permanently
                                Surface(
                                    onClick = {
                                        viewModel.triggerKeypressEffects(context)
                                        showPermanentDeleteConfirm = true
                                    },
                                    color = Color(0xFFEF4444).copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFEF4444).copy(alpha = 0.2f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444))
                                        }
                                        Column {
                                            Text("Delete Forever", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                            Text("Permanently delete file from vault and disk", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (showPermanentDeleteConfirm) {
                    AlertDialog(
                        onDismissRequest = { showPermanentDeleteConfirm = false },
                        containerColor = Color(0xFF1B2031),
                        titleContentColor = Color.White,
                        textContentColor = Color.White.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(24.dp),
                        title = {
                            Text("Permanently Delete File?", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                        },
                        text = {
                            Text("Are you sure you want to permanently delete \"${cleanDisplayName(originalName)}\"? This action is irreversible and cannot be undone.", fontSize = 14.sp)
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.triggerKeypressEffects(context)
                                    showPermanentDeleteConfirm = false
                                    val success = viewModel.permanentlyDeleteVaultFile(fileStr)
                                    if (success) {
                                        secureShareFileSerialized = null
                                        android.widget.Toast.makeText(context, "File deleted permanently.", android.widget.Toast.LENGTH_SHORT).show()
                                    } else {
                                        android.widget.Toast.makeText(context, "Failed to delete file.", android.widget.Toast.LENGTH_SHORT).show()
                                    }
                                }
                            ) {
                                Text("Delete Forever", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showPermanentDeleteConfirm = false }) {
                                Text("Cancel", color = Color.White.copy(alpha = 0.6f))
                            }
                        }
                    )
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
                        onValueChange = { noteTitle = it; viewModel.updateLastInteraction() },
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
                        onValueChange = { noteContent = it; viewModel.updateLastInteraction() },
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
                    val btnContentColor = if (IsWhiteTheme) Color(0xFF1E2235) else Color.White
                    Text("Save", color = btnContentColor)
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
    pendingDisguiseTarget?.let { (id, name) ->
        AlertDialog(
            onDismissRequest = { pendingDisguiseTarget = null },
            title = {
                Text(
                    text = "Apply App Disguise?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextDark
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to disguise this application as \"$name\"?\n\nThis will change its icon and name on your phone's home screen.\n\nNote: On some Android devices, the application will restart to safely apply the dynamic home screen disguise.",
                    fontSize = 14.sp,
                    color = TextMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.triggerKeypressEffects(context)
                        viewModel.setActiveAppIcon(context, id)
                        pendingDisguiseTarget = null
                        Toast.makeText(context, "App icon disguise updated to $name!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple)
                ) {
                    val btnContentColor = if (IsWhiteTheme) Color(0xFF1E2235) else Color.White
                    Text("Disguise Now", color = btnContentColor, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.triggerKeypressEffects(context)
                        pendingDisguiseTarget = null
                    }
                ) {
                    Text("Cancel", color = ThemePurple)
                }
            },
            containerColor = BrandBg
        )
    }
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
                            colors = dynamicSwitchColors()
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
                            colors = dynamicSwitchColors()
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
                                shape = RoundedCornerShape(8.dp),
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
                    val btnContentColor = if (IsWhiteTheme) Color(0xFF1E2235) else Color.White
                    Text("Save Settings", color = btnContentColor)
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
        val selectedLang by viewModel.selectedLanguage.collectAsStateWithLifecycle()
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
    // Dynamic Preferred Time Zone Selection Dialog
    if (showTimezoneDialog) {
        val selectedTz by viewModel.preferredTimezone.collectAsStateWithLifecycle()
        val timezones = listOf(
            Pair("Asia/Kolkata", "🇮🇳 India Standard Time (IST)"),
            Pair("America/Los_Angeles", "🇺🇸 US Pacific Time (PST/PDT)"),
            Pair("UTC", "🌐 Universal Time (UTC)"),
            Pair("System", "📱 Device/System Default")
        )
        AlertDialog(
            onDismissRequest = { showTimezoneDialog = false },
            title = {
                Text(
                    text = "Select Time Zone",
                    color = TextDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            containerColor = BrandBg,
            confirmButton = {
                TextButton(onClick = { showTimezoneDialog = false }) {
                    Text("OK", color = ThemePurple, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().heightIn(max = 350.dp)
                ) {
                    items(timezones) { (tzCode, tzName) ->
                        val isSelected = selectedTz == tzCode
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) ThemePurple.copy(alpha = 0.15f) else Color.Transparent)
                                .clickable {
                                    viewModel.triggerKeypressEffects(context)
                                    viewModel.setPreferredTimezone(tzCode)
                                    showTimezoneDialog = false
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = tzName,
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
    Box(modifier = Modifier.fillMaxSize()) {
        androidx.compose.material3.SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
        )
    }
    when (activeCameraMode) {
        "camera" -> {
            SecureCameraView(
                viewModel = viewModel,
                onDismiss = { activeCameraMode = null },
                onViewMedia = { fileStr, index, allFiles ->
                    activeViewerFiles = allFiles
                    activeViewerIndex = index
                }
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
    val canGoForward: Boolean = false,
    val isDesktopMode: Boolean = false
)

class ChromeContextWrapper(base: android.content.Context) : android.content.ContextWrapper(base) {
    override fun getPackageName(): String {
        return baseContext.packageName
    }
    override fun getApplicationContext(): android.content.Context {
        val appCtx = super.getApplicationContext()
        return if (appCtx != null) ChromeContextWrapper(appCtx) else this
    }
}

fun setSystemBarsVisibility(activity: android.app.Activity?, visible: Boolean) {
    if (activity == null) return
    val window = activity.window ?: return
    val decorView = window.decorView
    try {
        val controller = androidx.core.view.WindowCompat.getInsetsController(window, decorView)
        if (visible) {
            controller.show(androidx.core.view.WindowInsetsCompat.Type.systemBars())
        } else {
            controller.hide(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } catch (e: Throwable) {
        @Suppress("DEPRECATION")
        if (visible) {
            decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )
        } else {
            decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                android.view.View.SYSTEM_UI_FLAG_FULLSCREEN or
                android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }
    }
}

fun createPrivateWebView(
    ctx: android.content.Context,
    tabId: String,
    initialUrl: String,
    savePasswords: Boolean,
    isDesktopMode: Boolean = false,
    onDownloadRequested: (url: String, userAgent: String, contentDisposition: String, mimeType: String, contentLength: Long) -> Unit,
    onCreatePopup: (android.webkit.WebView?) -> Unit,
    onShowCustomView: (android.view.View, android.webkit.WebChromeClient.CustomViewCallback) -> Unit,
    onHideCustomView: () -> Unit,
    onUpdate: ((TabState) -> TabState) -> Unit
): android.webkit.WebView {
    val cleanMobileUa = "Mozilla/5.0 (Linux; Android 13; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
    val cleanDesktopUa = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
    val iOSMobileUa = "Mozilla/5.0 (iPhone; CPU iPhone OS 17_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4 Mobile/15E148 Safari/604.1"

    fun getUserAgentForUrl(url: String?, isDesktop: Boolean): String {
        val lowerUrl = url?.lowercase() ?: ""
        if (lowerUrl.contains("youtube.com") || lowerUrl.contains("youtu.be")) {
            return if (isDesktop) cleanDesktopUa else cleanMobileUa
        }
        if (isDesktop) return cleanDesktopUa
        val isSocialOrAuth = (lowerUrl.contains("facebook") ||
                lowerUrl.contains("instagram") ||
                lowerUrl.contains("google") ||
                lowerUrl.contains("oauth") ||
                lowerUrl.contains("auth") ||
                lowerUrl.contains("login") ||
                lowerUrl.contains("signin") ||
                lowerUrl.contains("twitter") ||
                lowerUrl.contains("x.com") ||
                lowerUrl.contains("linkedin") ||
                lowerUrl.contains("github") ||
                lowerUrl.contains("apple") ||
                lowerUrl.contains("microsoft") ||
                lowerUrl.contains("firebase") ||
                lowerUrl.contains("okta"))
                
        return if (isSocialOrAuth) {
            iOSMobileUa
        } else {
            cleanMobileUa
        }
    }

    fun checkAndRedirectSocialLogin(view: android.webkit.WebView?, url: String?): Boolean {
        return false
    }

    fun handleCustomUri(url: String, webView: android.webkit.WebView?): Boolean {
        val lower = url.lowercase()
        if (lower.startsWith("http://") || 
            lower.startsWith("https://") || 
            lower.startsWith("about:") || 
            lower.startsWith("javascript:") || 
            lower.startsWith("data:") || 
            lower.startsWith("blob:") || 
            lower.startsWith("file:")
        ) {
            return false
        }
        try {
            val intent = android.content.Intent.parseUri(url, android.content.Intent.URI_INTENT_SCHEME)
            if (intent != null) {
                try {
                    ctx.startActivity(intent)
                    return true
                } catch (e: Exception) {
                    val fallbackUrl = intent.getStringExtra("browser_fallback_url")
                    if (fallbackUrl != null && (fallbackUrl.startsWith("http://") || fallbackUrl.startsWith("https://"))) {
                        webView?.loadUrl(fallbackUrl)
                        return true
                    }
                    val uri = android.net.Uri.parse(url)
                    val fallbackParam = uri.getQueryParameter("browser_fallback_url")
                    if (fallbackParam != null && (fallbackParam.startsWith("http://") || fallbackParam.startsWith("https://"))) {
                        webView?.loadUrl(fallbackParam)
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            try {
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                ctx.startActivity(intent)
                return true
            } catch (ex: Exception) {}
        }
        return true
    }

    try {
        val webViewCacheDir = java.io.File(ctx.cacheDir, "WebView/Default/HTTP Cache/Code Cache")
        java.io.File(webViewCacheDir, "wasm").mkdirs()
        java.io.File(webViewCacheDir, "js").mkdirs()
    } catch (e: Exception) {}
    val wrappedCtx = ChromeContextWrapper(ctx)
    return object : android.webkit.WebView(wrappedCtx) {
        override fun loadUrl(url: String) {
            settings.userAgentString = getUserAgentForUrl(url, isDesktopMode)
            val headers = HashMap<String, String>()
            headers["X-Requested-With"] = ""
            super.loadUrl(url, headers)
        }
        override fun loadUrl(url: String, additionalHttpHeaders: Map<String, String>) {
            settings.userAgentString = getUserAgentForUrl(url, isDesktopMode)
            val headers = additionalHttpHeaders.toMutableMap()
            headers["X-Requested-With"] = ""
            super.loadUrl(url, headers)
        }
    }.apply {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            android.webkit.WebView.setWebContentsDebuggingEnabled(
                (ctx.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0
            )
        }
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)
            savePassword = savePasswords
            saveFormData = false
            mediaPlaybackRequiresUserGesture = false
            cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            userAgentString = getUserAgentForUrl(initialUrl, isDesktopMode)
            try {
                if (androidx.webkit.WebViewFeature.isFeatureSupported(androidx.webkit.WebViewFeature.REQUESTED_WITH_HEADER_ALLOW_LIST)) {
                    androidx.webkit.WebSettingsCompat.setRequestedWithHeaderOriginAllowList(this, emptySet())
                }
            } catch (e: Throwable) {}
        }
        setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            onDownloadRequested(url, userAgent, contentDisposition, mimetype, contentLength)
        }
        val cookieManager = android.webkit.CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(this, true)
        webViewClient = object : android.webkit.WebViewClient() {
            override fun onPageStarted(view: android.webkit.WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                if (checkAndRedirectSocialLogin(view, url)) return
                super.onPageStarted(view, url, favicon)
                view?.settings?.userAgentString = getUserAgentForUrl(url, isDesktopMode)
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
                try {
                    android.webkit.CookieManager.getInstance().flush()
                } catch (e: Exception) {}
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
            override fun shouldInterceptRequest(view: android.webkit.WebView?, request: android.webkit.WebResourceRequest?): android.webkit.WebResourceResponse? {
                return super.shouldInterceptRequest(view, request)
            }
            override fun shouldOverrideUrlLoading(view: android.webkit.WebView?, request: android.webkit.WebResourceRequest?): Boolean {
                val url = request?.url?.toString() ?: return false
                if (checkAndRedirectSocialLogin(view, url)) return true
                if (handleCustomUri(url, view)) return true
                val lowerUrl = url.lowercase()
                if (lowerUrl.startsWith("http://") || lowerUrl.startsWith("https://")) {
                    return false
                }
                return false
            }
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: android.webkit.WebView?, url: String?): Boolean {
                if (url == null) return false
                if (checkAndRedirectSocialLogin(view, url)) return true
                if (handleCustomUri(url, view)) return true
                val lowerUrl = url.lowercase()
                if (lowerUrl.startsWith("http://") || lowerUrl.startsWith("https://")) {
                    return false
                }
                return false
            }
            override fun onReceivedSslError(view: android.webkit.WebView?, handler: android.webkit.SslErrorHandler?, error: android.net.http.SslError?) {
                handler?.proceed()
            }
        }
        webChromeClient = object : android.webkit.WebChromeClient() {
            override fun onShowCustomView(view: android.view.View?, callback: android.webkit.WebChromeClient.CustomViewCallback?) {
                if (view != null && callback != null) {
                    onShowCustomView(view, callback)
                }
            }
            override fun onHideCustomView() {
                onHideCustomView()
            }
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
            override fun onCreateWindow(
                view: android.webkit.WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: android.os.Message?
            ): Boolean {
                if (view == null || resultMsg == null) return false
                val popupCtx = ChromeContextWrapper(view.context)
                val newWebView = object : android.webkit.WebView(popupCtx) {
                    override fun loadUrl(url: String) {
                        settings.userAgentString = getUserAgentForUrl(url, isDesktopMode)
                        val headers = HashMap<String, String>()
                        headers["X-Requested-With"] = ""
                        super.loadUrl(url, headers)
                    }
                    override fun loadUrl(url: String, additionalHttpHeaders: Map<String, String>) {
                        settings.userAgentString = getUserAgentForUrl(url, isDesktopMode)
                        val headers = additionalHttpHeaders.toMutableMap()
                        headers["X-Requested-With"] = ""
                        super.loadUrl(url, headers)
                    }
                }.apply {
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                        javaScriptCanOpenWindowsAutomatically = true
                        setSupportMultipleWindows(true)
                        savePassword = savePasswords
                        saveFormData = false
                        mediaPlaybackRequiresUserGesture = false
                        cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        }
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        setSupportZoom(true)
                        builtInZoomControls = true
                        displayZoomControls = false
                        userAgentString = getUserAgentForUrl(null, isDesktopMode)
                        try {
                            if (androidx.webkit.WebViewFeature.isFeatureSupported(androidx.webkit.WebViewFeature.REQUESTED_WITH_HEADER_ALLOW_LIST)) {
                                androidx.webkit.WebSettingsCompat.setRequestedWithHeaderOriginAllowList(this, emptySet())
                            }
                        } catch (e: Throwable) {}
                    }
                    val popupCookieManager = android.webkit.CookieManager.getInstance()
                    popupCookieManager.setAcceptCookie(true)
                    popupCookieManager.setAcceptThirdPartyCookies(this, true)
                    
                    webViewClient = object : android.webkit.WebViewClient() {
                        override fun onPageStarted(view: android.webkit.WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                            if (checkAndRedirectSocialLogin(view, url)) return
                            super.onPageStarted(view, url, favicon)
                            view?.settings?.userAgentString = getUserAgentForUrl(url, isDesktopMode)
                        }
                        override fun onPageFinished(newView: android.webkit.WebView?, url: String?) {
                            super.onPageFinished(newView, url)
                            try {
                                android.webkit.CookieManager.getInstance().flush()
                            } catch (e: Exception) {}
                        }
                        override fun shouldInterceptRequest(view: android.webkit.WebView?, request: android.webkit.WebResourceRequest?): android.webkit.WebResourceResponse? {
                            return super.shouldInterceptRequest(view, request)
                        }
                        override fun shouldOverrideUrlLoading(newView: android.webkit.WebView?, request: android.webkit.WebResourceRequest?): Boolean {
                            val url = request?.url?.toString() ?: return false
                            if (checkAndRedirectSocialLogin(newView, url)) return true
                            if (handleCustomUri(url, newView)) return true
                            val lowerUrl = url.lowercase()
                            if (lowerUrl.startsWith("http://") || lowerUrl.startsWith("https://")) {
                                return false
                            }
                            return false
                        }
                        @Deprecated("Deprecated in Java")
                        override fun shouldOverrideUrlLoading(newView: android.webkit.WebView?, url: String?): Boolean {
                            if (url == null) return false
                            if (checkAndRedirectSocialLogin(newView, url)) return true
                            if (handleCustomUri(url, newView)) return true
                            val lowerUrl = url.lowercase()
                            if (lowerUrl.startsWith("http://") || lowerUrl.startsWith("https://")) {
                                return false
                            }
                            return false
                        }
                        override fun onReceivedSslError(newView: android.webkit.WebView?, handler: android.webkit.SslErrorHandler?, error: android.net.http.SslError?) {
                            handler?.proceed()
                        }
                    }
                    webChromeClient = object : android.webkit.WebChromeClient() {
                        override fun onCloseWindow(window: android.webkit.WebView?) {
                            onCreatePopup(null)
                            try {
                                window?.destroy()
                            } catch (e: Exception) {}
                        }
                    }
                }
                
                onCreatePopup(newWebView)
                
                val transport = resultMsg.obj as? android.webkit.WebView.WebViewTransport
                if (transport != null) {
                    if (newWebView.isAttachedToWindow) {
                        transport.webView = newWebView
                        resultMsg.sendToTarget()
                    } else {
                        newWebView.addOnAttachStateChangeListener(object : android.view.View.OnAttachStateChangeListener {
                            override fun onViewAttachedToWindow(v: android.view.View) {
                                transport.webView = newWebView
                                resultMsg.sendToTarget()
                                newWebView.removeOnAttachStateChangeListener(this)
                            }
                            override fun onViewDetachedFromWindow(v: android.view.View) {}
                        })
                    }
                    return true
                }
                return false
            }
        }
        if (initialUrl != "home") { loadUrl(initialUrl) }
    }
}
fun launchSecureCustomTab(context: android.content.Context, url: String) {
    try {
        val builder = androidx.browser.customtabs.CustomTabsIntent.Builder()
        builder.setShowTitle(true)
        builder.setShareState(androidx.browser.customtabs.CustomTabsIntent.SHARE_STATE_ON)
        val params = androidx.browser.customtabs.CustomTabColorSchemeParams.Builder()
            .setToolbarColor(android.graphics.Color.parseColor("#161B2B"))
            .setNavigationBarColor(android.graphics.Color.parseColor("#0A0C16"))
            .build()
        builder.setDefaultColorSchemeParams(params)
        val customTabsIntent = builder.build()
        customTabsIntent.intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        customTabsIntent.launchUrl(context, android.net.Uri.parse(url))
    } catch (e: Exception) {
        try {
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (ex: Exception) {
            android.widget.Toast.makeText(context, "Secure browser could not be launched.", android.widget.Toast.LENGTH_SHORT).show()
        }
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
            (webView.parent as? android.view.ViewGroup)?.removeView(webView)
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
data class PendingDownloadData(
    val url: String,
    val userAgent: String,
    val contentDisposition: String,
    val mimeType: String,
    val contentLength: Long
)

@Composable
fun PrivateBrowserSection(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onExit: () -> Unit = {},
    onPanic: () -> Unit = {}
) {
    val context = LocalContext.current
    val tabs = viewModel.browserTabs
    val webViews = remember { mutableStateMapOf<String, android.webkit.WebView>() }
    var activeTabId: String? by remember { mutableStateOf(viewModel.activeTabId) }
    
    // Sync local activeTabId back to viewModel
    androidx.compose.runtime.LaunchedEffect(activeTabId) {
        viewModel.activeTabId = activeTabId
    }
    
    var showTabSwitcher by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var showDownloads by remember { mutableStateOf(false) }
    var showBookmarks by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var isEditingUrl by remember { mutableStateOf(false) }
    var editingUrlText by remember { mutableStateOf("") }

    val browserBookmarks by viewModel.browserBookmarks.collectAsStateWithLifecycle()
    val browserHistory by viewModel.browserHistory.collectAsStateWithLifecycle()
    val searchEngine by viewModel.searchEngine.collectAsStateWithLifecycle()
    val savePasswords by viewModel.savePasswords.collectAsStateWithLifecycle()
    val clearHistoryOnExit by viewModel.clearHistoryOnExit.collectAsStateWithLifecycle()

    var showSearchEngineDialog by remember { mutableStateOf(false) }
    var activePopupWebView by remember { mutableStateOf<android.webkit.WebView?>(null) }
    var pendingDownload by remember { mutableStateOf<PendingDownloadData?>(null) }
    
    var activeCustomView by remember { mutableStateOf<android.view.View?>(null) }
    var activeCustomViewCallback by remember { mutableStateOf<android.webkit.WebChromeClient.CustomViewCallback?>(null) }

    // Restore WebViews for existing tabs
    androidx.compose.runtime.LaunchedEffect(tabs.toList()) {
        tabs.forEach { tab ->
            if (!webViews.containsKey(tab.id)) {
                val webView = createPrivateWebView(
                    ctx = context,
                    tabId = tab.id,
                    initialUrl = tab.url,
                    savePasswords = savePasswords,
                    isDesktopMode = tab.isDesktopMode,
                    onDownloadRequested = { downloadUrl, userAgent, contentDisposition, mimeType, contentLength ->
                        pendingDownload = PendingDownloadData(downloadUrl, userAgent, contentDisposition, mimeType, contentLength)
                    },
                    onCreatePopup = { activePopupWebView = it },
                    onShowCustomView = { view, callback ->
                        activeCustomView = view
                        activeCustomViewCallback = callback
                        val activity = context as? android.app.Activity
                        activity?.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                        setSystemBarsVisibility(activity, false)
                    },
                    onHideCustomView = {
                        val viewToRemove = activeCustomView
                        if (viewToRemove != null) {
                            try { (viewToRemove.parent as? android.view.ViewGroup)?.removeView(viewToRemove) } catch(e: Exception) {}
                        }
                        activeCustomView = null
                        activeCustomViewCallback = null
                        val activity = context as? android.app.Activity
                        activity?.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                        setSystemBarsVisibility(activity, true)
                    }
                ) { transform ->
                    val index = tabs.indexOfFirst { it.id == tab.id }
                    if (index != -1) {
                        tabs[index] = transform(tabs[index])
                        val currentUrl = tabs[index].url
                        val currentTitle = tabs[index].title
                        if (currentUrl != "home" && currentUrl != "about:blank" && currentUrl.isNotEmpty()) {
                            viewModel.addBrowserHistory(currentTitle, currentUrl)
                        }
                    }
                }
                webViews[tab.id] = webView
            }
        }
    }

    val openNewTab: (String) -> Unit = { url ->
        val tabId = java.util.UUID.randomUUID().toString()
        val newTab = TabState(id = tabId, url = url, title = "New Tab")
        tabs.add(newTab)
        
        val webView = createPrivateWebView(
            ctx = context,
            tabId = tabId,
            initialUrl = url,
            savePasswords = savePasswords,
            onDownloadRequested = { downloadUrl, userAgent, contentDisposition, mimeType, contentLength ->
                pendingDownload = PendingDownloadData(downloadUrl, userAgent, contentDisposition, mimeType, contentLength)
            },
            onCreatePopup = { activePopupWebView = it },
            onShowCustomView = { view, callback ->
                activeCustomView = view
                activeCustomViewCallback = callback
                val activity = context as? android.app.Activity
                activity?.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                setSystemBarsVisibility(activity, false)
            },
            onHideCustomView = {
                val viewToRemove = activeCustomView
                if (viewToRemove != null) {
                    try { (viewToRemove.parent as? android.view.ViewGroup)?.removeView(viewToRemove) } catch(e: Exception) {}
                }
                activeCustomView = null
                activeCustomViewCallback = null
                val activity = context as? android.app.Activity
                activity?.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                setSystemBarsVisibility(activity, true)
            }
        ) { transform ->
            val index = tabs.indexOfFirst { it.id == tabId }
            if (index != -1) {
                tabs[index] = transform(tabs[index])
                val currentUrl = tabs[index].url
                val currentTitle = tabs[index].title
                if (currentUrl != "home" && currentUrl != "about:blank" && currentUrl.isNotEmpty()) {
                    viewModel.addBrowserHistory(currentTitle, currentUrl)
                }
            }
        }
        webViews[tabId] = webView
        activeTabId = tabId
    }

    LaunchedEffect(Unit) {
        if (tabs.isEmpty()) {
            openNewTab("home")
        }
    }

    val currentClearHistoryOnExit by androidx.compose.runtime.rememberUpdatedState(clearHistoryOnExit)
    DisposableEffect(Unit) {
        onDispose {
            if (currentClearHistoryOnExit) {
                viewModel.clearBrowserHistory()
                clearAllBrowsingData(context, tabs, webViews)
            }
        }
    }

    val activeTab = tabs.find { it.id == activeTabId }
    val activeWebView = webViews[activeTabId]

    val goBackOrExit = {
        if (showSearchEngineDialog) {
            showSearchEngineDialog = false
        } else if (showDownloads) {
            showDownloads = false
        } else if (showSettings) {
            showSettings = false
        } else if (showBookmarks) {
            showBookmarks = false
        } else if (showHistory) {
            showHistory = false
        } else if (showTabSwitcher) {
            showTabSwitcher = false
        } else if (activeWebView != null && activeWebView.canGoBack() && activeTab?.url != "home") {
            activeWebView.goBack()
        } else if (activeTab?.url != "home") {
            activeWebView?.loadUrl("about:blank")
            val index = tabs.indexOfFirst { it.id == activeTabId }
            if (index != -1) {
                tabs[index] = tabs[index].copy(url = "home", title = "New Tab")
            }
        } else {
            onExit()
        }
    }

    androidx.activity.compose.BackHandler {
        goBackOrExit()
    }


    pendingDownload?.let { download ->
        val guessedFilename = android.webkit.URLUtil.guessFileName(download.url, download.contentDisposition, download.mimeType) ?: "file"
        val sizeText = if (download.contentLength > 0) viewModel.formatFileSize(download.contentLength) else "Unknown Size"
        
        AlertDialog(
            onDismissRequest = { pendingDownload = null },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudDownload,
                        contentDescription = null,
                        tint = ThemePurple,
                        modifier = Modifier.size(24.dp)
                    )
                    Text("Confirm Download", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Do you want to download this file directly to your secure Vault?",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text("File details:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF141724), shape = RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text("Name: ", fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
                            Text(guessedFilename, color = Color.White, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text("Size: ", fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
                            Text(sizeText, color = Color.White, fontSize = 13.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text("Type: ", fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
                            Text(download.mimeType, color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.startVaultDownload(
                            context,
                            download.url,
                            download.userAgent,
                            download.contentDisposition,
                            download.mimeType,
                            download.contentLength
                        )
                        pendingDownload = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple)
                ) {
                    Text("Download", color = if (IsWhiteTheme) Color(0xFF1E2235) else Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDownload = null }) {
                    Text("Cancel", color = Color.White.copy(alpha = 0.6f))
                }
            },
            containerColor = Color(0xFF1E2235),
            titleContentColor = Color.White,
            textContentColor = Color.White
        )
    }

    if (activeCustomView != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            androidx.activity.compose.BackHandler(enabled = true) {
                try {
                    activeCustomViewCallback?.onCustomViewHidden()
                } catch (e: Exception) {}
                val viewToRemove = activeCustomView
                if (viewToRemove != null) {
                    try { (viewToRemove.parent as? android.view.ViewGroup)?.removeView(viewToRemove) } catch(e: Exception) {}
                }
                activeCustomView = null
                activeCustomViewCallback = null
                val activity = context as? android.app.Activity
                activity?.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                setSystemBarsVisibility(activity, true)
            }
            AndroidView(
                factory = { _ ->
                    (activeCustomView!!.parent as? android.view.ViewGroup)?.removeView(activeCustomView)
                    activeCustomView!!
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        return
    }

    if (showDownloads) {
        DownloadsScreen(
            viewModel = viewModel,
            onBack = { showDownloads = false },
            context = context
        )
        return
    }
    if (showSettings) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF0A0C16))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { showSettings = false }) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                }
                Text("Settings", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showSearchEngineDialog = true }
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(24.dp).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
                        Text("G", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold) // A generic icon representing search engine
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Search engine", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Text(searchEngine, color = Color(0xFF5E8AFF), fontSize = 14.sp)
                    }
                }
                Icon(Icons.Default.ArrowDropDown, "Dropdown", tint = Color.Gray)
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF5E8AFF), modifier = Modifier.size(24.dp)) 
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Save passwords", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
                Switch(
                    checked = savePasswords,
                    onCheckedChange = { viewModel.setSavePasswords(it) },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF5E8AFF))
                )
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.History, contentDescription = null, tint = Color(0xFFE0E0E0), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Clear history on exit", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
                Switch(
                    checked = clearHistoryOnExit,
                    onCheckedChange = { viewModel.setClearHistoryOnExit(it) },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF5E8AFF))
                )
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { 
                        android.webkit.WebStorage.getInstance().deleteAllData()
                        android.widget.Toast.makeText(context, "Cache cleared", android.widget.Toast.LENGTH_SHORT).show()
                    }
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFE0E0E0), modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Clear cache", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { 
                        android.webkit.CookieManager.getInstance().removeAllCookies(null)
                        android.widget.Toast.makeText(context, "Cookies cleared", android.widget.Toast.LENGTH_SHORT).show()
                    }
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.DeleteForever, contentDescription = null, tint = Color(0xFFE0E0E0), modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Clear cookies", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        
        if (showSearchEngineDialog) {
            androidx.compose.ui.window.Dialog(onDismissRequest = { showSearchEngineDialog = false }) {
                Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E202B))) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                        Text("Search Engine", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
                        val engines = listOf("Google", "DuckDuckGo", "Bing", "Yahoo")
                        engines.forEach { engine ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { 
                                        viewModel.setSearchEngine(engine)
                                        showSearchEngineDialog = false
                                    }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = searchEngine == engine,
                                    onClick = {
                                        viewModel.setSearchEngine(engine)
                                        showSearchEngineDialog = false
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF5E8AFF), unselectedColor = Color.Gray)
                                )
                                Text(engine, color = Color.White, fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                }
            }
        }
        return
    }

    if (showBookmarks) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF0A0C16))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { showBookmarks = false }) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                }
                Text("Bookmarks", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (browserBookmarks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No bookmarks yet", color = Color.Gray, fontSize = 16.sp)
                }
            } else {
                LazyColumn {
                    items(browserBookmarks) { bookmark ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showBookmarks = false
                                    activeWebView?.loadUrl(bookmark.url)
                                }
                                .padding(horizontal = 24.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(bookmark.title, color = Color.White, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(bookmark.url, color = Color.Gray, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            IconButton(onClick = { viewModel.removeBrowserBookmark(bookmark.url) }) {
                                Icon(Icons.Default.Delete, "Delete", tint = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
        return
    }

    if (showHistory) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF0A0C16))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { showHistory = false }) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                    }
                    Text("History", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
                }
                if (browserHistory.isNotEmpty()) {
                    IconButton(onClick = { viewModel.clearBrowserHistory() }) {
                        Icon(Icons.Default.DeleteForever, "Clear History", tint = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (browserHistory.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No history yet", color = Color.Gray, fontSize = 16.sp)
                }
            } else {
                LazyColumn {
                    items(browserHistory) { historyItem ->
                        val date = java.util.Date(historyItem.timestamp)
                        val formatter = java.text.SimpleDateFormat("MMM dd, HH:mm", java.util.Locale.getDefault())
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showHistory = false
                                    activeWebView?.loadUrl(historyItem.url)
                                }
                                .padding(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            Text(historyItem.title, color = Color.White, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(historyItem.url, color = Color.Gray, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                                Text(formatter.format(date), color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                }
            }
        }
        return
    }

    Box(modifier = modifier.fillMaxSize().background(Color(0xFF0A0C16))) {
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            val isHome = activeTab?.url == "home" || activeTab?.url == "about:blank" || activeTab?.url?.isEmpty() == true
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF161B2B))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .drawBehind {
                        drawLine(
                            color = Color.White.copy(alpha = 0.08f),
                            start = androidx.compose.ui.geometry.Offset(0f, size.height),
                            end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (isEditingUrl) {
                    IconButton(
                        onClick = { isEditingUrl = false },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.Close, "Cancel", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    
                    OutlinedTextField(
                        value = editingUrlText,
                        onValueChange = { editingUrlText = it },
                        placeholder = { Text("Search or enter URL", color = Color.Gray, fontSize = 13.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .height(36.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1C1F2D),
                            unfocusedContainerColor = Color(0xFF1C1F2D),
                            focusedBorderColor = Color(0xFF5E8AFF),
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                var target = editingUrlText.trim()
                                if (target.isNotEmpty()) {
                                    if (!target.startsWith("http://") && !target.startsWith("https://")) {
                                        if (target.contains(".") && !target.contains(" ")) {
                                            target = "https://$target"
                                        } else {
                                            val q = java.net.URLEncoder.encode(target, "UTF-8")
                                            target = when (searchEngine) {
                                                "DuckDuckGo" -> "https://duckduckgo.com/?q=$q&kl=us-en"
                                                "Bing" -> "https://www.bing.com/search?q=$q&setlang=en&cc=US"
                                                "Yahoo" -> "https://search.yahoo.com/search?p=$q&ei=UTF-8&vc=US&vl=en"
                                                else -> "https://www.google.com/search?q=$q&hl=en&gl=US"
                                            }
                                        }
                                    }
                                    activeWebView?.loadUrl(target)
                                }
                                isEditingUrl = false
                            }
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
                    )
                    
                    IconButton(
                        onClick = {
                            var target = editingUrlText.trim()
                            if (target.isNotEmpty()) {
                                if (!target.startsWith("http://") && !target.startsWith("https://")) {
                                    if (target.contains(".") && !target.contains(" ")) {
                                        target = "https://$target"
                                    } else {
                                        val q = java.net.URLEncoder.encode(target, "UTF-8")
                                        target = when (searchEngine) {
                                            "DuckDuckGo" -> "https://duckduckgo.com/?q=$q&kl=us-en"
                                            "Bing" -> "https://www.bing.com/search?q=$q&setlang=en&cc=US"
                                            "Yahoo" -> "https://search.yahoo.com/search?p=$q&ei=UTF-8&vc=US&vl=en"
                                            else -> "https://www.google.com/search?q=$q&hl=en&gl=US"
                                        }
                                    }
                                }
                                activeWebView?.loadUrl(target)
                            }
                            isEditingUrl = false
                        },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.Check, "Go", tint = Color(0xFF5E8AFF), modifier = Modifier.size(20.dp))
                    }
                } else {
                    IconButton(
                        onClick = { onExit() },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    
                    if (isHome) {
                        Text(
                            text = "Private browser",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 6.dp)
                        )
                    } else {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF1E202B))
                                .clickable {
                                    editingUrlText = activeTab?.url ?: ""
                                    isEditingUrl = true
                                }
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Secure Connection",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            val displayUrl = try {
                                java.net.URL(activeTab?.url).host.removePrefix("www.")
                            } catch(e: Exception) {
                                activeTab?.title ?: "Website"
                            }
                            Text(
                                text = displayUrl,
                                color = Color.White,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(
                                onClick = { activeWebView?.reload() },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                    
                    Box {
                        IconButton(
                            onClick = { showMenu = true },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(Icons.Default.MoreVert, "More Options", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(Color(0xFF1E202B), RoundedCornerShape(8.dp))
                        ) {
                            DropdownMenuItem(
                                text = { Text("Bookmarks", color = Color.White) },
                                onClick = { 
                                    showMenu = false
                                    showBookmarks = true
                                },
                                leadingIcon = { Icon(Icons.Default.Star, "Bookmarks", tint = Color.White) }
                            )
                            DropdownMenuItem(
                                text = { Text("History", color = Color.White) },
                                onClick = { 
                                    showMenu = false
                                    showHistory = true
                                },
                                leadingIcon = { Icon(Icons.Default.History, "History", tint = Color.White) }
                            )
                            DropdownMenuItem(
                                text = { Text("Downloads", color = Color.White) },
                                onClick = { 
                                    showMenu = false
                                    showDownloads = true
                                },
                                leadingIcon = { Icon(Icons.Default.Download, "Downloads", tint = Color.White) }
                            )
                            DropdownMenuItem(
                                text = { Text("Desktop site", color = Color.White) },
                                onClick = { 
                                    showMenu = false
                                    val webView = webViews[activeTabId]
                                    if (webView != null && activeTab != null) {
                                        val newMode = !activeTab.isDesktopMode
                                        val index = tabs.indexOfFirst { it.id == activeTabId }
                                        if (index != -1) {
                                            tabs[index] = tabs[index].copy(isDesktopMode = newMode)
                                        }
                                        if (newMode) {
                                            webView.settings.userAgentString = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                                            webView.settings.loadWithOverviewMode = true
                                            webView.settings.useWideViewPort = true
                                            webView.settings.setSupportZoom(true)
                                            webView.settings.builtInZoomControls = true
                                            webView.settings.displayZoomControls = false
                                        } else {
                                            webView.settings.userAgentString = android.webkit.WebSettings.getDefaultUserAgent(context)
                                            webView.settings.loadWithOverviewMode = true
                                            webView.settings.useWideViewPort = true
                                            webView.settings.setSupportZoom(true)
                                            webView.settings.builtInZoomControls = true
                                            webView.settings.displayZoomControls = false
                                        }
                                        webView.clearCache(true)
                                        webView.reload()
                                    }
                                },
                                leadingIcon = { 
                                    Icon(
                                        if (activeTab?.isDesktopMode == true) Icons.Default.DesktopMac else Icons.Default.Laptop, 
                                        "Desktop site", 
                                        tint = if (activeTab?.isDesktopMode == true) Color(0xFF5E8AFF) else Color.White
                                    ) 
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Settings", color = Color.White) },
                                onClick = { 
                                    showMenu = false
                                    showSettings = true
                                },
                                leadingIcon = { Icon(Icons.Default.Settings, "Settings", tint = Color.White) }
                            )
                            androidx.compose.material3.HorizontalDivider(color = Color.DarkGray)
                            DropdownMenuItem(
                                text = { Text("Return to Vault", color = Color.White) },
                                onClick = { 
                                    showMenu = false
                                    onExit()
                                },
                                leadingIcon = { Icon(Icons.Default.Lock, "Return to Vault", tint = Color.White) }
                            )
                            DropdownMenuItem(
                                text = { Text("Panic Mode", color = Color(0xFFFF5252)) },
                                onClick = { 
                                    showMenu = false
                                    if (clearHistoryOnExit) {
                                        viewModel.clearBrowserHistory()
                                    }
                                    clearAllBrowsingData(context, tabs, webViews)
                                    onPanic()
                                },
                                leadingIcon = { Icon(Icons.Default.Warning, "Panic Mode", tint = Color(0xFFFF5252)) }
                            )
                        }
                    }
                }
            }
            
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if (activeTab?.url == "home" || activeTab?.url == "about:blank" || activeTab?.url?.isEmpty() == true) {
                    var searchInput by remember { mutableStateOf("") }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(Color(0xFF0A0C16), Color(0xFF11142A))
                                )
                            )
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp)
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))

                        // Phantom Secure Shield Hero Section
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(ThemePurple.copy(alpha = 0.15f), CircleShape)
                                        .border(1.dp, ThemePurple.copy(alpha = 0.3f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = "Shield",
                                        tint = ThemePurple,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "PHANTOM SECURE",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(Color(0xFF4CAF50), CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Incognito Session Active",
                                        color = Color(0xFF4CAF50),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Premium Search Field
                        OutlinedTextField(
                            value = searchInput,
                            onValueChange = { searchInput = it },
                            placeholder = { Text("Search or enter URL", color = Color.Gray, fontSize = 15.sp) },
                            leadingIcon = {
                                IconButton(onClick = { showSearchEngineDialog = true }) {
                                    Icon(
                                        imageVector = when (searchEngine) {
                                            "DuckDuckGo" -> Icons.Default.Shield
                                            "Bing" -> Icons.Default.Language
                                            "Yahoo" -> Icons.Default.Language
                                            else -> Icons.Default.Search
                                        },
                                        contentDescription = "Engine",
                                        tint = ThemePurple,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    var target = searchInput.trim()
                                    if (target.isNotEmpty()) {
                                        if (!target.startsWith("http://") && !target.startsWith("https://")) {
                                            if (target.contains(".") && !target.contains(" ")) {
                                                target = "https://$target"
                                            } else {
                                                val q = java.net.URLEncoder.encode(target, "UTF-8")
                                                target = when (searchEngine) {
                                                    "DuckDuckGo" -> "https://duckduckgo.com/?q=$q&kl=us-en"
                                                    "Bing" -> "https://www.bing.com/search?q=$q&setlang=en&cc=US"
                                                    "Yahoo" -> "https://search.yahoo.com/search?p=$q&ei=UTF-8&vc=US&vl=en"
                                                    else -> "https://www.google.com/search?q=$q&hl=en&gl=US"
                                                }
                                            }
                                        }
                                        activeWebView?.loadUrl(target)
                                    }
                                }) {
                                    Icon(Icons.Default.ArrowForward, contentDescription = "Search", tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(26.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF161925),
                                unfocusedContainerColor = Color(0xFF161925),
                                focusedBorderColor = ThemePurple.copy(alpha = 0.4f),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.05f),
                                cursorColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    var target = searchInput.trim()
                                    if (target.isNotEmpty()) {
                                        if (!target.startsWith("http://") && !target.startsWith("https://")) {
                                            if (target.contains(".") && !target.contains(" ")) {
                                                target = "https://$target"
                                            } else {
                                                val q = java.net.URLEncoder.encode(target, "UTF-8")
                                                target = when (searchEngine) {
                                                    "DuckDuckGo" -> "https://duckduckgo.com/?q=$q&kl=us-en"
                                                    "Bing" -> "https://www.bing.com/search?q=$q&setlang=en&cc=US"
                                                    "Yahoo" -> "https://search.yahoo.com/search?p=$q&ei=UTF-8&vc=US&vl=en"
                                                    else -> "https://www.google.com/search?q=$q&hl=en&gl=US"
                                                }
                                            }
                                        }
                                        activeWebView?.loadUrl(target)
                                    }
                                }
                            )
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        // Quick Access Grid
                        Text(
                            text = "QUICK ACCESS",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            QuickAccessItem(url = "https://www.google.com/?hl=en&gl=US", label = "Google", onClick = { activeWebView?.loadUrl("https://www.google.com/?hl=en&gl=US") })
                            QuickAccessItem(url = "https://www.youtube.com", label = "YouTube", onClick = { activeWebView?.loadUrl("https://duckduckgo.com/?q=youtube&kl=us-en") })
                            QuickAccessItem(url = "https://duckduckgo.com/?kl=us-en", label = "DuckDuckGo", onClick = { activeWebView?.loadUrl("https://duckduckgo.com/?kl=us-en") })
                            QuickAccessItem(url = "https://en.wikipedia.org", label = "Wikipedia", onClick = { activeWebView?.loadUrl("https://en.wikipedia.org") })
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            QuickAccessItem(url = "https://www.reddit.com", label = "Reddit", onClick = { activeWebView?.loadUrl("https://www.reddit.com") })
                            QuickAccessItem(url = "https://x.com", label = "X / Twitter", onClick = { activeWebView?.loadUrl("https://x.com") })
                            QuickAccessItem(url = "https://web.telegram.org", label = "Telegram", onClick = { activeWebView?.loadUrl("https://web.telegram.org") })
                            QuickAccessItem(url = "https://www.quora.com", label = "Quora", onClick = { activeWebView?.loadUrl("https://www.quora.com") })
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Security Checklist Panel
                        Text(
                            text = "SECURITY PANEL",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF161925).copy(alpha = 0.6f)),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Active",
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text("Isolated Cookies", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                        Text("Cookies and site data remain separate.", color = Color.Gray, fontSize = 10.sp)
                                    }
                                }

                                androidx.compose.material3.HorizontalDivider(color = Color.White.copy(alpha = 0.04f), modifier = Modifier.padding(vertical = 4.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Active",
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text("Self-Destruct Session", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                        Text("All cache, storage, and history clear instantly on exit.", color = Color.Gray, fontSize = 10.sp)
                                    }
                                }

                                androidx.compose.material3.HorizontalDivider(color = Color.White.copy(alpha = 0.04f), modifier = Modifier.padding(vertical = 4.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Active",
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text("Local File Sandboxing", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                        Text("Vault data cannot leak to other system apps.", color = Color.Gray, fontSize = 10.sp)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                } else {
                    if (activeWebView != null) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            androidx.compose.runtime.key(activeWebView.hashCode()) {
                                AndroidView(
                                    factory = { _ ->
                                        (activeWebView.parent as? android.view.ViewGroup)?.removeView(activeWebView)
                                        activeWebView
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            if (activeTab?.isLoading == true) {
                                LinearProgressIndicator(
                                    progress = { (activeTab.progress) / 100f },
                                    modifier = Modifier.fillMaxWidth().height(2.dp).align(Alignment.TopCenter),
                                    color = Color(0xFF5E8AFF),
                                    trackColor = Color.Transparent
                                )
                            }
                        }
                    }
                    
                    val isBookmarked = browserBookmarks.any { it.url == activeTab?.url }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .size(48.dp)
                            .background(Color(0xFF1E202B).copy(alpha = 0.8f), CircleShape)
                            .clickable {
                                if (isBookmarked) {
                                    viewModel.removeBrowserBookmark(activeTab?.url ?: "")
                                    android.widget.Toast.makeText(context, "Removed from bookmarks", android.widget.Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.addBrowserBookmark(activeTab?.title ?: "New Tab", activeTab?.url ?: "")
                                    android.widget.Toast.makeText(context, "Added to bookmarks", android.widget.Toast.LENGTH_SHORT).show()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (isBookmarked) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) Color(0xFFFFC107) else Color.White
                        )
                    }
                }
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F1015))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .drawBehind {
                        drawLine(
                            color = Color.White.copy(alpha = 0.08f),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                    },
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { activeWebView?.goBack() },
                    enabled = activeTab?.canGoBack == true,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = if (activeTab?.canGoBack == true) Color.White else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
                
                IconButton(
                    onClick = { activeWebView?.goForward() },
                    enabled = activeTab?.canGoForward == true,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Forward",
                        tint = if (activeTab?.canGoForward == true) Color.White else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
                
                IconButton(
                    onClick = {
                        activeWebView?.loadUrl("about:blank")
                        val index = tabs.indexOfFirst { it.id == activeTabId }
                        if (index != -1) {
                            tabs[index] = tabs[index].copy(url = "home", title = "New Tab")
                        }
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = { openNewTab("home") },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Tab",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .border(1.5.dp, Color.White, RoundedCornerShape(5.dp))
                        .clickable { showTabSwitcher = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tabs.size.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
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
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E202B))
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
                                text = "Active Tabs (${tabs.size})",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            IconButton(onClick = { openNewTab("home"); showTabSwitcher = false }) {
                                Icon(Icons.Default.Add, "New Tab", tint = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyColumn(
                            modifier = Modifier
                                .heightIn(max = 300.dp)
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
                                        containerColor = if (isActive) Color(0xFF323647) else Color(0xFF262836)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = if (tab.url == "home") "Home Page" else tab.title,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            if (tab.url != "home") {
                                                Text(
                                                    text = tab.url,
                                                    fontSize = 12.sp,
                                                    color = Color.Gray,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                        IconButton(
                                            onClick = {
                                                val wv = webViews[tab.id]
                                                if (wv != null) {
                                                    try {
                                                        wv.stopLoading()
                                                        (wv.parent as? android.view.ViewGroup)?.removeView(wv)
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
                                                        openNewTab("home")
                                                    }
                                                }
                                            },
                                            modifier = Modifier.size(48.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Close Tab",
                                                tint = Color.White,
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

    if (activePopupWebView != null) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = {
                activePopupWebView?.destroy()
                activePopupWebView = null
            },
            properties = androidx.compose.ui.window.DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF0F111A)
            ) {
                androidx.activity.compose.BackHandler(enabled = true) {
                    if (activePopupWebView?.canGoBack() == true) {
                        activePopupWebView?.goBack()
                    } else {
                        activePopupWebView?.destroy()
                        activePopupWebView = null
                    }
                }
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF171A26))
                            .statusBarsPadding()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            activePopupWebView?.destroy()
                            activePopupWebView = null
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Close Popup", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Secure Log In / Authenticate",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            activePopupWebView?.reload()
                        }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                        }
                    }
                    
                    Box(modifier = Modifier.fillMaxSize().weight(1f)) {
                        AndroidView(
                            factory = { _ ->
                                (activePopupWebView!!.parent as? android.view.ViewGroup)?.removeView(activePopupWebView)
                                activePopupWebView!!
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }

    // Fullscreen video overlay is handled at the beginning of rendering
}

@Composable
fun QuickAccessItem(url: String, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.White, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            val domain = try {
                java.net.URI(url).host?.removePrefix("www.") ?: url
            } catch(e: Exception) { url }
            coil.compose.AsyncImage(
                model = "https://www.google.com/s2/favicons?domain=$domain&sz=128",
                contentDescription = label,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, color = Color.White, fontSize = 12.sp)
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
    val lockedApps by viewModel.lockedApps.collectAsStateWithLifecycle()
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
@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun UnifiedGlassCard(
    modifier: Modifier = Modifier,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(22.dp),
    bgColor: Color,
    elevation: androidx.compose.ui.unit.Dp = 4.dp,
    glowAlpha: Float = 0f,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
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
                                Color.White.copy(alpha = 0.12f * glowAlpha),
                                Color.White.copy(alpha = 0.02f * glowAlpha),
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
                                Color.White.copy(alpha = 0.10f * glowAlpha),
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
                if (onClick != null || onLongClick != null) Modifier.combinedClickable(
                    interactionSource = interactionSource,
                    indication = androidx.compose.material3.ripple(
                        bounded = true,
                        color = ThemePurple.copy(alpha = 0.3f)
                    ),
                    onClick = onClick ?: {},
                    onLongClick = onLongClick
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
        shape = RoundedCornerShape(14.dp),
        bgColor = Color(0xFF161B2B).copy(alpha = 0.95f),
        elevation = 4.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(10.dp).fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(themePurple.copy(alpha = 0.1f))
                        .border(1.dp, themePurple.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = themePurple, modifier = Modifier.size(15.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(1.dp))
            Text(count, color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Preview content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF0F121C))
                    .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                previewContent()
            }
        }
    }
}

fun parseRichTextToAnnotatedString(text: String): AnnotatedString {

        val builder = AnnotatedString.Builder()
    var i = 0
    val n = text.length
    
    class StyleInfo(
        val type: String,
        val value: String?,
        val start: Int
    )
    
    val activeStyles = mutableListOf<StyleInfo>()
    
    while (i < n) {
        if (text[i] == '<') {
            val closeIndex = text.indexOf('>', i)
            if (closeIndex != -1) {
                val tagContent = text.substring(i + 1, closeIndex).trim()
                val isClosing = tagContent.startsWith("/")
                val cleanTag = if (isClosing) tagContent.substring(1) else tagContent
                
                if (isClosing) {
                    val tagName = cleanTag.substringBefore(" ").lowercase()
                    val matchIndex = activeStyles.indexOfLast { it.type == tagName }
                    if (matchIndex != -1) {
                        val style = activeStyles[matchIndex]
                        val end = builder.length
                        applyStyleRange(builder, style.type, style.value, style.start, end)
                        activeStyles.removeAt(matchIndex)
                    }
                } else {
                    val tagName = cleanTag.substringBefore(" ").lowercase()
                    var tagValue: String? = null
                    if (tagName == "color" || tagName == "bg") {
                        val hexMatch = Regex("""hex=["']?([^"'\s>]+)["']?""").find(cleanTag)
                        if (hexMatch != null) {
                            tagValue = hexMatch.groupValues[1]
                        }
                    } else if (tagName == "font") {
                        val faceMatch = Regex("""face=["']?([^"'\s>]+)["']?""").find(cleanTag)
                        if (faceMatch != null) {
                            tagValue = faceMatch.groupValues[1]
                        }
                    }
                    activeStyles.add(StyleInfo(tagName, tagValue, builder.length))
                }
                i = closeIndex + 1
                continue
            }
        }
        builder.append(text[i])
        i++
    }
    
    for (style in activeStyles.reversed()) {
        val end = builder.length
        applyStyleRange(builder, style.type, style.value, style.start, end)
    }
    try {
    return builder.toAnnotatedString()
    } catch (e: Exception) {
        e.printStackTrace()
        return androidx.compose.ui.text.AnnotatedString(text)
    }
}

private fun applyStyleRange(builder: AnnotatedString.Builder, type: String, value: String?, start: Int, end: Int) {
    if (start >= end) return
    val cleanValue = value?.trim()?.removeSurrounding("\"")?.removeSurrounding("'")
    val style = when (type) {
        "b" -> SpanStyle(fontWeight = FontWeight.Bold)
        "i" -> SpanStyle(fontStyle = FontStyle.Italic)
        "u" -> SpanStyle(textDecoration = TextDecoration.Underline)
        "color" -> {
            val color = parseColorString(cleanValue)
            SpanStyle(color = color)
        }
        "bg" -> {
            val color = parseBgColorString(cleanValue)
            val finalColor = if (color != Color.Transparent) color.copy(alpha = 0.8f) else Color.Transparent
            SpanStyle(background = finalColor)
        }

        else -> null
    }
    if (style != null) {
        builder.addStyle(style, start, end)
    }
}

fun safeTextFieldValue(text: String, selectionStart: Int, selectionEnd: Int = selectionStart): TextFieldValue {
    val len = text.length
    val start = selectionStart.coerceIn(0, len)
    val end = selectionEnd.coerceIn(0, len)
    return TextFieldValue(text, TextRange(minOf(start, end), maxOf(start, end)))
}

fun clearTagTypeFromSelection(fieldValue: TextFieldValue, tagName: String): TextFieldValue {
    val text = fieldValue.text
    val start = minOf(fieldValue.selection.start, fieldValue.selection.end).coerceIn(0, text.length)
    val end = maxOf(fieldValue.selection.start, fieldValue.selection.end).coerceIn(0, text.length)
    if (start == end) return fieldValue
    
    val selectedText = text.substring(start, end)
    val regex = Regex("""<""" + tagName + """[^>]*>|</""" + tagName + """>""")
    val cleanedText = selectedText.replace(regex, "")
    
    val newText = text.substring(0, start) + cleanedText + text.substring(end)
    return safeTextFieldValue(newText, start, start + cleanedText.length)
}

fun applyStyleTagToSelection(fieldValue: TextFieldValue, tagName: String, attrName: String, attrValue: String): TextFieldValue {
    val text = fieldValue.text
    val start = minOf(fieldValue.selection.start, fieldValue.selection.end).coerceIn(0, text.length)
    val end = maxOf(fieldValue.selection.start, fieldValue.selection.end).coerceIn(0, text.length)
    
    val clearedValue = clearTagTypeFromSelection(fieldValue, tagName)
    
    val clearedText = clearedValue.text
    val newStart = minOf(clearedValue.selection.start, clearedValue.selection.end).coerceIn(0, clearedText.length)
    val newEnd = maxOf(clearedValue.selection.start, clearedValue.selection.end).coerceIn(0, clearedText.length)
    
    val selectedText = clearedText.substring(newStart, newEnd)
    if (attrValue == "transparent") return clearedValue // Just clear
    
    val tagOpen = "<$tagName $attrName=\"$attrValue\">"
    val tagClose = "</$tagName>"
    
    if (newStart == newEnd) {
        val newText = clearedText.substring(0, newStart) + tagOpen + tagClose + clearedText.substring(newStart)
        return safeTextFieldValue(newText, newStart + tagOpen.length)
    }
    
    val newText = clearedText.substring(0, newStart) + tagOpen + selectedText + tagClose + clearedText.substring(newEnd)
    return safeTextFieldValue(newText, newStart + tagOpen.length, newStart + tagOpen.length + selectedText.length)
}

fun applyTagToSelection(fieldValue: TextFieldValue, tagOpen: String, tagClose: String): TextFieldValue {

        val text = fieldValue.text
    val textLength = text.length
    val rawStart = fieldValue.selection.start
    val rawEnd = fieldValue.selection.end
    
    val normStart = minOf(rawStart, rawEnd)
    val normEnd = maxOf(rawStart, rawEnd)
    
    val start = normStart.coerceIn(0, textLength)
    val end = normEnd.coerceIn(0, textLength)
    
    val selectedText = text.substring(start, end)
    val newText = text.substring(0, start) + tagOpen + selectedText + tagClose + text.substring(end)
    
    val newSelectionStart = start + tagOpen.length
    val newSelectionEnd = newSelectionStart + selectedText.length
    
    return safeTextFieldValue(newText, newSelectionStart, newSelectionEnd)
}


fun isTagActive(fieldValue: TextFieldValue, tagOpen: String, tagClose: String): Boolean {
    val text = fieldValue.text
    val start = fieldValue.selection.start.coerceIn(0, text.length)
    if (start == 0 && text.isEmpty()) return false
    val searchEnd = (start - 1).coerceAtLeast(0)
    
    // Actually Kotlin's lastIndexOf with startIndex means it searches backwards starting from startIndex.
    val lastOpen = if (start > 0) text.lastIndexOf(tagOpen, searchEnd) else -1
    if (lastOpen != -1) {
        val lastCloseBeforeOpen = if (start > 0) text.lastIndexOf(tagClose, searchEnd) else -1
        if (lastCloseBeforeOpen < lastOpen) {
            val nextClose = text.indexOf(tagClose, start)
            if (nextClose != -1) {
                val nextOpen = text.indexOf(tagOpen, start)
                if (nextOpen == -1 || nextClose < nextOpen || nextClose == start) {
                    return true
                }
            } else {
                return true 
            }
        }
    }
    return false
}

fun isPrefixActive(fieldValue: TextFieldValue, prefix: String): Boolean {
    val text = fieldValue.text
    val cursor = fieldValue.selection.start.coerceIn(0, text.length)
    val searchEnd = (cursor - 1).coerceAtLeast(0)
    val lineStart = if (cursor > 0) text.lastIndexOf('\n', searchEnd) + 1 else 0
    if (lineStart < 0 || lineStart > text.length) return false
    return text.substring(lineStart).startsWith(prefix) || 
           (prefix == "[ ] " && text.substring(lineStart).startsWith("[x] "))
}

fun toggleTag(fieldValue: TextFieldValue, tagOpen: String, tagClose: String): TextFieldValue {
        val text = fieldValue.text
    val textLength = text.length
    val rawStart = fieldValue.selection.start
    val rawEnd = fieldValue.selection.end
    
    val normStart = minOf(rawStart, rawEnd)
    val normEnd = maxOf(rawStart, rawEnd)
    
    val start = normStart.coerceIn(0, textLength)
    val end = normEnd.coerceIn(0, textLength)

    if (start == end) {
        val lastOpen = if (start > 0) text.lastIndexOf(tagOpen, (start - 1).coerceIn(0, textLength - 1)) else -1
        if (lastOpen != -1) {
            val lastCloseBeforeOpen = if (start > 0) text.lastIndexOf(tagClose, (start - 1).coerceIn(0, textLength - 1)) else -1
            if (lastCloseBeforeOpen < lastOpen) {
                val nextClose = text.indexOf(tagClose, start.coerceIn(0, textLength))
                val nextOpenBeforeClose = text.indexOf(tagOpen, start.coerceIn(0, textLength))
                val hasNextClose = nextClose != -1 && (nextOpenBeforeClose == -1 || nextOpenBeforeClose > nextClose)
                if (hasNextClose) {
                    val openTagEnd = lastOpen + tagOpen.length
                    if (openTagEnd == start && nextClose == start) {
                        val newText = text.substring(0, lastOpen) + text.substring((nextClose + tagClose.length).coerceIn(0, textLength))
                        return safeTextFieldValue(newText, lastOpen)
                    }
                    if (nextClose == start) {
                        return safeTextFieldValue(text, (start + tagClose.length))
                    } else {
                        val newText = text.substring(0, start) + tagClose + tagOpen + text.substring(start)
                        return safeTextFieldValue(newText, (start + tagClose.length))
                    }
                } else {
                    val newText = text.substring(0, start) + tagClose + text.substring(start)
                    return safeTextFieldValue(newText, (start + tagClose.length))
                }
            }
        }
        val newText = text.substring(0, start) + tagOpen + tagClose + text.substring(start)
        return safeTextFieldValue(newText, (start + tagOpen.length))
    } else {
        val selectedText = text.substring(start, end)
        if (selectedText.startsWith(tagOpen) && selectedText.endsWith(tagClose)) {
            val unwrapped = if (selectedText.length >= tagOpen.length + tagClose.length) {
                selectedText.substring(tagOpen.length, (selectedText.length - tagClose.length).coerceAtLeast(0))
            } else {
                ""
            }
            val newText = text.substring(0, start) + unwrapped + text.substring(end)
            return safeTextFieldValue(newText, start, start + unwrapped.length)
        }
        if (start >= tagOpen.length && end + tagClose.length <= text.length) {
            val potentialOpen = text.substring((start - tagOpen.length).coerceIn(0, textLength), start)
            val potentialClose = text.substring(end, (end + tagClose.length).coerceIn(0, textLength))
            if (potentialOpen == tagOpen && potentialClose == tagClose) {
                val newText = text.substring(0, (start - tagOpen.length).coerceIn(0, textLength)) + selectedText + text.substring((end + tagClose.length).coerceIn(0, textLength))
                return safeTextFieldValue(newText, start - tagOpen.length, end - tagOpen.length)
            }
        }
        if (selectedText.contains(tagOpen) || selectedText.contains(tagClose)) {
            val cleaned = selectedText.replace(tagOpen, "").replace(tagClose, "")
            val newText = text.substring(0, start) + cleaned + text.substring(end)
            return safeTextFieldValue(newText, start, start + cleaned.length)
        }
        val newText = text.substring(0, start) + tagOpen + selectedText + tagClose + text.substring(end)
        return safeTextFieldValue(newText, start + tagOpen.length, start + tagOpen.length + selectedText.length)
    }

}
fun parseColorString(hex: String?): Color {
    if (hex == null) return Color.White
    val clean = hex.trim().replace("\"", "").replace("'", "")
    if (clean.isEmpty()) return Color.White
    val normalized = if (clean.startsWith("#")) clean else "#$clean"
    return try {
        Color(android.graphics.Color.parseColor(normalized))
    } catch (e: Exception) {
        when (clean.lowercase()) {
            "red" -> Color.Red
            "green" -> Color.Green
            "blue" -> Color.Blue
            "black" -> Color.Black
            "white" -> Color.White
            "transparent" -> Color.Transparent
            else -> Color.White
        }
    }
}

fun parseBgColorString(hex: String?): Color {
    if (hex == null) return Color.Transparent
    val clean = hex.trim().replace("\"", "").replace("'", "")
    if (clean.isEmpty() || clean == "transparent" || clean == "000000" || clean == "#000000") return Color.Transparent
    val normalized = if (clean.startsWith("#")) clean else "#$clean"
    return try {
        Color(android.graphics.Color.parseColor(normalized))
    } catch (e: Exception) {
        Color.Transparent
    }
}

fun toggleLinePrefix(fieldValue: TextFieldValue, prefix: String): TextFieldValue {
        val text = fieldValue.text
        val selection = fieldValue.selection
        val cursor = selection.start
        
        val lineStart = text.lastIndexOf('\n', cursor - 1) + 1
        
        val knownPrefixes = listOf("[ ] ", "[x] ", "• ", "- ")
        var currentPrefix = ""
        for (p in knownPrefixes) {
            if (text.startsWith(p, lineStart)) {
                currentPrefix = p
                break
            }
        }
        
        val newText: String
        val newCursor: Int
        if (currentPrefix == prefix) {
            // Remove prefix
            newText = text.substring(0, lineStart) + text.substring(lineStart + currentPrefix.length)
            newCursor = (cursor - currentPrefix.length).coerceAtLeast(lineStart)
        } else if (currentPrefix.isNotEmpty()) {
            // Replace prefix
            newText = text.substring(0, lineStart) + prefix + text.substring(lineStart + currentPrefix.length)
            val diff = prefix.length - currentPrefix.length
            newCursor = (cursor + diff).coerceAtLeast(lineStart)
        } else {
            // Add prefix
            newText = text.substring(0, lineStart) + prefix + text.substring(lineStart)
            newCursor = cursor + prefix.length
        }
        
        return TextFieldValue(
            text = newText,
            selection = TextRange(newCursor)
        )
}

class RichTextVisualTransformation(private val themePurple: Color) : VisualTransformation {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RichTextVisualTransformation) return false
        return themePurple == other.themePurple
    }

    override fun hashCode(): Int {
        return themePurple.hashCode()
    }

    override fun filter(text: AnnotatedString): TransformedText {
        try {
                val rawText = text.text
                val n = rawText.length
                
                val builder = AnnotatedString.Builder()
                
                class StyleInfo(
                    val type: String,
                    val value: String?,
                    val start: Int
                )
                
                val activeStyles = mutableListOf<StyleInfo>()
                val origToTrans = IntArray(n + 1)
                val transToOrig = mutableListOf<Int>()
                
                var i = 0
                var lineStartInTransformed = 0
                
                class LineStyle(val start: Int, val end: Int, val type: String)
                val lineStyles = mutableListOf<LineStyle>()
                
                while (i < n) {
                    origToTrans[i] = builder.length
                    if (rawText[i] == '<') {
                        val closeIndex = rawText.indexOf('>', i)
                        if (closeIndex != -1) {
                            val tagContent = rawText.substring(i + 1, closeIndex).trim()
                            val isClosing = tagContent.startsWith("/")
                            val cleanTag = if (isClosing) tagContent.substring(1) else tagContent
                            val tagName = cleanTag.substringBefore(" ").lowercase()
                            
                            if (isClosing) {
                                val matchIndex = activeStyles.indexOfLast { it.type == tagName }
                                if (matchIndex != -1) {
                                    val style = activeStyles[matchIndex]
                                    val end = builder.length
                                    applyStyleRangeLocal(builder, style.type, style.value, style.start, end)
                                    activeStyles.removeAt(matchIndex)
                                }
                            } else {
                                var tagValue: String? = null
                                if (tagName == "color" || tagName == "bg") {
                                    val hexMatch = Regex("""hex=["']?([^"'\s>]+)["']?""").find(cleanTag)
                                    if (hexMatch != null) {
                                        tagValue = hexMatch.groupValues[1]
                                    }
                                } else if (tagName == "font") {
                                    val faceMatch = Regex("""face=["']?([^"'\s>]+)["']?""").find(cleanTag)
                                    if (faceMatch != null) {
                                        tagValue = faceMatch.groupValues[1]
                                    }
                                }
                                activeStyles.add(StyleInfo(tagName, tagValue, builder.length))
                            }
                            
                            for (k in i..closeIndex) {
                                origToTrans[k] = builder.length
                            }
                            i = closeIndex + 1
                            continue
                        }
                    }
                    
                    if (rawText[i] == '\n') {
                        val lineText = builder.toString().substring(lineStartInTransformed)
                        if (lineText.startsWith("[x] ")) {
                            lineStyles.add(LineStyle(lineStartInTransformed, builder.length, "completed_checklist"))
                        } else if (lineText.startsWith("[ ] ")) {
                            lineStyles.add(LineStyle(lineStartInTransformed, builder.length, "pending_checklist"))
                        } else if (lineText.startsWith("• ") || lineText.startsWith("- ")) {
                            lineStyles.add(LineStyle(lineStartInTransformed, builder.length, "bullet"))
                        } else if (lineText.matches(Regex("""^\d+\.\s.*"""))) {
                            lineStyles.add(LineStyle(lineStartInTransformed, builder.length, "numbered"))
                        }
                        
                        transToOrig.add(i)
                        builder.append('\n')
                        lineStartInTransformed = builder.length
                        i++
                        continue
                    }
                    
                    transToOrig.add(i)
                    builder.append(rawText[i])
                    i++
                }
                origToTrans[n] = builder.length
                transToOrig.add(n)
                
                val finalLineText = builder.toString().substring(lineStartInTransformed)
                if (finalLineText.startsWith("[x] ")) {
                    lineStyles.add(LineStyle(lineStartInTransformed, builder.length, "completed_checklist"))
                } else if (finalLineText.startsWith("[ ] ")) {
                    lineStyles.add(LineStyle(lineStartInTransformed, builder.length, "pending_checklist"))
                } else if (finalLineText.startsWith("• ") || finalLineText.startsWith("- ")) {
                    lineStyles.add(LineStyle(lineStartInTransformed, builder.length, "bullet"))
                } else if (finalLineText.matches(Regex("""^\d+\.\s.*"""))) {
                    lineStyles.add(LineStyle(lineStartInTransformed, builder.length, "numbered"))
                }
                
                for (style in activeStyles.reversed()) {
                    val end = builder.length
                    applyStyleRangeLocal(builder, style.type, style.value, style.start, end)
                }
                
                for (lineStyle in lineStyles) {
                    when (lineStyle.type) {
                        "completed_checklist" -> {
                            builder.addStyle(SpanStyle(color = themePurple, fontWeight = FontWeight.Bold), lineStyle.start, lineStyle.start + 3)
                            builder.addStyle(SpanStyle(textDecoration = TextDecoration.LineThrough, color = Color.White.copy(alpha = 0.4f)), lineStyle.start + 4, lineStyle.end)
                        }
                        "pending_checklist" -> {
                            builder.addStyle(SpanStyle(color = Color.White.copy(alpha = 0.5f), fontWeight = FontWeight.Bold), lineStyle.start, lineStyle.start + 3)
                        }
                        "bullet" -> {
                            builder.addStyle(SpanStyle(color = themePurple, fontWeight = FontWeight.Bold), lineStyle.start, lineStyle.start + 1)
                        }
                        "numbered" -> {
                            val dotIdx = builder.toString().indexOf('.', lineStyle.start)
                            if (dotIdx != -1 && dotIdx < lineStyle.end) {
                                builder.addStyle(SpanStyle(color = themePurple, fontWeight = FontWeight.Bold), lineStyle.start, dotIdx + 1)
                            }
                        }
                    }
                }
                
                val transformedAnnotatedString = builder.toAnnotatedString()
                val transformedLength = transformedAnnotatedString.length
                
                val offsetMapping = object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int {
                        val clamped = offset.coerceIn(0, n)
                        return origToTrans[clamped].coerceIn(0, transformedLength)
                    }
                    
                    override fun transformedToOriginal(offset: Int): Int {
                        val clamped = offset.coerceIn(0, transformedLength)
                        return transToOrig[clamped].coerceIn(0, n)
                    }
                }
                
                return TransformedText(transformedAnnotatedString, offsetMapping)
        } catch (e: Exception) {
            e.printStackTrace()
            return TransformedText(
                androidx.compose.ui.text.AnnotatedString(text.text), 
                androidx.compose.ui.text.input.OffsetMapping.Identity
            )
        }
    }
    
    private fun applyStyleRangeLocal(builder: AnnotatedString.Builder, type: String, value: String?, start: Int, end: Int) {
        if (start >= end) return
        val cleanValue = value?.trim()?.removeSurrounding("\"")?.removeSurrounding("'")
        val style = when (type) {
            "b" -> SpanStyle(fontWeight = FontWeight.Bold)
            "i" -> SpanStyle(fontStyle = FontStyle.Italic)
            "u" -> SpanStyle(textDecoration = TextDecoration.Underline)
            "color" -> {
                val color = parseColorString(cleanValue)
                SpanStyle(color = color)
            }
            "bg" -> {
                val color = parseBgColorString(cleanValue)
                val finalColor = if (color != Color.Transparent) color.copy(alpha = 0.8f) else Color.Transparent
                SpanStyle(background = finalColor)
            }
            "font" -> {
                val fontFamily = when (cleanValue?.lowercase()) {
                    "serif" -> androidx.compose.ui.text.font.FontFamily.Serif
                    "monospace" -> androidx.compose.ui.text.font.FontFamily.Monospace
                    "cursive" -> androidx.compose.ui.text.font.FontFamily.Cursive
                    "sans-serif" -> androidx.compose.ui.text.font.FontFamily.SansSerif
                    else -> androidx.compose.ui.text.font.FontFamily.Default
                }
                SpanStyle(fontFamily = fontFamily)
            }
            else -> null
        }
        if (style != null) {
            builder.addStyle(style, start, end)
        }
    }
}

@Composable
fun RenderNoteContent(
    content: String,
    themePurple: Color,
    onToggleChecklist: (lineIndex: Int, checked: Boolean) -> Unit
) {
    val lines = content.split("\n")
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        lines.forEachIndexed { index, line ->
            when {
                line.startsWith("[ ] ") -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleChecklist(index, true) }
                            .padding(vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckBoxOutlineBlank,
                            contentDescription = "Unchecked",
                            tint = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = parseRichTextToAnnotatedString(line.substring(4)),
                            fontSize = 14.sp,
                            color = Color(0xFFF1F5F9),
                            lineHeight = 22.sp
                        )
                    }
                }
                line.startsWith("[x] ") -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleChecklist(index, false) }
                            .padding(vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckBox,
                            contentDescription = "Checked",
                            tint = themePurple,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = parseRichTextToAnnotatedString(line.substring(4)),
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.5f),
                            lineHeight = 22.sp,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
                line.startsWith("• ") || line.startsWith("- ") -> {
                    val cleanLine = line.substring(2)
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = "•",
                            fontSize = 14.sp,
                            color = themePurple,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                        Text(
                            text = parseRichTextToAnnotatedString(cleanLine),
                            fontSize = 14.sp,
                            color = Color(0xFFF1F5F9),
                            lineHeight = 22.sp
                        )
                    }
                }
                line.matches(Regex("""^\d+\.\s.*""")) -> {
                    val dotIndex = line.indexOf(".")
                    val numberStr = line.substring(0, dotIndex + 1)
                    val cleanLine = line.substring(dotIndex + 2)
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = numberStr,
                            fontSize = 14.sp,
                            color = themePurple,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = parseRichTextToAnnotatedString(cleanLine),
                            fontSize = 14.sp,
                            color = Color(0xFFF1F5F9),
                            lineHeight = 22.sp
                        )
                    }
                }
                else -> {
                    Text(
                        text = parseRichTextToAnnotatedString(line),
                        fontSize = 14.sp,
                        color = Color(0xFFF1F5F9),
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun StorageScreenSection(
    viewModel: CalculatorViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBack: () -> Unit,
    onNavigateToRecentlyDeleted: () -> Unit
) {
    val storageInfo by viewModel.storageInfo.collectAsStateWithLifecycle()
    val themePurple = ThemePurple
    val textMedium = TextMedium
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D1A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Total Vault Storage Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2031)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF383F56).copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Vault Storage", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val MAX_STORAGE = 15L * 1024 * 1024 * 1024 // 15 GB
                    val progress = if (MAX_STORAGE > 0) (storageInfo.totalBytes.toFloat() / MAX_STORAGE.toFloat()).coerceIn(0f, 1f) else 0f
                    // Storage Bar
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = themePurple,
                        trackColor = Color(0xFF383F56).copy(alpha = 0.5f),
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${storageInfo.totalUsedFormatted} Used", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("15.0 GB Total", color = textMedium, fontSize = 14.sp)
                    }
                }
            }

            // Categories
            SettingsGroup(title = "CATEGORIES") {
                StorageCategoryRow(title = "Photos", icon = Icons.Default.Image, size = storageInfo.photosFormatted, color = Color(0xFF42A5F5))
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                StorageCategoryRow(title = "Videos", icon = Icons.Default.PlayArrow, size = storageInfo.videosFormatted, color = Color(0xFFEF5350))
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                StorageCategoryRow(title = "Documents", icon = Icons.Default.Description, size = storageInfo.docsFormatted, color = Color(0xFFFFCA28))
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                StorageCategoryRow(title = "Audio", icon = Icons.Default.AudioFile, size = storageInfo.audioFormatted, color = Color(0xFFAB47BC))
                Spacer(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(Color(0xFF383F56).copy(alpha = 0.3f)))
                StorageCategoryRow(title = "Notes", icon = Icons.Default.Edit, size = storageInfo.notesFormatted, color = Color(0xFF66BB6A))
            }
            
            // Recently Deleted
            SettingsGroup(title = "TRASH") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onNavigateToRecentlyDeleted() }
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE53935).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Recently Deleted", tint = Color(0xFFE53935), modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Recently Deleted",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = storageInfo.trashFormatted,
                            color = textMedium,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Go",
                            tint = Color.White.copy(alpha = 0.3f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StorageCategoryRow(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, size: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Text(
            text = size,
            color = TextMedium,
            fontSize = 14.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadsScreen(
    viewModel: CalculatorViewModel,
    onBack: () -> Unit,
    context: android.content.Context
) {
    val downloads by viewModel.downloads.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Current", "Completed", "Failed")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0C16))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Column(modifier = Modifier.padding(start = 8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "SECURE DOWNLOADS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ThemePurple.copy(alpha = 0.8f),
                    letterSpacing = 1.8.sp
                )
                Text(
                    text = "Downloads",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Tabs
        androidx.compose.material3.TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = Color.White,
            indicator = { tabPositions ->
                androidx.compose.material3.TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = ThemeLightPurple
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                androidx.compose.material3.Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        // List
        val filteredDownloads = downloads.filter { task ->
            when (selectedTab) {
                0 -> task.status == "Downloading"
                1 -> task.status == "Completed"
                else -> task.status == "Failed"
            }
        }

        if (filteredDownloads.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No ${tabs[selectedTab].lowercase()} downloads", color = Color.White.copy(alpha = 0.5f))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredDownloads, key = { it.id }) { task ->
                    DownloadItem(
                        task = task,
                        onOpen = { viewModel.openDownload(context, task) },
                        onDelete = { viewModel.deleteDownload(task) },
                        onRetry = { viewModel.retryDownload(context, task) }
                    )
                }
            }
        }
    }
}

@Composable
fun DownloadItem(
    task: DownloadTask,
    onOpen: () -> Unit,
    onDelete: () -> Unit,
    onRetry: () -> Unit
) {
    androidx.compose.material3.Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF161B29),
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .clickable { if (task.status == "Completed") onOpen() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(Color(0xFF2B324B), Color(0xFF1B2236))
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        task.status == "Downloading" -> Icons.Default.CloudDownload
                        task.mimeType.startsWith("image/") -> Icons.Default.Image
                        task.mimeType.startsWith("video/") -> Icons.Default.VideoFile
                        task.mimeType.contains("pdf") -> Icons.Default.PictureAsPdf
                        else -> Icons.Default.InsertDriveFile
                    },
                    contentDescription = null,
                    tint = Color(0xFF90CAF9),
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.filename,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (task.status == "Downloading") {
                            "${(task.progress * 100).toInt()}% of ${task.sizeString}"
                        } else {
                            task.sizeString
                        },
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                color = when (task.status) {
                                    "Downloading" -> Color(0xFF2962FF).copy(alpha = 0.2f)
                                    "Completed" -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                                    else -> Color(0xFFF44336).copy(alpha = 0.2f)
                                },
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = task.status,
                            color = when (task.status) {
                                "Downloading" -> Color(0xFF90CAF9)
                                "Completed" -> Color(0xFFA5D6A7)
                                else -> Color(0xFFEF9A9A)
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                if (task.status == "Downloading") {
                    Spacer(modifier = Modifier.height(10.dp))
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { task.progress },
                        modifier = Modifier.fillMaxWidth().height(4.dp),
                        color = Color(0xFF90CAF9),
                        trackColor = Color.White.copy(alpha = 0.05f),
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Row(horizontalArrangement = Arrangement.End) {
                if (task.status == "Failed") {
                    IconButton(onClick = onRetry) {
                        Icon(Icons.Default.Refresh, "Retry", tint = Color.White.copy(alpha = 0.7f))
                    }
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Delete", tint = Color.White.copy(alpha = 0.4f))
                }
            }
        }
    }
}

@Composable
fun MonitoringSection(
    viewModel: CalculatorViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val intruderDetectionEnabled by viewModel.intruderDetectionEnabled.collectAsStateWithLifecycle()
    val intruderSelfieEnabled by viewModel.intruderSelfieEnabled.collectAsStateWithLifecycle()
    val failedAttemptsThreshold by viewModel.failedAttemptsThreshold.collectAsStateWithLifecycle()
    val intruderAttempts by viewModel.intruderAttempts.collectAsStateWithLifecycle()

    var selectedAttemptForDetail by remember { mutableStateOf<String?>(null) }
    var tempDetectionEnabled by remember(intruderDetectionEnabled) { mutableStateOf(intruderDetectionEnabled) }
    var tempSelfieEnabled by remember(intruderSelfieEnabled) { mutableStateOf(intruderSelfieEnabled) }
    var tempThreshold by remember(failedAttemptsThreshold) { mutableStateOf(failedAttemptsThreshold) }

    var hasUnsavedChanges by remember { mutableStateOf(false) }

    // Track if user changed any values
    LaunchedEffect(tempDetectionEnabled, tempSelfieEnabled, tempThreshold) {
        hasUnsavedChanges = (tempDetectionEnabled != intruderDetectionEnabled ||
                tempSelfieEnabled != intruderSelfieEnabled ||
                tempThreshold != failedAttemptsThreshold)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Hero Card (Monitoring Architecture)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF151929)),
            border = BorderStroke(1.dp, Color(0xFF232B44))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFE65100).copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Monitoring Active",
                            tint = Color(0xFFFF9100),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Security Monitoring",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Intruder Alert System",
                            fontSize = 11.sp,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Text(
                    text = "This system tracks unauthorized access attempts. If enabled, incorrect passcode entries are recorded. The front camera can also capture a snapshot of the intruder.",
                    fontSize = 12.sp,
                    color = TextMedium,
                    lineHeight = 16.sp
                )
            }
        }

        // 2. Configuration Settings (Configure -> Save)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF101422)),
            border = BorderStroke(1.dp, Color(0xFF1D2338))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "CONFIGURE MONITORING",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF635BFF),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Switch Row 1: Access Logs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Logs",
                            tint = Color(0xFFFF9100),
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text("Enable Access Logs", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            Text("Record date, time and entered key of failed attempts", fontSize = 10.sp, color = TextMedium)
                        }
                    }
                    Switch(
                        checked = tempDetectionEnabled,
                        onCheckedChange = {
                            viewModel.triggerKeypressEffects(context)
                            tempDetectionEnabled = it
                            if (!it) {
                                tempSelfieEnabled = false
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF635BFF)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                androidx.compose.material3.HorizontalDivider(color = Color(0xFF1D2338))
                Spacer(modifier = Modifier.height(14.dp))

                // Switch Row 2: Intruder Selfie
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Selfie",
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text("Capture Intruder Selfie", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = if (tempDetectionEnabled) Color.White else Color.Gray)
                            Text("Take a photo of the intruder using front camera", fontSize = 10.sp, color = TextMedium)
                        }
                    }
                    Switch(
                        checked = tempSelfieEnabled,
                        enabled = tempDetectionEnabled,
                        onCheckedChange = {
                            viewModel.triggerKeypressEffects(context)
                            tempSelfieEnabled = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF635BFF)
                        )
                    )
                }

                if (tempSelfieEnabled) {
                    Spacer(modifier = Modifier.height(14.dp))
                    androidx.compose.material3.HorizontalDivider(color = Color(0xFF1D2338))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Selector Row 3: Configurable threshold
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Threshold",
                                tint = Color(0xFFEF5350),
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text("Attempts Before Selfie", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                                Text("Failed attempts required before taking picture", fontSize = 10.sp, color = TextMedium)
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(1, 2, 3, 5).forEach { limit ->
                                val isSelected = tempThreshold == limit
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) Color(0xFF635BFF) else Color(0xFF161B2B))
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) Color.Transparent else Color(0xFF232B44),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            viewModel.triggerKeypressEffects(context)
                                            tempThreshold = limit
                                        }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (limit == 1) "1 Fail" else "$limit Fails",
                                        color = if (isSelected) Color.White else TextMedium,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Save Configuration Button
                Button(
                    onClick = {
                        viewModel.triggerKeypressEffects(context)
                        viewModel.setIntruderDetectionEnabled(tempDetectionEnabled)
                        viewModel.setIntruderSelfieEnabled(tempSelfieEnabled)
                        viewModel.setFailedAttemptsThreshold(tempThreshold)
                        android.widget.Toast.makeText(context, "Configuration Saved Successfully!", android.widget.Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hasUnsavedChanges) Color(0xFF635BFF) else Color(0xFF1C223A),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Save Configuration",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 3. Activity Log (Intruder Log)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "INTRUDER ACTIVITY LOGS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B92A5),
                    letterSpacing = 1.sp
                )
                if (intruderAttempts.isNotEmpty()) {
                    TextButton(
                        onClick = {
                            viewModel.triggerKeypressEffects(context)
                            viewModel.clearIntruderAttempts()
                            android.widget.Toast.makeText(context, "All logs deleted!", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear All Logs",
                            tint = Color(0xFFEF5350),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Clear All Logs", color = Color(0xFFEF5350), fontSize = 11.sp)
                    }
                }
            }

            if (intruderAttempts.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1322)),
                    border = BorderStroke(1.dp, Color(0xFF1D2338))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Safe",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "No Break-In Attempts",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Your vault is completely secure. Failed login attempts will record here.",
                            fontSize = 11.sp,
                            color = TextMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    intruderAttempts.forEach { attemptStr ->
                        val parts = attemptStr.split("|||")
                        if (parts.size >= 2) {
                            val timestamp = parts[0]
                            val enteredPin = parts[1]
                            val photoPath = if (parts.size >= 3) parts[2] else ""
                            val failedCount = if (parts.size >= 4) parts[3] else "1"

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.triggerKeypressEffects(context)
                                        selectedAttemptForDetail = attemptStr
                                    },
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF101422)),
                                border = BorderStroke(1.dp, Color(0xFF1F253B).copy(alpha = 0.8f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Circular badge showing failed count
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .background(Color(0xFFEF5350).copy(alpha = 0.15f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = failedCount,
                                                color = Color(0xFFEF5350),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = "Failed Unlock Attempt",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = Color.White
                                            )
                                            Text(
                                                text = timestamp,
                                                fontSize = 10.sp,
                                                color = TextMedium
                                            )
                                        }
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        if (photoPath.isNotEmpty()) {
                                            Icon(
                                                imageVector = Icons.Default.CameraAlt,
                                                contentDescription = "Selfie captured",
                                                tint = Color(0xFF00E5FF),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.Default.ChevronRight,
                                            contentDescription = "Details",
                                            tint = TextMedium,
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

    // Detail Dialog
    selectedAttemptForDetail?.let { attemptStr ->
        val parts = attemptStr.split("|||")
        if (parts.size >= 2) {
            val timestamp = parts[0]
            val enteredPin = parts[1]
            val photoPath = if (parts.size >= 3) parts[2] else ""
            val failedCount = if (parts.size >= 4) parts[3] else "1"

            AlertDialog(
                onDismissRequest = { selectedAttemptForDetail = null },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Alert",
                            tint = Color(0xFFEF5350),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Access Log Details",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (photoPath.isNotEmpty()) {
                            val selfieFile = java.io.File(photoPath)
                            if (selfieFile.exists()) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    border = BorderStroke(1.dp, Color(0xFF232B44))
                                ) {
                                    coil.compose.AsyncImage(
                                        model = selfieFile,
                                        contentDescription = "Intruder Selfie",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp)
                                        .background(Color(0xFF161B2B), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Selfie file not found", color = TextMedium, fontSize = 12.sp)
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .background(Color(0xFF161B2B), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "No Selfie",
                                        tint = TextMedium.copy(alpha = 0.5f)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("No Selfie Captured (Disabled)", color = TextMedium, fontSize = 11.sp)
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Timestamp", color = TextMedium, fontSize = 12.sp)
                            Text(timestamp, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Entered Passcode", color = TextMedium, fontSize = 12.sp)
                            Text(enteredPin, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Attempt Number", color = Color(0xFFEF5350), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("#$failedCount", color = Color(0xFFEF5350), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { selectedAttemptForDetail = null },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF635BFF))
                    ) {
                        Text("Close", color = Color.White)
                    }
                },
                containerColor = Color(0xFF0F1322)
            )
        }
    }
}

@Composable
fun AppDisguiseIconPreview(id: String, modifier: Modifier = Modifier) {
    val roundedShape = RoundedCornerShape(12.dp)
    if (id == "LauncherCalculator") {
        Box(
            modifier = modifier
                .clip(roundedShape)
        ) {
            coil.compose.AsyncImage(
                model = R.drawable.ic_launcher_background,
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            coil.compose.AsyncImage(
                model = R.drawable.ic_launcher_foreground,
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        val gradient = when (id) {
            "LauncherCalcClassic" -> Brush.verticalGradient(listOf(Color(0xFF78909C), Color(0xFF37474F)))
            "LauncherCalcRetro" -> Brush.verticalGradient(listOf(Color(0xFFA1887F), Color(0xFF4E342E)))
            "LauncherCalcNeon" -> Brush.verticalGradient(listOf(Color(0xFF00E5FF), Color(0xFF006064)))
            "LauncherNotes" -> Brush.verticalGradient(listOf(Color(0xFFFFD54F), Color(0xFFFF8F00)))
            "LauncherCompass" -> Brush.verticalGradient(listOf(Color(0xFF4DB6AC), Color(0xFF004D40)))
            "LauncherVoice" -> Brush.verticalGradient(listOf(Color(0xFFE57373), Color(0xFFC62828)))
            "LauncherSudoku" -> Brush.verticalGradient(listOf(Color(0xFF7986CB), Color(0xFF283593)))
            "LauncherWeather" -> Brush.verticalGradient(listOf(Color(0xFF4FC3F7), Color(0xFF0277BD)))
            else -> Brush.verticalGradient(listOf(Color(0xFF9575CD), Color(0xFF4527A0)))
        }
        
        val icon = when (id) {
            "LauncherNotes" -> Icons.Default.Article
            "LauncherCompass" -> Icons.Default.ScreenRotation
            "LauncherVoice" -> Icons.Default.MusicNote
            "LauncherSudoku" -> Icons.Default.GridView
            "LauncherWeather" -> Icons.Default.CloudQueue
            else -> Icons.Default.Calculate
        }

        Box(
            modifier = modifier
                .clip(roundedShape)
                .background(gradient)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.fillMaxSize(0.75f)
            )
            
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "SOON",
                    color = Color(0xFFFFCC00),
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SecurityStatusSection(
    overallSecurityRating: String,
    securityItems: List<com.example.SecurityItemState>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section header & Overall Rating Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "SECURITY STATUS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.4f),
                    letterSpacing = 1.8.sp
                )
                Text(
                    text = "Overall Rating",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }
            // Overall Status Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF10B981).copy(alpha = 0.08f))
                    .border(1.dp, Color(0xFF10B981).copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFF10B981))
                )
                Text(
                    text = overallSecurityRating, // "Excellent"
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF10B981)
                )
            }
        }

        // Individual security items card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFF111422).copy(alpha = 0.6f)) // Luxury deep obsidian glass
                .border(
                    width = 1.dp,
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.05f),
                            Color.White.copy(alpha = 0.01f)
                        )
                    ),
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            securityItems.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon & Title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Icon mapping based on item id
                        val icon = when (item.id) {
                            "encryption" -> Icons.Default.Lock
                            "privacy" -> Icons.Default.Shield
                            "cloud" -> Icons.Default.CloudQueue
                            "backup" -> Icons.Default.Restore
                            else -> Icons.Default.Security
                        }

                        // Icon container
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.02f))
                                .border(0.5.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Text(
                            text = item.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    // Status indicator & label
                    val indicatorColor = when (item.severity) {
                        "Safe" -> Color(0xFF10B981)      // Green
                        "Attention" -> Color(0xFFFBBF24) // Yellow
                        "Warning" -> Color(0xFFEF4444)   // Red
                        else -> Color.White
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(indicatorColor)
                        )
                        Text(
                            text = item.status,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Divider between rows
                if (index < securityItems.size - 1) {
                    androidx.compose.material3.Divider(
                        color = Color.White.copy(alpha = 0.04f),
                        thickness = 1.dp
                    )
                }
            }
        }

        // Informational disclaimer line below the card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.25f),
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Your Vault is currently protected using local-only security.",
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.4f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun YourJourneySection(
    timelineItems: List<com.example.JourneyTimelineItem>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp), // premium spacing from section above
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "YOUR JOURNEY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.4f),
                letterSpacing = 1.8.sp
            )
            Text(
                text = "Your Journey",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "Every memory you protect becomes part of your story.",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.5f),
                letterSpacing = 0.1.sp
            )
        }

        // Timeline container card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFF111422).copy(alpha = 0.6f)) // Luxury deep obsidian glass matching security card
                .border(
                    width = 1.dp,
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.05f),
                            Color.White.copy(alpha = 0.01f)
                        )
                    ),
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            if (timelineItems.isEmpty()) {
                // Should not happen as "Vault Created" is always present, but safe fallback
                Text(
                    text = "Your story is beginning...",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                )
            } else {
                timelineItems.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Left Column: Dot & Connecting Line
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxHeight().width(40.dp)
                        ) {
                            // Icon bubble
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                    .background(Color.White.copy(alpha = 0.03f))
                                    .border(0.5.dp, Color.White.copy(alpha = 0.08f), androidx.compose.foundation.shape.CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                val vectorIcon = when (item.id) {
                                    "vault_created" -> Icons.Default.Lock
                                    "first_secret" -> Icons.Default.Folder
                                    "first_note" -> Icons.Default.Edit
                                    "first_photo" -> Icons.Default.CameraAlt
                                    "first_video" -> Icons.Default.PlayCircle
                                    "first_doc" -> Icons.Default.Description
                                    "first_browser" -> Icons.Default.Language
                                    "first_voice_note" -> Icons.Default.Mic
                                    "first_qr_scan" -> Icons.Default.QrCodeScanner
                                    "first_metadata_cleaned" -> Icons.Default.CleaningServices
                                    "first_password_generated" -> Icons.Default.VpnKey
                                    "security_improved" -> Icons.Default.Shield
                                    "premium_activated" -> Icons.Default.WorkspacePremium
                                    "lifetime_activated" -> Icons.Default.WorkspacePremium
                                    else -> Icons.Default.Info
                                }
                                Icon(
                                    imageVector = vectorIcon,
                                    contentDescription = item.title,
                                    tint = ThemePurple,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Line connecting to next item
                            if (index < timelineItems.size - 1) {
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .weight(1f)
                                        .background(
                                            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.White.copy(alpha = 0.12f),
                                                    Color.White.copy(alpha = 0.02f)
                                                )
                                            )
                                        )
                                )
                            }
                        }

                        // Right Column: Title, Date & Description
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = if (index < timelineItems.size - 1) 24.dp else 4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = item.date,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFA5B4FC).copy(alpha = 0.8f),
                                    letterSpacing = 0.5.sp,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                            Text(
                                text = item.description,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White.copy(alpha = 0.55f),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        // Elegant quote at the bottom
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 32.dp, height = 1.dp)
                    .background(Color.White.copy(alpha = 0.15f))
            )
            Text(
                text = "“Every memory protected becomes part of your story.”",
                fontSize = 12.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}

@Composable
fun SecretVaultUnlockingAnimation(
    onAnimationComplete: () -> Unit
) {
    val view = androidx.compose.ui.platform.LocalView.current
    val progress = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(Unit) {
        try {
            view.performHapticFeedback(android.view.HapticFeedbackConstants.CONFIRM)
        } catch (e: Exception) { }

        progress.animateTo(
            targetValue = 1f,
            animationSpec = androidx.compose.animation.core.tween(
                durationMillis = 3200,
                easing = androidx.compose.animation.core.FastOutSlowInEasing
            )
        )
        onAnimationComplete()
    }

    val p = progress.value
    val percent = (p * 100).toInt().coerceIn(0, 100)
    val themeColors = LocalAppThemeColors.current
    val themePurple = themeColors.themePurple

    // Combination Dial physics movement formulas
    val dialAngle = when {
        p < 0.35f -> {
            val frac = p / 0.35f
            frac * 240f // CW turn to 240
        }
        p < 0.70f -> {
            val frac = (p - 0.35f) / 0.35f
            240f - (frac * 360f) // CCW turn to -120
        }
        p < 0.90f -> {
            val frac = (p - 0.70f) / 0.20f
            -120f + (frac * 200f) // CW turn to 80
        }
        else -> {
            80f
        }
    }

    // Interactive tactile haptic vibration feedback for every tick crossed
    val currentTick = (dialAngle / 6.0f).toInt()
    LaunchedEffect(currentTick) {
        if (p > 0.01f && p < 0.90f) {
            try {
                view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
            } catch (e: Exception) { }
        }
    }

    // Haptic trigger for bolt release
    LaunchedEffect(p >= 0.90f) {
        if (p >= 0.90f) {
            try {
                view.performHapticFeedback(android.view.HapticFeedbackConstants.CONFIRM)
            } catch (e: Exception) { }
        }
    }

    // Split/slide open animation variables
    val slideFraction = if (p > 0.90f) (p - 0.90f) / 0.10f else 0f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D)), // Luxe ultra-dark backdrop
        contentAlignment = Alignment.Center
    ) {
        // Golden/Warm inner glow revealed when safe doors slide open
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    androidx.compose.ui.graphics.Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF).copy(alpha = 0.15f),
                            Color(0xFF1F1F1F).copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Left Vault Door
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterStart)
                .graphicsLayer {
                    translationX = -size.width * slideFraction
                    alpha = 1f - (slideFraction * 0.7f)
                }
                .background(
                    androidx.compose.ui.graphics.Brush.horizontalGradient(
                        colors = listOf(Color(0xFF1E1E1E), Color(0xFF2E2E2E))
                    )
                )
        ) {
            // Mechanical Bolt Left
            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 30.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = (10).dp)
                    .graphicsLayer {
                        // Retracts from 10dp to inside as we progress
                        val retract = if (p < 0.25f) p / 0.25f else 1f
                        translationX = -retract * 30f
                    }
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color(0xFFE2E8F0), Color(0xFF94A3B8), Color(0xFF475569))
                        ),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                    )
            )
        }

        // Right Vault Door
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterEnd)
                .graphicsLayer {
                    translationX = size.width * slideFraction
                    alpha = 1f - (slideFraction * 0.7f)
                }
                .background(
                    androidx.compose.ui.graphics.Brush.horizontalGradient(
                        colors = listOf(Color(0xFF2E2E2E), Color(0xFF1E1E1E))
                    )
                )
        ) {
            // Mechanical Bolt Right
            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 30.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = (-10).dp)
                    .graphicsLayer {
                        val retract = if (p < 0.50f) p / 0.50f else 1f
                        translationX = retract * 30f
                    }
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color(0xFFE2E8F0), Color(0xFF94A3B8), Color(0xFF475569))
                        ),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                    )
            )
        }

        // Center seam line dividing the safe door panels
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp)
                .align(Alignment.Center)
                .graphicsLayer {
                    alpha = if (slideFraction > 0f) 0f else 0.4f
                }
                .background(Color.Black)
        )

        // Main Mechanical safe Dial & Info Group (Anchored to sliding layout by scale/fade)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.graphicsLayer {
                scaleX = 1f - (slideFraction * 0.15f)
                scaleY = scaleX
                alpha = 1f - slideFraction
            }
        ) {
            // Indication Needle pointing at the safe dial top
            Canvas(
                modifier = Modifier
                    .size(width = 16.dp, height = 16.dp)
                    .padding(bottom = 4.dp)
            ) {
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(size.width / 2f, size.height)
                    lineTo(0f, 0f)
                    lineTo(size.width, 0f)
                    close()
                }
                drawPath(path = path, color = Color(0xFFE2E8F0))
            }

            Spacer(modifier = Modifier.height(4.dp))

            // The Combination Lock Dial wheel with glowing ambient background
            Box(
                modifier = Modifier.size(240.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer Ambient Purple/Cyber Glow
                Box(
                    modifier = Modifier
                        .size(230.dp)
                        .background(
                            androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(
                                    themePurple.copy(alpha = 0.28f),
                                    themePurple.copy(alpha = 0.05f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // The Combination Lock Dial wheel itself
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .graphicsLayer {
                            rotationZ = dialAngle
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val r = size.width / 2f
                        val centerPt = Offset(r, r)

                        // 1. Outer Chrome Metallic Rim with sweep shading for 3D steel look
                        val silverMetallic = androidx.compose.ui.graphics.Brush.sweepGradient(
                            colors = listOf(
                                Color(0xFFE2E8F0),
                                Color(0xFF64748B),
                                Color(0xFFF1F5F9),
                                Color(0xFF334155),
                                Color(0xFFCBD5E1),
                                Color(0xFF94A3B8),
                                Color(0xFFE2E8F0)
                            ),
                            center = centerPt
                        )
                        drawCircle(
                            brush = silverMetallic,
                            radius = r,
                            style = androidx.compose.ui.graphics.drawscope.Fill
                        )

                        // 2. Beveled shadow ring inside outer rim
                        drawCircle(
                            color = Color(0xFF0F172A),
                            radius = r - 4.dp.toPx(),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                        )

                        // 3. Dark titanium/carbon inner disc
                        val titaniumBg = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(Color(0xFF334155), Color(0xFF1E293B), Color(0xFF0B0F19)),
                            center = centerPt
                        )
                        drawCircle(
                            brush = titaniumBg,
                            radius = r - 6.dp.toPx(),
                            style = androidx.compose.ui.graphics.drawscope.Fill
                        )

                        // 4. Fine glowing theme circle
                        drawCircle(
                            color = themePurple.copy(alpha = 0.4f),
                            radius = r - 16.dp.toPx(),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
                        )

                        // 5. Dial Notch Marks (Ticks) every 6 degrees (60 divisions total)
                        for (i in 0 until 60) {
                            val angleRad = Math.toRadians((i * 6).toDouble())
                            val cosVal = Math.cos(angleRad).toFloat()
                            val sinVal = Math.sin(angleRad).toFloat()

                            val isMajor = i % 5 == 0
                            val tickLen = if (isMajor) 12.dp.toPx() else 6.dp.toPx()
                            val tickStroke = if (isMajor) 2.dp.toPx() else 1.dp.toPx()
                            val tickColor = if (isMajor) Color(0xFFF1F5F9) else Color(0xFF94A3B8).copy(alpha = 0.6f)

                            val startPt = Offset(
                                centerPt.x + (r - 18.dp.toPx()) * cosVal,
                                centerPt.y + (r - 18.dp.toPx()) * sinVal
                            )
                            val endPt = Offset(
                                centerPt.x + (r - 18.dp.toPx() - tickLen) * cosVal,
                                centerPt.y + (r - 18.dp.toPx() - tickLen) * sinVal
                            )

                            drawLine(
                                color = tickColor,
                                start = startPt,
                                end = endPt,
                                strokeWidth = tickStroke
                            )
                        }
                    }

                    // Inner chrome handle bar/wheel
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(
                                androidx.compose.ui.graphics.Brush.radialGradient(
                                    colors = listOf(Color(0xFFE2E8F0), Color(0xFF64748B), Color(0xFF1E293B))
                                ),
                                shape = androidx.compose.foundation.shape.CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Glowing center neon ring
                        Box(
                            modifier = Modifier
                                .size(92.dp)
                                .background(Color.Transparent, shape = CircleShape)
                                .border(1.5.dp, themePurple.copy(alpha = 0.5f), CircleShape)
                        )

                        // Brushed steel handle spoke overlay
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .height(16.dp)
                                .background(
                                    androidx.compose.ui.graphics.Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFFFFFFFF),
                                            Color(0xFFE2E8F0),
                                            Color(0xFF94A3B8),
                                            Color(0xFF475569)
                                        )
                                    ),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(6.dp)
                                )
                                .border(1.dp, themePurple.copy(alpha = 0.4f), androidx.compose.foundation.shape.RoundedCornerShape(6.dp))
                        ) {
                            // Tiny glowing center bolt/rivet
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .align(Alignment.Center)
                                    .background(Color.White, shape = CircleShape)
                                    .border(1.dp, themePurple, CircleShape)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Uncomplicate the status message with warm & friendly security greetings
            val statusMsg = when {
                percent < 30 -> "HELLO! PREPARING VAULT..."
                percent < 60 -> "UNLOCKING SECURE SPACE..."
                percent < 90 -> "VERIFYING IDENTITY..."
                else -> "WELCOME HOME"
            }

            Text(
                text = statusMsg,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Premium matching progress bar
            Row(
                modifier = Modifier
                    .width(180.dp)
                    .height(4.dp)
                    .background(Color.White.copy(alpha = 0.12f), shape = RoundedCornerShape(2.dp)),
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(p)
                        .background(
                            androidx.compose.ui.graphics.Brush.horizontalGradient(
                                colors = listOf(themePurple, Color.White)
                            ),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "SECRET VAULT",
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 10.sp,
                    color = Color.White.copy(alpha = 0.95f),
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = themePurple.copy(alpha = 0.8f),
                        blurRadius = 12f
                    )
                )
            )
        }
    }
}



