package com.huertohogar.huertohogar_app.screen

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.huertohogar.huertohogar_app.R
import kotlin.math.min
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.core.FastOutSlowInEasing

@Composable
fun SplashScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val logoSize = min(screenWidth.value, screenHeight.value) * 0.60f

    var startAnimation by remember { mutableStateOf(false) }
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "logoScale"
    )
    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "logoAlpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2000)
        navController.navigate("intro") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
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
                .scale(logoScale)
                .alpha(logoAlpha)
        )
    }
}
