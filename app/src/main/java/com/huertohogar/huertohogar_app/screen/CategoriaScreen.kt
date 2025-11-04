package com.huertohogar.huertohogar_app.screen

import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import coil.compose.AsyncImage
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel
import com.huertohogar.huertohogar_app.components.ScaffoldWithBottomNav
import com.huertohogar.huertohogar_app.utils.formatPrecio
import java.text.Normalizer


private val REGEX_UNACCENT = "[\\p{InCombiningDiacriticalMarks}]+".toRegex()
fun CharSequence.unaccent(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_UNACCENT.replace(temp, "")
}

@Composable
fun CategoriaScreen(
    navController: NavController,
    categoria: String,
    viewModel: ProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val productos by viewModel.products.collectAsState()

    // Si la categoría es "Todos", mostrar todos los productos ordenados alfabéticamente
    // Si no, filtrar por categoría específica
    val productosFiltrados = if (categoria.equals("Todos", ignoreCase = true)) {
        productos.distinctBy { it.sku }.sortedBy { it.name }
    } else {
        productos.filter {
            it.category?.unaccent()?.equals(categoria.unaccent(), ignoreCase = true) == true
        }.distinctBy { it.sku }.sortedBy { it.name }
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val horizontalPadding = (screenWidth * 0.04f).coerceIn(12.dp, 20.dp)

    ScaffoldWithBottomNav(
        navController = navController,
        viewModel = viewModel,
        currentRoute = "categoria/$categoria"
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Spacer(modifier = Modifier.height((screenWidth * 0.06f).coerceIn(20.dp, 28.dp)))
                Box(
                    modifier = Modifier
                        .padding(horizontal = horizontalPadding, vertical = 4.dp)
                        .fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver atrás")
                    }
                    Text(
                        text = if (categoria.equals("Todos", ignoreCase = true)) "Todos los Productos" else categoria,
                        color = Color(0xFF05161B),
                        fontSize = (screenWidth.value * 0.055f).coerceIn(20f, 24f).sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = { /* Buscar o acción futura */ },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                }

                // Subtítulo con cantidad de productos
                Text(
                    text = "${productosFiltrados.size} productos",
                    color = Color(0xFF969899),
                    fontSize = (screenWidth.value * 0.032f).coerceIn(12f, 14f).sp,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                )

                Spacer(modifier = Modifier.height(4.dp))
                if (productosFiltrados.isEmpty()) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay productos en esta categoría.", color = Color(0xFF969899))
                    }
                } else {
                    // Grid responsivo - usa GridCells.Adaptive para ajustarse automáticamente
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = (screenWidth * 0.42f).coerceIn(150.dp, 180.dp)),
                        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy((screenWidth * 0.045f).coerceIn(14.dp, 20.dp)),
                        horizontalArrangement = Arrangement.spacedBy((screenWidth * 0.028f).coerceIn(9.dp, 14.dp)),
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        items(productosFiltrados) { producto ->
                            CategoriaProductoCardMinimal(
                                nombre = producto.name,
                                unidadMedida = producto.stock_unit ?: "",
                                precio = producto.price,
                                imagen = producto.imageUrl ?: "",
                                onAdd = { viewModel.addToCart(producto) },
                                onClick = { navController.navigate("detalle_producto/${producto.sku}") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoriaProductoCardMinimal(
    nombre: String,
    unidadMedida: String,
    precio: Double,
    imagen: String,
    onAdd: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Tamaños responsivos
    val cardPadding = (screenWidth * 0.03f).coerceIn(10.dp, 14.dp)
    val addButtonSize = (screenWidth * 0.08f).coerceIn(28.dp, 36.dp)
    val iconSize = (addButtonSize * 0.625f)

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFFF3F5F7))
            .clickable { onClick() }
            .padding(cardPadding)
            .fillMaxWidth()
    ) {
        // Imagen con AspectRatio para mantener proporciones
        AsyncImage(
            model = imagen,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height((screenWidth * 0.02f).coerceIn(6.dp, 10.dp)))
        Text(
            text = nombre,
            color = Color(0xFF1B1C1E),
            fontWeight = FontWeight.Bold,
            fontSize = (screenWidth.value * 0.04f).coerceIn(14f, 17f).sp,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height((screenWidth * 0.02f).coerceIn(6.dp, 10.dp)))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$unidadMedida, $${precio.formatPrecio()}",
                color = Color(0xFFFF314A),
                fontWeight = FontWeight.Bold,
                fontSize = (screenWidth.value * 0.035f).coerceIn(12f, 15f).sp,
            )
            IconButton(
                onClick = onAdd,
                modifier = Modifier
                    .size(addButtonSize)
                    .background(Color(0xFF23AA49), CircleShape)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar",
                    tint = Color.White,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}
