package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val FarasLightColorScheme = lightColorScheme(
    primary = FarasNeonGreen,
    onPrimary = FarasWhite,
    primaryContainer = FarasNeonGreenLight,
    onPrimaryContainer = FarasNeonGreenDark,
    secondary = FarasNeonGreenDark,
    onSecondary = FarasWhite,
    tertiary = FarasAmber,
    onTertiary = FarasWhite,
    background = FarasLightGreyBg,
    onBackground = FarasTextPrimary,
    surface = FarasWhite,
    onSurface = FarasTextPrimary,
    surfaceVariant = FarasSurfaceVariant,
    onSurfaceVariant = FarasTextSecondary,
    outline = FarasCardBorder,
    error = FarasRedAlert,
    onError = FarasWhite
)

@Composable
fun FarascooterTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FarasLightColorScheme,
        typography = AppTypography,
        content = content
    )
}
