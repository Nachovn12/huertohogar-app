package com.huertohogar.huertohogar_app.screen

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import java.util.Locale
import androidx.compose.ui.text.font.FontFamily

private const val SHOW_FONT_DEBUG = true

@Composable
fun FontDebugSample() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp, vertical = 8.dp)) {
        Text("DM Sans (MaterialTheme): ejemplo", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Default font (fallback): ejemplo", fontFamily = FontFamily.Default, style = MaterialTheme.typography.titleLarge)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: ProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val products by viewModel.products.collectAsState()
    val bestSellers = products.filter { 
        it.name == "Espinacas Frescas" || it.name == "Leche Entera"
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf("Concepción") }

    // estado para recibir la posición vertical (en px) del centro del banner
    var bannerCenterYPx by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) { viewModel.loadProducts() }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Elipse decorativa responsiva: empieza en la parte superior y es bastante más ancha que la pantalla
        Canvas(modifier = Modifier.fillMaxSize()) {
            // factores ajustables: ancho relativo y alto relativo respecto a la pantalla
            val widthFactor = 2.2f
            val verticalShiftFactor = 0.47f // ligero ajuste hacia abajo: bajada muy pequeña (antes 0.50)

            val ellipseWidthPx = size.width * widthFactor
            // si ya medimos el banner, usamos bannerCenterYPx para que el centro del óvalo coincida con la mitad del banner
            val ellipseHeightPx = if (bannerCenterYPx > 0f) {
                // queremos que el centro del óvalo esté en bannerCenterYPx -> entonces altura = centerY * 2
                bannerCenterYPx * 2f
            } else {
                // fallback mientras no se mida: 60% de la altura
                size.height * 0.6f
            }

            // centrar horizontalmente: calcular left para que el óvalo quede centrado
            val left = (size.width - ellipseWidthPx) / 2f
            // desplazar la elipse hacia arriba (valor negativo) para que la curva esté más alta
            val top = -ellipseHeightPx * verticalShiftFactor

            drawOval(
                color = Color(0xFFF3F5F7),
                topLeft = androidx.compose.ui.geometry.Offset(left, top),
                size = androidx.compose.ui.geometry.Size(ellipseWidthPx, ellipseHeightPx)
            )
        }

        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = { AppBottomNavigationBar(navController, viewModel) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                HomeHeader(selectedCity = selectedCity, onLocationClick = { showBottomSheet = true })
                if (SHOW_FONT_DEBUG) FontDebugSample()
                SearchBar()
                // pasar callback para medir el centro del banner
                BannerOferta(onMeasuredCenterY = { centerY -> bannerCenterYPx = centerY })
                CategoriesRow(navController)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Mas Vendido 🔥",
                        color = Color(0xFF05161B),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text("Ver todo", color = Color(0xFF23AA49), style = MaterialTheme.typography.bodyMedium)
                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(bestSellers) { product ->
                        BestSellerCard(product, viewModel) {
                            navController.navigate("detalle_producto/${product.id}")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (showBottomSheet) {
                LocationPickerBottomSheet(
                    sheetState = sheetState,
                    selectedCity = selectedCity,
                    onDismiss = { showBottomSheet = false },
                    onCitySelected = { city ->
                        selectedCity = city
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) { showBottomSheet = false }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun HomeHeader(selectedCity: String, onLocationClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 24.dp, start = 24.dp, end = 16.dp, bottom = 8.dp)
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/2vg760JCpJ/gs64j45e_expires_30_days.png",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(end = 11.dp).size(44.dp).clip(CircleShape)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text("Buen día", color = Color(0xFF969899), style = MaterialTheme.typography.bodySmall)
            Text("Valentina López", color = Color(0xFF05161B), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color.White, RoundedCornerShape(50))
                .clickable { onLocationClick() }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Contenedor pequeño para el icono de ubicación: asegura que se vea completo
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEFF6F3)), // sutil fondo para contrastar
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/2vg760JCpJ/dv8s1034_expires_30_days.png",
                    contentDescription = "Icono ubicación",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(18.dp) // tamaño algo menor que el contenedor para que no se corte
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(selectedCity, color = Color(0xFF05161B), style = MaterialTheme.typography.bodySmall)
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Cambiar ciudad", tint = Color(0xFF05161B))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerBottomSheet(
    sheetState: SheetState,
    selectedCity: String,
    onDismiss: () -> Unit,
    onCitySelected: (String) -> Unit
) {
    val cities = listOf("Santiago", "Puerto Montt", "Villarica", "Nacimiento", "Viña del Mar", "Valparaíso", "Concepción")

    ModalBottomSheet(onDismissRequest = { onDismiss() }, sheetState = sheetState) {
        Text(
            "Elige tu ciudad",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn {
            items(cities) { city ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCitySelected(city) }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(city, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                    if (city == selectedCity) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = "Selected", tint = Color(0xFF23AA49))
                    } else {
                        Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = null, tint = Color.LightGray)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 20.dp, start = 24.dp, end = 24.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.White)
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 24.dp)
    ) {
        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color(0xFF23AA49))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "Categoría de búsqueda",
            color = Color(0xFF969899),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun BannerOferta(onMeasuredCenterY: (Float) -> Unit = {}) {
    Box(
        modifier = Modifier
            .padding(bottom = 8.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFE5F8EC))
            .onGloballyPositioned { coords: LayoutCoordinates ->
                // posición y en píxeles dentro del root
                val position = coords.positionInRoot()
                val centerY = position.y + coords.size.height / 2f
                onMeasuredCenterY(centerY)
            }
    ) {
        AsyncImage(
            model = "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/2vg760JCpJ/bpwraoyn_expires_30_days.png",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
    }
}

@Composable
fun CategoriesRow(navController: NavController) {
    Column(modifier = Modifier.padding(start = 24.dp, bottom = 6.dp, end = 24.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text("Categorías", color = Color(0xFF05161B), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Spacer(Modifier.weight(1f))
            Text("Ver todo", color = Color(0xFF23AA49), style = MaterialTheme.typography.bodyMedium)
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val categories = listOf(
                "Frutas" to "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/2vg760JCpJ/l1py66oy_expires_30_days.png",
                "Verduras" to "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/2vg760JCpJ/zm6wb1sf_expires_30_days.png",
                "Orgánicos" to "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/2vg760JCpJ/kvisq9dj_expires_30_days.png",
                "Lácteos" to "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/2vg760JCpJ/r3kz6ssi_expires_30_days.png"
            )
            items(categories) { (title, img) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { navController.navigate("categoria/$title") }
                ) {
                    Box(
                        modifier = Modifier
                            .size(73.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF3F5F7)),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = img,
                            contentDescription = null,
                            modifier = Modifier.size(45.dp)
                        )
                    }
                    Text(title, color = Color(0xFF05161B), style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}

@Composable
fun BestSellerCard(product: com.huertohogar.huertohogar_app.model.Product, viewModel: ProductViewModel, onClick: () -> Unit) {
    // Tarjeta más fiel al prototipo: ajustar tamaños y orden visual
    Box(modifier = Modifier.width(180.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(Color.White)
                .clickable { onClick() }
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp) // imagen más alta para parecerse al prototipo
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF3F5F7)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = product.imageUrl ?: "",
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(100.dp) // tamaño controlado dentro del contenedor para mantener proporción
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                product.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF1B1C1E),
                maxLines = 1
            )

            Spacer(Modifier.height(8.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // unidad en gris y precio en rojo/bold (precio más grande)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = product.stock_unit ?: "",
                        color = Color(0xFF6B6E70),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$${product.price.formatPrecio()}",
                        color = Color(0xFFFF314A),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // espacio reservado para el botón; el botón real está superpuesto fuera del flujo
                Spacer(modifier = Modifier.width(48.dp))
            }
        }

        // Botón verde circular superpuesto en la esquina inferior derecha (más grande y sobresaliente)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-12).dp, y = 12.dp) // sobresale más como en el prototipo
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(Color(0xFF23AA49), CircleShape)
                    .clickable { viewModel.addToCart(product) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White, modifier = Modifier.size(24.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomNavigationBar(navController: NavController, viewModel: ProductViewModel) { 
    var selectedItem by remember { mutableStateOf(0) }
    val totalCartItems by viewModel.totalCartItems.collectAsState()
    val items = listOf("Home", "Categorias", "", "Pedidos", "Perfil")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.GridView, Icons.Filled.ShoppingCart, Icons.Filled.CalendarToday, Icons.Filled.Person)

    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 4.dp, // Añadir elevación para la sombra
            modifier = Modifier.align(Alignment.BottomCenter).height(80.dp)
        ) {
            items.forEachIndexed { index, screen ->
                 NavigationBarItem(
                    icon = { Icon(icons[index], contentDescription = screen, modifier = Modifier.size(28.dp)) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF05161B),
                        unselectedIconColor = Color(0xFFE0E0E0),
                        indicatorColor = Color.White
                    )
                )
            }
        }

        Box(modifier = Modifier.align(Alignment.TopCenter)) {
            FloatingActionButton(
                onClick = { navController.navigate("cart") }, 
                containerColor = Color(0xFF23AA49),
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(Icons.Filled.ShoppingBasket, "Carrito", tint = Color.White, modifier = Modifier.size(32.dp))
            }
            if (totalCartItems > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 4.dp, end = 4.dp)
                        .size(20.dp)
                        .background(Color(0xFFFF314A), CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(totalCartItems.toString(), color = Color.White, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

private fun Double.formatPrecio(): String {
    return if (this % 1.0 == 0.0) {
        this.toInt().toString().reversed().chunked(3).joinToString(".").reversed()
    } else {
        String.format(Locale.getDefault(), "%,.2f", this)
    }
}
