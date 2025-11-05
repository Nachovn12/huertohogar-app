package com.huertohogar.huertohogar_app.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.huertohogar.huertohogar_app.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val logoSize = (screenWidth.value * 0.75f).coerceIn(200f, 320f)

    var startAnimation by remember { mutableStateOf(false) }
    
    // Animación de escala con bounce elegante
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "logoScale"
    )
    
    // Animación de opacidad sincronizada
    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "logoAlpha"
    )
    
    // Animación sutil de elevación (Y position)
    val logoOffsetY by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 50f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "logoOffsetY"
    )

    LaunchedEffect(Unit) {
        delay(100) // Pequeño delay antes de iniciar
        startAnimation = true
        delay(1700) // Logo visible 1.7 segundos después de la animación (aumentado de 1.3s)
        navController.navigate("intro") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF23AA49),
                        Color(0xFF1E9640)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_huerto_hogar),
            contentDescription = "Logo Huerto Hogar",
            modifier = Modifier
                .size(logoSize.dp)
                .offset(y = logoOffsetY.dp)
                .scale(logoScale)
                .alpha(logoAlpha)
        )
    }
}
