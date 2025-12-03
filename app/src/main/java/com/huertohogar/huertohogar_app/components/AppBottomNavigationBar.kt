package com.huertohogar.huertohogar_app.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.layout.navigationBarsPadding

data class NavItem(
    val label: String,
    val iconOutlined: ImageVector,
    val iconFilled: ImageVector,
    val route: String
)

@Composable
fun AppBottomNavigationBar(
    navController: NavController,
    viewModel: ProductViewModel,
    currentRoute: String = "home"
) {
    val totalCartItems by viewModel.totalCartItems.collectAsState()
    val pedidos by viewModel.pedidos.collectAsState()
    val totalPedidos = pedidos.size
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Items con iconos más apropiados
    val items = listOf(
        NavItem("Inicio", Icons.Outlined.Home, Icons.Filled.Home, "home"),
        NavItem("Productos", Icons.Outlined.StoreMallDirectory, Icons.Filled.StoreMallDirectory, "categoria/Todos"),
        NavItem("", Icons.Filled.ShoppingCart, Icons.Filled.ShoppingCart, ""), // FAB
        NavItem("Pedidos", Icons.Outlined.Receipt, Icons.Filled.Receipt, "pedidos"),
        NavItem("Perfil", Icons.Outlined.Person, Icons.Filled.Person, "profile")
    )

    // Determinar el índice seleccionado
    val selectedIndex = when {
        currentRoute.startsWith("home") -> 0
        currentRoute.startsWith("categoria") -> 1
        currentRoute.startsWith("pedidos") -> 3
        currentRoute.startsWith("profile") -> 4
        else -> 0
    }

    // Altura responsiva del NavigationBar
    val navBarHeight = (screenWidth * 0.17f).coerceIn(60.dp, 72.dp)
    val fabSize = (screenWidth * 0.14f).coerceIn(52.dp, 60.dp)
    // Ajustar el offset del FAB para que se superponga al pill (mitad del tamaño del FAB)
    val fabOffset = -(fabSize / 2)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(navBarHeight)
            .navigationBarsPadding() // respetar gestos del sistema
    ) {
        // NavigationBar con pill blanco con esquinas redondeadas
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(navBarHeight - 6.dp)
                .align(Alignment.BottomCenter),
            color = Color.White,
            shadowElevation = 10.dp,
            tonalElevation = 0.dp,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    if (index == 2) {
                        // Espacio para el FAB - dinámico según tamaño de pantalla
                        Spacer(modifier = Modifier.width(fabSize * 1.05f))
                    } else {
                        NavBarItem(
                            item = item,
                            isSelected = selectedIndex == index,
                            onClick = {
                                if (item.route.isNotEmpty()) {
                                    navController.navigate(item.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            screenWidth = screenWidth,
                            badgeCount = if (index == 3) totalPedidos else 0 // Badge para Pedidos
                        )
                    }
                }
            }
        }

        // FAB Central mejorado y responsivo
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = fabOffset)
        ) {
            FloatingActionButton(
                onClick = {
                    navController.navigate("cart") {
                        launchSingleTop = true
                    }
                },
                containerColor = Color(0xFF23AA49),
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                ),
                modifier = Modifier.size(fabSize)
            ) {
                Icon(
                    Icons.Filled.ShoppingCart,
                    contentDescription = "Carrito",
                    modifier = Modifier.size(fabSize * 0.43f)
                )
            }

            // Badge moderno y responsivo
            if (totalCartItems > 0) {
                val badgeSize = (fabSize * 0.36f).coerceIn(18.dp, 22.dp)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (fabSize * 0.04f), y = -(fabSize * 0.04f))
                        .size(badgeSize)
                        .shadow(2.dp, CircleShape)
                        .background(Color(0xFFFF314A), CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (totalCartItems > 9) "9+" else totalCartItems.toString(),
                        color = Color.White,
                        fontSize = (badgeSize.value * 0.5f).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun NavBarItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    screenWidth: Dp,
    badgeCount: Int = 0 // Número para el badge
) {
    // Tamaños responsivos basados en ancho de pantalla
    val iconSize by animateDpAsState(
        targetValue = if (isSelected) (screenWidth * 0.064f).coerceIn(23.dp, 28.dp)
                      else (screenWidth * 0.058f).coerceIn(21.dp, 25.dp),
        animationSpec = tween(300), label = ""
    )

    val fontSize = (screenWidth * 0.022f).value.coerceIn(8f, 10f)

    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Contenedor circular para icon cuando está seleccionado
        Box(
            modifier = Modifier
                .size((screenWidth * 0.095f).coerceIn(36.dp, 42.dp))
                .clip(CircleShape)
                .background(if (isSelected) Color(0xFFEAF6ED) else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onClick,
                modifier = Modifier.size((screenWidth * 0.095f).coerceIn(32.dp, 40.dp))
            ) {
                Icon(
                    imageVector = if (isSelected) item.iconFilled else item.iconOutlined,
                    contentDescription = item.label,
                    tint = if (isSelected) Color(0xFF23AA49) else Color(0xFF969899),
                    modifier = Modifier.size(iconSize)
                )
            }

            // Badge para pedidos
            if (badgeCount > 0) {
                val badgeSize = (screenWidth * 0.045f).coerceIn(16.dp, 20.dp)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-4).dp)
                        .size(badgeSize)
                        .shadow(2.dp, CircleShape)
                        .background(Color(0xFFFF314A), CircleShape)
                        .border(1.5.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (badgeCount > 9) "9+" else badgeCount.toString(),
                        color = Color.White,
                        fontSize = (badgeSize.value * 0.55f).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = item.label,
            fontSize = fontSize.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            color = if (isSelected) Color(0xFF23AA49) else Color(0xFF969899),
            maxLines = 1
        )
    }
}

@Composable
fun ScaffoldWithBottomNav(
    navController: NavController,
    viewModel: ProductViewModel,
    currentRoute: String,
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = topBar,
        floatingActionButton = floatingActionButton,
        bottomBar = {
            AppBottomNavigationBar(
                navController = navController,
                viewModel = viewModel,
                currentRoute = currentRoute
            )
        },
        containerColor = Color.Transparent,
        modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            content(PaddingValues(0.dp))
        }
    }
}
