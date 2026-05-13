package com.example.sromeromusicapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary   = PurplePrimary,
    secondary = PurpleLight,
    background = PurpleLight,
    surface   = CardWhite,
    onPrimary = CardWhite,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
)

@Composable
fun SRomeroMusicAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography  = Typography,
        content     = content
    )
}