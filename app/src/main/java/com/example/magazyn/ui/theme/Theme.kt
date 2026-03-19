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

@Composable
fun MagazynTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // MUSI BYĆ FALSE
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme, // Zawsze używamy naszej palety
        typography = Typography,
        content = content
    )
}