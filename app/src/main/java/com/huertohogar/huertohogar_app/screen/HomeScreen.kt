package com.huertohogar.huertohogar_app.screen

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
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
                product.name.removeAccents().lowercase().contains(normalizedQuery) ||
                (product.category?.removeAccents()?.lowercase()?.contains(normalizedQuery) == true) ||
                (product.description?.removeAccents()?.lowercase()?.contains(normalizedQuery) == true)
            }
        }
    }

    val bestSellers = products.filter {
        it.name == "Espinacas Frescas" || it.name == "Leche Entera"
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf("Concepción") }
    var bannerCenterYPx by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) { viewModel.loadProducts() }

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
                        onLocationClick = { showBottomSheet = true }
                    )
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

@Composable
fun HomeHeader(
    greeting: String,
    greetingEmoji: String,
    userName: String,
    selectedCity: String,
    onLocationClick: () -> Unit
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
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(avatarSize)
                .clip(CircleShape)
                .background(Color(0xFF23AA49)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Perfil",
                tint = Color.White,
                modifier = Modifier.size(iconSize)
            )
        }
        Spacer(modifier = Modifier.width(horizontalPadding * 0.46f))
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
                    product.name,
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
fun CategoriesRow(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val horizontalPadding = (screenWidth * 0.06f).coerceIn(16.dp, 28.dp)
    val categorySize = (screenWidth * 0.18f).coerceIn(65.dp, 80.dp)
    val iconSize = (categorySize * 0.49f).coerceIn(30.dp, 40.dp)
    val spacing = (screenWidth * 0.04f).coerceIn(12.dp, 18.dp)

    Column(modifier = Modifier.padding(start = horizontalPadding, bottom = 6.dp, end = horizontalPadding)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Categorías", color = Color(0xFF05161B), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Spacer(Modifier.weight(1f))
            Text("Ver todo", color = Color(0xFF23AA49), style = MaterialTheme.typography.bodyMedium)
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            val categories = listOf(
                "Frutas" to Icons.Default.Spa,
                "Verduras" to Icons.Default.Yard,
                "Orgánicos" to Icons.Default.Eco,
                "Lácteos" to Icons.Default.LocalCafe
            )
            items(categories.size) { index ->
                val (title, icon) = categories[index]
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
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = Color(0xFF23AA49),
                            modifier = Modifier.size(iconSize)
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
                product.name,
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
