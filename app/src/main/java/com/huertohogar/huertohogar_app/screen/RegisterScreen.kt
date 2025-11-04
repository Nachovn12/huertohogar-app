package com.huertohogar.huertohogar_app.screen

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.res.painterResource
import com.huertohogar.huertohogar_app.R

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
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF0FFF5), Color.White)
                )
            )
            .windowInsetsPadding(WindowInsets.systemBars)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Logo de HuertoHogar
        Image(
            painter = painterResource(id = R.drawable.logo_huerto_hogar),
            contentDescription = "Logo Huerto Hogar",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Crear Cuenta",
            color = Color(0xFF23AA49),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )

        Text(
            "Regístrate para comenzar",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF868889)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo de nombre
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre completo") },
            placeholder = { Text("Ingrese su nombre") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF82CD47),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                cursorColor = Color(0xFF82CD47),
                focusedContainerColor = Color(0xFFF3F5F7),
                unfocusedContainerColor = Color(0xFFF3F5F7)
            ),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            placeholder = { Text("correo@ejemplo.com") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF82CD47),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                cursorColor = Color(0xFF82CD47),
                focusedContainerColor = Color(0xFFF3F5F7),
                unfocusedContainerColor = Color(0xFFF3F5F7)
            ),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            placeholder = { Text("Ingrese su contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF82CD47),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                cursorColor = Color(0xFF82CD47),
                focusedContainerColor = Color(0xFFF3F5F7),
                unfocusedContainerColor = Color(0xFFF3F5F7)
            ),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirmar contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar contraseña") },
            placeholder = { Text("Confirme su contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF82CD47),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                cursorColor = Color(0xFF82CD47),
                focusedContainerColor = Color(0xFFF3F5F7),
                unfocusedContainerColor = Color(0xFFF3F5F7)
            ),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de registro
        Button(
            onClick = {
                when {
                    name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                        errorMessage = "Por favor complete todos los campos"
                        showError = true
                    }
                    password != confirmPassword -> {
                        errorMessage = "Las contraseñas no coinciden"
                        showError = true
                    }
                    else -> {
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23AA49)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                "Registrarse",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        if (showError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Login
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "¿Ya tienes cuenta? ",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF868889)
            )
            Text(
                "Inicia sesión",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF23AA49),
                modifier = Modifier.clickable {
                    navController.navigate("login")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
