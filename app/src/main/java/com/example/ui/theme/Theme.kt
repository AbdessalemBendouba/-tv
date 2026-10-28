package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val MidnightColorScheme = darkColorScheme(
    primary = SkyBlue,
    onPrimary = MidnightNavy,
    primaryContainer = ElevatedNavyCard,
    onPrimaryContainer = TextPrimary,
    secondary = EmeraldGreen,
    onSecondary = MidnightNavy,
    secondaryContainer = DarkNavyCard,
    onSecondaryContainer = TextGold,
    tertiary = CelestialGold,
    background = MidnightNavy,
    onBackground = TextPrimary,
    surface = DarkNavyCard,
    onSurface = TextPrimary,
    surfaceVariant = ElevatedNavyCard,
    onSurfaceVariant = TextSecondary,
    outline = BorderNavy
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldGreen,
    onPrimary = Color.White,
    primaryContainer = LightElevatedCard,
    onPrimaryContainer = LightTextPrimary,
    secondary = SkyBlue,
    onSecondary = Color.White,
    secondaryContainer = LightCard,
    onSecondaryContainer = LightTextPrimary,
    tertiary = CelestialGold,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightCard,
    onSurface = LightTextPrimary,
    surfaceVariant = LightElevatedCard,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) MidnightColorScheme else LightColorScheme
    val appColors = if (darkTheme) DarkAppColors else LightAppColors

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
