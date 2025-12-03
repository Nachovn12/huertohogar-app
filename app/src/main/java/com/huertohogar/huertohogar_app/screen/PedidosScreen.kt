package com.huertohogar.huertohogar_app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel
import com.huertohogar.huertohogar_app.components.ScaffoldWithBottomNav
import com.huertohogar.huertohogar_app.model.Pedido
import java.util.Locale

// Pantalla principal de pedidos
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosScreen(
    navController: NavController,
    viewModel: ProductViewModel
) {
    val pedidos by viewModel.pedidos.collectAsState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val horizontalPadding = (screenWidth * 0.045f).coerceIn(14.dp, 20.dp)

    var pedidoSeleccionado by remember { mutableStateOf<Pedido?>(null) }

    ScaffoldWithBottomNav(
        navController = navController,
        viewModel = viewModel,
        currentRoute = "pedidos",
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mis Pedidos",
                        fontWeight = FontWeight.Bold,
                        fontSize = (screenWidth.value * 0.055f).coerceIn(20f, 24f).sp,
                        color = Color(0xFF05161B)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(paddingValues)
        ) {
            if (pedidos.isEmpty()) {
                EmptyPedidosState(navController, screenWidth)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = horizontalPadding,
                        vertical = (screenWidth * 0.04f).coerceIn(12.dp, 18.dp)
                    ),
                    verticalArrangement = Arrangement.spacedBy((screenWidth * 0.032f).coerceIn(10.dp, 14.dp))
                ) {
                    items(pedidos) { pedido ->
                        PedidoCard(
                            pedido = pedido,
                            onClick = { pedidoSeleccionado = pedido },
                            screenWidth = screenWidth
                        )
                    }
                }
            }
        }
    }

    pedidoSeleccionado?.let { pedido ->
        PedidoDetalleDialog(
            pedido = pedido,
            onDismiss = { pedidoSeleccionado = null },
            screenWidth = screenWidth
        )
    }
}

// Estado vacío cuando no hay pedidos
@Composable
fun EmptyPedidosState(navController: NavController, screenWidth: androidx.compose.ui.unit.Dp) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.ShoppingBag,
            contentDescription = null,
            tint = Color(0xFF23AA49),
            modifier = Modifier.size((screenWidth * 0.24f).coerceIn(80.dp, 100.dp))
        )
        Spacer(modifier = Modifier.height((screenWidth * 0.06f).coerceIn(20.dp, 28.dp)))
        Text(
            "No tienes pedidos aún",
            fontSize = (screenWidth.value * 0.06f).coerceIn(22f, 26f).sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF05161B)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Tus pedidos aparecerán aquí",
            fontSize = (screenWidth.value * 0.042f).coerceIn(15f, 18f).sp,
            color = Color(0xFF969899),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height((screenWidth * 0.08f).coerceIn(28.dp, 36.dp)))
        Button(
            onClick = {
                navController.navigate("home") {
                    launchSingleTop = true
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF23AA49)
            ),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .padding(horizontal = (screenWidth * 0.08f).coerceIn(28.dp, 36.dp))
                .height((screenWidth * 0.14f).coerceIn(52.dp, 64.dp))
        ) {
            Text(
                "Ir a Comprar",
                fontSize = (screenWidth.value * 0.045f).coerceIn(16f, 20f).sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Tarjeta de pedido individual
@Composable
fun PedidoCard(
    pedido: Pedido,
    onClick: () -> Unit,
    screenWidth: androidx.compose.ui.unit.Dp
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding((screenWidth * 0.04f).coerceIn(12.dp, 18.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = Color(0xFF23AA49),
                        modifier = Modifier.size((screenWidth * 0.058f).coerceIn(20.dp, 24.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        pedido.numeroPedido,
                        fontSize = (screenWidth.value * 0.045f).coerceIn(16f, 19f).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF05161B)
                    )
                }

                Box(
                    modifier = Modifier
                        .background(
                            Color(pedido.estado.color).copy(alpha = 0.15f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        pedido.estado.displayName,
                        fontSize = (screenWidth.value * 0.032f).coerceIn(11f, 13f).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(pedido.estado.color)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFE8E8E8), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    icon = Icons.Default.CalendarToday,
                    label = pedido.getFechaFormateada(),
                    screenWidth = screenWidth
                )
                InfoItem(
                    icon = Icons.Default.ShoppingCart,
                    label = "${pedido.getCantidadProductos()} productos",
                    screenWidth = screenWidth
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Total:",
                    fontSize = (screenWidth.value * 0.038f).coerceIn(14f, 16f).sp,
                    color = Color(0xFF969899)
                )
                Text(
                    "$${pedido.total.formatPrecio()}",
                    fontSize = (screenWidth.value * 0.05f).coerceIn(18f, 22f).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF23AA49)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Ver Detalles",
                    fontSize = (screenWidth.value * 0.038f).coerceIn(14f, 16f).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF23AA49)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFF23AA49),
                    modifier = Modifier.size((screenWidth * 0.053f).coerceIn(18.dp, 22.dp))
                )
            }
        }
    }
}

