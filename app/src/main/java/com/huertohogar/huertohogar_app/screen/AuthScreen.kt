package com.huertohogar.huertohogar_app.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.huertohogar.huertohogar_app.R
import com.huertohogar.huertohogar_app.utils.getResponsiveDimensions

@Composable
fun AuthScreen(navController: NavController, initialTab: Int = 0) {
    var selectedTab by remember { mutableStateOf(initialTab) }
    val dimens = getResponsiveDimensions()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF5F9F6),
                        Color.White
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.2f))

            // Logo con diseño moderno
            Box(
                modifier = Modifier
                    .size((dimens.screenWidth.value * 0.22f).coerceIn(70f, 95f).dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color(0xFF66BB6A).copy(alpha = 0.3f),
                        spotColor = Color(0xFF66BB6A).copy(alpha = 0.3f)
                    )
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF66BB6A),
                                Color(0xFF4CAF50)
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_huerto_hogar),
                    contentDescription = "Logo",
                    modifier = Modifier.size((dimens.screenWidth.value * 0.14f).coerceIn(45f, 60f).dp)
                )
            }

            Spacer(modifier = Modifier.height(dimens.spacingMedium))

            // Título moderno
            Text(
                text = if (selectedTab == 0) "¡Bienvenido!" else "Crear Cuenta",
                fontSize = (dimens.screenWidth.value * 0.075f).coerceIn(26f, 34f).sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Text(
                text = if (selectedTab == 0) "Inicia sesión para continuar" else "Únete a HuertoHogar",
                fontSize = dimens.textMedium,
                color = Color(0xFF757575),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(dimens.spacingLarge * 1.2f))

            // Tabs modernas con indicador - SIN navegación, solo cambio de estado
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF5F5F5),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                ) {
                    // Tab Login
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { selectedTab = 0 },
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedTab == 0) Color.White else Color.Transparent,
                        shadowElevation = if (selectedTab == 0) 4.dp else 0.dp
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Iniciar Sesión",
                                fontSize = dimens.textMedium,
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == 0) Color(0xFF66BB6A) else Color(0xFF9E9E9E)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Tab Register
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { selectedTab = 1 },
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedTab == 1) Color.White else Color.Transparent,
                        shadowElevation = if (selectedTab == 1) 4.dp else 0.dp
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Registrar",
                                fontSize = dimens.textMedium,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == 1) Color(0xFF66BB6A) else Color(0xFF9E9E9E)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimens.spacingLarge * 1.2f))

            // Contenido animado
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        ) + fadeIn(animationSpec = tween(300)) togetherWith
                                slideOutHorizontally(
                                    targetOffsetX = { -it },
                                    animationSpec = tween(300)
                                ) + fadeOut(animationSpec = tween(300))
                    } else {
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(300)
                        ) + fadeIn(animationSpec = tween(300)) togetherWith
                                slideOutHorizontally(
                                    targetOffsetX = { it },
                                    animationSpec = tween(300)
                                ) + fadeOut(animationSpec = tween(300))
                    }
                },
                label = "auth_content"
            ) { tab ->
                when (tab) {
                    0 -> LoginContent(navController, dimens)
                    1 -> RegisterContent(navController, dimens)
                }
            }

            Spacer(modifier = Modifier.weight(0.2f))
        }
    }
}

