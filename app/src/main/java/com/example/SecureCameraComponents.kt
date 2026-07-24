package com.example

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.AnnotatedString


// Theme Constant matches Calculator Screen
private val ThemePurple = Color(0xFF635BFF)
private val ThemeContainerBorder = Color(0xFF1E2438)
private val BrandBg = Color(0xFF090D1A)
private val TextMedium = Color(0xFF8B92A5)

data class LastMediaInfo(
    val id: String,
    val path: String,
    val isVideo: Boolean,
    val name: String,
    val size: String,
    val raw: String
)

@Composable
fun SecureCameraView(
    viewModel: CalculatorViewModel,
    onDismiss: () -> Unit,
    onViewMedia: ((String, Int, List<String>) -> Unit)? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    var isFrontCamera by remember { mutableStateOf(false) }
    var isVideoMode by remember { mutableStateOf(false) }
    var flashMode by remember { mutableStateOf("AUTO") } // ON, OFF, AUTO
    var isRecording by remember { mutableStateOf(false) }
    var activeRecording by remember { mutableStateOf<Recording?>(null) }
    var showPermissionError by remember { mutableStateOf(false) }

    val vaultFiles by viewModel.vaultFiles.collectAsStateWithLifecycle()
    
    val mediaFiles = remember(vaultFiles) {
        vaultFiles.filter {
            it.contains("|||image/") || it.contains("|||video/")
        }
    }

    val lastCapturedFile = remember(vaultFiles) {
        vaultFiles.firstOrNull {
            it.contains("|||image/") || it.contains("|||video/")
        }?.let { fileStr ->
            val parts = fileStr.split("|||")
            if (parts.size >= 6) {
                val id = parts[0]
                val mimeType = parts[3]
                val path = parts[4]
                val isVideo = mimeType.startsWith("video/")
                val name = parts[2]
                val size = parts[5]
                LastMediaInfo(id = id, path = path, isVideo = isVideo, name = name, size = size, raw = fileStr)
            } else null
        }
    }

    // Use cases references
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var videoCapture: VideoCapture<Recorder>? by remember { mutableStateOf(null) }
    var camera: androidx.camera.core.Camera? by remember { mutableStateOf(null) }

    val cameraProviderFuture = remember { try { CameraInitializer.initAndGetProvider(context) } catch (e: Exception) { null } }

    var previewViewRef by remember { mutableStateOf<PreviewView?>(null) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    DisposableEffect(cameraProviderFuture) {
        onDispose {
            try {
                cameraProviderFuture?.get()?.unbindAll()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(isFrontCamera, isVideoMode, cameraProviderFuture, previewViewRef, hasCameraPermission) {
        if (!hasCameraPermission) return@LaunchedEffect
        val previewView = previewViewRef ?: return@LaunchedEffect
        val cameraProvider = withContext(kotlinx.coroutines.Dispatchers.IO) {
            try {
                cameraProviderFuture?.get()
            } catch (e: Exception) {
                null
            }
        } ?: return@LaunchedEffect

        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }

        val imgCap = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
        imageCapture = imgCap

        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HD))
            .build()
        val vidCap = VideoCapture.withOutput(recorder)
        videoCapture = vidCap

        val cameraSelector = if (isFrontCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }

        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                if (isVideoMode) vidCap else imgCap
            )
            
            // Set initial flash and torch state
            imgCap.flashMode = when (flashMode) {
                "ON" -> ImageCapture.FLASH_MODE_ON
                "OFF" -> ImageCapture.FLASH_MODE_OFF
                else -> ImageCapture.FLASH_MODE_AUTO
            }
            camera?.cameraControl?.enableTorch(isVideoMode && flashMode == "ON")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LaunchedEffect(flashMode, isVideoMode, imageCapture, camera) {
        val imgCap = imageCapture
        if (imgCap != null) {
            imgCap.flashMode = when (flashMode) {
                "ON" -> ImageCapture.FLASH_MODE_ON
                "OFF" -> ImageCapture.FLASH_MODE_OFF
                else -> ImageCapture.FLASH_MODE_AUTO
            }
        }
        camera?.cameraControl?.enableTorch(isVideoMode && flashMode == "ON")
    }

    // Handle permissions natively
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        viewModel.isPickingFile = false
        val cameraGranted = perms[Manifest.permission.CAMERA] ?: false
        hasCameraPermission = cameraGranted
        if (!cameraGranted) {
            showPermissionError = true
        }
    }

    LaunchedEffect(Unit) {
        val cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        val audioPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
        
        if (cameraPermission != PackageManager.PERMISSION_GRANTED || audioPermission != PackageManager.PERMISSION_GRANTED) {
            viewModel.isPickingFile = true
            permissionsLauncher.launch(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
            )
        }
    }

    if (showPermissionError) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Permissions Required", color = Color.White) },
            text = { Text("Built-in secure camera requires Camera and Audio permissions to record photos and videos directly into the private sandbox.", color = TextMedium) },
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionError = false
                        permissionsLauncher.launch(
                            arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple)
                ) {
                    Text("Grant", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = TextMedium)
                }
            },
            containerColor = Color(0xFF0F1322)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Real-time Camera Preview View
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    previewViewRef = this
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = {}
        )

        // Top Control Overlay
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Exit Camera", tint = Color.White)
            }

            // Flash Toggle Button
            IconButton(
                onClick = {
                    flashMode = when (flashMode) {
                        "AUTO" -> "ON"
                        "ON" -> "OFF"
                        else -> "AUTO"
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = when (flashMode) {
                        "ON" -> Icons.Default.FlashOn
                        "OFF" -> Icons.Default.FlashOff
                        else -> Icons.Default.FlashAuto
                    },
                    contentDescription = "Flash Mode",
                    tint = if (flashMode == "OFF") Color.LightGray else Color.Yellow
                )
            }
        }

        // Timer badge for active video recording
        if (isRecording) {
            var recordSeconds by remember { mutableStateOf(0) }
            LaunchedEffect(isRecording) {
                recordSeconds = 0
                while (isRecording) {
                    kotlinx.coroutines.delay(1000)
                    recordSeconds++
                }
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 20.dp)
                    .background(Color.Red.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color.White, CircleShape)
                )
                Text(
                    text = String.format("%02d:%02d", recordSeconds / 60, recordSeconds % 60),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Bottom Dashboard
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sliding Mode Selection Tabs (PHOTO vs VIDEO)
            Row(
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(false to "PHOTO", true to "VIDEO").forEach { (mode, label) ->
                    val isSelected = isVideoMode == mode
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) ThemePurple else Color.Transparent)
                            .clickable {
                                if (!isRecording) {
                                    isVideoMode = mode
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else Color.LightGray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Controls Row (Gallery Preview, Shutter Button, Switcher Button)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Gallery Thumbnail (Last captured photo/video)
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                        .clickable {
                            if (lastCapturedFile != null && onViewMedia != null) {
                                val index = mediaFiles.indexOf(lastCapturedFile.raw)
                                if (index != -1) {
                                    onViewMedia(lastCapturedFile.raw, index, mediaFiles)
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (lastCapturedFile != null) {
                        AsyncImage(
                            model = coil.request.ImageRequest.Builder(context)
                                .data(File(lastCapturedFile.path))
                                .crossfade(true)
                                .build(),
                            contentDescription = "Last captured item",
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        if (lastCapturedFile.isVideo) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Video",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    } else {
                        // Default placeholder icon
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = "Gallery Empty",
                            tint = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Center: Capture Shutter trigger button
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(if (isVideoMode) Color.Red else Color.White)
                            .clickable {
                                if (isVideoMode) {
                                    // Toggle Video Recording
                                    if (isRecording) {
                                        activeRecording?.stop()
                                        activeRecording = null
                                        isRecording = false
                                    } else {
                                        val vidCap = videoCapture ?: return@clickable
                                        val id = System.currentTimeMillis().toString()
                                        val vaultDir = File(context.filesDir, "vault_files")
                                        if (!vaultDir.exists()) vaultDir.mkdirs()
                                        val destFile = File(vaultDir, "$id.mp4")

                                        val outputOpts = FileOutputOptions.Builder(destFile).build()
                                        
                                        try {
                                            val recording = vidCap.output
                                                .prepareRecording(context, outputOpts)
                                                .apply {
                                                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                                        withAudioEnabled()
                                                    }
                                                }
                                                .start(ContextCompat.getMainExecutor(context)) { recordEvent ->
                                                    when (recordEvent) {
                                                        is VideoRecordEvent.Start -> {
                                                            isRecording = true
                                                        }
                                                        is VideoRecordEvent.Finalize -> {
                                                            isRecording = false
                                                            if (!recordEvent.hasError()) {
                                                                viewModel.registerDirectVaultFile(
                                                                    context,
                                                                    destFile,
                                                                    "Video_$id.mp4",
                                                                    "video/mp4"
                                                                )
                                                                Toast.makeText(context, "Video secured directly in Vault!", Toast.LENGTH_SHORT).show()
                                                            } else {
                                                                Toast.makeText(context, "Failed to capture video", Toast.LENGTH_SHORT).show()
                                                            }
                                                        }
                                                    }
                                                }
                                            activeRecording = recording
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Video recording error", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    // Capture Photo
                                    val imgCap = imageCapture ?: return@clickable
                                    val id = System.currentTimeMillis().toString()
                                    val vaultDir = File(context.filesDir, "vault_files")
                                    if (!vaultDir.exists()) vaultDir.mkdirs()
                                    val destFile = File(vaultDir, "$id.jpg")

                                    val outputOpts = ImageCapture.OutputFileOptions.Builder(destFile).build()
                                    
                                    imgCap.takePicture(
                                        outputOpts,
                                        ContextCompat.getMainExecutor(context),
                                        object : ImageCapture.OnImageSavedCallback {
                                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                                viewModel.registerDirectVaultFile(
                                                    context,
                                                    destFile,
                                                    "Photo_$id.jpg",
                                                    "image/jpeg"
                                                )
                                                Toast.makeText(context, "Photo secured directly in Vault!", Toast.LENGTH_SHORT).show()
                                            }

                                            override fun onError(exception: ImageCaptureException) {
                                                Toast.makeText(context, "Capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    )
                                }
                            }
                    )
                }

                // Right: Front/Back Switcher Button
                IconButton(
                    onClick = { isFrontCamera = !isFrontCamera },
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.FlipCameraAndroid,
                        contentDescription = "Flip Camera",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SecureScannerView(
    viewModel: CalculatorViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current
    val hapticFeedback = LocalHapticFeedback.current

    var isFrontCamera by remember { mutableStateOf(false) }
    var isFlashOn by remember { mutableStateOf(false) }
    var isScanningPaused by remember { mutableStateOf(false) }
    var showPermissionError by remember { mutableStateOf(false) }

    // Active scanned item (floating card overlay)
    var activeScanItem by remember { mutableStateOf<QrScanItem?>(null) }

    // Show Scan History dialog/slide-up
    var showHistoryScreen by remember { mutableStateOf(false) }

    // Search query for history
    var historySearchQuery by remember { mutableStateOf("") }

    // State for delete confirmation dialog
    var showClearHistoryConfirm by remember { mutableStateOf(false) }

    // Camera variables
    var previewViewRef by remember { mutableStateOf<PreviewView?>(null) }
    var cameraControl: CameraControl? by remember { mutableStateOf(null) }
    val cameraProviderFuture = remember { try { CameraInitializer.initAndGetProvider(context) } catch (e: Exception) { null } }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission launcher
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.isPickingFile = false
        hasCameraPermission = granted
        if (!granted) showPermissionError = true
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            viewModel.isPickingFile = true
            permissionsLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    DisposableEffect(cameraProviderFuture) {
        onDispose {
            try {
                cameraProviderFuture?.get()?.unbindAll()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Bind Camera Preview and Image Analysis
    LaunchedEffect(isFrontCamera, cameraProviderFuture, previewViewRef, hasCameraPermission) {
        if (!hasCameraPermission) return@LaunchedEffect
        val previewView = previewViewRef ?: return@LaunchedEffect
        val cameraProvider = withContext(Dispatchers.IO) {
            try {
                cameraProviderFuture?.get()
            } catch (e: Exception) {
                null
            }
        } ?: return@LaunchedEffect

        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), object : ImageAnalysis.Analyzer {
            @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
            override fun analyze(imageProxy: ImageProxy) {
                val mediaImage = imageProxy.image
                if (mediaImage != null && !isScanningPaused && activeScanItem == null && !showHistoryScreen) {
                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                    val scanner = BarcodeScanning.getClient()
                    scanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            if (barcodes.isNotEmpty()) {
                                val barcode = barcodes.first()
                                val rawValue = barcode.rawValue ?: ""
                                if (rawValue.isNotEmpty()) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    val parsed = parseBarcode(barcode)
                                    
                                    // Save inside vault scan history (automatically supports Decoy or Real Vault inside ViewModel)
                                    val savedItem = viewModel.addQrScanItem(
                                        rawValue = rawValue,
                                        type = parsed.type,
                                        title = parsed.title,
                                        formattedDetails = parsed.details
                                    )
                                    activeScanItem = savedItem
                                    isScanningPaused = true
                                }
                            }
                        }
                        .addOnFailureListener {
                            it.printStackTrace()
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                } else {
                    imageProxy.close()
                }
            }
        })

        val cameraSelector = if (isFrontCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }

        try {
            cameraProvider.unbindAll()
            val camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
            cameraControl = camera.cameraControl
            // Sync torch mode
            cameraControl?.enableTorch(isFlashOn)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Sync Torch state change
    LaunchedEffect(isFlashOn) {
        try {
            cameraControl?.enableTorch(isFlashOn)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Entire layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        previewViewRef = this
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = {}
            )

            // Scanning Overlay Laser Line and Alignment border
            if (!isScanningPaused && activeScanItem == null && !showHistoryScreen) {
                val infiniteTransition = rememberInfiniteTransition(label = "LaserEffect")
                val laserProgress by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2200, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "LaserLine"
                )

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    // Translucent dark mask
                    drawRect(
                        color = Color.Black.copy(alpha = 0.6f),
                        size = size
                    )

                    // Target scanning cutout box (square for QR codes)
                    val boxSize = canvasWidth * 0.7f
                    val x = (canvasWidth - boxSize) / 2
                    val y = (canvasHeight - boxSize) / 2

                    drawRoundRect(
                        color = Color.Transparent,
                        topLeft = Offset(x, y),
                        size = Size(boxSize, boxSize),
                        cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx()),
                        blendMode = androidx.compose.ui.graphics.BlendMode.Clear
                    )

                    // Outer alignment target box
                    drawRoundRect(
                        color = ThemePurple,
                        topLeft = Offset(x, y),
                        size = Size(boxSize, boxSize),
                        cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx()),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
                    )

                    // Green Scanner Laser
                    val laserYPosition = y + (boxSize * laserProgress)
                    drawLine(
                        color = Color(0xFF00FFCC),
                        start = Offset(x + 12.dp.toPx(), laserYPosition),
                        end = Offset(x + boxSize - 12.dp.toPx(), laserYPosition),
                        strokeWidth = 3.dp.toPx()
                    )
                }
            } else {
                // Dim camera view completely when paused or overlay is showing
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.75f))
                )
            }
        } else {
            // No Permission state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = TextMedium,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Camera Permission Required",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Please enable camera permissions to scan QR codes securely.",
                    color = TextMedium,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { permissionsLauncher.launch(Manifest.permission.CAMERA) },
                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple)
                ) {
                    Text("Grant Permission", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onDismiss) {
                    Text("Go Back", color = ThemePurple)
                }
            }
        }

        // Top Glassmorphic Navigation & Control Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Close / Exit
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Exit Scanner", tint = Color.White)
            }

            // Title / Status Label
            Text(
                text = if (isScanningPaused) "SCANNING PAUSED" else "ALIGN QR CODE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .background(
                        if (isScanningPaused) Color.DarkGray.copy(alpha = 0.8f) else ThemePurple.copy(alpha = 0.8f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            )

            // Flash / Camera Switch controls
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Torch Switch
                IconButton(
                    onClick = { isFlashOn = !isFlashOn },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                        contentDescription = "Flash Mode",
                        tint = if (isFlashOn) Color.Yellow else Color.White
                    )
                }

                // Front/Back Switch
                IconButton(
                    onClick = { isFrontCamera = !isFrontCamera },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "Switch Camera",
                        tint = Color.White
                    )
                }
            }
        }

        // Floating Scanner Controls Bottom Row (History & Pause State)
        if (activeScanItem == null && !showHistoryScreen) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 36.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // History Button
                    Button(
                        onClick = { showHistoryScreen = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black.copy(alpha = 0.65f)),
                        border = BorderStroke(1.dp, ThemeContainerBorder.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, tint = ThemePurple, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Private Scan History", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    // Pause/Resume Analyzer Button
                    IconButton(
                        onClick = { isScanningPaused = !isScanningPaused },
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                            .border(1.dp, ThemeContainerBorder.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isScanningPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = "Pause / Resume scanning",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        // Scanned Detail Glassmorphic Card (Floating Overlay)
        AnimatedVisibility(
            visible = activeScanItem != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            activeScanItem?.let { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ThemeContainerBorder.copy(alpha = 0.6f), RoundedCornerShape(24.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1424).copy(alpha = 0.95f)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        // Title header based on type
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(ThemePurple.copy(alpha = 0.2f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when (item.type) {
                                            "WIFI" -> Icons.Default.Wifi
                                            "URL" -> Icons.Default.Link
                                            "CONTACT" -> Icons.Default.Person
                                            "EMAIL" -> Icons.Default.Email
                                            "PHONE" -> Icons.Default.Phone
                                            else -> Icons.Default.Description
                                        },
                                        contentDescription = null,
                                        tint = ThemePurple,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = when (item.type) {
                                            "WIFI" -> "WI-FI NETWORK"
                                            "URL" -> "WEBSITE URL"
                                            "CONTACT" -> "CONTACT INFO"
                                            "EMAIL" -> "EMAIL DETECTED"
                                            "PHONE" -> "PHONE NUMBER"
                                            else -> "PLAIN TEXT"
                                        },
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ThemePurple,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = item.title,
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            // Close Details Overlay
                            IconButton(
                                onClick = {
                                    activeScanItem = null
                                    isScanningPaused = false // resume scanning
                                },
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Box for Formatted Details with clean border
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                                .border(1.dp, ThemeContainerBorder.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Column {
                                Text(
                                    text = item.formattedDetails,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )
                                if (item.type == "WIFI") {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    // Extract password from details if possible to make copying easy
                                    val passwordLine = item.formattedDetails.lines().firstOrNull { it.contains("Password:", ignoreCase = true) }
                                    val passwordVal = passwordLine?.substringAfter("Password:")?.trim() ?: ""
                                    if (passwordVal.isNotEmpty() && passwordVal != "None") {
                                        Button(
                                            onClick = {
                                                clipboardManager.setText(AnnotatedString(passwordVal))
                                                Toast.makeText(context, "Password copied!", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = ThemePurple.copy(alpha = 0.15f)),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.height(32.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                                        ) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = ThemePurple, modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Copy Wi-Fi Password", color = ThemePurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Action row (Copy, Open URL, etc.)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(item.rawValue))
                                    Toast.makeText(context, "Copied raw scanner content!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Copy Content", color = Color.White, fontSize = 13.sp)
                            }

                            val isValidUrl = item.type == "URL" || item.rawValue.startsWith("http://", true) || item.rawValue.startsWith("https://", true)
                            if (isValidUrl) {
                                Button(
                                    onClick = {
                                        try {
                                            val urlToOpen = if (!item.rawValue.startsWith("http://", true) && !item.rawValue.startsWith("https://", true)) {
                                                "https://${item.rawValue}"
                                            } else {
                                                item.rawValue
                                            }
                                            uriHandler.openUri(urlToOpen)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Cannot open URL", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.weight(1.1f)
                                ) {
                                    Icon(Icons.Default.OpenInBrowser, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Open URL", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Full Screen Slide-Up Private Scan History Screen
        AnimatedVisibility(
            visible = showHistoryScreen,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            val historyItems by viewModel.qrScanHistory.collectAsStateWithLifecycle()
            val filteredItems = remember(historyItems, historySearchQuery) {
                if (historySearchQuery.isBlank()) {
                    historyItems
                } else {
                    historyItems.filter {
                        it.title.contains(historySearchQuery, ignoreCase = true) ||
                                it.formattedDetails.contains(historySearchQuery, ignoreCase = true) ||
                                it.rawValue.contains(historySearchQuery, ignoreCase = true)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BrandBg)
                    .statusBarsPadding()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header Area
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { showHistoryScreen = false },
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Private Scan History",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (historyItems.isNotEmpty()) {
                            IconButton(
                                onClick = { showClearHistoryConfirm = true },
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.Red.copy(alpha = 0.1f), CircleShape)
                            ) {
                                Icon(Icons.Default.DeleteSweep, contentDescription = "Clear All", tint = Color(0xFFFF5252))
                            }
                        }
                    }

                    // Interactive Search Bar
                    OutlinedTextField(
                        value = historySearchQuery,
                        onValueChange = { historySearchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 12.dp),
                        placeholder = { Text("Search inside scan history...", color = TextMedium, fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMedium) },
                        trailingIcon = {
                            if (historySearchQuery.isNotEmpty()) {
                                IconButton(onClick = { historySearchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.White)
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ThemePurple,
                            unfocusedBorderColor = ThemeContainerBorder.copy(alpha = 0.5f),
                            focusedContainerColor = Color(0xFF13192B),
                            unfocusedContainerColor = Color(0xFF0C101D),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    // Scrollable Scan History list
                    if (filteredItems.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(32.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(ThemePurple.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CenterFocusWeak,
                                    contentDescription = null,
                                    tint = ThemePurple,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = if (historySearchQuery.isEmpty()) "No QR Scans Saved" else "No matches found",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (historySearchQuery.isEmpty()) "Any QR code you scan will automatically appear in this Private Scan History." else "Try modifying your search filter query.",
                                color = TextMedium,
                                textAlign = TextAlign.Center,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 24.dp)
                        ) {
                            items(
                                items = filteredItems,
                                key = { it.id }
                            ) { item ->
                                val dateStr = remember(item.timestamp) {
                                    SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(item.timestamp))
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF0F1424), RoundedCornerShape(18.dp))
                                        .border(1.dp, ThemeContainerBorder.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                                        .clickable {
                                            // Open Details popup
                                            activeScanItem = item
                                            showHistoryScreen = false
                                        }
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .background(ThemePurple.copy(alpha = 0.15f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = when (item.type) {
                                                    "WIFI" -> Icons.Default.Wifi
                                                    "URL" -> Icons.Default.Link
                                                    "CONTACT" -> Icons.Default.Person
                                                    "EMAIL" -> Icons.Default.Email
                                                    "PHONE" -> Icons.Default.Phone
                                                    else -> Icons.Default.Description
                                                },
                                                contentDescription = null,
                                                tint = ThemePurple,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(14.dp))

                                        Column {
                                            Text(
                                                text = item.title,
                                                color = Color.White,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = item.rawValue,
                                                color = TextMedium,
                                                fontSize = 12.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = dateStr,
                                                color = ThemePurple.copy(alpha = 0.7f),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }

                                    // Individual Delete Action
                                    IconButton(
                                        onClick = { viewModel.deleteQrScanItem(item.id) },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color.White.copy(alpha = 0.03f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Item",
                                            tint = Color.LightGray.copy(alpha = 0.7f),
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

    // Clear History Confirmation dialog
    if (showClearHistoryConfirm) {
        AlertDialog(
            onDismissRequest = { showClearHistoryConfirm = false },
            title = { Text("Clear Scan History?", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
            text = { Text("Are you absolutely sure you want to delete all scans from your Private Scan History? This action is irreversible.", color = Color.LightGray, fontSize = 14.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllQrScanHistory()
                        showClearHistoryConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
                ) {
                    Text("Clear All", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryConfirm = false }) {
                    Text("Cancel", color = ThemePurple)
                }
            },
            containerColor = Color(0xFF0F1424),
            shape = RoundedCornerShape(24.dp)
        )
    }
}

// Private QrCode parser helper
private data class ParsedQr(val type: String, val title: String, val details: String)

private fun parseBarcode(barcode: Barcode): ParsedQr {
    val raw = barcode.rawValue ?: ""
    return when (barcode.valueType) {
        Barcode.TYPE_WIFI -> {
            val wifi = barcode.wifi
            val ssid = wifi?.ssid ?: ""
            val pwd = wifi?.password ?: ""
            val encryption = when (wifi?.encryptionType) {
                Barcode.WiFi.TYPE_OPEN -> "None"
                Barcode.WiFi.TYPE_WEP -> "WEP"
                Barcode.WiFi.TYPE_WPA -> "WPA/WPA2"
                else -> "WPA"
            }
            ParsedQr(
                type = "WIFI",
                title = ssid,
                details = "Network SSID: $ssid\nPassword: $pwd\nSecurity: $encryption"
            )
        }
        Barcode.TYPE_URL -> {
            val urlStr = barcode.url?.url ?: raw
            ParsedQr(
                type = "URL",
                title = barcode.url?.title?.ifBlank { "Website URL" } ?: urlStr,
                details = urlStr
            )
        }
        Barcode.TYPE_CONTACT_INFO -> {
            val contact = barcode.contactInfo
            val name = contact?.name?.formattedName ?: "Contact"
            val phone = contact?.phones?.firstOrNull()?.number ?: ""
            val email = contact?.emails?.firstOrNull()?.address ?: ""
            val org = contact?.organization ?: ""
            val title = name
            val details = buildString {
                append("Name: $name\n")
                if (phone.isNotEmpty()) append("Phone: $phone\n")
                if (email.isNotEmpty()) append("Email: $email\n")
                if (org.isNotEmpty()) append("Organization: $org\n")
            }.trim()
            ParsedQr(type = "CONTACT", title = title, details = details)
        }
        Barcode.TYPE_EMAIL -> {
            val email = barcode.email
            val to = email?.address ?: ""
            val sub = email?.subject ?: ""
            val body = email?.body ?: ""
            ParsedQr(
                type = "EMAIL",
                title = to,
                details = "To: $to\nSubject: $sub\nBody: $body"
            )
        }
        Barcode.TYPE_PHONE -> {
            val phoneNum = barcode.phone?.number ?: raw
            ParsedQr(
                type = "PHONE",
                title = phoneNum,
                details = phoneNum
            )
        }
        else -> {
            val isUrl = raw.startsWith("http://", ignoreCase = true) || raw.startsWith("https://", ignoreCase = true)
            if (isUrl) {
                ParsedQr(
                    type = "URL",
                    title = raw,
                    details = raw
                )
            } else {
                ParsedQr(
                    type = "TEXT",
                    title = if (raw.length > 25) raw.take(22) + "..." else raw,
                    details = raw
                )
            }
        }
    }
}

@Composable
fun DocumentReviewScreen(
    originalBitmap: Bitmap,
    tempFile: File,
    viewModel: CalculatorViewModel,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    var docName by remember { mutableStateOf("Scan_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}") }
    var filterType by remember { mutableStateOf("ORIGINAL") } // ORIGINAL, GRAYSCALE, BW, MAGIC
    var outputFormat by remember { mutableStateOf("PDF") } // PDF, JPEG
    var isSaving by remember { mutableStateOf(false) }

    val processedBitmap = remember(originalBitmap, filterType) {
        applyDocumentFilter(originalBitmap, filterType)
    }

    Scaffold(
        containerColor = BrandBg,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onCancel,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                
                Text(
                    text = "Review scanned Document",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                
                Box(modifier = Modifier.size(40.dp)) // spacer balance
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Document Image Preview Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF101424))
                    .border(1.dp, ThemeContainerBorder, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = processedBitmap.asImageBitmap(),
                    contentDescription = "Document Scan Preview",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentScale = ContentScale.Inside
                )
            }

            // Name Field and Configuration Settings
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF101424)),
                border = BorderStroke(1.dp, ThemeContainerBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = docName,
                        onValueChange = { docName = it },
                        label = { Text("Document Name", color = TextMedium) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = ThemePurple,
                            unfocusedBorderColor = ThemeContainerBorder,
                            cursorColor = ThemePurple
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // Output Format Selection (PDF vs JPEG)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Export format", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        
                        Row(
                            modifier = Modifier
                                .background(Color(0xFF080B14), RoundedCornerShape(12.dp))
                                .padding(2.dp)
                        ) {
                            listOf("PDF", "JPEG").forEach { fmt ->
                                val selected = outputFormat == fmt
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (selected) ThemePurple else Color.Transparent)
                                        .clickable { outputFormat = fmt }
                                        .padding(horizontal = 16.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = fmt,
                                        color = if (selected) Color.White else TextMedium,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Visual Enhancement Filters Selector (Horizontal Row)
            Text(
                text = "Apply enhancement Filters",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextMedium,
                modifier = Modifier.padding(start = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf(
                    "ORIGINAL" to "Original",
                    "GRAYSCALE" to "Grayscale",
                    "BW" to "Crisp B&W",
                    "MAGIC" to "Enhanced Color"
                )
                
                filters.forEach { (type, name) ->
                    val selected = filterType == type
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) ThemePurple else Color(0xFF101424))
                            .border(1.dp, if (selected) Color.Transparent else ThemeContainerBorder, RoundedCornerShape(12.dp))
                            .clickable { filterType = type }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = name,
                            color = if (selected) Color.White else Color.LightGray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Action Button: Save to Vault
            Button(
                onClick = {
                    if (isSaving) return@Button
                    isSaving = true
                    
                    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                    val filenameClean = docName.trim().ifEmpty { "Scan_$timestamp" }

                    val id = System.currentTimeMillis().toString()
                    val vaultDir = File(context.filesDir, "vault_files")
                    if (!vaultDir.exists()) vaultDir.mkdirs()

                    val targetFile = if (outputFormat == "PDF") {
                        File(vaultDir, "$id.pdf")
                    } else {
                        File(vaultDir, "$id.jpg")
                    }

                    try {
                        if (outputFormat == "PDF") {
                            // Convert processed image directly to a beautiful single-page PDF document!
                            val pdfDocument = PdfDocument()
                            val pageInfo = PdfDocument.PageInfo.Builder(
                                processedBitmap.width,
                                processedBitmap.height,
                                1
                            ).create()
                            val page = pdfDocument.startPage(pageInfo)
                            page.canvas.drawBitmap(processedBitmap, 0f, 0f, null)
                            pdfDocument.finishPage(page)

                            val fos = FileOutputStream(targetFile)
                            pdfDocument.writeTo(fos)
                            fos.close()
                            pdfDocument.close()

                            viewModel.registerDirectVaultFile(
                                context,
                                targetFile,
                                "$filenameClean.pdf",
                                "application/pdf"
                            )
                        } else {
                            // Save as high-quality compressed JPEG inside the Vault
                            val fos = FileOutputStream(targetFile)
                            processedBitmap.compress(Bitmap.CompressFormat.JPEG, 92, fos)
                            fos.close()

                            viewModel.registerDirectVaultFile(
                                context,
                                targetFile,
                                "$filenameClean.jpg",
                                "image/jpeg"
                            )
                        }

                        tempFile.delete() // clean up temp cache picture
                        Toast.makeText(context, "Scanned document secured directly in Vault!", Toast.LENGTH_SHORT).show()
                        onSaved()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Save error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    } finally {
                        isSaving = false
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ThemePurple),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .height(50.dp)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Save, contentDescription = "Save")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Save scanned Doc directly to Vault",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// Custom document enhancement matrix filters
private fun applyDocumentFilter(bitmap: Bitmap, filterType: String): Bitmap {
    val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val paint = Paint()

    when (filterType) {
        "GRAYSCALE" -> {
            val matrix = ColorMatrix()
            matrix.setSaturation(0f)
            paint.colorFilter = ColorMatrixColorFilter(matrix)
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
        }
        "BW" -> {
            // High contrast monochromatic filter that extracts paper page text beautifully
            val contrastMatrix = ColorMatrix(floatArrayOf(
                2.5f, 0f, 0f, 0f, -120f,
                0f, 2.5f, 0f, 0f, -120f,
                0f, 0f, 2.5f, 0f, -120f,
                0f, 0f, 0f, 1f, 0f
            ))
            val desaturateMatrix = ColorMatrix()
            desaturateMatrix.setSaturation(0f)
            contrastMatrix.postConcat(desaturateMatrix)
            paint.colorFilter = ColorMatrixColorFilter(contrastMatrix)
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
        }
        "MAGIC" -> {
            // Sharpens document paper color while boosting ink saturation
            val contrastMatrix = ColorMatrix(floatArrayOf(
                1.2f, 0f, 0f, 0f, 10f,
                0f, 1.2f, 0f, 0f, 10f,
                0f, 0f, 1.2f, 0f, 10f,
                0f, 0f, 0f, 1f, 0f
            ))
            val saturateMatrix = ColorMatrix()
            saturateMatrix.setSaturation(1.4f)
            contrastMatrix.postConcat(saturateMatrix)
            paint.colorFilter = ColorMatrixColorFilter(contrastMatrix)
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
        }
        else -> {
            // ORIGINAL color output
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
        }
    }
    return output
}
