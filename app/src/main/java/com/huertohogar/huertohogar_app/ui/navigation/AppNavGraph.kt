package com.huertohogar.huertohogar_app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.compose.ui.Modifier
import com.huertohogar.huertohogar_app.screen.*
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel
import com.huertohogar.huertohogar_app.viewmodel.UserViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    productViewModel: ProductViewModel
) {
    // ViewModel compartido para datos del usuario
    val userViewModel: UserViewModel = viewModel()

    NavHost(navController, startDestination = "splash") {

        // --- PANTALLAS INMERSIVAS (Sin Padding) ---
        // Estas ocupan toda la pantalla, dibujando detrás de las barras del sistema

        composable("splash") {
            SplashScreen(navController)
        }

        composable("intro") {
            IntroScreen(navController)
        }

        composable("auth") {
            AuthScreen(navController, initialTab = 0)
        }
        composable("login") {
            AuthScreen(navController, initialTab = 0)
        }
        composable("register") {
            AuthScreen(navController, initialTab = 1)
        }

        // --- PANTALLAS ESTÁNDAR (Con Padding) ---
        // Estas respetan el espacio de la barra de navegación y status bar

        composable("home") {
            Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                HomeScreen(navController, productViewModel, userViewModel)
            }
        }

        composable(
            route = "categoria/{categoria}",
            arguments = listOf(navArgument("categoria") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoria = backStackEntry.arguments?.getString("categoria") ?: ""
            Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                CategoriaScreen(navController, categoria, productViewModel)
            }
        }

        composable(
            route = "detalle_producto/{productSku}",
            arguments = listOf(navArgument("productSku") { type = NavType.StringType })
        ) { backStackEntry ->
            val productSku = backStackEntry.arguments?.getString("productSku") ?: ""
            Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                DetalleProductoScreen(navController, productSku, productViewModel)
            }
        }

        composable("cart") {
            Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                CartScreen(navController, productViewModel)
            }
        }

        composable("checkout") {
            Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                CheckoutScreen(navController, productViewModel, userViewModel)
            }
        }

        composable("pedidos") {
            Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                PedidosScreen(navController, productViewModel)
            }
        }

        composable("profile") {
            Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                ProfileScreen(navController, userViewModel, productViewModel)
            }
        }

        // Fallback: ruta sin parámetro (compatibilidad)
        composable("listado_productos") {
            Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                ListadoScreen(navController, productViewModel, source = "none")
            }
        }

        // Ruta con parametro 'source' para controlar desde donde cargar la lista
        composable(
            route = "listado_productos?source={source}",
            arguments = listOf(navArgument("source") { type = NavType.StringType; defaultValue = "none" })
        ) { backStackEntry ->
            val source = backStackEntry.arguments?.getString("source") ?: "none"
            Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                ListadoScreen(navController, productViewModel, source)
            }
        }
    }
}