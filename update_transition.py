import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

target = """    // Switch to the private vault screen automatically when unlocked via passcode
    var transitionState by remember { mutableStateOf(0) } // 0=Calc, 1=Blurring, 2=Vault
    
    val blurRadius by animateDpAsState(
        targetValue = if (transitionState > 0) 40.dp else 0.dp,
        animationSpec = tween(400)
    )
    val calcAlpha by animateFloatAsState(
        targetValue = if (transitionState == 2) 0f else 1f,
        animationSpec = tween(400)
    )
    val vaultScale by animateFloatAsState(
        targetValue = if (transitionState == 2) 1f else 0.9f,
        animationSpec = tween(500, easing = FastOutSlowInEasing)
    )
    val vaultAlpha by animateFloatAsState(
        targetValue = if (transitionState == 2) 1f else 0f,
        animationSpec = tween(400)
    )

    LaunchedEffect(vaultUnlocked) {
        if (vaultUnlocked) {
            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
            transitionState = 1
            delay(200) // Start fading in vault slightly after blur starts
            transitionState = 2
            delay(300)
            activeTab = ActiveTab.VAULT
        } else {
            transitionState = 0
            activeTab = ActiveTab.CALCULATOR
        }
    }"""

replacement = """    // Switch to the private vault screen automatically when unlocked via passcode
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
    val vaultAlpha by animateFloatAsState(
        targetValue = if (transitionState >= 2) 1f else 0f,
        animationSpec = tween(300)
    )
    
    val authOverlayAlpha by animateFloatAsState(
        targetValue = if (transitionState == 1) 1f else 0f,
        animationSpec = tween(150)
    )

    LaunchedEffect(vaultUnlocked) {
        if (vaultUnlocked) {
            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
            transitionState = 1 // Authenticating
            delay(300)
            transitionState = 2 // Transition
            delay(300)
            transitionState = 3 // Vault fully emerges
            activeTab = ActiveTab.VAULT
        } else {
            transitionState = 0
            activeTab = ActiveTab.CALCULATOR
        }
    }"""

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)

