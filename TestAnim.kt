import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable

@Composable
fun Test() {
    AnimatedContent(
        targetState = "1",
        transitionSpec = { fadeIn(tween(100)) togetherWith fadeOut(tween(100)) },
        label = ""
    ) { _ -> }
}
