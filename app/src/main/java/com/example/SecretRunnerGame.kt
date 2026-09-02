package com.example

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
    LOW_VAULT_BOX,         // Low crate/barrier -> Jump or Vault
    HIGH_OVERHEAD_LASER,   // Overhead firewall laser -> Must Slide under
    CYBER_SPIRE,           // Medium spiked barrier -> Jump/Double Jump
    WALL_STRUCTURE,        // Tall wall -> Double Jump or Wall Jump
    SECURITY_LASER_GATE    // Twin laser barrier -> Jump
}

enum class CollectibleType {
    SHIELD_TOKEN,
    PRIVACY_STAR,
    ENCRYPTED_CORE
}

data class RunnerObstacle(
    val id: Long,
    var x: Float,
    val yOffset: Float, // distance from ground
    val width: Float,
    val height: Float,
    val type: ObstacleType,
    var wallJumped: Boolean = false
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
    var alpha: Float = 1f,
    val color: Color = Color(0xFFFFD600)
)

data class RunnerParticle(
    val id: Long,
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val color: Color,
    var alpha: Float = 1f,
    val radius: Float = 3f,
    val maxLife: Float = 0.4f,
    var age: Float = 0f
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

    // Runner physics & parkour state
    var runnerY by remember { mutableFloatStateOf(0f) } // 0 = ground, >0 = air
    var runnerVelocity by remember { mutableFloatStateOf(0f) }
    var isGrounded by remember { mutableStateOf(true) }
    var canDoubleJump by remember { mutableStateOf(true) }
    var isDoubleJumping by remember { mutableStateOf(false) }
    var isSliding by remember { mutableStateOf(false) }
    var slideTimer by remember { mutableFloatStateOf(0f) }
    var isWallJumping by remember { mutableStateOf(false) }
    var wallJumpTimer by remember { mutableFloatStateOf(0f) }
    var runAnimationPhase by remember { mutableFloatStateOf(0f) }

    // Visual effect state
    var speedLineAlpha by remember { mutableFloatStateOf(0f) }

    // World entities
    val obstacles = remember { mutableStateListOf<RunnerObstacle>() }
    val collectibles = remember { mutableStateListOf<RunnerCollectible>() }
    val scorePopups = remember { mutableStateListOf<ScorePopup>() }
    val particles = remember { mutableStateListOf<RunnerParticle>() }

    var nextSpawnDistance by remember { mutableFloatStateOf(420f) }
    var nextEntityId by remember { mutableLongStateOf(1L) }
    var groundOffset by remember { mutableFloatStateOf(0f) }
    var midgroundOffset by remember { mutableFloatStateOf(0f) }
    var bgParallaxOffset by remember { mutableFloatStateOf(0f) }

    // Physics constants (tuned for ultra-responsive parkour flow)
    val gravity = -1950f
    val jumpVelocity = 700f
    val doubleJumpVelocity = 620f
    val wallJumpBoostY = 740f
    val baseSpeed = 380f
    val slideDuration = 0.48f

    // Particle spawn helper
    val spawnParticles = { x: Float, y: Float, count: Int, baseColor: Color, speedScale: Float ->
        for (i in 0 until count) {
            val angle = Random.nextFloat() * 2f * PI.toFloat()
            val speed = (Random.nextFloat() * 120f + 40f) * speedScale
            particles.add(
                RunnerParticle(
                    id = nextEntityId++,
                    x = x,
                    y = y,
                    vx = cos(angle) * speed,
                    vy = sin(angle) * speed,
                    color = baseColor,
                    alpha = 1f,
                    radius = Random.nextFloat() * 2.5f + 1.5f,
                    maxLife = Random.nextFloat() * 0.35f + 0.2f
                )
            )
        }
    }

    val jumpAction = {
        if (gameState == RunnerGameState.NOT_STARTED) {
            gameState = RunnerGameState.PLAYING
            runnerVelocity = jumpVelocity
            isGrounded = false
            canDoubleJump = true
            isSliding = false
            isDoubleJumping = false
            spawnParticles(100f, 10f, 6, Color(0xFF00E5FF), 0.8f)
        } else if (gameState == RunnerGameState.PLAYING) {
            // Cancel slide if jumping
            if (isSliding) {
                isSliding = false
                slideTimer = 0f
            }

            // Check if adjacent to a tall wall for Parkour WALL JUMP
            val playerX = 85f
            var triggeredWallJump = false
            val nearbyWall = obstacles.find { obs ->
                obs.type == ObstacleType.WALL_STRUCTURE &&
                        !obs.wallJumped &&
                        abs(obs.x - playerX) < 55f &&
                        runnerY > 15f && runnerY < obs.height + 25f
            }

            if (nearbyWall != null) {
                nearbyWall.wallJumped = true
                runnerVelocity = wallJumpBoostY
                isGrounded = false
                canDoubleJump = true
                isWallJumping = true
                wallJumpTimer = 0.35f
                triggeredWallJump = true
                spawnParticles(playerX + 20f, runnerY + 20f, 12, Color(0xFFFF6A00), 1.2f)
                scorePopups.add(
                    ScorePopup(
                        id = nextEntityId++,
                        text = "WALL LEAP!",
                        x = playerX,
                        y = runnerY + 45f,
                        color = Color(0xFFFF6A00)
                    )
                )
            }

            if (!triggeredWallJump) {
                if (isGrounded) {
                    runnerVelocity = jumpVelocity
                    isGrounded = false
                    canDoubleJump = true
                    isDoubleJumping = false
                    spawnParticles(100f, 10f, 6, Color(0xFF00E5FF), 0.8f)
                } else if (canDoubleJump) {
                    runnerVelocity = doubleJumpVelocity
                    canDoubleJump = false
                    isDoubleJumping = true
                    spawnParticles(100f, runnerY + 15f, 10, Color(0xFF00E5FF), 1.1f)
                    scorePopups.add(
                        ScorePopup(
                            id = nextEntityId++,
                            text = "AIR VAULT!",
                            x = 90f,
                            y = runnerY + 40f,
                            color = Color(0xFF00E5FF)
                        )
                    )
                }
            }
        }
    }

    val slideAction = {
        if (gameState == RunnerGameState.PLAYING && !isSliding) {
            isSliding = true
            slideTimer = slideDuration
            if (!isGrounded) {
                // Fast dive if in mid-air
                runnerVelocity = -800f
            }
            spawnParticles(95f, 8f, 8, Color(0xFFFFB300), 0.9f)
        }
    }

    val restartGame = {
        obstacles.clear()
        collectibles.clear()
        scorePopups.clear()
        particles.clear()
        distanceScore = 0f
        bonusScore = 0
        runnerY = 0f
        runnerVelocity = 0f
        isGrounded = true
        canDoubleJump = true
        isDoubleJumping = false
        isSliding = false
        slideTimer = 0f
        isWallJumping = false
        wallJumpTimer = 0f
        runAnimationPhase = 0f
        nextSpawnDistance = 480f
        isNewRecord = false
        gameState = RunnerGameState.PLAYING
    }

    // Palette harmonized with Secret Browser & Vault Dark/Light theme
    val bgColorStart = if (isDark) Color(0xFF090D16) else Color(0xFFF1F5F9)
    val bgColorEnd = if (isDark) Color(0xFF101726) else Color(0xFFE2E8F0)
    val groundColor = if (isDark) Color(0xFF151F33) else Color(0xFFCBD5E1)
    val groundLineColor = if (isDark) Color(0xFF00E5FF) else Color(0xFF0284C7)
    val playerSuitColor = if (isDark) Color(0xFFE2E8F0) else Color(0xFF1E293B)
    val playerVisorColor = Color(0xFFFF6A00) // Brand Orange Accent
    val shieldGlowColor = Color(0xFF00E5FF)
    val starColor = Color(0xFFFFD600)
    val coreColor = Color(0xFF10B981)
    val cardBg = if (isDark) Color(0xFF141E33) else Color(0xFFFFFFFF)
    val cardBorder = if (isDark) Color(0xFF243452) else Color(0xFFE2E8F0)
    val textP = if (isDark) Color(0xFFF8FAFC) else Color(0xFF0F172A)
    val textS = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)

    // Game loop running with 60FPS target
    LaunchedEffect(gameState) {
        if (gameState != RunnerGameState.PLAYING) return@LaunchedEffect

        var lastFrameTimeNanos = 0L

        while (isActive && gameState == RunnerGameState.PLAYING) {
            withFrameNanos { frameTimeNanos ->
                if (lastFrameTimeNanos == 0L) {
                    lastFrameTimeNanos = frameTimeNanos
                    return@withFrameNanos
                }

                val dt = ((frameTimeNanos - lastFrameTimeNanos) / 1_000_000_000f).coerceIn(0.001f, 0.04f)
                lastFrameTimeNanos = frameTimeNanos

                // Speed scale
                val currentSpeedMultiplier = 1f + (distanceScore / 650f).coerceAtMost(1.85f)
                val currentSpeed = baseSpeed * currentSpeedMultiplier

                speedLineAlpha = if (currentSpeedMultiplier > 1.35f) {
                    ((currentSpeedMultiplier - 1.35f) / 0.5f).coerceIn(0f, 0.35f)
                } else 0f

                // Advance distance score
                distanceScore += currentSpeed * dt * 0.1f
                val currentTotalScore = (distanceScore.toInt() + bonusScore)
                if (currentTotalScore > bestScore) {
                    bestScore = currentTotalScore
                    isNewRecord = true
                    prefs.edit().putInt("best_score", bestScore).apply()
                }

                // Slide Timer
                if (isSliding) {
                    slideTimer -= dt
                    if (slideTimer <= 0f) {
                        isSliding = false
                        slideTimer = 0f
                    }
                    if (isGrounded && Random.nextFloat() < 0.35f) {
                        spawnParticles(85f + Random.nextFloat() * 15f, 4f, 2, Color(0xFFFFB300), 0.7f)
                    }
                }

                // Wall Jump Timer
                if (isWallJumping) {
                    wallJumpTimer -= dt
                    if (wallJumpTimer <= 0f) {
                        isWallJumping = false
                    }
                }

                // Physics: Player Y & Velocity
                runnerVelocity += gravity * dt
                runnerY += runnerVelocity * dt
                if (runnerY <= 0f) {
                    val wasAirborne = !isGrounded
                    runnerY = 0f
                    runnerVelocity = 0f
                    isGrounded = true
                    canDoubleJump = true
                    isDoubleJumping = false
                    isWallJumping = false

                    if (wasAirborne) {
                        spawnParticles(100f, 4f, 5, Color(0xFF94A3B8), 0.6f)
                    }
                } else {
                    isGrounded = false
                }

                // Run leg stride animation
                if (isGrounded && !isSliding) {
                    runAnimationPhase += dt * (18f * currentSpeedMultiplier)
                }

                // Parallax offsets
                groundOffset = (groundOffset + currentSpeed * dt) % 40f
                midgroundOffset = (midgroundOffset + currentSpeed * 0.45f * dt) % 180f
                bgParallaxOffset = (bgParallaxOffset + currentSpeed * 0.18f * dt) % 240f

                // Spawning Obstacles & Collectibles
                nextSpawnDistance -= currentSpeed * dt
                if (nextSpawnDistance <= 0f) {
                    val spawnX = 960f
                    val roll = Random.nextFloat()

                    if (roll < 0.65f) {
                        // Spawn Obstacle
                        val typeChoice = when {
                            distanceScore < 80f -> if (Random.nextBoolean()) ObstacleType.LOW_VAULT_BOX else ObstacleType.CYBER_SPIRE
                            distanceScore < 250f -> ObstacleType.entries.random()
                            else -> ObstacleType.entries.random()
                        }

                        val (w, h, yOff) = when (typeChoice) {
                            ObstacleType.LOW_VAULT_BOX -> Triple(38f, 32f, 0f)
                            ObstacleType.HIGH_OVERHEAD_LASER -> Triple(52f, 24f, 38f) // Above ground -> must slide
                            ObstacleType.CYBER_SPIRE -> Triple(32f, 50f, 0f)
                            ObstacleType.WALL_STRUCTURE -> Triple(36f, 74f, 0f) // Tall wall -> wall jump or double jump
                            ObstacleType.SECURITY_LASER_GATE -> Triple(46f, 48f, 0f)
                        }

                        obstacles.add(
                            RunnerObstacle(
                                id = nextEntityId++,
                                x = spawnX,
                                yOffset = yOff,
                                width = w,
                                height = h,
                                type = typeChoice
                            )
                        )
                    } else {
                        // Spawn Collectible
                        val cRand = Random.nextFloat()
                        val (cType, pts, yPos) = when {
                            cRand < 0.50f -> Triple(CollectibleType.SHIELD_TOKEN, 25, if (Random.nextBoolean()) 20f else 65f)
                            cRand < 0.85f -> Triple(CollectibleType.PRIVACY_STAR, 50, if (Random.nextBoolean()) 35f else 85f)
                            else -> Triple(CollectibleType.ENCRYPTED_CORE, 100, 75f)
                        }
                        collectibles.add(
                            RunnerCollectible(
                                id = nextEntityId++,
                                x = spawnX,
                                y = yPos,
                                type = cType,
                                points = pts
                            )
                        )
                    }

                    // Next spawn interval between 340px and 580px
                    nextSpawnDistance = Random.nextFloat() * 240f + 340f
                }

                // Update Obstacles & Collision Check
                val playerX = 85f
                val playerWidth = 30f
                val playerHeight = if (isSliding) 22f else 48f
                val playerBottom = runnerY
                val playerTop = runnerY + playerHeight

                val obsIterator = obstacles.iterator()
                while (obsIterator.hasNext()) {
                    val obs = obsIterator.next()
                    obs.x -= currentSpeed * dt

                    val obsLeft = obs.x + 4f
                    val obsRight = obs.x + obs.width - 4f
                    val obsBottom = obs.yOffset
                    val obsTop = obs.yOffset + obs.height

                    // AABB Collision check
                    val isOverlapX = (playerX + playerWidth > obsLeft) && (playerX < obsRight)
                    val isOverlapY = (playerTop > obsBottom + 3f) && (playerBottom < obsTop - 3f)

                    if (isOverlapX && isOverlapY) {
                        gameState = RunnerGameState.GAME_OVER
                        spawnParticles(playerX + 15f, playerBottom + 20f, 25, Color(0xFFFF5252), 1.4f)
                        break
                    }

                    if (obs.x < -120f) {
                        obsIterator.remove()
                    }
                }

                // Update Collectibles
                val colIterator = collectibles.iterator()
                while (colIterator.hasNext()) {
                    val col = colIterator.next()
                    col.x -= currentSpeed * dt

                    if (!col.isCollected) {
                        val colCenterX = col.x + 14f
                        val colCenterY = col.y + 14f
                        val playerCenterX = playerX + playerWidth / 2f
                        val playerCenterY = playerBottom + playerHeight / 2f

                        val dx = abs(colCenterX - playerCenterX)
                        val dy = abs(colCenterY - playerCenterY)

                        if (dx < 26f && dy < 32f) {
                            col.isCollected = true
                            bonusScore += col.points
                            val colColor = when (col.type) {
                                CollectibleType.SHIELD_TOKEN -> shieldGlowColor
                                CollectibleType.PRIVACY_STAR -> starColor
                                CollectibleType.ENCRYPTED_CORE -> coreColor
                            }
                            spawnParticles(colCenterX, colCenterY, 12, colColor, 1.2f)
                            scorePopups.add(
                                ScorePopup(
                                    id = nextEntityId++,
                                    text = "+${col.points}",
                                    x = col.x,
                                    y = col.y + 20f,
                                    color = colColor
                                )
                            )
                        }
                    }

                    if (col.x < -100f || (col.isCollected && col.x < playerX - 40f)) {
                        colIterator.remove()
                    }
                }

                // Update Floating Popups
                val popupIterator = scorePopups.iterator()
                while (popupIterator.hasNext()) {
                    val popup = popupIterator.next()
                    popup.y += dt * 55f
                    popup.alpha -= dt * 1.6f
                    if (popup.alpha <= 0f) {
                        popupIterator.remove()
                    }
                }

                // Update Particle Pool
                val particleIterator = particles.iterator()
                while (particleIterator.hasNext()) {
                    val p = particleIterator.next()
                    p.age += dt
                    p.x += p.vx * dt
                    p.y += p.vy * dt
                    p.alpha = (1f - (p.age / p.maxLife)).coerceIn(0f, 1f)
                    if (p.age >= p.maxLife) {
                        particleIterator.remove()
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
                detectTapGestures { offset ->
                    // Tap right/upper 60% = Jump / Wall Jump, tap lower-left = Slide
                    if (offset.y > size.height * 0.70f && offset.x < size.width * 0.45f) {
                        slideAction()
                    } else {
                        jumpAction()
                    }
                }
            }
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = constraints.maxWidth.toFloat()
            val canvasHeight = constraints.maxHeight.toFloat()
            val groundY = canvasHeight * 0.73f

            // Game Graphics Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                // 1. Layer 1: Distant Cyber Skyline (Parallax Deep Background)
                drawCyberSkyline(
                    width = canvasWidth,
                    groundY = groundY,
                    offset = bgParallaxOffset,
                    isDark = isDark
                )

                // 2. Layer 2: Midground Facility Corridor (Pillars & Data Cables)
                drawMidgroundCorridor(
                    width = canvasWidth,
                    groundY = groundY,
                    offset = midgroundOffset,
                    isDark = isDark
                )

                // 3. Layer 3: Speed lines at high velocity
                if (speedLineAlpha > 0f) {
                    drawSpeedStreaks(
                        width = canvasWidth,
                        groundY = groundY,
                        alpha = speedLineAlpha,
                        isDark = isDark
                    )
                }

                // 4. Ground Foundation & Neon Power Conduits
                drawRect(
                    color = groundColor,
                    topLeft = Offset(0f, groundY),
                    size = Size(canvasWidth, canvasHeight - groundY)
                )
                // Glowing Cyberline
                drawLine(
                    color = groundLineColor,
                    start = Offset(0f, groundY),
                    end = Offset(canvasWidth, groundY),
                    strokeWidth = 3.5.dp.toPx()
                )
                // Sub-glow line
                drawLine(
                    color = groundLineColor.copy(alpha = 0.35f),
                    start = Offset(0f, groundY + 4f),
                    end = Offset(canvasWidth, groundY + 4f),
                    strokeWidth = 2.dp.toPx()
                )

                // Track circuit ticks
                var tickX = -groundOffset
                while (tickX < canvasWidth + 40f) {
                    if (tickX >= 0f) {
                        drawLine(
                            color = groundLineColor.copy(alpha = 0.45f),
                            start = Offset(tickX, groundY),
                            end = Offset(tickX + 14f, groundY + 16f),
                            strokeWidth = 1.5.dp.toPx()
                        )
                    }
                    tickX += 38f
                }

                // 5. Draw Obstacles
                obstacles.forEach { obs ->
                    drawParkourObstacle(
                        obstacle = obs,
                        groundY = groundY,
                        accentColor = playerVisorColor,
                        isDark = isDark
                    )
                }

                // 6. Draw Collectibles
                collectibles.forEach { col ->
                    if (!col.isCollected) {
                        drawParkourCollectible(
                            collectible = col,
                            groundY = groundY,
                            shieldColor = shieldGlowColor,
                            starColor = starColor,
                            coreColor = coreColor
                        )
                    }
                }

                // 7. Draw Active Particles
                particles.forEach { p ->
                    drawCircle(
                        color = p.color.copy(alpha = p.alpha),
                        radius = p.radius,
                        center = Offset(p.x, groundY - p.y)
                    )
                }

                // 8. Draw Secret Privacy Runner Mascot (Parkour Animation)
                drawParkourRunnerMascot(
                    playerX = 85f,
                    groundY = groundY,
                    runnerY = runnerY,
                    isGrounded = isGrounded,
                    isSliding = isSliding,
                    isDoubleJumping = isDoubleJumping,
                    isWallJumping = isWallJumping,
                    phase = runAnimationPhase,
                    suitColor = playerSuitColor,
                    visorColor = playerVisorColor,
                    shieldColor = shieldGlowColor,
                    isDark = isDark
                )
            }

            // Top HUD Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Return to Browser
                Surface(
                    onClick = onClose,
                    shape = RoundedCornerShape(12.dp),
                    color = cardBg.copy(alpha = 0.92f),
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
                        color = cardBg.copy(alpha = 0.88f),
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

            // On-Screen Parkour Touch Action Controls (Ergonomic, Transparent)
            if (gameState == RunnerGameState.PLAYING) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Slide Action Pill
                    Surface(
                        onClick = { slideAction() },
                        shape = RoundedCornerShape(16.dp),
                        color = (if (isSliding) playerVisorColor else cardBg).copy(alpha = 0.85f),
                        border = BorderStroke(1.5.dp, if (isSliding) playerVisorColor else cardBorder),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "👟 SLIDE",
                                color = if (isSliding) Color.White else textP,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    // Jump / Wall Leap Pill
                    Surface(
                        onClick = { jumpAction() },
                        shape = RoundedCornerShape(16.dp),
                        color = (if (!isGrounded) Color(0xFF00E5FF) else playerVisorColor).copy(alpha = 0.88f),
                        border = BorderStroke(1.5.dp, if (!isGrounded) Color(0xFF00E5FF) else playerVisorColor),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 22.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (!isGrounded && canDoubleJump) "⚡ DOUBLE JUMP" else "🚀 JUMP",
                                color = if (!isGrounded) Color(0xFF090D16) else Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }

            // Start Screen Overlay
            if (gameState == RunnerGameState.NOT_STARTED) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(26.dp),
                        color = cardBg.copy(alpha = 0.96f),
                        border = BorderStroke(1.5.dp, cardBorder),
                        shadowElevation = 10.dp,
                        modifier = Modifier.widthIn(max = 390.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(26.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(playerVisorColor.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "Secret Runner",
                                    tint = playerVisorColor,
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "SECRET RUNNER",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Black,
                                color = textP,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "PARKOUR ESCAPE",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = playerVisorColor,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "RUN. ESCAPE. STAY PRIVATE.",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textS,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Parkour Moves Guide Card
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isDark) Color(0xFF0F172A) else Color(0xFFF1F5F9),
                                border = BorderStroke(1.dp, cardBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "• Tap JUMP for jump & mid-air double jump",
                                        color = textP,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "• Tap JUMP near tall walls for a WALL LEAP",
                                        color = textP,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "• Tap SLIDE to duck under overhead lasers",
                                        color = textP,
                                        fontSize = 12.sp
                                    )
                                }
                            }

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
                                    text = "START ESCAPE",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Game Over Overlay
            if (gameState == RunnerGameState.GAME_OVER) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(26.dp),
                        color = cardBg,
                        border = BorderStroke(1.5.dp, cardBorder),
                        shadowElevation = 14.dp,
                        modifier = Modifier.widthIn(max = 380.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(26.dp),
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

// Draw Layer 1: Parallax Distant Cyber Skyline
private fun DrawScope.drawCyberSkyline(
    width: Float,
    groundY: Float,
    offset: Float,
    isDark: Boolean
) {
    val colColor = if (isDark) Color(0xFF101728) else Color(0xFFD8E1ED)
    val gridColor = if (isDark) Color(0xFF19253F).copy(alpha = 0.4f) else Color(0xFFC7D3E2).copy(alpha = 0.5f)

    val towerWidth = 48f
    val spacing = 80f
    var x = -offset
    var index = 0
    while (x < width + spacing) {
        val towerHeight = ((index * 43) % 120 + 45).toFloat()
        drawRect(
            color = colColor,
            topLeft = Offset(x, groundY - towerHeight),
            size = Size(towerWidth, towerHeight)
        )
        // Window rows
        drawLine(
            color = gridColor,
            start = Offset(x + 8f, groundY - towerHeight + 12f),
            end = Offset(x + towerWidth - 8f, groundY - towerHeight + 12f),
            strokeWidth = 2f
        )
        drawLine(
            color = gridColor,
            start = Offset(x + 8f, groundY - towerHeight + 24f),
            end = Offset(x + towerWidth - 8f, groundY - towerHeight + 24f),
            strokeWidth = 2f
        )
        x += spacing
        index++
    }
}

// Draw Layer 2: Midground Facility Corridor
private fun DrawScope.drawMidgroundCorridor(
    width: Float,
    groundY: Float,
    offset: Float,
    isDark: Boolean
) {
    val pillarColor = if (isDark) Color(0xFF16233B) else Color(0xFFCBD6E4)
    val beaconColor = if (isDark) Color(0xFF00E5FF).copy(alpha = 0.35f) else Color(0xFF0284C7).copy(alpha = 0.3f)

    val pWidth = 22f
    val spacing = 160f
    var x = -offset
    var idx = 0
    while (x < width + spacing) {
        val pH = 110f
        drawRect(
            color = pillarColor,
            topLeft = Offset(x, groundY - pH),
            size = Size(pWidth, pH)
        )
        // Neon beacon dot
        drawCircle(
            color = beaconColor,
            radius = 3.5f,
            center = Offset(x + pWidth / 2f, groundY - pH + 12f)
        )
        x += spacing
        idx++
    }
}

// Draw Layer 3: High speed streaks
private fun DrawScope.drawSpeedStreaks(
    width: Float,
    groundY: Float,
    alpha: Float,
    isDark: Boolean
) {
    val streakColor = (if (isDark) Color(0xFF00E5FF) else Color(0xFF0284C7)).copy(alpha = alpha)
    for (i in 0..4) {
        val y = groundY - 40f - (i * 35f)
        val startX = (i * 90f) % width
        val length = 120f + (i * 20f)
        drawLine(
            color = streakColor,
            start = Offset(startX, y),
            end = Offset(startX + length, y),
            strokeWidth = 1.5f
        )
    }
}

// Draw Parkour Obstacles
private fun DrawScope.drawParkourObstacle(
    obstacle: RunnerObstacle,
    groundY: Float,
    accentColor: Color,
    isDark: Boolean
) {
    val obsBottom = groundY - obstacle.yOffset
    val obsTop = obsBottom - obstacle.height
    val dangerColor = if (isDark) Color(0xFFFF5252) else Color(0xFFDC2626)

    when (obstacle.type) {
        ObstacleType.LOW_VAULT_BOX -> {
            // Low Vault Box / Crate
            drawRoundRect(
                color = dangerColor,
                topLeft = Offset(obstacle.x, obsTop),
                size = Size(obstacle.width, obstacle.height),
                cornerRadius = CornerRadius(6f, 6f)
            )
            // Top vault stripe
            drawRoundRect(
                color = accentColor,
                topLeft = Offset(obstacle.x + 3f, obsTop + 3f),
                size = Size(obstacle.width - 6f, 5f),
                cornerRadius = CornerRadius(2.5f, 2.5f)
            )
        }

        ObstacleType.HIGH_OVERHEAD_LASER -> {
            // Overhead laser beam (Requires Slide)
            val beamHeight = obstacle.height
            // Twin hanging emitters
            drawRect(
                color = if (isDark) Color(0xFF334155) else Color(0xFF64748B),
                topLeft = Offset(obstacle.x, obsTop),
                size = Size(6f, beamHeight)
            )
            drawRect(
                color = if (isDark) Color(0xFF334155) else Color(0xFF64748B),
                topLeft = Offset(obstacle.x + obstacle.width - 6f, obsTop),
                size = Size(6f, beamHeight)
            )
            // Glowing laser across
            drawLine(
                color = Color(0xFFFF1744),
                start = Offset(obstacle.x + 6f, obsTop + beamHeight / 2f),
                end = Offset(obstacle.x + obstacle.width - 6f, obsTop + beamHeight / 2f),
                strokeWidth = 5f
            )
            drawLine(
                color = Color(0xFFFF8A80),
                start = Offset(obstacle.x + 6f, obsTop + beamHeight / 2f),
                end = Offset(obstacle.x + obstacle.width - 6f, obsTop + beamHeight / 2f),
                strokeWidth = 2.5f
            )
            // Downward danger beam indicators
            drawCircle(
                color = Color(0xFFFF1744).copy(alpha = 0.4f),
                radius = 8f,
                center = Offset(obstacle.x + obstacle.width / 2f, obsTop + beamHeight / 2f)
            )
        }

        ObstacleType.CYBER_SPIRE -> {
            // Triangular Spire
            val path = Path().apply {
                moveTo(obstacle.x, obsBottom)
                lineTo(obstacle.x + obstacle.width * 0.5f, obsTop)
                lineTo(obstacle.x + obstacle.width, obsBottom)
                close()
            }
            drawPath(path, dangerColor)
            // Core accent line
            drawLine(
                color = Color.White.copy(alpha = 0.85f),
                start = Offset(obstacle.x + obstacle.width * 0.5f, obsTop + 6f),
                end = Offset(obstacle.x + obstacle.width * 0.5f, obsBottom - 4f),
                strokeWidth = 2.5f
            )
        }

        ObstacleType.WALL_STRUCTURE -> {
            // Tall Wall Structure (Suitable for Wall Jump)
            drawRoundRect(
                color = if (isDark) Color(0xFF1E293B) else Color(0xFF475569),
                topLeft = Offset(obstacle.x, obsTop),
                size = Size(obstacle.width, obstacle.height),
                cornerRadius = CornerRadius(4f, 4f)
            )
            // Neon grip ledge
            drawLine(
                color = accentColor,
                start = Offset(obstacle.x + 2f, obsTop + 14f),
                end = Offset(obstacle.x + obstacle.width - 2f, obsTop + 14f),
                strokeWidth = 3.5f
            )
            drawLine(
                color = accentColor,
                start = Offset(obstacle.x + 2f, obsTop + 36f),
                end = Offset(obstacle.x + obstacle.width - 2f, obsTop + 36f),
                strokeWidth = 3f
            )
        }

        ObstacleType.SECURITY_LASER_GATE -> {
            // Security Gate
            drawRoundRect(
                color = dangerColor,
                topLeft = Offset(obstacle.x, obsTop),
                size = Size(8f, obstacle.height),
                cornerRadius = CornerRadius(4f, 4f)
            )
            drawRoundRect(
                color = dangerColor,
                topLeft = Offset(obstacle.x + obstacle.width - 8f, obsTop),
                size = Size(8f, obstacle.height),
                cornerRadius = CornerRadius(4f, 4f)
            )
            drawLine(
                color = Color(0xFFFF1744),
                start = Offset(obstacle.x + 8f, obsTop + 12f),
                end = Offset(obstacle.x + obstacle.width - 8f, obsTop + 12f),
                strokeWidth = 4f
            )
            drawLine(
                color = Color(0xFFFF1744),
                start = Offset(obstacle.x + 8f, obsTop + 28f),
                end = Offset(obstacle.x + obstacle.width - 8f, obsTop + 28f),
                strokeWidth = 3f
            )
        }
    }
}

// Draw Collectibles
private fun DrawScope.drawParkourCollectible(
    collectible: RunnerCollectible,
    groundY: Float,
    shieldColor: Color,
    starColor: Color,
    coreColor: Color
) {
    val center = Offset(collectible.x + 14f, groundY - collectible.y - 14f)

    when (collectible.type) {
        CollectibleType.SHIELD_TOKEN -> {
            drawCircle(
                color = shieldColor.copy(alpha = 0.25f),
                radius = 16f,
                center = center
            )
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
            drawCircle(
                color = starColor.copy(alpha = 0.3f),
                radius = 16f,
                center = center
            )
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

        CollectibleType.ENCRYPTED_CORE -> {
            drawCircle(
                color = coreColor.copy(alpha = 0.3f),
                radius = 16f,
                center = center
            )
            // Diamond polygon
            val path = Path().apply {
                moveTo(center.x, center.y - 11f)
                lineTo(center.x + 11f, center.y)
                lineTo(center.x, center.y + 11f)
                lineTo(center.x - 11f, center.y)
                close()
            }
            drawPath(path, coreColor)
            drawCircle(color = Color.White, radius = 3f, center = center)
        }
    }
}

// Draw Mascot with Parkour Animation Poses
private fun DrawScope.drawParkourRunnerMascot(
    playerX: Float,
    groundY: Float,
    runnerY: Float,
    isGrounded: Boolean,
    isSliding: Boolean,
    isDoubleJumping: Boolean,
    isWallJumping: Boolean,
    phase: Float,
    suitColor: Color,
    visorColor: Color,
    shieldColor: Color,
    isDark: Boolean
) {
    if (isSliding) {
        // SLIDE POSE: Low, elongated horizontal silhouette with friction sparks
        val slideTop = groundY - 20f

        // Horizontal body
        drawRoundRect(
            color = suitColor,
            topLeft = Offset(playerX - 6f, slideTop + 4f),
            size = Size(38f, 15f),
            cornerRadius = CornerRadius(6f, 6f)
        )
        // Head tilted forward
        drawCircle(
            color = suitColor,
            radius = 8f,
            center = Offset(playerX + 34f, slideTop + 10f)
        )
        // Visor
        drawRoundRect(
            color = visorColor,
            topLeft = Offset(playerX + 34f, slideTop + 8f),
            size = Size(8f, 4f),
            cornerRadius = CornerRadius(2f, 2f)
        )
        // Trailing leg line
        drawLine(
            color = suitColor,
            start = Offset(playerX - 6f, slideTop + 12f),
            end = Offset(playerX - 16f, slideTop + 18f),
            strokeWidth = 4f
        )
        return
    }

    val playerY = groundY - runnerY - 48f
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

    // 3. Cyber Visor
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

    // 5. Double Jump Energy Ring
    if (isDoubleJumping && !isGrounded) {
        drawCircle(
            color = shieldColor.copy(alpha = 0.45f),
            radius = 18f,
            center = Offset(bodyCenterX, bodyCenterY),
            style = Stroke(width = 2f)
        )
    }

    // 6. Legs / Motion Stride
    if (isGrounded) {
        val legSwing = sin(phase) * 12f

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
    } else if (isWallJumping) {
        // Wall Jump Acrobatic Kick Pose
        drawLine(
            color = suitColor,
            start = Offset(playerX + 11f, playerY + 32f),
            end = Offset(playerX + 24f, playerY + 44f),
            strokeWidth = 4f
        )
        drawLine(
            color = suitColor,
            start = Offset(playerX + 18f, playerY + 32f),
            end = Offset(playerX + 6f, playerY + 42f),
            strokeWidth = 3.5f
        )
    } else {
        // Airborne Tucked Jump Pose
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
