package com.huertohogar.huertohogar_app.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.huertohogar.huertohogar_app.viewmodel.UserViewModel
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.huertohogar.huertohogar_app.components.ScaffoldWithBottomNav
import androidx.compose.ui.platform.LocalConfiguration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel()
) {
    val userProfile by userViewModel.userProfile.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var selectedLanguage by remember { mutableStateOf("Español") }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf<EditField?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF3F5F7), Color.White)
                )
            )
        }

        ScaffoldWithBottomNav(
            navController = navController,
            viewModel = productViewModel,
            currentRoute = "profile",
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Mi Perfil",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color(0xFF05161B)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    actions = {
                        IconButton(onClick = { /* Settings */ }) {
                            Icon(
                                Icons.Outlined.Settings,
                                contentDescription = "Configuración",
                                tint = Color(0xFF23AA49)
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp)
            ) {
                    ProfileHeaderModern(userName = userProfile.nombre)
                    Spacer(modifier = Modifier.height(20.dp))
                    QuickAccessCards(navController)
                    Spacer(modifier = Modifier.height(24.dp))

                    // Información Personal - EDITABLE
                    ProfileSectionModern(title = "Información Personal") {
                        EditableProfileItem(
                            icon = Icons.Outlined.Person,
                            title = "Nombre completo",
                            value = userProfile.nombre,
                            onClick = {
                                showEditDialog = EditField.NOMBRE
                            }
                        )
                        HorizontalDivider(color = Color(0xFFF1F1F5))
                        EditableProfileItem(
                            icon = Icons.Outlined.Email,
                            title = "Correo electrónico",
                            value = userProfile.email,
                            onClick = {
                                showEditDialog = EditField.EMAIL
                            }
                        )
                        HorizontalDivider(color = Color(0xFFF1F1F5))
                        EditableProfileItem(
                            icon = Icons.Outlined.Phone,
                            title = "Teléfono",
                            value = userProfile.telefono,
                            onClick = {
                                showEditDialog = EditField.TELEFONO
                            }
                        )
                        HorizontalDivider(color = Color(0xFFF1F1F5))
                        EditableProfileItem(
                            icon = Icons.Outlined.LocationOn,
                            title = "Dirección",
                            value = userProfile.direccion,
                            onClick = {
                                showEditDialog = EditField.DIRECCION
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Preferencias
                    ProfileSectionModern(title = "Preferencias") {
                        SwitchProfileItem(
                            icon = Icons.Outlined.Notifications,
                            title = "Notificaciones push",
                            subtitle = "Alertas de pedidos",
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it }
                        )
                        HorizontalDivider(color = Color(0xFFF1F1F5))
                        ClickableProfileItem(
                            icon = Icons.Outlined.Language,
                            title = "Idioma",
                            value = selectedLanguage,
                            onClick = { showLanguageDialog = true }
                        )
                        HorizontalDivider(color = Color(0xFFF1F1F5))
                        ClickableProfileItem(
                            icon = Icons.Outlined.Security,
                            title = "Privacidad",
                            value = "",
                            onClick = { /* Security */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Ayuda
                    ProfileSectionModern(title = "Ayuda y Soporte") {
                        ClickableProfileItem(
                            icon = Icons.Outlined.HelpOutline,
                            title = "Centro de ayuda",
                            value = "",
                            onClick = { /* Help */ }
                        )
                        HorizontalDivider(color = Color(0xFFF1F1F5))
                        ClickableProfileItem(
                            icon = Icons.Outlined.Info,
                            title = "Acerca de",
                            value = "v1.0.0",
                            onClick = { /* About */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    LogoutButton(onClick = { showLogoutDialog = true })
                }
            }

        // Diálogos
        if (showLogoutDialog) {
            LogoutDialog(
                onDismiss = { showLogoutDialog = false },
                onConfirm = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        if (showLanguageDialog) {
            LanguageDialog(
                currentLanguage = selectedLanguage,
                onDismiss = { showLanguageDialog = false },
                onLanguageSelected = {
                    selectedLanguage = it
                    showLanguageDialog = false
                }
            )
        }

        showEditDialog?.let { field ->
            EditFieldDialog(
                field = field,
                currentValue = when(field) {
                    EditField.NOMBRE -> userProfile.nombre
                    EditField.EMAIL -> userProfile.email
                    EditField.TELEFONO -> userProfile.telefono
                    EditField.DIRECCION -> userProfile.direccion
                },
                onDismiss = { showEditDialog = null },
                onSave = { newValue ->
                    when(field) {
                        EditField.NOMBRE -> userViewModel.updateNombre(newValue)
                        EditField.EMAIL -> userViewModel.updateEmail(newValue)
                        EditField.TELEFONO -> userViewModel.updateTelefono(newValue)
                        EditField.DIRECCION -> userViewModel.updateDireccion(newValue)
                    }
                    showEditDialog = null
                }
            )
        }
    } // Cierre del Box principal
} // Cierre de ProfileScreen

enum class EditField(val title: String, val icon: ImageVector) {
    NOMBRE("Nombre completo", Icons.Outlined.Person),
    EMAIL("Correo electrónico", Icons.Outlined.Email),
    TELEFONO("Teléfono", Icons.Outlined.Phone),
    DIRECCION("Dirección", Icons.Outlined.LocationOn)
}

@Composable
fun EditFieldDialog(
    field: EditField,
    currentValue: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf(currentValue) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    Icon(
                        field.icon,
                        contentDescription = null,
                        tint = Color(0xFF23AA49),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Editar ${field.title}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF05161B)
                    )
                }

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(field.title) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF23AA49),
                        focusedLabelColor = Color(0xFF23AA49),
                        cursorColor = Color(0xFF23AA49)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = field != EditField.DIRECCION,
                    maxLines = if (field == EditField.DIRECCION) 2 else 1
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF23AA49)
                        )
                    ) {
                        Text("Cancelar", modifier = Modifier.padding(vertical = 8.dp))
                    }
                    Button(
                        onClick = { onSave(text) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF23AA49)
                        )
                    ) {
                        Text("Guardar", modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileHeaderModern(userName: String = "Juanito Pérez") {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Dimensiones responsivas
    val horizontalPadding = (screenWidth * 0.04f).coerceIn(12.dp, 20.dp)
    val cardPadding = (screenWidth * 0.055f).coerceIn(16.dp, 24.dp)
    val headerHeight = (screenWidth * 0.28f).coerceIn(90.dp, 120.dp)
    val avatarSize = (screenWidth * 0.24f).coerceIn(80.dp, 100.dp)
    val iconSize = (avatarSize * 0.56f)
    val statIconSize = (screenWidth * 0.05f).coerceIn(16.dp, 20.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .shadow(6.dp, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF23AA49), Color(0xFF2ECC71))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(cardPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height((headerHeight * 0.3f)))

                Box(
                    modifier = Modifier
                        .size(avatarSize)
                        .background(Color.White, CircleShape)
                        .padding((avatarSize * 0.033f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color(0xFF23AA49)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }

                Spacer(modifier = Modifier.height((screenWidth * 0.032f).coerceIn(10.dp, 14.dp)))

                Text(
                    userName,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = (screenWidth.value * 0.058f).coerceIn(20f, 24f).sp
                    ),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF05161B)
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEE58)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFA000),
                            modifier = Modifier.size((screenWidth * 0.038f).coerceIn(12.dp, 16.dp))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Cliente Premium",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = (screenWidth.value * 0.03f).coerceIn(10f, 12f).sp
                            ),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7F6003)
                        )
                    }
                }

                Spacer(modifier = Modifier.height((screenWidth * 0.055f).coerceIn(16.dp, 24.dp)))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(Icons.Default.ShoppingBag, "24", "Pedidos", statIconSize)
                    VerticalDivider(
                        modifier = Modifier.height((screenWidth * 0.125f).coerceIn(40.dp, 50.dp)),
                        color = Color(0xFFE0E0E0)
                    )
                    StatItem(Icons.Default.Star, "4.8", "Rating", statIconSize)
                    VerticalDivider(
                        modifier = Modifier.height((screenWidth * 0.125f).coerceIn(40.dp, 50.dp)),
                        color = Color(0xFFE0E0E0)
                    )
                    StatItem(Icons.Default.CalendarToday, "2", "Años", statIconSize)
                }
            }
        }
    }
}

