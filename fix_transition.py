import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

target = """                // Section Contents with Crossfade Animation
            androidx.compose.animation.Crossfade(
                targetState = activeSection,
                animationSpec = androidx.compose.animation.core.tween(300),
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) { section ->"""

replacement = """                // Section Contents with Fluid Transition
            androidx.compose.animation.AnimatedContent(
                targetState = activeSection,
                transitionSpec = {
                    (androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(300, easing = androidx.compose.animation.core.LinearOutSlowInEasing)) + 
                     androidx.compose.animation.scaleIn(initialScale = 0.95f, animationSpec = androidx.compose.animation.core.tween(300, easing = androidx.compose.animation.core.LinearOutSlowInEasing)))
                    .togetherWith(
                     androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(200)) + 
                     androidx.compose.animation.scaleOut(targetScale = 1.05f, animationSpec = androidx.compose.animation.core.tween(200)))
                },
                modifier = Modifier.weight(1f).fillMaxWidth(),
                label = "VaultSection"
            ) { section ->"""

if target in content:
    content = content.replace(target, replacement)
    with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
        f.write(content)
    print("Transition replaced")
else:
    print("Transition target not found")
