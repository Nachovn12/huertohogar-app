package com.huertohogar.huertohogar_app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: ProductViewModel) {
    val cartItems by viewModel.cartItems.collectAsState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Carro 🧺", fontWeight = FontWeight.Bold, color = Color(0xFF05161B)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Button(
                    onClick = { /* TODO: Navegar a la pasarela de pago */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23AA49))
                ) {
                    Text("Continuar", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "Tu carrito está vacío",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                items(cartItems) { item ->
                    CartItemRow(
                        item = item,
                        onQuantityChange = { newQuantity ->
                            viewModel.updateQuantity(item.product.id, newQuantity)
                        }
                    )
                    Divider(color = Color(0xFFF1F1F5), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onQuantityChange: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = item.product.imageUrl,
            contentDescription = item.product.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(60.dp)
                .padding(end = 16.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.product.name,
                color = Color(0xFF05161B),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                "${item.product.stock_unit ?: ""}, $${item.product.price.formatPrecio()}",
                color = Color(0xFFFF314A),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IconButton(onClick = { onQuantityChange(item.quantity - 1) }, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Disminuir cantidad", tint = Color(0xFFDDDDDD))
            }
            Text(
                item.quantity.toString(),
                color = Color(0xFF05161B),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { onQuantityChange(item.quantity + 1) }, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.AddCircleOutline, contentDescription = "Aumentar cantidad", tint = Color(0xFF23AA49))
            }
        }
    }
}

private fun Double.formatPrecio(): String {
    val asInt = this.toInt()
    return if (this == asInt.toDouble()) {
        asInt.toString().reversed().chunked(3).joinToString(".").reversed()
    } else {
        String.format("%,.2f", this)
    }
}
