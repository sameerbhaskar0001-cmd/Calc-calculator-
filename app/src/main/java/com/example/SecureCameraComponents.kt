package com.example

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

// Theme Constant matches Calculator Screen
private val ThemePurple = Color(0xFF635BFF)
private val ThemeContainerBorder = Color(0xFF1E2438)
private val BrandBg = Color(0xFF090D1A)
private val TextMedium = Color(0xFF8B92A5)

@Composable
fun SecureCameraView(
    viewModel: CalculatorViewModel,
    onDismiss: () -> Unit
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

    // Use cases references
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var videoCapture: VideoCapture<Recorder>? by remember { mutableStateOf(null) }
    var camera: androidx.camera.core.Camera? by remember { mutableStateOf(null) }

    val cameraProviderFuture = remember { try { CameraInitializer.initAndGetProvider(context) } catch (e: Exception) { null } }

    // Handle permissions natively
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val cameraGranted = perms[Manifest.permission.CAMERA] ?: false
        if (!cameraGranted) {
            showPermissionError = true
        }
    }

    LaunchedEffect(Unit) {
        val cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        val audioPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
        
        if (cameraPermission != PackageManager.PERMISSION_GRANTED || audioPermission != PackageManager.PERMISSION_GRANTED) {
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
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { previewView ->
                val cameraProvider = try {
                    cameraProviderFuture?.get() ?: return@AndroidView
                } catch (e: Exception) {
                    return@AndroidView
                }

                val preview = Preview.Builder().build().apply {
                    setSurfaceProvider(previewView.surfaceProvider)
                }

                // Photo Capture Config
                val imgCap = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .setFlashMode(
                        when (flashMode) {
                            "ON" -> ImageCapture.FLASH_MODE_ON
                            "OFF" -> ImageCapture.FLASH_MODE_OFF
                            else -> ImageCapture.FLASH_MODE_AUTO
                        }
                    )
                    .build()
                imageCapture = imgCap

                // Video Capture Config
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
                    
                    // Enable/Disable flash/torch based on selection
                    camera?.let { cam ->
                        if (isVideoMode) {
                            cam.cameraControl.enableTorch(flashMode == "ON")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
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

            // Front/Back Switcher Button
            IconButton(
                onClick = { isFrontCamera = !isFrontCamera },
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(Icons.Default.FlipCameraAndroid, contentDescription = "Flip Camera", tint = Color.White)
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

            // Capture Shutter trigger button
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

    var isFrontCamera by remember { mutableStateOf(false) }
    var flashMode by remember { mutableStateOf("AUTO") }
    var showPermissionError by remember { mutableStateOf(false) }

    // Captured image for processing review
    var capturedTempFile by remember { mutableStateOf<File?>(null) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Use cases references
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    val cameraProviderFuture = remember { try { CameraInitializer.initAndGetProvider(context) } catch (e: Exception) { null } }

    // Handle permissions natively
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) showPermissionError = true
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (capturedBitmap != null && capturedTempFile != null) {
        // --- Document Processing / Review Screen ---
        DocumentReviewScreen(
            originalBitmap = capturedBitmap!!,
            tempFile = capturedTempFile!!,
            viewModel = viewModel,
            onSaved = {
                capturedBitmap = null
                capturedTempFile = null
                onDismiss()
            },
            onCancel = {
                capturedTempFile?.delete()
                capturedBitmap = null
                capturedTempFile = null
            }
        )
    } else {
        // --- Live Scanner Camera Frame ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { previewView ->
                    val cameraProvider = try {
                        cameraProviderFuture?.get() ?: return@AndroidView
                    } catch (e: Exception) {
                        return@AndroidView
                    }

                    val preview = Preview.Builder().build().apply {
                        setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val imgCap = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                        .setFlashMode(
                            when (flashMode) {
                                "ON" -> ImageCapture.FLASH_MODE_ON
                                "OFF" -> ImageCapture.FLASH_MODE_OFF
                                else -> ImageCapture.FLASH_MODE_AUTO
                            }
                        )
                        .build()
                    imageCapture = imgCap

                    val cameraSelector = if (isFrontCamera) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    }

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imgCap
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            )

            // Dynamic Scanning Mask and Green Laser line
            val infiniteTransition = rememberInfiniteTransition(label = "LaserEffect")
            val laserProgress by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2500, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "LaserLine"
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Draw translucent gray mask overlay
                drawRect(
                    color = Color.Black.copy(alpha = 0.65f),
                    size = size
                )

                // Cutout dimensions representing standard document sheet ratio
                val width = canvasWidth * 0.85f
                val height = canvasHeight * 0.65f
                val x = (canvasWidth - width) / 2
                val y = (canvasHeight - height) / 2

                // Clear/punch the document rectangle cutout
                drawRoundRect(
                    color = Color.Transparent,
                    topLeft = Offset(x, y),
                    size = Size(width, height),
                    cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
                    blendMode = androidx.compose.ui.graphics.BlendMode.Clear
                )

                // Draw premium alignment guides
                drawRoundRect(
                    color = ThemePurple,
                    topLeft = Offset(x, y),
                    size = Size(width, height),
                    cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                )

                // Laser Scanning Line with soft neon glow
                val laserYPosition = y + (height * laserProgress)
                drawLine(
                    color = Color(0xFF00FFCC),
                    start = Offset(x + 4.dp.toPx(), laserYPosition),
                    end = Offset(x + width - 4.dp.toPx(), laserYPosition),
                    strokeWidth = 3.dp.toPx()
                )
            }

            // Top Control Action Bar
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
                    Icon(Icons.Default.Close, contentDescription = "Exit Scanner", tint = Color.White)
                }

                Text(
                    text = "ALIGN DOCUMENT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .background(ThemePurple.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                )

                // Flash Switcher
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

            // Bottom Shutter Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable {
                                val imgCap = imageCapture ?: return@clickable
                                val id = System.currentTimeMillis().toString()
                                val tempFile = File(context.cacheDir, "scan_$id.jpg")

                                val outputOpts = ImageCapture.OutputFileOptions.Builder(tempFile).build()
                                imgCap.takePicture(
                                    outputOpts,
                                    ContextCompat.getMainExecutor(context),
                                    object : ImageCapture.OnImageSavedCallback {
                                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                            val options = BitmapFactory.Options().apply { inMutable = true }
                                            val bitmap = BitmapFactory.decodeFile(tempFile.absolutePath, options)
                                            if (bitmap != null) {
                                                capturedTempFile = tempFile
                                                capturedBitmap = bitmap
                                            } else {
                                                Toast.makeText(context, "Scan decode error", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        override fun onError(exception: ImageCaptureException) {
                                            Toast.makeText(context, "Failed to capture document: ${exception.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                )
                            }
                    )
                }
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