@Composable
fun StatItem(icon: ImageVector, value: String, label: String, iconSize: Dp) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF23AA49), modifier = Modifier.size(iconSize))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = (screenWidth.value * 0.045f).coerceIn(16f, 20f).sp
            ),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF05161B)
        )
        Text(
            label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = (screenWidth.value * 0.03f).coerceIn(10f, 12f).sp
            ),
            color = Color(0xFF969899)
        )
    }
}

@Composable
fun QuickAccessCards(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val horizontalPadding = (screenWidth * 0.04f).coerceIn(12.dp, 20.dp)
    val cardHeight = (screenWidth * 0.24f).coerceIn(85.dp, 100.dp)
    val spacing = (screenWidth * 0.032f).coerceIn(10.dp, 16.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        QuickCard(
            Icons.Default.ShoppingBag,
            "Pedidos",
            "2 activos",
            Color(0xFF4CAF50),
            Modifier.weight(1f),
            cardHeight
        ) {
            // Navigate to orders
        }
        QuickCard(
            Icons.Default.Favorite,
            "Favoritos",
            "8 productos",
            Color(0xFFFF5252),
            Modifier.weight(1f),
            cardHeight
        ) {
            // Navigate to favorites
        }
    }
}

@Composable
fun QuickCard(icon: ImageVector, title: String, subtitle: String, color: Color, modifier: Modifier, cardHeight: Dp, onClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val iconBoxSize = (screenWidth * 0.096f).coerceIn(34.dp, 40.dp)
    val iconSize = (iconBoxSize * 0.56f)
    val cardPadding = (screenWidth * 0.037f).coerceIn(12.dp, 16.dp)

    Card(
        modifier = modifier
            .height(cardHeight)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(cardPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(iconBoxSize)
                    .background(color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(iconSize))
            }
            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = (screenWidth.value * 0.038f).coerceIn(13f, 16f).sp
                    ),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF05161B)
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = (screenWidth.value * 0.029f).coerceIn(10f, 12f).sp
                    ),
                    color = Color(0xFF969899)
                )
            }
        }
    }
}

