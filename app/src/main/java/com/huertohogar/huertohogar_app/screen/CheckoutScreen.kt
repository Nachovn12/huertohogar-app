package com.huertohogar.huertohogar_app.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.huertohogar.huertohogar_app.components.ScaffoldWithBottomNav
import com.huertohogar.huertohogar_app.utils.formatPrecio
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel
import com.huertohogar.huertohogar_app.viewmodel.UserViewModel
import com.huertohogar.huertohogar_app.viewmodel.CartItem
import com.huertohogar.huertohogar_app.model.Pedido
import com.huertohogar.huertohogar_app.model.DatosEntrega
import com.huertohogar.huertohogar_app.model.EstadoPedido
import kotlinx.coroutines.delay

// Datos autocompletados para el prototipo
data class CheckoutData(
    val nombre: String,
    val email: String,
    val telefono: String,
    val direccion: String,
    val ciudad: String,
    val region: String,
    val comuna: String,
    val metodoPago: String = "Tarjeta de Crédito",
    val numeroTarjeta: String = "**** **** **** 4532",
    val titularTarjeta: String = "JUANITO PEREZ"
)

enum class PaymentMethod(val displayName: String, val icon: ImageVector) {
    CREDIT_CARD("Tarjeta de Crédito", Icons.Default.CreditCard),
    DEBIT_CARD("Tarjeta de Débito", Icons.Default.AccountBalance),
    TRANSFER("Transferencia Bancaria", Icons.Default.AccountBalanceWallet),
    CASH("Pago contra entrega", Icons.Default.Money)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    viewModel: ProductViewModel,
    userViewModel: UserViewModel
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val userProfile by userViewModel.userProfile.collectAsState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Dimensiones responsivas
    val horizontalPadding = (screenWidth * 0.045f).coerceIn(14.dp, 20.dp)
    val cardPadding = (screenWidth * 0.04f).coerceIn(12.dp, 18.dp)

    // Estado para el método de pago seleccionado
    var selectedPaymentMethod by remember { mutableStateOf(PaymentMethod.CREDIT_CARD) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }

    // Datos del checkout (obtenidos del perfil del usuario)
    val checkoutData = CheckoutData(
        nombre = userProfile.nombre,
        email = userProfile.email,
        telefono = userProfile.telefono,
        direccion = userProfile.direccion,
        ciudad = userProfile.ciudad,
        comuna = userProfile.comuna,
        region = userProfile.region
    )

    // Calcular totales
    val subtotal = cartItems.sumOf { it.product.price * it.quantity }
    val envio = if (subtotal > 30000) 0.0 else 2500.0
    val total = subtotal + envio

