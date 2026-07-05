import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

target = """    val vaultAlpha by animateFloatAsState(
        targetValue = if (transitionState >= 2) 1f else 0f,
        animationSpec = tween(300)
    )
    
    val authOverlayAlpha by animateFloatAsState(
        targetValue = if (transitionState == 1) 1f else 0f,
        animationSpec = tween(150)
    )"""

replacement = """    val welcomeAlpha by animateFloatAsState(
        targetValue = if (transitionState == 2) 1f else 0f,
        animationSpec = tween(500)
    )
    
    val vaultAlpha by animateFloatAsState(
        targetValue = if (transitionState >= 3) 1f else 0f,
        animationSpec = tween(500)
    )
    
    val authOverlayAlpha by animateFloatAsState(
        targetValue = if (transitionState == 1) 1f else 0f,
        animationSpec = tween(300)
    )"""

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
