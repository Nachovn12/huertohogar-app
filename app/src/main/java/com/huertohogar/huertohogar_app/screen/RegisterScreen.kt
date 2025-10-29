package com.huertohogar.huertohogar_app.screen

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        AsyncImage(
            model = "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/2vg760JCpJ/a44fuqid_expires_30_days.png",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 24.dp, start = 24.dp, end = 322.dp)
                .clip(RoundedCornerShape(24.dp))
                .size(44.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(bottom = 29.dp, start = 34.dp, end = 34.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/2vg760JCpJ/blwyvjvd_expires_30_days.png",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .height(200.dp)
                    .fillMaxWidth()
            )
            Text(
                "Registrarse",
                color = Color(0xFF82CD47),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Nombre", color = Color.Black, style = MaterialTheme.typography.titleMedium)
        TextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("Cheguevaran") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF82CD47), RoundedCornerShape(10.dp))
                .padding(vertical = 8.dp, horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Email", color = Color.Black, style = MaterialTheme.typography.titleMedium)
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("cheguevaran007@gmail.com") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF82CD47), RoundedCornerShape(10.dp))
                .padding(vertical = 8.dp, horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Contraseña", color = Color.Black, style = MaterialTheme.typography.titleMedium)
        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF82CD47), RoundedCornerShape(10.dp))
                .padding(vertical = 8.dp, horizontal = 20.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Confirmar contraseña", color = Color.Black, style = MaterialTheme.typography.titleMedium)
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text("Confirmar contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF82CD47), RoundedCornerShape(10.dp))
                .padding(vertical = 8.dp, horizontal = 20.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        if (showError) {
            Text(
                errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    errorMessage = "Todos los campos son obligatorios"
                    showError = true
                } else if (password != confirmPassword) {
                    errorMessage = "Las contraseñas no coinciden"
                    showError = true
                } else {
                    showError = false
                    // Aquí puedes guardar el usuario o llamar a tu backend
                    navController.navigate("login")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF82CD47))
        ) {
            Text("Registrarse", color = Color.White, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "¿Ya tienes una cuenta? Iniciar Sesión",
                color = Color(0xFF868889),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(54.dp))
    }
}