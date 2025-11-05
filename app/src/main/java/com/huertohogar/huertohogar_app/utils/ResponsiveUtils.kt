package com.huertohogar.huertohogar_app.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Sistema de diseño responsivo basado en porcentajes del ancho de pantalla
 * Implementé esto para que la app se vea bien en cualquier dispositivo
 */

/**
 * Data class con todas las dimensiones responsivas
 * Calculadas como porcentajes del tamaño de pantalla con límites min/max
 */
data class ResponsiveDimensions(
    val screenWidth: Dp,
    val screenHeight: Dp,
    
    // Paddings (4%, 6%, 8%, 10% del ancho con límites)
    val paddingSmall: Dp,
    val paddingMedium: Dp,
    val paddingLarge: Dp,
    val paddingExtraLarge: Dp,
    
    // Tamaños de texto (2.8% a 6.5% del ancho con límites)
    val textTiny: TextUnit,
    val textSmall: TextUnit,
    val textMedium: TextUnit,
    val textLarge: TextUnit,
    val textExtraLarge: TextUnit,
    val textTitle: TextUnit,
    
    // Tamaños de iconos (6%, 8%, 10% del ancho)
    val iconSmall: Dp,
    val iconMedium: Dp,
    val iconLarge: Dp,
    
    // Tamaños de botones (12%, 14% del ancho)
    val buttonHeight: Dp,
    val buttonHeightSmall: Dp,
    
    // Tamaños de imágenes (20%, 30%, 40% del ancho)
    val imageSmall: Dp,
    val imageMedium: Dp,
    val imageLarge: Dp,
    
    // Espaciados (2%, 3%, 4%, 6% del ancho)
    val spacingTiny: Dp,
    val spacingSmall: Dp,
    val spacingMedium: Dp,
    val spacingLarge: Dp,
    
    // Bordes redondeados (2%, 3%, 8% del ancho)
    val cornerRadiusSmall: Dp,
    val cornerRadiusMedium: Dp,
    val cornerRadiusLarge: Dp
)

/**
 * Función principal que calcula todas las dimensiones responsivas
 * Uso coerceIn para establecer valores mínimos y máximos
 */
@Composable
fun getResponsiveDimensions(): ResponsiveDimensions {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    
    return ResponsiveDimensions(
        screenWidth = screenWidth,
        screenHeight = screenHeight,
        
        // Paddings calculados como % del ancho
        paddingSmall = (screenWidth * 0.04f).coerceIn(12.dp, 16.dp),
        paddingMedium = (screenWidth * 0.06f).coerceIn(16.dp, 24.dp),
        paddingLarge = (screenWidth * 0.08f).coerceIn(24.dp, 32.dp),
        paddingExtraLarge = (screenWidth * 0.10f).coerceIn(32.dp, 40.dp),
        
        // Textos - importante mantener legibilidad
        textTiny = (screenWidth.value * 0.028f).coerceIn(10f, 11f).sp,
        textSmall = (screenWidth.value * 0.035f).coerceIn(12f, 14f).sp,
        textMedium = (screenWidth.value * 0.042f).coerceIn(14f, 16f).sp,
        textLarge = (screenWidth.value * 0.047f).coerceIn(16f, 18f).sp,
        textExtraLarge = (screenWidth.value * 0.055f).coerceIn(18f, 22f).sp,
        textTitle = (screenWidth.value * 0.065f).coerceIn(22f, 28f).sp,
        
        // Iconos
        iconSmall = (screenWidth * 0.06f).coerceIn(20.dp, 24.dp),
        iconMedium = (screenWidth * 0.08f).coerceIn(28.dp, 32.dp),
        iconLarge = (screenWidth * 0.10f).coerceIn(36.dp, 44.dp),
        
        // Botones - suficientemente grandes para tocar cómodamente
        buttonHeight = (screenWidth * 0.14f).coerceIn(52.dp, 60.dp),
        buttonHeightSmall = (screenWidth * 0.12f).coerceIn(44.dp, 52.dp),
        
        // Imágenes de productos
        imageSmall = (screenWidth * 0.20f).coerceIn(70.dp, 90.dp),
        imageMedium = (screenWidth * 0.30f).coerceIn(100.dp, 130.dp),
        imageLarge = (screenWidth * 0.40f).coerceIn(140.dp, 180.dp),
        
        // Espaciados entre elementos
        spacingTiny = (screenWidth * 0.02f).coerceIn(6.dp, 8.dp),
        spacingSmall = (screenWidth * 0.03f).coerceIn(8.dp, 12.dp),
        spacingMedium = (screenWidth * 0.04f).coerceIn(12.dp, 16.dp),
        spacingLarge = (screenWidth * 0.06f).coerceIn(20.dp, 28.dp),
        
        // Bordes redondeados
        cornerRadiusSmall = (screenWidth * 0.02f).coerceIn(8.dp, 10.dp),
        cornerRadiusMedium = (screenWidth * 0.03f).coerceIn(12.dp, 14.dp),
        cornerRadiusLarge = (screenWidth * 0.08f).coerceIn(28.dp, 32.dp)
    )
}

// Funciones auxiliares para cálculos específicos

@Composable
fun screenWidthPercent(percent: Float): Dp {
    val configuration = LocalConfiguration.current
    return (configuration.screenWidthDp.dp * percent)
}

@Composable
fun screenHeightPercent(percent: Float): Dp {
    val configuration = LocalConfiguration.current
    return (configuration.screenHeightDp.dp * percent)
}

@Composable
fun responsiveTextSize(basePercent: Float, minSize: Float, maxSize: Float): TextUnit {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    return (screenWidth * basePercent).coerceIn(minSize, maxSize).sp
}

@Composable
fun responsivePadding(basePercent: Float, minDp: Float, maxDp: Float): Dp {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    return (screenWidth * basePercent).coerceIn(minDp.dp, maxDp.dp)
}
