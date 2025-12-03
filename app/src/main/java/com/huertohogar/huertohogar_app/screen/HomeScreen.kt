package com.huertohogar.huertohogar_app.screen

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.huertohogar.huertohogar_app.R
import com.huertohogar.huertohogar_app.model.Product
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel
import com.huertohogar.huertohogar_app.viewmodel.UserViewModel
import com.huertohogar.huertohogar_app.components.ScaffoldWithBottomNav
import com.huertohogar.huertohogar_app.utils.formatPrecio
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import java.text.Normalizer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: ProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    userViewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val products by viewModel.products.collectAsState()
    val userProfile by userViewModel.userProfile.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    // Estado del Drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Función para normalizar texto eliminando tildes
    fun String.removeAccents(): String {
        val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
        return normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    }

    // Filtrar productos según la búsqueda (con y sin tildes)
    val filteredProducts = remember(products, searchQuery) {
        if (searchQuery.isBlank()) {
            products
        } else {
            val normalizedQuery = searchQuery.removeAccents().lowercase()
            products.filter { product ->
                (product.name?.removeAccents()?.lowercase()?.contains(normalizedQuery) == true) ||
                (product.category?.removeAccents()?.lowercase()?.contains(normalizedQuery) == true) ||
                (product.description?.removeAccents()?.lowercase()?.contains(normalizedQuery) == true)
            }
        }
    }

    val bestSellers = products.filter {
        it.name == "Espinacas Frescas" || it.name == "Leche Entera"
    }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf("Concepción") }
    var bannerCenterYPx by remember { mutableStateOf(0f) }

    // ModalNavigationDrawer envuelve todo el contenido
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HuertoHogarDrawerContent(
                navController = navController,
                userProfile = userProfile,
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        },
        gesturesEnabled = true
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val widthFactor = 2.2f
                val verticalShiftFactor = 0.47f
                val ellipseWidthPx = size.width * widthFactor
                val ellipseHeightPx = if (bannerCenterYPx > 0f) {
                    bannerCenterYPx * 2f
                } else {
                    size.height * 0.6f
                }
                val left = (size.width - ellipseWidthPx) / 2f
                val top = -ellipseHeightPx * verticalShiftFactor

                drawOval(
                    color = Color(0xFFF3F5F7),
                    topLeft = androidx.compose.ui.geometry.Offset(left, top),
                    size = androidx.compose.ui.geometry.Size(ellipseWidthPx, ellipseHeightPx)
                )
            }

            ScaffoldWithBottomNav(
                navController = navController,
                viewModel = viewModel,
                currentRoute = "home"
            ) { paddingValues ->
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                    ) {
                        HomeHeader(
                            greeting = userViewModel.getGreeting(),
                            greetingEmoji = userViewModel.getGreetingEmoji(),
                            userName = userProfile.nombre,
                            selectedCity = selectedCity,
                            onLocationClick = { showBottomSheet = true },
                            onMenuClick = {
                                scope.launch { drawerState.open() }
                            }
                        )

                        // ============================================================
                        // ✅ BOTONES REQUERIDOS POR LA RÚBRICA
                        // ============================================================
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                        ) {
                            // 1. Botón "Cargar desde Rest API"
                            Button(
                                onClick = {
                                    // ✅ VALIDAR CONEXIÓN A INTERNET PROGRAMÁTICAMENTE (REQUISITO RÚBRICA)
                                    if (isInternetAvailable(context)) {
                                        // Si hay conexión: navegar cargando datos frescos desde la URL
                                        navController.navigate("listado_productos?source=api")
                                    } else {
                                        // Si NO hay conexión: mostrar mensaje
                                        Toast.makeText(
                                            context,
                                            "Sin conexión a Internet",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23AA49)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    Icons.Default.CloudDownload,
                                    contentDescription = "Cargar desde API",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "API Rest",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }

                            // 2. Botón "Cargar desde Base de Datos Local"
                            Button(
                                onClick = {
                                    // ✅ CONSULTAR BASE DE DATOS INTERNA (REQUISITO RÚBRICA)
                                    viewModel.loadProductsFromDb(
                                        onComplete = {
                                            // Si existen registros: navegar al Listado mostrando datos locales
                                            Toast.makeText(context, "Productos cargados desde BD Local", Toast.LENGTH_SHORT).show()
                                            navController.navigate("listado_productos?source=local")
                                        },
                                        onEmpty = {
                                            // Si NO existen registros: mostrar mensaje
                                            Toast.makeText(context, "No hay datos locales almacenados", Toast.LENGTH_LONG).show()
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B1C1E)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    Icons.Default.Storage,
                                    contentDescription = "Cargar desde BD Local",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "BD Local",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                        // ============================================================
                        // FIN BOTONES RÚBRICA
                        // ============================================================

                    SearchBar(
                        searchQuery = searchQuery,
                        onSearchQueryChange = { query ->
                            searchQuery = query
                            isSearching = query.isNotBlank()
                        },
                        onClearSearch = {
                            searchQuery = ""
                            isSearching = false
                        }
                    )

                    // Mostrar resultados de búsqueda o contenido normal
                    if (isSearching) {
                        SearchResults(
                            filteredProducts = filteredProducts,
                            searchQuery = searchQuery,
                            navController = navController,
                            viewModel = viewModel
                        )
                    } else {
                        BannerOferta(onMeasuredCenterY = { centerY -> bannerCenterYPx = centerY })
                        CategoriesRow(navController, products)
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
                                    navController.navigate("detalle_producto/${product.sku}")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }

            if (showBottomSheet) {
                LocationPickerBottomSheet(
                    sheetState = sheetState,
                    selectedCity = selectedCity,
                    onDismiss = { showBottomSheet = false },
                    onCitySelected = { city ->
                        selectedCity = city
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }
                )
            }
        }
    }
    }
}

@Composable
fun HomeHeader(
    greeting: String,
    greetingEmoji: String,
    userName: String,
    selectedCity: String,
    onLocationClick: () -> Unit,
    onMenuClick: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Dimensiones responsivas basadas en ancho de pantalla
    val avatarSize = (screenWidth * 0.11f).coerceIn(40.dp, 52.dp)
    val iconSize = (avatarSize * 0.64f)
    val horizontalPadding = (screenWidth * 0.06f).coerceIn(16.dp, 28.dp)
    val verticalPadding = (screenWidth * 0.06f).coerceIn(20.dp, 32.dp)

    Row(
        modifier = Modifier
            .padding(top = verticalPadding, start = horizontalPadding, end = horizontalPadding * 0.7f, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón del menú hamburguesa
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier.size(avatarSize)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menú",
                tint = Color(0xFF23AA49),
                modifier = Modifier.size(iconSize)
            )
        }
        
        Spacer(modifier = Modifier.width(horizontalPadding * 0.3f))
        
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    greeting,
                    color = Color(0xFF969899),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    greetingEmoji,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                userName,
                color = Color(0xFF05161B),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFF8F9FA))
                .border(
                    width = 1.dp,
                    color = Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(50)
                )
                .clickable { onLocationClick() }
                .padding(horizontal = (screenWidth * 0.03f).coerceIn(10.dp, 14.dp),
                         vertical = (screenWidth * 0.02f).coerceIn(6.dp, 10.dp))
        ) {
            Box(
                modifier = Modifier
                    .size((screenWidth * 0.07f).coerceIn(24.dp, 32.dp))
                    .clip(CircleShape)
                    .background(Color(0xFFEFF6F3)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Ubicación",
                    tint = Color(0xFF23AA49),
                    modifier = Modifier.size((screenWidth * 0.045f).coerceIn(16.dp, 20.dp))
                )
            }
            Spacer(modifier = Modifier.width((screenWidth * 0.02f).coerceIn(6.dp, 10.dp)))
            Text(selectedCity, color = Color(0xFF05161B), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Cambiar ciudad", tint = Color(0xFF23AA49))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val horizontalPadding = (screenWidth * 0.06f).coerceIn(16.dp, 28.dp)

    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        placeholder = {
            Text(
                "Buscar productos...",
                color = Color(0xFF969899),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Buscar",
                tint = Color(0xFF23AA49)
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = onClearSearch) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Limpiar búsqueda",
                        tint = Color(0xFF969899)
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = 12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color(0xFF23AA49),
            unfocusedBorderColor = Color.Transparent
        ),
        shape = RoundedCornerShape(50.dp),
        singleLine = true
    )
}

