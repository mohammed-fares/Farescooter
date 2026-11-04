package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FarasDarkColorScheme = darkColorScheme(
    primary = FarasNeonGreen,
    onPrimary = FarasBlack,
    primaryContainer = FarasNeonGreenDark,
    onPrimaryContainer = FarasWhite,
    secondary = FarasNeonGreen,
    onSecondary = FarasBlack,
    tertiary = FarasAmber,
    onTertiary = FarasBlack,
    background = FarasBlack,
    onBackground = FarasWhite,
    surface = FarasSurfaceDark,
    onSurface = FarasWhite,
    surfaceVariant = FarasCardBg,
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
        colorScheme = FarasDarkColorScheme,
        typography = AppTypography,
        content = content
    )
}