@Composable
fun LoginContent(navController: NavController, dimens: com.huertohogar.huertohogar_app.utils.ResponsiveDimensions) {
    // ✅ CREDENCIALES PRE-CARGADAS PARA DEMOSTRACIÓN
    var username by remember { mutableStateOf("admin@huertohogar.com") }
    var password by remember { mutableStateOf("huertohogar2025") }
    var showError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    
    // ✅ Evitar que el teclado se abra automáticamente
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        focusManager.clearFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo Usuario
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 3.dp
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    showError = false
                },
                placeholder = { Text("Nombre de usuario", color = Color(0xFFBDBDBD)) },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFE8F5E9), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF66BB6A),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFF66BB6A),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = dimens.textMedium,
                    color = Color(0xFF1A1A1A),
                    fontWeight = FontWeight.Medium
                )
            )
        }

        Spacer(modifier = Modifier.height(dimens.spacingMedium))

        // Campo Contraseña
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 3.dp
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    showError = false
                },
                placeholder = { Text("Contraseña", color = Color(0xFFBDBDBD)) },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFE8F5E9), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFF66BB6A),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color(0xFF9E9E9E)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFF66BB6A),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = dimens.textMedium,
                    color = Color(0xFF1A1A1A),
                    fontWeight = FontWeight.Medium
                )
            )
        }

        Spacer(modifier = Modifier.height(dimens.spacingMedium))

        // Recordar y olvidaste
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { rememberMe = !rememberMe }
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF66BB6A),
                        uncheckedColor = Color(0xFFBDBDBD),
                        checkmarkColor = Color.White
                    )
                )
                Text(
                    text = "Recordar",
                    fontSize = dimens.textSmall,
                    color = Color(0xFF616161),
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color(0xFF66BB6A),
                fontSize = dimens.textSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { /* Acción futura */ }
            )
        }

        // Mensaje de error
        AnimatedVisibility(
            visible = showError,
            enter = fadeIn(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(200))
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimens.spacingMedium),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFEBEE)
            ) {
                Row(
                    modifier = Modifier.padding(dimens.paddingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⚠️", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Usuario o contraseña incorrectos",
                        color = Color(0xFFC62828),
                        fontSize = dimens.textSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(dimens.spacingLarge * 1.5f))

        // Botón moderno
        Button(
            onClick = {
                // ✅ VALIDACIÓN DE CREDENCIALES (acepta credenciales pre-cargadas o las antiguas)
                if ((username == "admin@huertohogar.com" && password == "huertohogar2025") ||
                    (username == "prueba" && password == "prueba")) {
                    // Navegar a home después de login exitoso
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                        popUpTo("auth") { inclusive = true }
                        launchSingleTop = true
                    }
                } else {
                    showError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Color(0xFF66BB6A).copy(alpha = 0.4f),
                    spotColor = Color(0xFF66BB6A).copy(alpha = 0.4f)
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF66BB6A),
                                Color(0xFF4CAF50)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Iniciar Sesión",
                    color = Color.White,
                    fontSize = dimens.textLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RegisterContent(navController: NavController, dimens: com.huertohogar.huertohogar_app.utils.ResponsiveDimensions) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo Nombre
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 3.dp
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    showError = false
                },
                placeholder = { Text("Nombre completo", color = Color(0xFFBDBDBD)) },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFE8F5E9), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF66BB6A),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFF66BB6A),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = dimens.textMedium,
                    color = Color(0xFF1A1A1A),
                    fontWeight = FontWeight.Medium
                )
            )
        }

        Spacer(modifier = Modifier.height(dimens.spacingMedium))

        // Campo Email
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 3.dp
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    showError = false
                },
                placeholder = { Text("Correo electrónico", color = Color(0xFFBDBDBD)) },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFE8F5E9), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = Color(0xFF66BB6A),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFF66BB6A),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = dimens.textMedium,
                    color = Color(0xFF1A1A1A),
                    fontWeight = FontWeight.Medium
                )
            )
        }

        Spacer(modifier = Modifier.height(dimens.spacingMedium))

        // Campo Contraseña
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 3.dp
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    showError = false
                },
                placeholder = { Text("Contraseña", color = Color(0xFFBDBDBD)) },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFE8F5E9), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFF66BB6A),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color(0xFF9E9E9E)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFF66BB6A),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = dimens.textMedium,
                    color = Color(0xFF1A1A1A),
                    fontWeight = FontWeight.Medium
                )
            )
        }

        Spacer(modifier = Modifier.height(dimens.spacingMedium))

        // Confirmar Contraseña
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 3.dp
        ) {
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    showError = false
                },
                placeholder = { Text("Confirmar contraseña", color = Color(0xFFBDBDBD)) },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFE8F5E9), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFF66BB6A),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color(0xFF9E9E9E)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFF66BB6A),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = dimens.textMedium,
                    color = Color(0xFF1A1A1A),
                    fontWeight = FontWeight.Medium
                )
            )
        }

        // Mensaje de error
        AnimatedVisibility(
            visible = showError,
            enter = fadeIn(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(200))
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimens.spacingMedium),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFEBEE)
            ) {
                Row(
                    modifier = Modifier.padding(dimens.paddingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⚠️", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = errorMessage,
                        color = Color(0xFFC62828),
                        fontSize = dimens.textSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(dimens.spacingLarge * 1.5f))

        // Botón moderno
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
                        navController.navigate("home") {
                            popUpTo("auth") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Color(0xFF66BB6A).copy(alpha = 0.4f),
                    spotColor = Color(0xFF66BB6A).copy(alpha = 0.4f)
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF66BB6A),
                                Color(0xFF4CAF50)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Crear Cuenta",
                    color = Color.White,
                    fontSize = dimens.textLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
