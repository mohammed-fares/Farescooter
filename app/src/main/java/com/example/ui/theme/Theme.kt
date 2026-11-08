package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun FarascooterTheme(
    primaryColor: Color = FarasNeonGreen,
    backgroundColor: Color = FarasLightGreyBg,
    typography: Typography = AppTypography,
    content: @Composable () -> Unit
) {
    val dynamicColorScheme = lightColorScheme(
        primary = primaryColor,
        onPrimary = FarasWhite,
        primaryContainer = primaryColor.copy(alpha = 0.12f),
        onPrimaryContainer = primaryColor,
        secondary = primaryColor,
        onSecondary = FarasWhite,
        tertiary = FarasAmber,
        onTertiary = FarasWhite,
        background = backgroundColor,
        onBackground = FarasTextPrimary,
        surface = FarasWhite,
        onSurface = FarasTextPrimary,
        surfaceVariant = FarasSurfaceVariant,
        onSurfaceVariant = FarasTextSecondary,
        outline = FarasCardBorder,
        error = FarasRedAlert,
        onError = FarasWhite
    )

    MaterialTheme(
        colorScheme = dynamicColorScheme,
        typography = typography,
        content = content
    )
}