@Composable
fun ProfileSectionModern(title: String, content: @Composable ColumnScope.() -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val horizontalPadding = (screenWidth * 0.04f).coerceIn(12.dp, 20.dp)

    Column(modifier = Modifier.padding(horizontal = horizontalPadding)) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = (screenWidth.value * 0.042f).coerceIn(15f, 18f).sp
            ),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF05161B),
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column { content() }
        }
    }
}

@Composable
fun EditableProfileItem(icon: ImageVector, title: String, value: String, onClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val iconBoxSize = (screenWidth * 0.104f).coerceIn(36.dp, 44.dp)
    val iconSize = (iconBoxSize * 0.5f)
    val itemPadding = (screenWidth * 0.037f).coerceIn(12.dp, 16.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(itemPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(iconBoxSize)
                .background(Color(0xFFF3F5F7), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF23AA49), modifier = Modifier.size(iconSize))
        }
        Spacer(modifier = Modifier.width((screenWidth * 0.037f).coerceIn(12.dp, 16.dp)))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = (screenWidth.value * 0.032f).coerceIn(11f, 13f).sp
                ),
                color = Color(0xFF969899)
            )
            Text(
                value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = (screenWidth.value * 0.037f).coerceIn(13f, 15f).sp
                ),
                color = Color(0xFF05161B),
                fontWeight = FontWeight.Medium
            )
        }
        Icon(
            Icons.Default.Edit,
            contentDescription = "Editar",
            tint = Color(0xFF23AA49),
            modifier = Modifier.size((screenWidth * 0.048f).coerceIn(16.dp, 20.dp))
        )
    }
}

