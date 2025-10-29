package com.huertohogar.huertohogar_app.screen

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        AsyncImage(
            model = "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/2vg760JCpJ/jf1h57ec_expires_30_days.png",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 24.dp, start = 24.dp, end = 322.dp)
                .clip(RoundedCornerShape(24.dp))
                .size(44.dp)
        )
        AsyncImage(
            model = "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/2vg760JCpJ/tpx3my01_expires_30_days.png",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .height(243.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Nombre de usuario", style = MaterialTheme.typography.titleMedium, color = Color.Black, modifier = Modifier.padding(bottom = 8.dp))
        TextField(
            value = username,
            onValueChange = {
                username = it
                showError = false
            },
            placeholder = { Text("Ingrese su nombre de usuario") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF82CD47), RoundedCornerShape(10.dp))
                .padding(vertical = 8.dp, horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Contraseña", style = MaterialTheme.typography.titleMedium, color = Color.Black, modifier = Modifier.padding(bottom = 8.dp))
        TextField(
            value = password,
            onValueChange = {
                password = it
                showError = false
            },
            placeholder = { Text("Ingrese su contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF82CD47), RoundedCornerShape(10.dp))
                .padding(vertical = 8.dp, horizontal = 20.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Text(
            "¿Olvidaste tu contraseña?",
            color = Color(0xFF82CD47),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if (username == "prueba" && password == "prueba") {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    showError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF82CD47))
        ) {
            Text("Iniciar sesión", color = Color.White, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        }
        if (showError) {
            Text(
                "Usuario o contraseña incorrectos",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/2vg760JCpJ/ybltajff_expires_30_days.png",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(15.dp)
                    .padding(end = 8.dp)
            )
            Text("Iniciar sesión con Google", style = MaterialTheme.typography.bodySmall, color = Color.Black)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "¿No tienes cuenta? Regístrate.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF868889),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(160.dp))
    }
}