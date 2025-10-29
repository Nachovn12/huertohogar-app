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
import androidx.compose.material.icons.filled.ArrowBack
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
import coil.compose.AsyncImage
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel
import java.text.Normalizer

// Función de ayuda para comparar texto ignorando tildes
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
    val productos by viewModel.products.collectAsState() // debe ser List<Product>
    // Filtro actualizado para ignorar tildes
    val productosFiltrados = productos.filter {
        it.category?.name?.unaccent()?.equals(categoria.unaccent(), ignoreCase = true) == true
    }.distinctBy { it.id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(36.dp))
        // Barra superior con título centrado
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .fillMaxWidth(),
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver atrás")
            }
            Text(
                text = categoria,
                color = Color(0xFF05161B),
                fontSize = 22.sp,
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalArrangement = Arrangement.spacedBy(11.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productosFiltrados) { producto ->
                    CategoriaProductoCardMinimal(
                        nombre = producto.name,
                        unidadMedida = producto.stock_unit ?: "", // Corregido
                        precio = producto.price,
                        imagen = producto.imageUrl ?: "",
                        onAdd = { /* lógica agregar al carrito */ },
                        onClick = { navController.navigate("detalle_producto/${producto.id}") } // Navegación añadida
                    )
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
    onClick: () -> Unit, // Parámetro onClick añadido
    modifier: Modifier = Modifier
) {
    // Diseño de tarjeta actualizado
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFFF3F5F7))
            .clickable { onClick() } // Modificador clickable añadido
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = imagen,
            contentDescription = null,
            contentScale = ContentScale.Fit, // Corregido para que la imagen se vea completa
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = nombre,
            color = Color(0xFF1B1C1E),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$unidadMedida, $${precio.formatPrecio()}",
                color = Color(0xFFFF314A),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )
            IconButton(
                onClick = onAdd,
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF23AA49), CircleShape)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

private fun Double.formatPrecio(): String {
    return if (this % 1.0 == 0.0) {
        this.toInt().toString().reversed().chunked(3).joinToString(".").reversed()
    } else {
        String.format("%,.2f", this)
    }
}
