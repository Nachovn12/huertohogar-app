package com.huertohogar.huertohogar_app.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.huertohogar.huertohogar_app.viewmodel.CartItem
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel
import com.huertohogar.huertohogar_app.components.ScaffoldWithBottomNav
import androidx.compose.ui.platform.LocalConfiguration


// Pantalla del carrito de compras
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: ProductViewModel) {
    val cartItems by viewModel.cartItems.collectAsState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Dimensiones responsivas
    val horizontalPadding = (screenWidth * 0.06f).coerceIn(16.dp, 28.dp)
    val buttonHeight = (screenWidth * 0.14f).coerceIn(52.dp, 64.dp)

    Box(modifier = Modifier.fillMaxSize()) {
        ScaffoldWithBottomNav(
            navController = navController,
            viewModel = viewModel,
            currentRoute = "cart",
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Carro 🧺", fontWeight = FontWeight.Bold, color = Color(0xFF05161B)) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { paddingValues ->
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    if (cartItems.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Tu carrito está vacío",
                                fontSize = (screenWidth.value * 0.045f).coerceIn(16f, 20f).sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = 16.dp)
                        ) {
                            items(cartItems, key = { it.product.sku }) { item ->
                                SwipeToDeleteCartItem(
                                    item = item,
                                    onDelete = { viewModel.removeFromCart(item.product.sku) },
                                    onQuantityChange = { newQuantity ->
                                        viewModel.updateQuantity(item.product.sku, newQuantity)
                                    }
                                )
                                HorizontalDivider(
                                    color = Color(0xFFF1F1F5),
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }
                        }
                    }
                }

                // Resumen de compra
                if (cartItems.isNotEmpty()) {
                    val subtotal = cartItems.sumOf { it.product.price * it.quantity }
                    val envio = if (subtotal >= 30000) 0.0 else 2500.0
                    val total = subtotal + envio

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF8F9FA))
                            .padding(horizontal = horizontalPadding)
                            .padding(vertical = (screenWidth * 0.04f).coerceIn(12.dp, 18.dp))
                    ) {
                        // Subtotal
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Subtotal",
                                color = Color(0xFF7C7C7C),
                                fontSize = (screenWidth.value * 0.038f).coerceIn(14f, 16f).sp
                            )
                            Text(
                                "$${subtotal.formatPrecio()}",
                                color = Color(0xFF05161B),
                                fontSize = (screenWidth.value * 0.038f).coerceIn(14f, 16f).sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Envío
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Envío",
                                color = Color(0xFF7C7C7C),
                                fontSize = (screenWidth.value * 0.038f).coerceIn(14f, 16f).sp
                            )
                            Text(
                                if (envio == 0.0) "¡Gratis!" else "$${envio.formatPrecio()}",
                                color = if (envio == 0.0) Color(0xFF23AA49) else Color(0xFF05161B),
                                fontSize = (screenWidth.value * 0.038f).coerceIn(14f, 16f).sp,
                                fontWeight = if (envio == 0.0) FontWeight.Bold else FontWeight.Medium
                            )
                        }

                        if (envio > 0.0 && subtotal < 30000) {
                            Text(
                                "¡Agrega $${(30000 - subtotal).formatPrecio()} más para envío gratis!",
                                color = Color(0xFF23AA49),
                                fontSize = (screenWidth.value * 0.032f).coerceIn(11f, 13f).sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        HorizontalDivider(
                            color = Color(0xFFE0E0E0),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )

                        // Total
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Total",
                                color = Color(0xFF05161B),
                                fontSize = (screenWidth.value * 0.045f).coerceIn(16f, 19f).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "$${total.formatPrecio()}",
                                color = Color(0xFF23AA49),
                                fontSize = (screenWidth.value * 0.05f).coerceIn(18f, 22f).sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botón Finalizar Compra
                        Button(
                            onClick = {
                                navController.navigate("checkout") {
                                    launchSingleTop = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(buttonHeight),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23AA49)),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Finalizar Compra",
                                    color = Color.White,
                                    fontSize = (screenWidth.value * 0.045f).coerceIn(16f, 20f).sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "💳",
                                    fontSize = (screenWidth.value * 0.05f).coerceIn(18f, 22f).sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Componente deslizable para eliminar items
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteCartItem(
    item: CartItem,
    onDelete: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> Color(0xFFFF314A)
                    else -> Color.Transparent
                }, label = "swipe_background_color"
            )
            val scale by animateFloatAsState(
                if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) 1.3f else 0.8f,
                label = "swipe_icon_scale"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, RoundedCornerShape(12.dp))
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.White,
                    modifier = Modifier.scale(scale)
                )
            }
        },
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true
    ) {
        CartItemRow(item = item, onQuantityChange = onQuantityChange)
    }
}

// Fila individual de item en el carrito
@Composable
fun CartItemRow(item: CartItem, onQuantityChange: (Int) -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Tamaños responsivos
    val imageSize = (screenWidth * 0.15f).coerceIn(54.dp, 70.dp)
    val buttonSize = (screenWidth * 0.09f).coerceIn(32.dp, 40.dp)
    val fontSize = (screenWidth.value * 0.04f).coerceIn(14f, 17f).sp
    val quantityFontSize = (screenWidth.value * 0.045f).coerceIn(16f, 20f).sp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp)
    ) {
        AsyncImage(
            model = item.product.imageUrl,
            contentDescription = item.product.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(imageSize)
                .padding(end = (screenWidth * 0.04f).coerceIn(12.dp, 18.dp))
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.product.name ?: "Producto",
                color = Color(0xFF05161B),
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp),
                maxLines = 2
            )
            Text(
                "${item.product.stock_unit ?: ""}, $${item.product.price.formatPrecio()}",
                color = Color(0xFFFF314A),
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy((screenWidth * 0.04f).coerceIn(12.dp, 18.dp))
        ) {
            IconButton(
                onClick = { onQuantityChange(item.quantity - 1) },
                modifier = Modifier.size(buttonSize)
            ) {
                Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Disminuir cantidad", tint = Color(0xFFDDDDDD))
            }
            Text(
                item.quantity.toString(),
                color = Color(0xFF05161B),
                fontSize = quantityFontSize,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = { onQuantityChange(item.quantity + 1) },
                modifier = Modifier.size(buttonSize)
            ) {
                Icon(Icons.Default.AddCircleOutline, contentDescription = "Aumentar cantidad", tint = Color(0xFF23AA49))
            }
        }
    }
}

