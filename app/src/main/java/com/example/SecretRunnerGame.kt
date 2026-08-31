package com.example

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

enum class RunnerGameState {
    NOT_STARTED,
    PLAYING,
    GAME_OVER
}

enum class ObstacleType {
    FIREWALL_GATE,
    CYBER_BLOCK,
    SECURITY_BARRIER,
    SHADOW_SPIRE
}

enum class CollectibleType {
    SHIELD_TOKEN,
    PRIVACY_STAR
}

data class RunnerObstacle(
    val id: Long,
    var x: Float,
    val width: Float,
    val height: Float,
    val type: ObstacleType
)

data class RunnerCollectible(
    val id: Long,
    var x: Float,
    val y: Float, // height above ground
    val type: CollectibleType,
    val points: Int,
    var isCollected: Boolean = false
)

data class ScorePopup(
    val id: Long,
    val text: String,
    var x: Float,
    var y: Float,
    var alpha: Float = 1f
)

@Composable
fun SecretRunnerGameView(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val isDark = isSystemInDarkTheme()

    // Preferences for Best Score
    val prefs = remember { context.getSharedPreferences("secret_runner_prefs", Context.MODE_PRIVATE) }
    var bestScore by remember { mutableStateOf(prefs.getInt("best_score", 0)) }

    // Intercept back button to return safely to browser
    BackHandler {
        onClose()
    }

    var gameState by remember { mutableStateOf(RunnerGameState.NOT_STARTED) }
    var distanceScore by remember { mutableFloatStateOf(0f) }
    var bonusScore by remember { mutableIntStateOf(0) }
    val totalScore = (distanceScore.toInt() + bonusScore)
    var isNewRecord by remember { mutableStateOf(false) }

    // Runner physics state
    var runnerY by remember { mutableFloatStateOf(0f) } // 0 = on ground, >0 = in air
    var runnerVelocity by remember { mutableFloatStateOf(0f) }
    var isGrounded by remember { mutableStateOf(true) }
    var canDoubleJump by remember { mutableStateOf(true) }
    var runAnimationPhase by remember { mutableFloatStateOf(0f) }

    // World state
    val obstacles = remember { mutableStateListOf<RunnerObstacle>() }
    val collectibles = remember { mutableStateListOf<RunnerCollectible>() }
    val scorePopups = remember { mutableStateListOf<ScorePopup>() }
    var nextSpawnDistance by remember { mutableFloatStateOf(400f) }
    var nextEntityId by remember { mutableLongStateOf(1L) }
    var groundOffset by remember { mutableFloatStateOf(0f) }
    var bgParallaxOffset by remember { mutableFloatStateOf(0f) }

    // Constants in pixels
    val gravity = -1800f
    val jumpVelocity = 680f
    val doubleJumpVelocity = 600f
    val baseSpeed = 360f

    val jumpAction = {
        if (gameState == RunnerGameState.NOT_STARTED) {
            gameState = RunnerGameState.PLAYING
            runnerVelocity = jumpVelocity
            isGrounded = false
            canDoubleJump = true
        } else if (gameState == RunnerGameState.PLAYING) {
            if (isGrounded) {
                runnerVelocity = jumpVelocity
                isGrounded = false
                canDoubleJump = true
            } else if (canDoubleJump) {
                runnerVelocity = doubleJumpVelocity
                canDoubleJump = false
            }
        }
    }

    val restartGame = {
        obstacles.clear()
        collectibles.clear()
        scorePopups.clear()
        distanceScore = 0f
        bonusScore = 0
        runnerY = 0f
        runnerVelocity = 0f
        isGrounded = true
        canDoubleJump = true
        runAnimationPhase = 0f
        nextSpawnDistance = 500f
        isNewRecord = false
        gameState = RunnerGameState.PLAYING
    }

    // Colors matching Secret Browser Design System
    val bgColorStart = if (isDark) Color(0xFF0B101B) else Color(0xFFF4F7FB)
    val bgColorEnd = if (isDark) Color(0xFF131C2E) else Color(0xFFE2E9F3)
    val groundColor = if (isDark) Color(0xFF1A263D) else Color(0xFFCBD6E2)
    val groundLineColor = if (isDark) Color(0xFF00E5FF) else Color(0xFF1E88E5)
    val obstacleColor = if (isDark) Color(0xFFFF5252) else Color(0xFFD32F2F)
    val playerSuitColor = if (isDark) Color(0xFFECEFF1) else Color(0xFF263238)
    val playerVisorColor = Color(0xFFFF6A00) // AccentColor
    val shieldGlowColor = Color(0xFF00E5FF)
    val tokenStarColor = Color(0xFFFFD600)
    val cardBg = if (isDark) Color(0xFF162238) else Color(0xFFFFFFFF)
    val cardBorder = if (isDark) Color(0xFF2A3C5C) else Color(0xFFE2E8F0)
    val textP = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A)
    val textS = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)

    // Game loop running on native frame clock
    LaunchedEffect(gameState) {
        if (gameState != RunnerGameState.PLAYING) return@LaunchedEffect

        var lastFrameTimeNanos = 0L

        while (isActive && gameState == RunnerGameState.PLAYING) {
            withFrameNanos { frameTimeNanos ->
                if (lastFrameTimeNanos == 0L) {
                    lastFrameTimeNanos = frameTimeNanos
                    return@withFrameNanos
                }

                val dt = ((frameTimeNanos - lastFrameTimeNanos) / 1_000_000_000f).coerceIn(0.001f, 0.05f)
                lastFrameTimeNanos = frameTimeNanos

                // Speed ramps up gradually with score
                val currentSpeedMultiplier = 1f + (distanceScore / 750f).coerceAtMost(2.0f)
                val currentSpeed = baseSpeed * currentSpeedMultiplier

                // Advance distance score
                distanceScore += currentSpeed * dt * 0.09f
                val currentTotalScore = (distanceScore.toInt() + bonusScore)
                if (currentTotalScore > bestScore) {
                    bestScore = currentTotalScore
                    isNewRecord = true
                    prefs.edit().putInt("best_score", bestScore).apply()
                }

                // Physics: Player Y & Velocity
                runnerVelocity += gravity * dt
                runnerY += runnerVelocity * dt
                if (runnerY <= 0f) {
                    runnerY = 0f
                    runnerVelocity = 0f
                    isGrounded = true
                    canDoubleJump = true
                } else {
                    isGrounded = false
                }

                // Run leg stride animation
                if (isGrounded) {
                    runAnimationPhase += dt * (16f * currentSpeedMultiplier)
                }

                // Parallax offsets
                groundOffset = (groundOffset + currentSpeed * dt) % 40f
                bgParallaxOffset = (bgParallaxOffset + currentSpeed * 0.25f * dt) % 120f

                // Obstacle & Collectible Spawning
                nextSpawnDistance -= currentSpeed * dt
                if (nextSpawnDistance <= 0f) {
                    val spawnX = 900f // Off-screen right
                    val rand = Random.nextFloat()

                    if (rand < 0.68f) {
                        // Spawn Obstacle
                        val type = ObstacleType.entries.random()
                        val width = when (type) {
                            ObstacleType.FIREWALL_GATE -> 32f
                            ObstacleType.CYBER_BLOCK -> 38f
                            ObstacleType.SECURITY_BARRIER -> 44f
                            ObstacleType.SHADOW_SPIRE -> 28f
                        }
                        val height = when (type) {
                            ObstacleType.FIREWALL_GATE -> 54f
                            ObstacleType.CYBER_BLOCK -> 42f
                            ObstacleType.SECURITY_BARRIER -> 46f
                            ObstacleType.SHADOW_SPIRE -> 60f
                        }
                        obstacles.add(
                            RunnerObstacle(
                                id = nextEntityId++,
                                x = spawnX,
                                width = width,
                                height = height,
                                type = type
                            )
                        )
                    } else {
                        // Spawn Collectible
                        val isStar = Random.nextBoolean()
                        val cType = if (isStar) CollectibleType.PRIVACY_STAR else CollectibleType.SHIELD_TOKEN
                        val points = if (isStar) 50 else 25
                        val heightAboveGround = if (Random.nextBoolean()) 25f else 75f
                        collectibles.add(
                            RunnerCollectible(
                                id = nextEntityId++,
                                x = spawnX,
                                y = heightAboveGround,
                                type = cType,
                                points = points
                            )
                        )
                    }

                    // Next safe interval between 360px and 620px
                    nextSpawnDistance = Random.nextFloat() * 260f + 360f
                }

                // Update Obstacles position & collision
                val playerX = 80f
                val playerWidth = 32f
                val playerHeight = 48f
                val playerBottom = runnerY
                val playerTop = runnerY + playerHeight

                val obstacleIterator = obstacles.iterator()
                while (obstacleIterator.hasNext()) {
                    val obs = obstacleIterator.next()
                    obs.x -= currentSpeed * dt

                    // Bounding box collision check
                    val obsLeft = obs.x
                    val obsRight = obs.x + obs.width
                    val obsTop = obs.height

                    if (playerX + playerWidth > obsLeft + 6f && playerX < obsRight - 6f) {
                        if (playerBottom < obsTop - 4f) {
                            // Collision detected! Game over
                            gameState = RunnerGameState.GAME_OVER
                            break
                        }
                    }

                    if (obs.x < -100f) {
                        obstacleIterator.remove()
                    }
                }

                // Update Collectibles position & collection check
                val collectibleIterator = collectibles.iterator()
                while (collectibleIterator.hasNext()) {
                    val col = collectibleIterator.next()
                    col.x -= currentSpeed * dt

                    if (!col.isCollected) {
                        val colCenterX = col.x + 14f
                        val colCenterY = col.y + 14f
                        val playerCenterX = playerX + playerWidth / 2f
                        val playerCenterY = playerBottom + playerHeight / 2f

                        val dx = abs(colCenterX - playerCenterX)
                        val dy = abs(colCenterY - playerCenterY)

                        if (dx < 26f && dy < 34f) {
                            col.isCollected = true
                            bonusScore += col.points
                            scorePopups.add(
                                ScorePopup(
                                    id = nextEntityId++,
                                    text = "+${col.points}",
                                    x = col.x,
                                    y = col.y + 20f
                                )
                            )
                        }
                    }

                    if (col.x < -100f || (col.isCollected && col.x < playerX - 50f)) {
                        collectibleIterator.remove()
                    }
                }

                // Update floating score popups
                val popupIterator = scorePopups.iterator()
                while (popupIterator.hasNext()) {
                    val popup = popupIterator.next()
                    popup.y += dt * 50f
                    popup.alpha -= dt * 1.5f
                    if (popup.alpha <= 0f) {
                        popupIterator.remove()
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(bgColorStart, bgColorEnd)
                )
            )
            .pointerInput(gameState) {
                detectTapGestures {
                    jumpAction()
                }
            }
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = constraints.maxWidth.toFloat()
            val canvasHeight = constraints.maxHeight.toFloat()
            val groundY = canvasHeight * 0.72f // ground baseline

            // Rendering Canvas for Game Graphics
            Canvas(modifier = Modifier.fillMaxSize()) {
                val scope = this

                // 1. Background Cyber Skyline (Parallax)
                drawCyberBackground(
                    width = canvasWidth,
                    height = groundY,
                    offset = bgParallaxOffset,
                    isDark = isDark
                )

                // 2. Ground & Cyber Grid
                drawRect(
                    color = groundColor,
                    topLeft = Offset(0f, groundY),
                    size = Size(canvasWidth, canvasHeight - groundY)
                )
                // Glowing baseline
                drawLine(
                    color = groundLineColor,
                    start = Offset(0f, groundY),
                    end = Offset(canvasWidth, groundY),
                    strokeWidth = 3.dp.toPx()
                )
                // Moving track tick marks
                var tickX = -groundOffset
                while (tickX < canvasWidth + 40f) {
                    if (tickX >= 0f) {
                        drawLine(
                            color = groundLineColor.copy(alpha = 0.4f),
                            start = Offset(tickX, groundY),
                            end = Offset(tickX + 12f, groundY + 14f),
                            strokeWidth = 1.5.dp.toPx()
                        )
                    }
                    tickX += 36f
                }

                // 3. Draw Obstacles
                obstacles.forEach { obs ->
                    drawObstacle(
                        obstacle = obs,
                        groundY = groundY,
                        obstacleColor = obstacleColor,
                        accentColor = playerVisorColor,
                        isDark = isDark
                    )
                }

                // 4. Draw Collectibles
                collectibles.forEach { col ->
                    if (!col.isCollected) {
                        drawCollectible(
                            collectible = col,
                            groundY = groundY,
                            shieldColor = shieldGlowColor,
                            starColor = tokenStarColor
                        )
                    }
                }

                // 5. Draw Secret Privacy Runner Mascot
                drawRunnerMascot(
                    playerX = 80f,
                    playerY = groundY - runnerY - 48f,
                    runnerY = runnerY,
                    isGrounded = isGrounded,
                    phase = runAnimationPhase,
                    suitColor = playerSuitColor,
                    visorColor = playerVisorColor,
                    shieldColor = shieldGlowColor,
                    isDark = isDark
                )
            }

            // Top HUD Bar: Clean, Accessible, with Back Button & Scores
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Exit Game / Return to Browser button
                Surface(
                    onClick = onClose,
                    shape = RoundedCornerShape(12.dp),
                    color = cardBg.copy(alpha = 0.9f),
                    border = BorderStroke(1.dp, cardBorder),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Browser",
                            tint = textP,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Back",
                            color = textP,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Scores Display
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Best Score Pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = cardBg.copy(alpha = 0.85f),
                        border = BorderStroke(1.dp, cardBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Best Score",
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "BEST $bestScore",
                                color = textS,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    // Live Score Pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = playerVisorColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.5.dp, playerVisorColor)
                    ) {
                        Text(
                            text = "SCORE $totalScore",
                            color = if (isDark) Color.White else Color(0xFFD84315),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Start Screen Prompt
            if (gameState == RunnerGameState.NOT_STARTED) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = cardBg.copy(alpha = 0.95f),
                        border = BorderStroke(1.5.dp, cardBorder),
                        shadowElevation = 8.dp,
                        modifier = Modifier.widthIn(max = 380.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(playerVisorColor.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "Secret Runner",
                                    tint = playerVisorColor,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "SECRET RUNNER",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = textP,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Tap screen to jump. Collect shields & avoid firewalls!",
                                fontSize = 13.sp,
                                color = textS,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Button(
                                onClick = { jumpAction() },
                                colors = ButtonDefaults.buttonColors(containerColor = playerVisorColor),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "START RUN",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Game Over Modal Overlay
            if (gameState == RunnerGameState.GAME_OVER) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.55f))
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = cardBg,
                        border = BorderStroke(1.5.dp, cardBorder),
                        shadowElevation = 12.dp,
                        modifier = Modifier.widthIn(max = 380.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Run Over!",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = textP
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Nice try, Buddy.",
                                fontSize = 14.sp,
                                color = textS
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Score Breakdown Card
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isDark) Color(0xFF0F172A) else Color(0xFFF1F5F9),
                                border = BorderStroke(1.dp, cardBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (isNewRecord) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFFFFB300).copy(alpha = 0.2f),
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        ) {
                                            Text(
                                                text = "✨ NEW BEST SCORE! ✨",
                                                color = Color(0xFFFFB300),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Score", color = textS, fontSize = 13.sp)
                                        Text(
                                            "$totalScore",
                                            color = textP,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Best Record", color = textS, fontSize = 13.sp)
                                        Text(
                                            "$bestScore",
                                            color = Color(0xFFFFB300),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Action Buttons
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = { restartGame() },
                                    colors = ButtonDefaults.buttonColors(containerColor = playerVisorColor),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "RUN AGAIN",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }

                                OutlinedButton(
                                    onClick = onClose,
                                    shape = RoundedCornerShape(14.dp),
                                    border = BorderStroke(1.dp, cardBorder),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                ) {
                                    Text(
                                        text = "BACK TO BROWSER",
                                        color = textP,
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

// Draw Parallax Cyber Skyline in background
private fun DrawScope.drawCyberBackground(
    width: Float,
    height: Float,
    offset: Float,
    isDark: Boolean
) {
    val colColor = if (isDark) Color(0xFF162033) else Color(0xFFDCE4EE)
    val gridColor = if (isDark) Color(0xFF1E2D47).copy(alpha = 0.3f) else Color(0xFFCCD6E4).copy(alpha = 0.4f)

    // Data towers in distance
    val towerWidth = 44f
    val spacing = 76f
    var x = -offset
    var index = 0
    while (x < width + spacing) {
        val towerHeight = ((index * 37) % 110 + 40).toFloat()
        drawRect(
            color = colColor,
            topLeft = Offset(x, height - towerHeight),
            size = Size(towerWidth, towerHeight)
        )
        // Tower window stripes
        drawLine(
            color = gridColor,
            start = Offset(x + 8f, height - towerHeight + 10f),
            end = Offset(x + towerWidth - 8f, height - towerHeight + 10f),
            strokeWidth = 2f
        )
        x += spacing
        index++
    }
}

// Draw Obstacles (Firewall gate, cyber block, security barrier)
private fun DrawScope.drawObstacle(
    obstacle: RunnerObstacle,
    groundY: Float,
    obstacleColor: Color,
    accentColor: Color,
    isDark: Boolean
) {
    val obsTop = groundY - obstacle.height
    when (obstacle.type) {
        ObstacleType.FIREWALL_GATE -> {
            // Two vertical laser posts
            val pillarWidth = 7f
            drawRoundRect(
                color = obstacleColor,
                topLeft = Offset(obstacle.x, obsTop),
                size = Size(pillarWidth, obstacle.height),
                cornerRadius = CornerRadius(4f, 4f)
            )
            drawRoundRect(
                color = obstacleColor,
                topLeft = Offset(obstacle.x + obstacle.width - pillarWidth, obsTop),
                size = Size(pillarWidth, obstacle.height),
                cornerRadius = CornerRadius(4f, 4f)
            )
            // Laser beam across
            drawLine(
                color = Color(0xFFFF1744),
                start = Offset(obstacle.x + pillarWidth, obsTop + 14f),
                end = Offset(obstacle.x + obstacle.width - pillarWidth, obsTop + 14f),
                strokeWidth = 4f
            )
            drawLine(
                color = Color(0xFFFF8A80),
                start = Offset(obstacle.x + pillarWidth, obsTop + 28f),
                end = Offset(obstacle.x + obstacle.width - pillarWidth, obsTop + 28f),
                strokeWidth = 3f
            )
        }
        ObstacleType.CYBER_BLOCK -> {
            // Beveled block
            drawRoundRect(
                color = obstacleColor,
                topLeft = Offset(obstacle.x, obsTop),
                size = Size(obstacle.width, obstacle.height),
                cornerRadius = CornerRadius(8f, 8f)
            )
            // Hazard accent stripe
            drawRoundRect(
                color = accentColor,
                topLeft = Offset(obstacle.x + 4f, obsTop + 4f),
                size = Size(obstacle.width - 8f, 6f),
                cornerRadius = CornerRadius(3f, 3f)
            )
        }
        ObstacleType.SECURITY_BARRIER, ObstacleType.SHADOW_SPIRE -> {
            // Angled security barrier
            val path = Path().apply {
                moveTo(obstacle.x, groundY)
                lineTo(obstacle.x + obstacle.width * 0.2f, obsTop)
                lineTo(obstacle.x + obstacle.width * 0.8f, obsTop)
                lineTo(obstacle.x + obstacle.width, groundY)
                close()
            }
            drawPath(path, obstacleColor)
            // Warning stripe
            drawLine(
                color = Color.White.copy(alpha = 0.8f),
                start = Offset(obstacle.x + obstacle.width * 0.35f, obsTop + 8f),
                end = Offset(obstacle.x + obstacle.width * 0.65f, obsTop + 8f),
                strokeWidth = 3f
            )
        }
    }
}

// Draw Collectible Tokens (Shield / Star)
private fun DrawScope.drawCollectible(
    collectible: RunnerCollectible,
    groundY: Float,
    shieldColor: Color,
    starColor: Color
) {
    val center = Offset(collectible.x + 14f, groundY - collectible.y - 14f)

    when (collectible.type) {
        CollectibleType.SHIELD_TOKEN -> {
            // Glowing outer ring
            drawCircle(
                color = shieldColor.copy(alpha = 0.25f),
                radius = 16f,
                center = center
            )
            // Shield polygon
            val path = Path().apply {
                moveTo(center.x, center.y - 11f)
                lineTo(center.x + 9f, center.y - 6f)
                lineTo(center.x + 7f, center.y + 5f)
                lineTo(center.x, center.y + 11f)
                lineTo(center.x - 7f, center.y + 5f)
                lineTo(center.x - 9f, center.y - 6f)
                close()
            }
            drawPath(path, shieldColor)
            drawPath(path, Color.White, style = Stroke(width = 1.5f))
        }
        CollectibleType.PRIVACY_STAR -> {
            // Glowing star ring
            drawCircle(
                color = starColor.copy(alpha = 0.3f),
                radius = 16f,
                center = center
            )
            // 4-point star polygon
            val path = Path().apply {
                moveTo(center.x, center.y - 12f)
                lineTo(center.x + 4f, center.y - 4f)
                lineTo(center.x + 12f, center.y)
                lineTo(center.x + 4f, center.y + 4f)
                lineTo(center.x, center.y + 12f)
                lineTo(center.x - 4f, center.y + 4f)
                lineTo(center.x - 12f, center.y)
                lineTo(center.x - 4f, center.y - 4f)
                close()
            }
            drawPath(path, starColor)
            drawCircle(color = Color.White, radius = 2.5f, center = center)
        }
    }
}

// Draw the Original Secret Privacy Runner mascot
private fun DrawScope.drawRunnerMascot(
    playerX: Float,
    playerY: Float,
    runnerY: Float,
    isGrounded: Boolean,
    phase: Float,
    suitColor: Color,
    visorColor: Color,
    shieldColor: Color,
    isDark: Boolean
) {
    val bodyCenterX = playerX + 16f
    val bodyCenterY = playerY + 20f

    // 1. Torso & Hoodie Silhouette
    drawRoundRect(
        color = suitColor,
        topLeft = Offset(playerX + 6f, playerY + 12f),
        size = Size(18f, 22f),
        cornerRadius = CornerRadius(6f, 6f)
    )

    // 2. Head / Stealth Helmet
    drawCircle(
        color = suitColor,
        radius = 9f,
        center = Offset(bodyCenterX, playerY + 9f)
    )

    // 3. Cyber Visor (Glowing Neon Accent)
    drawRoundRect(
        color = visorColor,
        topLeft = Offset(bodyCenterX, playerY + 6f),
        size = Size(10f, 5f),
        cornerRadius = CornerRadius(2.5f, 2.5f)
    )

    // 4. Holographic Arm Shield
    val shieldPath = Path().apply {
        moveTo(playerX + 22f, bodyCenterY - 6f)
        lineTo(playerX + 28f, bodyCenterY - 2f)
        lineTo(playerX + 26f, bodyCenterY + 6f)
        lineTo(playerX + 22f, bodyCenterY + 10f)
        lineTo(playerX + 18f, bodyCenterY + 6f)
        lineTo(playerX + 18f, bodyCenterY - 2f)
        close()
    }
    drawPath(shieldPath, shieldColor.copy(alpha = 0.85f))
    drawPath(shieldPath, Color.White, style = Stroke(width = 1.2f))

    // 5. Legs & Running Stride / Jump Animation
    if (isGrounded) {
        val legSwing = sin(phase) * 11f

        // Front leg
        drawLine(
            color = suitColor,
            start = Offset(playerX + 11f, playerY + 32f),
            end = Offset(playerX + 12f + legSwing, playerY + 46f),
            strokeWidth = 4f
        )
        // Back leg
        drawLine(
            color = suitColor.copy(alpha = 0.8f),
            start = Offset(playerX + 19f, playerY + 32f),
            end = Offset(playerX + 18f - legSwing, playerY + 46f),
            strokeWidth = 4f
        )
    } else {
        // Tucked jump legs
        drawLine(
            color = suitColor,
            start = Offset(playerX + 11f, playerY + 32f),
            end = Offset(playerX + 8f, playerY + 40f),
            strokeWidth = 4f
        )
        drawLine(
            color = suitColor,
            start = Offset(playerX + 8f, playerY + 40f),
            end = Offset(playerX + 18f, playerY + 44f),
            strokeWidth = 3.5f
        )
    }
}
