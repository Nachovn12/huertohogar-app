package com.huertohogar.huertohogar_app.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimary,
    secondary = HuertoGreen,
    tertiary = DarkGreen,
    background = Color(0xFF222A22),
    surface = Color(0xFF223122),
    onPrimary = PureWhite,
    onSecondary = PureWhite,
    onTertiary = PureWhite,
    onBackground = PureWhite,
    onSurface = PureWhite,
    error = ActionRed,
    onError = PureWhite
)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    secondary = HuertoGreen,
    tertiary = DarkGreen,
    background = GrayCard,
    surface = PureWhite,
    onPrimary = PureWhite,
    onSecondary = PureWhite,
    onTertiary = PureWhite,
    onBackground = Color(0xFF1B1C1E),
    onSurface = Color(0xFF1B1C1E),
    error = ActionRed,
    onError = PureWhite
)

@Composable
fun HuertoHogar_AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}