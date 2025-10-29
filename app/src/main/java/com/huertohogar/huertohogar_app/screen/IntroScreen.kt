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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@Composable
fun IntroScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF0FFF5), Color.White),
                    endY = 600f
                )
            )
    ) {
        // Contenido principal centrado
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            AsyncImage(
                model = "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/2vg760JCpJ/hmjt48pj_expires_30_days.png",
                contentDescription = "Logo Huerto Hogar",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Titulo
            Text(
                text = "Reciba sus compras en su domicilio",
                color = Color(0xFF05161B),
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Subtítulo
            Text(
                text = "La mejor aplicación de entrega de la ciudad para entregar tus alimentos frescos diarios",
                color = Color(0xFF969899),
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Botón
            Button(
                onClick = { navController.navigate("login") },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23AA49)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Compra ahora",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }

        // Imagen inferior
        AsyncImage(
            model = "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/2vg760JCpJ/qoa3xzm7_expires_30_days.png",
            contentDescription = "Alimentos frescos",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
