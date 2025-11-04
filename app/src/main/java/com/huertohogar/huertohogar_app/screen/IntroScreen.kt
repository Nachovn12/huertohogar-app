package com.huertohogar.huertohogar_app.screen

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import androidx.compose.ui.res.painterResource
import com.huertohogar.huertohogar_app.R

@Composable
fun IntroScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // IMAGEN DE FONDO COMPLETA desde Figma
        Image(
            painter = painterResource(id = R.drawable.intro_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // Solo texto y botón encima del fondo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Espaciador para empujar el contenido hacia abajo
            Spacer(modifier = Modifier.height(120.dp))

            // Título principal
            Text(
                text = "Reciba sus compras en su domicilio",
                color = Color(0xFF1B5E20),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 38.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtítulo
            Text(
                text = "La mejor aplicación de entrega de la ciudad para entregar tus alimentos frescos diarios",
                color = Color(0xFF9E9E9E),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón "Compra ahora"
            Button(
                onClick = { navController.navigate("login") },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 10.dp
                )
            ) {
                Text(
                    text = "Compra ahora",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Espaciador para dejar espacio para la imagen inferior
            Spacer(modifier = Modifier.height(280.dp))
        }
    }
}