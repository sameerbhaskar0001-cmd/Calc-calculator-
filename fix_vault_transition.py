import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

# 1. Update SecretVaultUnlockingAnimation signature and LaunchedEffect
target_animation_def = """fun SecretVaultUnlockingAnimation(
    viewModel: CalculatorViewModel,
    onAnimationComplete: () -> Unit
) {
    val view = androidx.compose.ui.platform.LocalView.current
    val progress = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(Unit) {
        try { view.performHapticFeedback(android.view.HapticFeedbackConstants.CONFIRM) } catch (e: Exception) { }
        
        progress.animateTo(
            targetValue = 1f,
            animationSpec = androidx.compose.animation.core.tween(
                durationMillis = 1600, 
                easing = androidx.compose.animation.core.FastOutSlowInEasing
            )
        )
        onAnimationComplete()
    }"""

replacement_animation_def = """fun SecretVaultUnlockingAnimation(
    viewModel: CalculatorViewModel,
    onPreloadVault: () -> Unit = {},
    onAnimationComplete: () -> Unit
) {
    val view = androidx.compose.ui.platform.LocalView.current
    val progress = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(Unit) {
        try { view.performHapticFeedback(android.view.HapticFeedbackConstants.CONFIRM) } catch (e: Exception) { }
        
        kotlinx.coroutines.launch {
            kotlinx.coroutines.delay(1000)
            onPreloadVault()
        }
        
        progress.animateTo(
            targetValue = 1f,
            animationSpec = androidx.compose.animation.core.tween(
                durationMillis = 1600, 
                easing = androidx.compose.animation.core.FastOutSlowInEasing
            )
        )
        onAnimationComplete()
    }"""
content = content.replace(target_animation_def, replacement_animation_def)

# 2. Update the calling site in Main View Area
target_call = """            // 1. Dashboard Content when unlocked
            if (activeTab == ActiveTab.VAULT || transitionState == 3) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(0f)
                ) {
                    VaultTabUnlockedContent(
                        viewModel = viewModel,
                        onLockExit = { activeTab = ActiveTab.CALCULATOR }
                    )
                }
            }

            // 2. Secret Vault Unlocking Animation
            if (transitionState == 1) {
                Box(modifier = Modifier.zIndex(2f)) {
                    SecretVaultUnlockingAnimation(
                        viewModel = viewModel,
                        onAnimationComplete = {
                            activeTab = ActiveTab.VAULT
                            transitionState = 3
                        }
                    )
                }
            }"""

replacement_call = """            // 1. Dashboard Content when unlocked
            val vaultAlpha by animateFloatAsState(
                targetValue = if (transitionState >= 2) 1f else 0f,
                animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
            )

            if (activeTab == ActiveTab.VAULT || transitionState >= 2) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(0f)
                        .graphicsLayer {
                            alpha = if (transitionState == 3 || transitionState == 0 || activeTab == ActiveTab.VAULT) 1f else vaultAlpha
                        }
                ) {
                    VaultTabUnlockedContent(
                        viewModel = viewModel,
                        onLockExit = { activeTab = ActiveTab.CALCULATOR }
                    )
                }
            }

            // 2. Secret Vault Unlocking Animation
            if (transitionState == 1 || transitionState == 2) {
                Box(modifier = Modifier.zIndex(2f)) {
                    SecretVaultUnlockingAnimation(
                        viewModel = viewModel,
                        onPreloadVault = { transitionState = 2 },
                        onAnimationComplete = {
                            activeTab = ActiveTab.VAULT
                            transitionState = 3
                        }
                    )
                }
            }"""
content = content.replace(target_call, replacement_call)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
print("Updated animation transitions")