@Composable
fun SearchResults(
    filteredProducts: List<com.huertohogar.huertohogar_app.model.Product>,
    searchQuery: String,
    navController: NavController,
    viewModel: ProductViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            "Resultados para \"$searchQuery\"",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF05161B),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (filteredProducts.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Sin resultados",
                    tint = Color(0xFF969899),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "No se encontraron productos",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF969899)
                )
                Text(
                    "Intenta con otros términos de búsqueda",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF969899)
                )
            }
        } else {
            Text(
                "${filteredProducts.size} producto${if (filteredProducts.size != 1) "s" else ""} encontrado${if (filteredProducts.size != 1) "s" else ""}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF969899),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            filteredProducts.forEach { product ->
                ProductSearchItem(
                    product = product,
                    onClick = { navController.navigate("detalle_producto/${product.sku}") },
                    onAddToCart = { viewModel.addToCart(product) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ProductSearchItem(
    product: com.huertohogar.huertohogar_app.model.Product,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF3F5F7)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.imageUrl ?: "")
                        .crossfade(true)
                        .build(),
                    contentDescription = product.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    product.name ?: "Producto",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF1B1C1E),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    product.category ?: "Sin categoría",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF969899)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        product.stock_unit ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B6E70)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "$${product.price.formatPrecio()}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFFF314A)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFF23AA49), CircleShape)
                    .clickable { onAddToCart() },
                contentAlignment = Alignment.Center
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

