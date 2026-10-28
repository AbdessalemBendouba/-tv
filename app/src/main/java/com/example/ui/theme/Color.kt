package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ==========================================
// MODULAR DESIGN SYSTEM - APP COLOR TOKENS
// ==========================================
data class AppColors(
    val isDark: Boolean,
    val screenBackground: Color,
    val cardBackground: Color,
    val cardElevatedBackground: Color,
    val cardBorder: Color,
    val buttonBackground: Color,
    val buttonBackgroundFocused: Color,
    val buttonText: Color,
    val primaryAccent: Color,      // Emerald Green
    val secondaryAccent: Color,    // Sky Blue
    val goldAccent: Color,         // Celestial Gold
    val orangeAccent: Color,       // Sunset Orange (Iqama)
    val redAccent: Color,          // Crimson Red (Destructive)
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val textGold: Color,
    val focusGlow: Color,
    val bottomBarBackground: Color
)

val DarkAppColors = AppColors(
    isDark = true,
    screenBackground = Color(0xFF081018),
    cardBackground = Color(0xFF0E1A26),
    cardElevatedBackground = Color(0xFF152638),
    cardBorder = Color(0xFF1E354D),
    buttonBackground = Color(0xFF152638),
    buttonBackgroundFocused = Color(0xFFF59E0B),
    buttonText = Color(0xFFF8FAFC),
    primaryAccent = Color(0xFF10B981),    // Emerald
    secondaryAccent = Color(0xFF38BDF8),  // Sky Blue
    goldAccent = Color(0xFFFBBF24),       // Celestial Gold
    orangeAccent = Color(0xFFF97316),     // Sunset Orange
    redAccent = Color(0xFFEF4444),        // Crimson Red
    textPrimary = Color(0xFFF8FAFC),
    textSecondary = Color(0xFF94A3B8),
    textMuted = Color(0xFF64748B),
    textGold = Color(0xFFFCD34D),
    focusGlow = Color(0xFFF59E0B),
    bottomBarBackground = Color(0xFA081018)
)

val LightAppColors = AppColors(
    isDark = false,
    screenBackground = Color(0xFFF1F5F9),
    cardBackground = Color(0xFFFFFFFF),
    cardElevatedBackground = Color(0xFFF8FAFC),
    cardBorder = Color(0xFFE2E8F0),
    buttonBackground = Color(0xFFE2E8F0),
    buttonBackgroundFocused = Color(0xFFF59E0B),
    buttonText = Color(0xFF0F172A),
    primaryAccent = Color(0xFF059669),    // Deep Emerald
    secondaryAccent = Color(0xFF0284C7),  // Sky Blue
    goldAccent = Color(0xFFD97706),       // Deep Gold
    orangeAccent = Color(0xFFEA580C),     // Orange
    redAccent = Color(0xFFDC2626),        // Red
    textPrimary = Color(0xFF0F172A),
    textSecondary = Color(0xFF475569),
    textMuted = Color(0xFF94A3B8),
    textGold = Color(0xFFB45309),
    focusGlow = Color(0xFFF59E0B),
    bottomBarBackground = Color(0xFAF8FAFC)
)

val LocalAppColors = staticCompositionLocalOf { DarkAppColors }

// Classic aliases for backward compatibility
val EmeraldGreen = Color(0xFF10B981)
val SkyBlue = Color(0xFF38BDF8)
val CelestialGold = Color(0xFFFBBF24)
val SunsetOrange = Color(0xFFF97316)
val CrimsonRed = Color(0xFFEF4444)
val MidnightNavy = Color(0xFF081018)
val DarkNavyCard = Color(0xFF0E1A26)
val ElevatedNavyCard = Color(0xFF152638)
val BorderNavy = Color(0xFF1E354D)
val FocusedBorderColor = Color(0xFFF59E0B)
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)
val TextGold = Color(0xFFFCD34D)
val LightBackground = Color(0xFFF1F5F9)
val LightCard = Color(0xFFFFFFFF)
val LightElevatedCard = Color(0xFFF8FAFC)
val LightBorder = Color(0xFFCBD5E1)
val LightTextPrimary = Color(0xFF0F172A)
val LightTextSecondary = Color(0xFF475569)
