package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

enum class AppTheme(val id: String, val displayName: String, val flag: String) {
    CLASSIC_LAVENDER("classic_lavender", "Classic Neon Black", "⚡"),
    SUNSET_ROSE("sunset_rose", "Sunset Rose", "🌅"),
    NORDIC_EMERALD("nordic_emerald", "Nordic Emerald", "🌲"),
    OCEAN_BREEZE("ocean_breeze", "Ocean Breeze", "🌊")
}

data class AppThemeColors(
    val brandBg: Color,
    val textDark: Color,
    val textMedium: Color,
    val themePurple: Color,
    val themeLightPurple: Color,
    val themeContainerBorder: Color,
    val keypadBg: Color,
    val digitBg: Color
)

val ClassicLavenderColors = AppThemeColors(
    brandBg = Color(0xFF030304),
    textDark = Color(0xFFF5F5FA),
    textMedium = Color(0xFF8B8B9A),
    themePurple = Color(0xFFC400FF),
    themeLightPurple = Color(0xFF230D35),
    themeContainerBorder = Color(0xFF1F1F26),
    keypadBg = Color(0xFF0C0C10),
    digitBg = Color(0xFF13131A)
)

val SunsetRoseColors = AppThemeColors(
    brandBg = Color(0xFFFFF7F6),
    textDark = Color(0xFF3E2723),
    textMedium = Color(0xFF795548),
    themePurple = Color(0xFFD84315),
    themeLightPurple = Color(0xFFFFE0B2),
    themeContainerBorder = Color(0xFFFFB74D),
    keypadBg = Color(0xFFFFF3E0),
    digitBg = Color(0xFFFFFFFF)
)

val NordicEmeraldColors = AppThemeColors(
    brandBg = Color(0xFFF4F9F6),
    textDark = Color(0xFF1B2E24),
    textMedium = Color(0xFF4E6B5A),
    themePurple = Color(0xFF00796B),
    themeLightPurple = Color(0xFFE0F2F1),
    themeContainerBorder = Color(0xFFB2DFDB),
    keypadBg = Color(0xFFE8F5E9),
    digitBg = Color(0xFFFFFFFF)
)

val OceanBreezeColors = AppThemeColors(
    brandBg = Color(0xFFF0F4F8),
    textDark = Color(0xFF102A43),
    textMedium = Color(0xFF486581),
    themePurple = Color(0xFF0F60FF),
    themeLightPurple = Color(0xFFDCEFFC),
    themeContainerBorder = Color(0xFFBAC7E8),
    keypadBg = Color(0xFFF0F4FA),
    digitBg = Color(0xFFFFFFFF)
)

val LocalAppThemeColors = androidx.compose.runtime.staticCompositionLocalOf { ClassicLavenderColors }

@Composable
fun MyApplicationTheme(
  theme: AppTheme = AppTheme.CLASSIC_LAVENDER,
  content: @Composable () -> Unit,
) {
  val colors = when (theme) {
    AppTheme.CLASSIC_LAVENDER -> ClassicLavenderColors
    AppTheme.SUNSET_ROSE -> SunsetRoseColors
    AppTheme.NORDIC_EMERALD -> NordicEmeraldColors
    AppTheme.OCEAN_BREEZE -> OceanBreezeColors
  }

  val colorScheme = lightColorScheme(
    primary = colors.themePurple,
    secondary = colors.themeLightPurple,
    tertiary = colors.themeContainerBorder,
    background = colors.brandBg,
    surface = colors.brandBg
  )

  androidx.compose.runtime.CompositionLocalProvider(LocalAppThemeColors provides colors) {
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
  }
}