@Composable
fun ClickableProfileItem(icon: ImageVector, title: String, value: String, onClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val iconBoxSize = (screenWidth * 0.104f).coerceIn(36.dp, 44.dp)
    val iconSize = (iconBoxSize * 0.5f)
    val itemPadding = (screenWidth * 0.037f).coerceIn(12.dp, 16.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(itemPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(iconBoxSize)
                .background(Color(0xFFF3F5F7), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF23AA49), modifier = Modifier.size(iconSize))
        }
        Spacer(modifier = Modifier.width((screenWidth * 0.037f).coerceIn(12.dp, 16.dp)))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = (screenWidth.value * 0.037f).coerceIn(13f, 15f).sp
                ),
                color = Color(0xFF05161B),
                fontWeight = FontWeight.Medium
            )
            if (value.isNotEmpty()) {
                Text(
                    value,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = (screenWidth.value * 0.032f).coerceIn(11f, 13f).sp
                    ),
                    color = Color(0xFF969899)
                )
            }
        }
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color(0xFF969899),
            modifier = Modifier.size((screenWidth * 0.053f).coerceIn(18.dp, 22.dp))
        )
    }
}

@Composable
fun SwitchProfileItem(icon: ImageVector, title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val iconBoxSize = (screenWidth * 0.104f).coerceIn(36.dp, 44.dp)
    val iconSize = (iconBoxSize * 0.5f)
    val itemPadding = (screenWidth * 0.037f).coerceIn(12.dp, 16.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(itemPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(iconBoxSize)
                .background(Color(0xFFF3F5F7), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF23AA49), modifier = Modifier.size(iconSize))
        }
        Spacer(modifier = Modifier.width((screenWidth * 0.037f).coerceIn(12.dp, 16.dp)))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = (screenWidth.value * 0.037f).coerceIn(13f, 15f).sp
                ),
                color = Color(0xFF05161B),
                fontWeight = FontWeight.Medium
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = (screenWidth.value * 0.029f).coerceIn(10f, 12f).sp
                ),
                color = Color(0xFF969899)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF23AA49),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE0E0E0)
            )
        )
    }
}

@Composable
fun LogoutButton(onClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val horizontalPadding = (screenWidth * 0.04f).coerceIn(12.dp, 20.dp)
    val itemPadding = (screenWidth * 0.037f).coerceIn(12.dp, 16.dp)
    val iconSize = (screenWidth * 0.058f).coerceIn(20.dp, 24.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(itemPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Logout,
                contentDescription = null,
                tint = Color(0xFFFF314A),
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width((screenWidth * 0.026f).coerceIn(8.dp, 12.dp)))
            Text(
                "Cerrar Sesión",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = (screenWidth.value * 0.042f).coerceIn(15f, 18f).sp
                ),
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF314A)
            )
        }
    }
}

@Composable
fun LogoutDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFFFFEBEE), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, tint = Color(0xFFFF314A), modifier = Modifier.size(28.dp))
            }
        },
        title = { Text("Cerrar Sesión", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold) },
        text = { Text("¿Estás seguro que deseas cerrar sesión?", textAlign = TextAlign.Center, color = Color(0xFF969899)) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF314A)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar Sesión", modifier = Modifier.padding(vertical = 8.dp))
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF23AA49))
            ) {
                Text("Cancelar", modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    )
}

@Composable
fun LanguageDialog(currentLanguage: String, onDismiss: () -> Unit, onLanguageSelected: (String) -> Unit) {
    val languages = listOf("Español", "English", "Português")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar Idioma", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                languages.forEach { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageSelected(language) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = language == currentLanguage,
                            onClick = { onLanguageSelected(language) },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF23AA49))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(language, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = Color(0xFF23AA49))
            }
        }
    )
}

