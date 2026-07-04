import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

# We need to find the LaunchedEffect(vaultUnlocked) and replace it
# Also wrap the Box containing CalculatorTabContent and VaultTabUnlockedContent

# 1. Add transition state to CalculatorScreen
transition_imports = """
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import kotlinx.coroutines.delay
"""

if "import androidx.compose.animation.core.animateDpAsState" not in content:
    content = content.replace("import androidx.compose.runtime.Composable", transition_imports + "\nimport androidx.compose.runtime.Composable")

# Replace LaunchedEffect
old_launched_effect = """    LaunchedEffect(vaultUnlocked) {
        if (vaultUnlocked) {
            activeTab = ActiveTab.VAULT
        } else {
            activeTab = ActiveTab.CALCULATOR
        }
    }"""

new_launched_effect = """    var transitionState by remember { mutableStateOf(0) } // 0=Calc, 1=Blurring, 2=Vault
    
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

content = content.replace(old_launched_effect, new_launched_effect)

# Replace Main View Area Box
old_main_box_start = """            // Main View Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = if (activeTab == ActiveTab.VAULT) 0.dp else 24.dp)
            ) {"""

# We need to change the padding logic to animate or handle it differently.
# If vault is showing, no padding. If calc, 24.dp.
new_main_box_start = """            // Main View Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {"""

content = content.replace(old_main_box_start, new_main_box_start)

# Now find the when(activeTab) inside the Main View Area
old_when = """                when (activeTab) {
                    ActiveTab.CALCULATOR -> {
                        CalculatorTabContent(viewModel = viewModel)
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
                }"""

new_when = """                // We render Calculator if it's not fully transitioned to Vault
                if (transitionState < 2 || activeTab == ActiveTab.CALCULATOR) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .blur(blurRadius)
                        .alpha(calcAlpha)
                    ) {
                        CalculatorTabContent(viewModel = viewModel)
                    }
                }
                
                // We render Vault if transition has started or is complete
                if (transitionState > 0 || activeTab == ActiveTab.VAULT) {
                    val isVaultUnlocked by viewModel.vaultUnlocked.collectAsState()
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .scale(if (isVaultUnlocked) vaultScale else 1f)
                        .alpha(if (isVaultUnlocked) vaultAlpha else 1f)
                    ) {
                        if (!isVaultUnlocked) {
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
                }"""

content = content.replace(old_when, new_when)

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)

