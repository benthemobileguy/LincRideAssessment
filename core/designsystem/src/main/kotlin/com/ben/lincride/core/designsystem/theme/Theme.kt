package com.ben.lincride.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = LincGreen,
    secondary = LincBlue,
    tertiary = EmergencyBlue,
    background = TextPrimary,
    surface = TextSecondary,
    onPrimary = BackgroundWhite,
    onSecondary = BackgroundWhite,
    onBackground = BackgroundWhite,
    onSurface = BackgroundWhite
)

private val LightColorScheme = lightColorScheme(
    primary = LincGreen,
    secondary = LincBlue,
    tertiary = EmergencyBlue,
    background = BackgroundWhite,
    surface = SurfaceGray,
    onPrimary = BackgroundWhite,
    onSecondary = BackgroundWhite,
    onTertiary = BackgroundWhite,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = BorderGray
)

@Composable
fun LincRideTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = LincRideTypography,
        content = content
    )
}