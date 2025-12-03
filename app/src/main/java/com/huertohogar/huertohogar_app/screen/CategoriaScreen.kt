package com.huertohogar.huertohogar_app.screen

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Save // Icono para guardar
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.* // Importamos componentes Material3
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.huertohogar.huertohogar_app.R
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel
import com.huertohogar.huertohogar_app.components.ScaffoldWithBottomNav
import com.huertohogar.huertohogar_app.utils.formatPrecio
import com.huertohogar.huertohogar_app.utils.getResponsiveDimensions
import java.text.Normalizer

private val REGEX_UNACCENT = "[\\p{InCombiningDiacriticalMarks}]+".toRegex()
fun CharSequence.unaccent(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_UNACCENT.replace(temp, "")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriaScreen(
    navController: NavController,
    categoria: String,
    viewModel: ProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val productos by viewModel.products.collectAsState()
    val dimens = getResponsiveDimensions()
    val context = LocalContext.current

    // Filtrado de productos
    val productosFiltrados = if (categoria.equals("Todos", ignoreCase = true)) {
        productos.distinctBy { it.sku }.shuffled()
    } else {
        productos.filter {
            it.category?.unaccent()?.equals(categoria.unaccent(), ignoreCase = true) == true
        }.distinctBy { it.sku }.sortedBy { it.name }
    }

    ScaffoldWithBottomNav(
        navController = navController,
        viewModel = viewModel,
        currentRoute = "categoria/$categoria"
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // --- BARRA SUPERIOR PERSONALIZADA ---
                Spacer(modifier = Modifier.height(dimens.spacingLarge))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimens.paddingMedium, vertical = dimens.spacingTiny),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Botón Atrás
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás",
                            modifier = Modifier.size(dimens.iconMedium),
                            tint = Color(0xFF05161B)
                        )
                    }

                    // Título
                    Text(
                        text = if (categoria.equals("Todos", ignoreCase = true)) "Todos los Productos" else categoria,
                        color = Color(0xFF05161B),
                        fontSize = if (categoria.equals("Todos", ignoreCase = true)) (dimens.textTitle * 0.85f) else dimens.textTitle,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                        softWrap = false
                    )

                    // --- BOTÓN DE GUARDAR (Requerimiento Rúbrica) ---
                    // Solo se muestra si hay productos en la lista
                    if (productosFiltrados.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                // Llamada a la función de guardar en BD
                                viewModel.saveProductsToDb({
                                    Toast.makeText(context, "¡Productos guardados localmente! 💾", Toast.LENGTH_SHORT).show()
                                }, { errMsg ->
                                    Toast.makeText(context, "Error al guardar: $errMsg", Toast.LENGTH_SHORT).show()
                                })
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = "Almacenar en Base de Datos Local",
                                modifier = Modifier.size(dimens.iconMedium),
                                tint = Color(0xFF23AA49) // Color verde de la marca
                            )
                        }
                    }
                }

                // Subtítulo con cantidad
                Text(
                    text = "${productosFiltrados.size} productos",
                    color = Color(0xFF969899),
                    fontSize = dimens.textSmall,
                    modifier = Modifier.padding(horizontal = dimens.paddingMedium)
                )

                Spacer(modifier = Modifier.height(dimens.spacingTiny))

                // --- LISTA DE PRODUCTOS ---
                if (productosFiltrados.isEmpty()) {
                    Box(
                        Modifier.fillMaxWidth().weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No hay productos disponibles.",
                            color = Color(0xFF969899),
                            fontSize = dimens.textMedium
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = (dimens.screenWidth * 0.42f).coerceIn(150.dp, 180.dp)),
                        contentPadding = PaddingValues(horizontal = dimens.paddingMedium, vertical = dimens.spacingMedium),
                        verticalArrangement = Arrangement.spacedBy(dimens.spacingMedium),
                        horizontalArrangement = Arrangement.spacedBy(dimens.spacingSmall),
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        items(productosFiltrados, key = { it.sku }) { producto ->
                            CategoriaProductoCardMinimal(
                                sku = producto.sku ?: "SIN-SKU",
                                nombre = producto.name ?: "Producto",
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
    sku: String,
    nombre: String,
    unidadMedida: String,
    precio: Double,
    imagen: String,
    onAdd: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = getResponsiveDimensions()
    val addButtonSize = (dimens.screenWidth * 0.08f).coerceIn(28.dp, 36.dp)
    val iconSize = (addButtonSize * 0.625f)

    // Usamos Card de Material3 para cumplir con el criterio de UI/UX "uso correcto de Cards"
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(dimens.cornerRadiusMedium),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp), // Elevación sutil
        colors = CardDefaults.cardColors(containerColor = Color.White) // Fondo blanco limpio
    ) {
        Column(
            modifier = Modifier.padding(dimens.paddingSmall)
        ) {
            // Imagen con manejo de errores (Placeholder)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(dimens.cornerRadiusMedium))
                    .background(Color(0xFFF3F5F7)), // Fondo gris claro para la imagen
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imagen)
                        .crossfade(true)
                        .error(R.drawable.logo_huerto_hogar) // Placeholder si falla la URL
                        .placeholder(R.drawable.logo_huerto_hogar) // Placeholder mientras carga
                        .build(),
                    contentDescription = nombre,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(dimens.spacingTiny))

            // SKU (Requisito de rúbrica: mostrar SKU en el ítem)
            Text(
                text = "SKU: $sku",
                color = Color.Gray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )

            // Nombre del producto
            Text(
                text = nombre,
                color = Color(0xFF1B1C1E),
                fontWeight = FontWeight.Bold,
                fontSize = dimens.textMedium,
                maxLines = 1,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(dimens.spacingTiny))

            // Precio y Botón Agregar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    if (unidadMedida.isNotEmpty()) {
                        Text(
                            text = unidadMedida,
                            color = Color(0xFF6B6E70),
                            fontSize = 10.sp
                        )
                    }
                    Text(
                        text = "$ ${precio.formatPrecio()}", // Formato chileno
                        color = Color(0xFFFF314A),
                        fontWeight = FontWeight.Bold,
                        fontSize = dimens.textSmall,
                    )
                }

                IconButton(
                    onClick = onAdd,
                    modifier = Modifier
                        .size(addButtonSize)
                        .background(Color(0xFF23AA49), CircleShape)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Agregar al carrito",
                        tint = Color.White,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        }
    }
}