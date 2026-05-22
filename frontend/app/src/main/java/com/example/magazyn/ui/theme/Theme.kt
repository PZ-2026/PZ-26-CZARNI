package com.example.magazyn.ui.theme

import android.app.Activity
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

private val LightColorScheme = lightColorScheme(
    primary = DeepBurgundy,
    background = SoftPinkBG,
    surface = CardWhite,
    secondaryContainer = MutedRose, // Kolor nagłówka "Panel Zaopatrzeniowca"
    onSecondaryContainer = DarkText,
    onBackground = DarkText,
    onSurface = DarkText
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB71C1C),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    secondaryContainer = Color(0xFF4A148C),
    onSecondaryContainer = Color(0xFFE1BEE7),
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0)
)

@Composable
fun MagazynTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // MUSI BYĆ FALSE
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}