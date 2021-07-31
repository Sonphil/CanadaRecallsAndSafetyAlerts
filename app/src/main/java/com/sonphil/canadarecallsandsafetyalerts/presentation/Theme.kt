package com.sonphil.canadarecallsandsafetyalerts.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Created by Sonphil on 30-07-21.
 */

private val DarkColorPalette = darkColors(
    primary = Color(0xFFD31A1A),
    secondary = Color(0xFF812222),
)

private val LightColorPalette = lightColors(
    primary = Color(0xFFD31A1A),
    secondary = Color(0xFF812222),
    surface = Color(0xFFFAFAFA)
)

@get:Composable
val Colors.onBackgroundSecondary: Color
    get() = if (isLight) {
        Color(0xFF646464)
    } else {
        Color(0xFFABABAB)
    }

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}
