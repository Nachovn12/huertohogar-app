package com.huertohogar.huertohogar_app.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    navController: NavController,
    productId: Long,
    viewModel: ProductViewModel
) {
    val product by viewModel.selectedProduct.collectAsState()
    var cantidad by remember { mutableStateOf(1) }

    LaunchedEffect(productId) { viewModel.loadProductById(productId) }

    product?.let { prod ->
        Scaffold(
            containerColor = Color(0xFFF6F6F6),
            bottomBar = {
                Button(
                    onClick = { /* Acción de continuar */ },
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
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Bar
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                    IconButton(onClick = { /* Acción de búsqueda */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                }

                // Imagen del producto
                AsyncImage(
                    model = prod.imageUrl,
                    contentDescription = prod.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.2f)
                        .padding(horizontal = 32.dp)
                )
                Spacer(Modifier.height(16.dp))

                // Contenido de información
                Column(Modifier.padding(horizontal = 24.dp)) {
                    Text(
                        prod.name,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B1C1E)
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "${prod.stock_unit ?: ""}, $${prod.price.formatPrecio()}",
                            fontSize = 20.sp,
                            color = Color(0xFFFF314A),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.weight(1f))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { if (cantidad > 1) cantidad-- },
                                enabled = cantidad > 1
                            ) {
                                Icon(Icons.Default.RemoveCircleOutline, tint = Color(0xFFDDDDDD), contentDescription = "Menos")
                            }
                            Text("$cantidad", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { cantidad++ }) {
                                Icon(Icons.Default.AddCircleOutline, tint = Color(0xFF23AA49), contentDescription = "Más")
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        prod.description ?: "",
                        color = Color(0xFF969899),
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                    Spacer(Modifier.height(24.dp))

                    // Características
                    Column {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            FeatureBox(textTop = prod.season ?: "-", textBottom = "Temporada", emoji = "🌱", modifier = Modifier.weight(1f))
                            FeatureBox(textTop = prod.difficulty ?: "-", textBottom = "Dificultad", emoji = "💪", modifier = Modifier.weight(1f))
                        }
                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            FeatureBox(textTop = prod.plantingDepth ?: "-", textBottom = "Profundidad", emoji = "🌿", modifier = Modifier.weight(1f))
                            FeatureBox(textTop = prod.harvestTime ?: "-", textBottom = "Cosecha", emoji = "⏱️", modifier = Modifier.weight(1f))
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    } ?: Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun FeatureBox(
    textTop: String,
    textBottom: String,
    emoji: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp))
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(emoji, fontSize = 22.sp, modifier = Modifier.padding(bottom = 2.dp))
        Text(textTop, color = Color(0xFF23AA49), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(textBottom, color = Color(0xFF969899), fontSize = 13.sp)
    }
}

private fun Double.formatPrecio(): String {
    return if (this % 1.0 == 0.0) {
        this.toInt().toString().reversed().chunked(3).joinToString(".").reversed()
    } else {
        String.format("%,.2f", this) // Fallback for decimals
    }
}