// Item pequeño de información
@Composable
fun InfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, screenWidth: androidx.compose.ui.unit.Dp) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color(0xFF969899),
            modifier = Modifier.size((screenWidth * 0.042f).coerceIn(14.dp, 18.dp))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            label,
            fontSize = (screenWidth.value * 0.035f).coerceIn(12f, 14f).sp,
            color = Color(0xFF969899)
        )
    }
}

// Diálogo de detalle del pedido
@Composable
fun PedidoDetalleDialog(
    pedido: Pedido,
    onDismiss: () -> Unit,
    screenWidth: androidx.compose.ui.unit.Dp
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .wrapContentHeight()
                .heightIn(max = (screenWidth * 2.2f).coerceIn(400.dp, 650.dp))
        ) {
            LazyColumn(
                modifier = Modifier.padding(
                    horizontal = (screenWidth * 0.055f).coerceIn(18.dp, 24.dp),
                    vertical = (screenWidth * 0.05f).coerceIn(16.dp, 22.dp)
                )
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFF23AA49).copy(alpha = 0.1f),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(
                                horizontal = (screenWidth * 0.045f).coerceIn(14.dp, 20.dp),
                                vertical = (screenWidth * 0.04f).coerceIn(12.dp, 18.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Detalle del Pedido",
                                    fontSize = (screenWidth.value * 0.052f).coerceIn(18f, 22f).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF05161B)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    pedido.numeroPedido,
                                    fontSize = (screenWidth.value * 0.055f).coerceIn(19f, 23f).sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF23AA49)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size((screenWidth * 0.085f).coerceIn(30.dp, 36.dp))
                                    .background(
                                        Color.White.copy(alpha = 0.9f),
                                        CircleShape
                                    )
                                    .clip(CircleShape)
                                    .clickable(onClick = onDismiss),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Cerrar",
                                    tint = Color(0xFF7C7C7C),
                                    modifier = Modifier.size((screenWidth * 0.055f).coerceIn(18.dp, 24.dp))
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height((screenWidth * 0.04f).coerceIn(12.dp, 18.dp)))
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFFF8F9FA),
                                RoundedCornerShape(12.dp)
                            )
                            .padding((screenWidth * 0.035f).coerceIn(10.dp, 16.dp)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color(0xFF23AA49),
                                modifier = Modifier.size((screenWidth * 0.045f).coerceIn(14.dp, 18.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                pedido.getFechaFormateada(),
                                fontSize = (screenWidth.value * 0.033f).coerceIn(11f, 14f).sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF7C7C7C)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(
                                    Color(pedido.estado.color).copy(alpha = 0.12f),
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(
                                    horizontal = (screenWidth * 0.038f).coerceIn(12.dp, 16.dp),
                                    vertical = (screenWidth * 0.02f).coerceIn(6.dp, 9.dp)
                                )
                        ) {
                            Text(
                                pedido.estado.displayName,
                                fontSize = (screenWidth.value * 0.033f).coerceIn(11f, 14f).sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(pedido.estado.color)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height((screenWidth * 0.04f).coerceIn(12.dp, 18.dp)))
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF8F9FA)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding((screenWidth * 0.045f).coerceIn(14.dp, 20.dp))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = (screenWidth * 0.032f).coerceIn(10.dp, 14.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size((screenWidth * 0.1f).coerceIn(36.dp, 42.dp))
                                        .background(
                                            Color(0xFF23AA49).copy(alpha = 0.12f),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = Color(0xFF23AA49),
                                        modifier = Modifier.size((screenWidth * 0.055f).coerceIn(18.dp, 24.dp))
                                    )
                                }
                                Spacer(modifier = Modifier.width((screenWidth * 0.032f).coerceIn(10.dp, 14.dp)))
                                Text(
                                    "Datos de Entrega",
                                    fontSize = (screenWidth.value * 0.045f).coerceIn(15f, 18f).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF05161B)
                                )
                            }

                            DetalleFieldMejorado("Nombre", pedido.datosEntrega.nombre, Icons.Default.Person, screenWidth)
                            Spacer(modifier = Modifier.height((screenWidth * 0.025f).coerceIn(8.dp, 12.dp)))
                            DetalleFieldMejorado("Teléfono", pedido.datosEntrega.telefono, Icons.Default.Phone, screenWidth)
                            Spacer(modifier = Modifier.height((screenWidth * 0.025f).coerceIn(8.dp, 12.dp)))
                            DetalleFieldMejorado("Dirección", pedido.datosEntrega.direccion, Icons.Default.LocationOn, screenWidth)
                            Spacer(modifier = Modifier.height((screenWidth * 0.025f).coerceIn(8.dp, 12.dp)))
                            DetalleFieldMejorado("Comuna", pedido.datosEntrega.comuna, Icons.Default.Place, screenWidth)
                        }
                    }

                    Spacer(modifier = Modifier.height((screenWidth * 0.04f).coerceIn(12.dp, 18.dp)))
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = (screenWidth * 0.02f).coerceIn(6.dp, 10.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .size((screenWidth * 0.1f).coerceIn(36.dp, 42.dp))
                                .background(
                                    Color(0xFF23AA49).copy(alpha = 0.12f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = Color(0xFF23AA49),
                                modifier = Modifier.size((screenWidth * 0.055f).coerceIn(18.dp, 24.dp))
                            )
                        }
                        Spacer(modifier = Modifier.width((screenWidth * 0.032f).coerceIn(10.dp, 14.dp)))
                        Text(
                            "Productos (${pedido.getCantidadProductos()})",
                            fontSize = (screenWidth.value * 0.045f).coerceIn(15f, 18f).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF05161B)
                        )
                    }
                    Spacer(modifier = Modifier.height((screenWidth * 0.025f).coerceIn(8.dp, 12.dp)))
                }

                items(pedido.items) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = (screenWidth * 0.015f).coerceIn(4.dp, 8.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding((screenWidth * 0.038f).coerceIn(12.dp, 16.dp)),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size((screenWidth * 0.095f).coerceIn(32.dp, 40.dp))
                                        .background(
                                            Color(0xFF23AA49),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "${item.quantity}x",
                                        fontSize = (screenWidth.value * 0.035f).coerceIn(12f, 15f).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width((screenWidth * 0.032f).coerceIn(10.dp, 14.dp)))
                                Column {
                                    Text(
                                        item.product.name ?: "Producto",
                                        fontSize = (screenWidth.value * 0.04f).coerceIn(14f, 17f).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF05161B),
                                        maxLines = 1
                                    )
                                    if (!item.product.stock_unit.isNullOrEmpty()) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            item.product.stock_unit,
                                            fontSize = (screenWidth.value * 0.03f).coerceIn(10f, 13f).sp,
                                            color = Color(0xFF969899)
                                        )
                                    }
                                }
                            }
                            Text(
                                "$${(item.product.price * item.quantity).formatPrecio()}",
                                fontSize = (screenWidth.value * 0.045f).coerceIn(16f, 19f).sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF23AA49)
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height((screenWidth * 0.035f).coerceIn(10.dp, 16.dp)))
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF8F9FA)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding((screenWidth * 0.045f).coerceIn(14.dp, 20.dp))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = (screenWidth * 0.032f).coerceIn(10.dp, 14.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size((screenWidth * 0.1f).coerceIn(36.dp, 42.dp))
                                        .background(
                                            Color(0xFF23AA49).copy(alpha = 0.12f),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Payment,
                                        contentDescription = null,
                                        tint = Color(0xFF23AA49),
                                        modifier = Modifier.size((screenWidth * 0.055f).coerceIn(18.dp, 24.dp))
                                    )
                                }
                                Spacer(modifier = Modifier.width((screenWidth * 0.032f).coerceIn(10.dp, 14.dp)))
                                Text(
                                    "Resumen de Pago",
                                    fontSize = (screenWidth.value * 0.045f).coerceIn(15f, 18f).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF05161B)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Subtotal",
                                    fontSize = (screenWidth.value * 0.038f).coerceIn(13f, 16f).sp,
                                    color = Color(0xFF7C7C7C),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    "$${pedido.subtotal.formatPrecio()}",
                                    fontSize = (screenWidth.value * 0.038f).coerceIn(13f, 16f).sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF05161B)
                                )
                            }

                            Spacer(modifier = Modifier.height((screenWidth * 0.022f).coerceIn(6.dp, 10.dp)))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Envío",
                                    fontSize = (screenWidth.value * 0.038f).coerceIn(13f, 16f).sp,
                                    color = Color(0xFF7C7C7C),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    if (pedido.envio == 0.0) "¡GRATIS!" else "$${pedido.envio.formatPrecio()}",
                                    fontSize = (screenWidth.value * 0.038f).coerceIn(13f, 16f).sp,
                                    fontWeight = if (pedido.envio == 0.0) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (pedido.envio == 0.0) Color(0xFF23AA49) else Color(0xFF05161B)
                                )
                            }

                            Spacer(modifier = Modifier.height((screenWidth * 0.022f).coerceIn(6.dp, 10.dp)))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Método de pago",
                                    fontSize = (screenWidth.value * 0.038f).coerceIn(13f, 16f).sp,
                                    color = Color(0xFF7C7C7C),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    pedido.metodoPago,
                                    fontSize = (screenWidth.value * 0.038f).coerceIn(13f, 16f).sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF05161B)
                                )
                            }

                            Spacer(modifier = Modifier.height((screenWidth * 0.032f).coerceIn(10.dp, 14.dp)))
                            HorizontalDivider(
                                color = Color(0xFFE0E0E0),
                                thickness = 1.dp
                            )
                            Spacer(modifier = Modifier.height((screenWidth * 0.032f).coerceIn(10.dp, 14.dp)))

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
                                    "$${pedido.total.formatPrecio()}",
                                    fontSize = (screenWidth.value * 0.058f).coerceIn(21f, 25f).sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF23AA49)
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height((screenWidth * 0.04f).coerceIn(12.dp, 18.dp)))
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((screenWidth * 0.13f).coerceIn(48.dp, 56.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF23AA49)
                        ),
                        shape = RoundedCornerShape(50),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        Text(
                            "Cerrar",
                            fontSize = (screenWidth.value * 0.042f).coerceIn(15f, 18f).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height((screenWidth * 0.02f).coerceIn(6.dp, 10.dp)))
                }
            }
        }
    }
}

// Campo de detalle con icono
@Composable
fun DetalleFieldMejorado(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    screenWidth: androidx.compose.ui.unit.Dp
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color(0xFF23AA49),
            modifier = Modifier.size((screenWidth * 0.045f).coerceIn(16.dp, 20.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                label,
                fontSize = (screenWidth.value * 0.029f).coerceIn(10f, 12f).sp,
                color = Color(0xFF969899),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                value,
                fontSize = (screenWidth.value * 0.038f).coerceIn(14f, 16f).sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF05161B)
            )
        }
    }
}

// Función para formatear precios
fun Double.formatPrecio(): String {
    val asInt = this.toInt()
    return if (this == asInt.toDouble()) {
        asInt.toString().reversed().chunked(3).joinToString(".").reversed()
    } else {
        String.format(Locale.getDefault(), "%,.0f", this)
    }
}

