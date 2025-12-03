package com.huertohogar.huertohogar_app.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.huertohogar.huertohogar_app.components.ProductItem
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel
import com.huertohogar.huertohogar_app.components.ScaffoldWithBottomNav
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListadoScreen(
    navController: NavController,
    viewModel: ProductViewModel,
    source: String = "none"
) {
    val products by viewModel.products.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Estados para mostrar mensajes y diálogos
    var isSaving by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var savedProductCount by remember { mutableStateOf(0) }

    // Cargar según la fuente (si aplica)
    LaunchedEffect(key1 = source) {
        when (source.lowercase()) {
            "api", "remote" -> viewModel.loadProductsFromApi()
            "local", "db" -> viewModel.loadProductsFromDb()
            else -> {
                // No forzar carga para evitar duplicados
            }
        }
    }

    // ✅ DIÁLOGO DE ÉXITO PROFESIONAL DESPUÉS DE GUARDAR
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { /* No permitir cerrar tocando fuera */ },
            icon = {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(56.dp)
                )
            },
            title = {
                Text(
                    text = "¡Guardado Exitoso!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFF05161B)
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$savedProductCount productos almacenados",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Los productos se guardaron correctamente en la Base de Datos Local.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF05161B)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CloudOff,
                            contentDescription = null,
                            tint = Color(0xFF5F6368),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ahora están disponibles sin conexión a Internet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5F6368)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        // Volver al Home después de confirmar
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF23AA49)
                    ),
                    modifier = Modifier.fillMaxWidth(0.48f)
                ) {
                    Text("Volver al Inicio", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showSuccessDialog = false },
                    modifier = Modifier.fillMaxWidth(0.48f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF5465DD)
                    )
                ) {
                    Text("Continuar aquí", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    ScaffoldWithBottomNav(navController = navController, viewModel = viewModel, currentRoute = "listado_productos") { _ ->
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Listado de Productos",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        // ✅ BOTÓN PARA VOLVER ATRÁS
                        IconButton(
                            onClick = {
                                // Navegar hacia atrás o al Home
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver al Inicio",
                                tint = Color(0xFF05161B),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color(0xFF05161B)
                    )
                )
            },
            content = { innerPadding ->
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        // ✅ BANNER INFORMATIVO CON BOTÓN DE GUARDAR DESTACADO
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF5F9FF)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${products.distinctBy { it.sku }.size} productos",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color(0xFF05161B),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Presiona",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color(0xFF5F6368)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Filled.Save,
                                            contentDescription = null,
                                            tint = Color(0xFF5465DD),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "para guardar localmente",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color(0xFF5F6368)
                                        )
                                    }
                                }

                                // BOTÓN GRANDE DE GUARDAR
                                Button(
                                    onClick = {
                                        if (products.isEmpty()) {
                                            Toast.makeText(
                                                context,
                                                "No hay productos para guardar",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            return@Button
                                        }

                                        isSaving = true
                                        val productCount = products.distinctBy { it.sku }.size

                                        viewModel.saveProductsToDb(
                                            onSuccess = {
                                                isSaving = false
                                                savedProductCount = productCount

                                                // ✅ TOAST MUY VISIBLE
                                                Toast.makeText(
                                                    context,
                                                    "$productCount productos guardados exitosamente",
                                                    Toast.LENGTH_LONG
                                                ).show()

                                                // ✅ DIÁLOGO DE CONFIRMACIÓN
                                                showSuccessDialog = true
                                            },
                                            onError = { error ->
                                                isSaving = false
                                                Toast.makeText(
                                                    context,
                                                    "Error al guardar: $error",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        )
                                    },
                                    enabled = !isSaving && products.isNotEmpty(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF5465DD),
                                        disabledContainerColor = Color(0xFFE0E0E0)
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                                ) {
                                    if (isSaving) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            strokeWidth = 2.dp,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Guardando...",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Filled.Save,
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp),
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Guardar en BD",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }

                        // ✅ LISTA DE PRODUCTOS CON RECYCLERVIEW (LAZYCOLUMN)
                        if (products.isEmpty()) {
                            // Estado vacío
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Inventory,
                                        contentDescription = null,
                                        tint = Color(0xFF9E9E9E),
                                        modifier = Modifier.size(80.dp)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "No hay productos para mostrar",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color(0xFF5F6368),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Carga productos desde la API o la Base de Datos",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF9E9E9E)
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Button(
                                        onClick = {
                                            navController.navigate("home") {
                                                popUpTo("home") { inclusive = true }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF23AA49)
                                        )
                                    ) {
                                        Text("Volver al Inicio", color = Color.White)
                                    }
                                }
                            }
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(products.distinctBy { it.sku }, key = { it.sku }) { product ->
                                    ProductItem(
                                        product = product,
                                        onClick = {
                                            // ✅ NAVEGACIÓN AL DETALLE (REQUERIMIENTO RÚBRICA)
                                            navController.navigate("detalle_producto/${product.sku}")
                                        },
                                        onAdd = {
                                            viewModel.addToCart(product)
                                            Toast.makeText(
                                                context,
                                                "${product.name ?: "Producto"} agregado al carrito",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }

                                // Espaciado final
                                item {
                                    Spacer(modifier = Modifier.height(80.dp))
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
