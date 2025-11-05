package com.huertohogar.huertohogar_app.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.huertohogar.huertohogar_app.R
import com.huertohogar.huertohogar_app.utils.getResponsiveDimensions

@Composable
fun IntroScreen(navController: NavController) {
    val dimens = getResponsiveDimensions()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo que ocupa toda la pantalla sin bordes
        Image(
            painter = painterResource(id = R.drawable.intro_background),
            contentDescription = "Productos frescos de fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight,
            alignment = Alignment.Center
        )

        // Contenido superpuesto centrado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Espaciador superior (30% del espacio disponible)
            Spacer(modifier = Modifier.weight(0.35f))

            // Título principal
            Text(
                text = "Reciba sus compras\nen su domicilio",
                color = Color(0xFF1B5E20),
                fontSize = (dimens.screenWidth.value * 0.072f).coerceIn(24f, 32f).sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = (dimens.screenWidth.value * 0.085f).coerceIn(30f, 40f).sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimens.paddingSmall)
            )

            Spacer(modifier = Modifier.height(dimens.spacingMedium))

            // Subtítulo
            Text(
                text = "La mejor aplicación de entrega de la ciudad\npara entregar tus alimentos frescos diarios",
                color = Color(0xFF757575),
                fontSize = dimens.textMedium,
                textAlign = TextAlign.Center,
                lineHeight = (dimens.textMedium.value * 1.5f).sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimens.paddingSmall)
            )

            Spacer(modifier = Modifier.height(dimens.spacingLarge * 1.5f))

            // Botón "Compra ahora"
            Button(
                onClick = { navController.navigate("login") },
                shape = RoundedCornerShape(dimens.cornerRadiusLarge),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(dimens.buttonHeight),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 10.dp
                )
            ) {
                Text(
                    text = "Compra ahora",
                    color = Color.White,
                    fontSize = dimens.textLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // Espaciador inferior (65% del espacio disponible)
            Spacer(modifier = Modifier.weight(0.65f))
        }
    }
}