package com.pasteleria_app.pasteleria_app.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🎨 Paleta de colores Pastelería Mil Sabores
private val Crema = Color(0xFFF9E8D9)
private val Marron = Color(0xFF8B5E3C)
private val MarronClaro = Color(0xFFB98C66)
private val Blanco = Color(0xFFFFFBF8)

private val LightColors = lightColorScheme(
    primary = Marron,
    onPrimary = Color.White,
    secondary = MarronClaro,
    onSecondary = Color.White,
    background = Crema,
    onBackground = Marron,
    surface = Blanco,
    onSurface = Marron
)

private val DarkColors = darkColorScheme(
    primary = MarronClaro,
    onPrimary = Color.Black,
    background = Color(0xFF3B2C24),
    onBackground = Crema,
    surface = Color(0xFF4A382D),
    onSurface = Crema
)

@Composable
fun Pasteleria_APPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}
