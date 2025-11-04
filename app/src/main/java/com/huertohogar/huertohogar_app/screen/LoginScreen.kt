package com.huertohogar.huertohogar_app.screen

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import com.huertohogar.huertohogar_app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

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
            .imePadding()
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
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF23AA49)
        )

        Text(
            text = "Inicia sesión para continuar",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF868889)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Campo de usuario
        Text(
            "Nombre de usuario",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                showError = false
            },
            placeholder = { Text("Ingrese su nombre de usuario", color = Color(0xFF868889)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Usuario",
                    tint = Color(0xFF82CD47)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF82CD47),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                cursorColor = Color(0xFF82CD47),
                focusedContainerColor = Color(0xFFF3F5F7),
                unfocusedContainerColor = Color(0xFFF3F5F7)
            ),
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña
        Text(
            "Contraseña",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                showError = false
            },
            placeholder = { Text("Ingrese su contraseña", color = Color(0xFF868889)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Contraseña",
                    tint = Color(0xFF82CD47)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF82CD47),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                cursorColor = Color(0xFF82CD47),
                focusedContainerColor = Color(0xFFF3F5F7),
                unfocusedContainerColor = Color(0xFFF3F5F7)
            ),
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
        )

        Text(
            "¿Olvidaste tu contraseña?",
            color = Color(0xFF82CD47),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            textAlign = TextAlign.End
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de inicio de sesión
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
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23AA49)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                "Iniciar sesión",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        if (showError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Usuario o contraseña incorrectos",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Registro
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "¿No tienes cuenta? ",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF868889)
            )
            Text(
                "Regístrate",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF23AA49),
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}