    Box(modifier = Modifier.fillMaxSize()) {
        ScaffoldWithBottomNav(
            navController = navController,
            viewModel = viewModel,
            currentRoute = "checkout",
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Confirmar Pedido",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF05161B),
                            fontSize = (screenWidth.value * 0.05f).coerceIn(18f, 22f).sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color(0xFF05161B)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF8F9FA)),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Sección: Datos de Entrega
                item {
                    Spacer(modifier = Modifier.height((screenWidth * 0.04f).coerceIn(12.dp, 18.dp)))
                    CheckoutSection(
                        title = "📍 Datos de Entrega",
                        screenWidth = screenWidth
                    ) {
                        InfoField("Nombre completo", checkoutData.nombre, Icons.Default.Person)
                        Divider(color = Color(0xFFE8E8E8), thickness = 1.dp)
                        InfoField("Correo electrónico", checkoutData.email, Icons.Default.Email)
                        Divider(color = Color(0xFFE8E8E8), thickness = 1.dp)
                        InfoField("Teléfono", checkoutData.telefono, Icons.Default.Phone)
                        Divider(color = Color(0xFFE8E8E8), thickness = 1.dp)
                        InfoField("Dirección", checkoutData.direccion, Icons.Default.LocationOn)
                        Divider(color = Color(0xFFE8E8E8), thickness = 1.dp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                InfoField("Comuna", checkoutData.comuna, Icons.Default.Home, compact = true)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                InfoField("Ciudad", checkoutData.ciudad, Icons.Default.LocationCity, compact = true)
                            }
                        }
                    }
                }

                // Sección: Método de Pago
                item {
                    Spacer(modifier = Modifier.height((screenWidth * 0.04f).coerceIn(12.dp, 18.dp)))
                    CheckoutSection(
                        title = "💳 Método de Pago",
                        screenWidth = screenWidth
                    ) {
                        PaymentMethod.values().forEach { method ->
                            PaymentMethodItem(
                                method = method,
                                isSelected = selectedPaymentMethod == method,
                                onClick = { selectedPaymentMethod = method },
                                screenWidth = screenWidth
                            )
                            if (method != PaymentMethod.values().last()) {
                                Divider(color = Color(0xFFE8E8E8), thickness = 1.dp)
                            }
                        }

                        // Mostrar datos de la tarjeta si está seleccionado
                        if (selectedPaymentMethod == PaymentMethod.CREDIT_CARD ||
                            selectedPaymentMethod == PaymentMethod.DEBIT_CARD) {
                            Divider(color = Color(0xFFE8E8E8), thickness = 1.dp)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF8F9FA), RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        checkoutData.numeroTarjeta,
                                        fontSize = (screenWidth.value * 0.038f).coerceIn(14f, 16f).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF05161B)
                                    )
                                    Icon(
                                        Icons.Default.CreditCard,
                                        contentDescription = null,
                                        tint = Color(0xFF23AA49),
                                        modifier = Modifier.size((screenWidth * 0.064f).coerceIn(22.dp, 28.dp))
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    checkoutData.titularTarjeta,
                                    fontSize = (screenWidth.value * 0.032f).coerceIn(11f, 13f).sp,
                                    color = Color(0xFF969899)
                                )
                            }
                        }
                    }
                }

                // Sección: Resumen del Pedido
                item {
                    Spacer(modifier = Modifier.height((screenWidth * 0.04f).coerceIn(12.dp, 18.dp)))
                    CheckoutSection(
                        title = "🛍️ Resumen del Pedido (${cartItems.size} productos)",
                        screenWidth = screenWidth
                    ) {
                        cartItems.forEach { item ->
                            OrderSummaryItem(item = item, screenWidth = screenWidth)
                            if (item != cartItems.last()) {
                                Divider(color = Color(0xFFE8E8E8), thickness = 1.dp)
                            }
                        }
                    }
                }

                // Sección: Resumen de Pago
                item {
                    Spacer(modifier = Modifier.height((screenWidth * 0.04f).coerceIn(12.dp, 18.dp)))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalPadding),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(cardPadding)
                        ) {
                            Text(
                                "💰 Resumen de Pago",
                                fontSize = (screenWidth.value * 0.045f).coerceIn(16f, 19f).sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF05161B),
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            PriceRow("Subtotal", subtotal, screenWidth)
                            Spacer(modifier = Modifier.height(8.dp))
                            PriceRow("Envío", envio, screenWidth, highlight = envio == 0.0)

                            if (envio == 0.0) {
                                Text(
                                    "¡Envío gratis por compra superior a $30.000!",
                                    fontSize = (screenWidth.value * 0.029f).coerceIn(10f, 12f).sp,
                                    color = Color(0xFF23AA49),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Divider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = Color(0xFFE8E8E8),
                                thickness = 1.dp
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Total",
                                    fontSize = (screenWidth.value * 0.05f).coerceIn(18f, 22f).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF05161B)
                                )
                                Text(
                                    "$${total.formatPrecio()}",
                                    fontSize = (screenWidth.value * 0.055f).coerceIn(20f, 24f).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF23AA49)
                                )
                            }
                        }
                    }
                }

                // Botón Confirmar Pedido
                item {
                    Spacer(modifier = Modifier.height((screenWidth * 0.055f).coerceIn(18.dp, 24.dp)))
                    Button(
                        onClick = {
                            isProcessing = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalPadding)
                            .height((screenWidth * 0.14f).coerceIn(52.dp, 64.dp)),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23AA49)),
                        enabled = !isProcessing
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size((screenWidth * 0.064f).coerceIn(22.dp, 28.dp))
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size((screenWidth * 0.064f).coerceIn(22.dp, 28.dp))
                                )
                                Text(
                                    "Confirmar Pedido - $${total.formatPrecio()}",
                                    color = Color.White,
                                    fontSize = (screenWidth.value * 0.042f).coerceIn(15f, 18f).sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Nota de seguridad
                    Text(
                        "🔒 Pago seguro y protegido",
                        fontSize = (screenWidth.value * 0.032f).coerceIn(11f, 13f).sp,
                        color = Color(0xFF969899),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }

    // Simulación de procesamiento y éxito
    LaunchedEffect(isProcessing) {
        if (isProcessing) {
            delay(2000) // Simular procesamiento

            // Crear el pedido
            val numeroPedido = "#${(10000..99999).random()}"
            val datosEntrega = DatosEntrega(
                nombre = checkoutData.nombre,
                email = checkoutData.email,
                telefono = checkoutData.telefono,
                direccion = checkoutData.direccion,
                ciudad = checkoutData.ciudad,
                comuna = checkoutData.comuna
            )

            val nuevoPedido = Pedido(
                numeroPedido = numeroPedido,
                items = cartItems,
                subtotal = subtotal,
                envio = envio,
                total = total,
                datosEntrega = datosEntrega,
                metodoPago = selectedPaymentMethod.displayName,
                estado = EstadoPedido.PROCESANDO
            )

            // Guardar el pedido en el ViewModel
            viewModel.crearPedido(nuevoPedido)

            isProcessing = false
            showSuccessDialog = true
        }
    }

    // Diálogo de éxito
    if (showSuccessDialog) {
        SuccessDialog(
            onDismiss = {
                showSuccessDialog = false
                viewModel.clearCart()
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            },
            total = total,
            screenWidth = screenWidth
        )
    }
}