@Composable
fun BannerOferta(onMeasuredCenterY: (Float) -> Unit = {}) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Altura responsiva del banner (40-45% del ancho de pantalla)
    val bannerHeight = (screenWidth * 0.42f).coerceIn(140.dp, 180.dp)
    val horizontalPadding = (screenWidth * 0.06f).coerceIn(16.dp, 28.dp)
    val bannerPadding = (screenWidth * 0.06f).coerceIn(20.dp, 28.dp)
    val iconSize = (bannerHeight * 0.5f).coerceIn(60.dp, 90.dp)

    Box(
        modifier = Modifier
            .padding(bottom = 8.dp, start = horizontalPadding, end = horizontalPadding)
            .fillMaxWidth()
            .height(bannerHeight)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF23AA49), Color(0xFF2ECC71))
                )
            )
            .onGloballyPositioned { coords: LayoutCoordinates ->
                val position = coords.positionInRoot()
                val centerY = position.y + coords.size.height / 2f
                onMeasuredCenterY(centerY)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bannerPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "¡Ofertas Especiales!",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Hasta 30% de descuento",
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Icon(
                imageVector = Icons.Default.LocalOffer,
                contentDescription = "Ofertas",
                tint = Color.White.copy(alpha = 0.3f),
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

@Composable
fun CategoriesRow(navController: NavController, products: List<Product>) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val horizontalPadding = (screenWidth * 0.06f).coerceIn(16.dp, 28.dp)
    val categorySize = (screenWidth * 0.18f).coerceIn(65.dp, 80.dp)
    val iconSize = (categorySize * 0.49f).coerceIn(30.dp, 40.dp)
    val spacing = (screenWidth * 0.04f).coerceIn(12.dp, 18.dp)

    // ✅ OBTENER CATEGORÍAS DINÁMICAS DE LA API
    val dynamicCategories = remember(products) {
        if (products.isEmpty()) {
            // Categorías por defecto si no hay productos cargados
            listOf("Frutas", "Verduras", "Lácteos", "Limpieza")
        } else {
            products.mapNotNull { it.category }
                .filter { it.isNotBlank() }
                .distinct()
                .sorted()
        }
    }

    // Mapa de iconos para categorías conocidas (fallback a icono genérico)
    fun getCategoryIcon(category: String): Int {
        return when (category.lowercase()) {
            // Categorías API
            "hogar" -> R.drawable.ic_categoria_hogar
            "electrónica", "electronica" -> R.drawable.ic_categoria_electrodomesticos
            "computación", "computacion" -> R.drawable.ic_categoria_electrodomesticos
            "accesorios" -> R.drawable.ic_categoria_organicos // Usar icono distinto para diferenciar
            
            // Categorías Assets (HuertoHogar original)
            "frutas" -> R.drawable.ic_categoria_frutas
            "verduras" -> R.drawable.ic_categoria_verduras
            "lacteos", "lácteos" -> R.drawable.ic_categoria_lacteos
            "organicos", "orgánicos" -> R.drawable.ic_categoria_organicos
            "limpieza" -> R.drawable.ic_categoria_hogar
            "electrodomesticos", "electrodomésticos" -> R.drawable.ic_categoria_electrodomesticos
            
            // Fallback
            else -> R.drawable.ic_categoria_frutas 
        }
    }

    Column(modifier = Modifier.padding(start = horizontalPadding, bottom = 6.dp, end = horizontalPadding)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Categorías", color = Color(0xFF05161B), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Spacer(Modifier.weight(1f))
            Text(
                "Ver todo", 
                color = Color(0xFF23AA49), 
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { navController.navigate("categoria/Todos") }
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            items(dynamicCategories.size) { index ->
                val title = dynamicCategories[index]
                val iconRes = getCategoryIcon(title)
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { navController.navigate("categoria/$title") }
                ) {
                    Box(
                        modifier = Modifier
                            .size(categorySize)
                            .clip(CircleShape)
                            .background(Color(0xFFF3F5F7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = title,
                            modifier = Modifier.size(iconSize),
                            colorFilter = ColorFilter.tint(Color(0xFF23AA49))
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
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Ancho de tarjeta responsivo (43-47% del ancho de pantalla)
    val cardWidth = (screenWidth * 0.45f).coerceIn(160.dp, 200.dp)
    val imageHeight = (cardWidth * 0.78f).coerceIn(120.dp, 160.dp)
    val addButtonSize = (cardWidth * 0.29f).coerceIn(48.dp, 56.dp)

    Box(modifier = Modifier.width(cardWidth)) {
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
                    .height(imageHeight)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF3F5F7)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.imageUrl ?: "")
                        .crossfade(true)
                        .build(),
                    contentDescription = product.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                product.name ?: "Producto",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF1B1C1E),
                maxLines = 1
            )
            Spacer(Modifier.height(4.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                Spacer(modifier = Modifier.width((addButtonSize * 0.92f)))
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-12).dp, y = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(addButtonSize)
                    .background(Color(0xFF23AA49), CircleShape)
                    .clickable { viewModel.addToCart(product) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White,
                     modifier = Modifier.size(addButtonSize * 0.46f))
            }
        }
    }
}

// Componente del Drawer con diseño HuertoHogar
@Composable
fun HuertoHogarDrawerContent(
    navController: NavController,
    userProfile: com.huertohogar.huertohogar_app.viewmodel.UserProfileData,
    onCloseDrawer: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    
    // Ancho del drawer responsivo: 80% en móviles pequeños, máximo 380dp en tablets
    val drawerWidth = (screenWidth * 0.8f).coerceIn(280.dp, 380.dp)
    val headerHeight = (screenHeight * 0.22f).coerceIn(160.dp, 200.dp)
    val avatarSize = (drawerWidth * 0.20f).coerceIn(56.dp, 72.dp)
    val headerPadding = (drawerWidth * 0.06f).coerceIn(16.dp, 24.dp)
    
    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        modifier = Modifier.width(drawerWidth)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header del Drawer con gradiente verde
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF23AA49),
                                Color(0xFF2EC561)
                            )
                        )
                    )
                    .padding(headerPadding)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.BottomStart)
                ) {
                    // Avatar del usuario
                    Box(
                        modifier = Modifier
                            .size(avatarSize)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f))
                            .border(3.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Usuario",
                            tint = Color.White,
                            modifier = Modifier.size(avatarSize * 0.56f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height((headerHeight * 0.06f).coerceIn(8.dp, 12.dp)))
                    
                    // Nombre del usuario
                    Text(
                        text = userProfile.nombre,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = (drawerWidth.value * 0.055f).coerceIn(16f, 20f).sp
                        ),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = userProfile.email,
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = (drawerWidth.value * 0.038f).coerceIn(12f, 14f).sp
                        ),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Opciones del menú
            DrawerMenuItem(
                icon = Icons.Outlined.Home,
                title = "Inicio",
                drawerWidth = drawerWidth,
                onClick = {
                    onCloseDrawer()
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )

            DrawerMenuItem(
                icon = Icons.Outlined.Category,
                title = "Categorías",
                drawerWidth = drawerWidth,
                onClick = {
                    onCloseDrawer()
                    navController.navigate("categoria/Todos")
                }
            )

            DrawerMenuItem(
                icon = Icons.Outlined.ShoppingCart,
                title = "Carrito de Compras",
                drawerWidth = drawerWidth,
                onClick = {
                    onCloseDrawer()
                    navController.navigate("cart")
                }
            )

            DrawerMenuItem(
                icon = Icons.Outlined.Receipt,
                title = "Mis Pedidos",
                drawerWidth = drawerWidth,
                onClick = {
                    onCloseDrawer()
                    navController.navigate("pedidos")
                }
            )

            DrawerMenuItem(
                icon = Icons.Outlined.Person,
                title = "Mi Perfil",
                drawerWidth = drawerWidth,
                onClick = {
                    onCloseDrawer()
                    navController.navigate("profile")
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = (drawerWidth * 0.053f).coerceIn(12.dp, 20.dp)),
                color = Color(0xFFE0E0E0)
            )

            DrawerMenuItem(
                icon = Icons.Outlined.Favorite,
                title = "Favoritos",
                drawerWidth = drawerWidth,
                onClick = {
                    onCloseDrawer()
                    // Navegar a favoritos cuando esté implementado
                }
            )

            DrawerMenuItem(
                icon = Icons.Outlined.LocalShipping,
                title = "Seguimiento de Envíos",
                drawerWidth = drawerWidth,
                onClick = {
                    onCloseDrawer()
                    // Navegar a seguimiento cuando esté implementado
                }
            )

            DrawerMenuItem(
                icon = Icons.Outlined.Notifications,
                title = "Notificaciones",
                drawerWidth = drawerWidth,
                onClick = {
                    onCloseDrawer()
                    // Navegar a notificaciones cuando esté implementado
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = (drawerWidth * 0.053f).coerceIn(12.dp, 20.dp)),
                color = Color(0xFFE0E0E0)
            )

            DrawerMenuItem(
                icon = Icons.Outlined.Settings,
                title = "Configuración",
                drawerWidth = drawerWidth,
                onClick = {
                    onCloseDrawer()
                    // Navegar a configuración cuando esté implementado
                }
            )

            DrawerMenuItem(
                icon = Icons.Outlined.Help,
                title = "Ayuda y Soporte",
                drawerWidth = drawerWidth,
                onClick = {
                    onCloseDrawer()
                    // Navegar a ayuda cuando esté implementado
                }
            )

            DrawerMenuItem(
                icon = Icons.Outlined.Info,
                title = "Acerca de",
                drawerWidth = drawerWidth,
                onClick = {
                    onCloseDrawer()
                    // Mostrar información de la app
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botón de cerrar sesión
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = (drawerWidth * 0.053f).coerceIn(12.dp, 20.dp)),
                color = Color(0xFFE0E0E0)
            )

            DrawerMenuItem(
                icon = Icons.Outlined.Logout,
                title = "Cerrar Sesión",
                drawerWidth = drawerWidth,
                iconTint = Color(0xFFFF314A),
                textColor = Color(0xFFFF314A),
                onClick = {
                    onCloseDrawer()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )

            Spacer(modifier = Modifier.height((drawerWidth * 0.04f).coerceIn(12.dp, 16.dp)))

            // Footer con versión
            Text(
                text = "HuertoHogar v1.0",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = (drawerWidth.value * 0.035f).coerceIn(10f, 12f).sp
                ),
                color = Color(0xFF969899),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = (drawerWidth * 0.04f).coerceIn(12.dp, 16.dp)),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

// Item individual del menú del Drawer
@Composable
fun DrawerMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    drawerWidth: Dp,
    iconTint: Color = Color(0xFF23AA49),
    textColor: Color = Color(0xFF05161B),
    onClick: () -> Unit
) {
    // Tamaños responsivos basados en el ancho del drawer
    val horizontalPadding = (drawerWidth * 0.066f).coerceIn(16.dp, 24.dp)
    val verticalPadding = (drawerWidth * 0.046f).coerceIn(12.dp, 16.dp)
    val iconSize = (drawerWidth * 0.08f).coerceIn(22.dp, 26.dp)
    val spacerWidth = (drawerWidth * 0.053f).coerceIn(14.dp, 20.dp)
    val textSize = (drawerWidth.value * 0.047f).coerceIn(13f, 16f).sp
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconTint,
            modifier = Modifier.size(iconSize)
        )
        
        Spacer(modifier = Modifier.width(spacerWidth))
        
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = textSize),
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}

// ✅ FUNCIÓN PARA VALIDAR CONEXIÓN A INTERNET (REQUISITO RÚBRICA)
fun isInternetAvailable(context: android.content.Context): Boolean {
    val connectivityManager = context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