@Composable
fun CheckoutSection(
    title: String,
    screenWidth: androidx.compose.ui.unit.Dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val horizontalPadding = (screenWidth * 0.045f).coerceIn(14.dp, 20.dp)
    val cardPadding = (screenWidth * 0.04f).coerceIn(12.dp, 18.dp)

    Column(modifier = Modifier.padding(horizontal = horizontalPadding)) {
        Text(
            title,
            fontSize = (screenWidth.value * 0.045f).coerceIn(16f, 19f).sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF05161B),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(cardPadding)) {
                content()
            }
        }
    }
}

@Composable
fun InfoField(
    label: String,
    value: String,
    icon: ImageVector,
    compact: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (compact) 8.dp else 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color(0xFF23AA49),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                label,
                fontSize = 11.sp,
                color = Color(0xFF969899)
            )
            Text(
                value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF05161B)
            )
        }
    }
}

@Composable
fun PaymentMethodItem(
    method: PaymentMethod,
    isSelected: Boolean,
    onClick: () -> Unit,
    screenWidth: androidx.compose.ui.unit.Dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(if (isSelected) Color(0xFFF0F9F3) else Color.Transparent)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size((screenWidth * 0.104f).coerceIn(36.dp, 44.dp))
                .background(
                    if (isSelected) Color(0xFF23AA49) else Color(0xFFF3F5F7),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                method.icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else Color(0xFF969899),
                modifier = Modifier.size((screenWidth * 0.053f).coerceIn(18.dp, 22.dp))
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            method.displayName,
            fontSize = (screenWidth.value * 0.038f).coerceIn(14f, 16f).sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color(0xFF23AA49) else Color(0xFF05161B),
            modifier = Modifier.weight(1f)
        )
        if (isSelected) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "Seleccionado",
                tint = Color(0xFF23AA49),
                modifier = Modifier.size((screenWidth * 0.058f).coerceIn(20.dp, 24.dp))
            )
        }
    }
}

@Composable
fun OrderSummaryItem(
    item: CartItem,
    screenWidth: androidx.compose.ui.unit.Dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "${item.quantity}x ${item.product.name}",
                fontSize = (screenWidth.value * 0.035f).coerceIn(13f, 15f).sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF05161B)
            )
            Text(
                item.product.stock_unit ?: "",
                fontSize = (screenWidth.value * 0.029f).coerceIn(10f, 12f).sp,
                color = Color(0xFF969899)
            )
        }
        Text(
            "$${(item.product.price * item.quantity).formatPrecio()}",
            fontSize = (screenWidth.value * 0.038f).coerceIn(14f, 16f).sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF05161B)
        )
    }
}

@Composable
fun PriceRow(
    label: String,
    amount: Double,
    screenWidth: androidx.compose.ui.unit.Dp,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            fontSize = (screenWidth.value * 0.038f).coerceIn(14f, 16f).sp,
            color = if (highlight) Color(0xFF23AA49) else Color(0xFF969899)
        )
        Text(
            if (amount == 0.0) "¡GRATIS!" else "$${amount.formatPrecio()}",
            fontSize = (screenWidth.value * 0.038f).coerceIn(14f, 16f).sp,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
            color = if (highlight) Color(0xFF23AA49) else Color(0xFF05161B)
        )
    }
}

@Composable
fun SuccessDialog(
    onDismiss: () -> Unit,
    total: Double,
    screenWidth: androidx.compose.ui.unit.Dp
) {
    Dialog(onDismissRequest = { }) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icono de éxito animado
                Box(
                    modifier = Modifier
                        .size((screenWidth * 0.24f).coerceIn(80.dp, 100.dp))
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF23AA49).copy(alpha = 0.2f),
                                    Color(0xFF23AA49).copy(alpha = 0.05f)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF23AA49),
                        modifier = Modifier.size((screenWidth * 0.16f).coerceIn(56.dp, 68.dp))
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "¡Pedido Confirmado!",
                    fontSize = (screenWidth.value * 0.058f).coerceIn(20f, 24f).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF05161B),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Tu pedido ha sido procesado exitosamente",
                    fontSize = (screenWidth.value * 0.038f).coerceIn(14f, 16f).sp,
                    color = Color(0xFF969899),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Detalles del pedido
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Número de pedido",
                            fontSize = (screenWidth.value * 0.032f).coerceIn(11f, 13f).sp,
                            color = Color(0xFF969899)
                        )
                        Text(
                            "#${(10000..99999).random()}",
                            fontSize = (screenWidth.value * 0.05f).coerceIn(18f, 22f).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF05161B)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Divider(color = Color(0xFFE8E8E8))

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Total pagado:",
                                fontSize = (screenWidth.value * 0.038f).coerceIn(14f, 16f).sp,
                                color = Color(0xFF969899)
                            )
                            Text(
                                "$${total.formatPrecio()}",
                                fontSize = (screenWidth.value * 0.042f).coerceIn(15f, 18f).sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF23AA49)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Recibirás un correo con los detalles de tu pedido",
                    fontSize = (screenWidth.value * 0.032f).coerceIn(11f, 13f).sp,
                    color = Color(0xFF969899),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((screenWidth * 0.13f).coerceIn(48.dp, 56.dp)),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23AA49))
                ) {
                    Text(
                        "Continuar Comprando",
                        fontSize = (screenWidth.value * 0.042f).coerceIn(15f, 18f).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
