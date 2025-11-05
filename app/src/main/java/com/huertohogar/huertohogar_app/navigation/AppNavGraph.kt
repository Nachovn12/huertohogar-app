package com.huertohogar.huertohogar_app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.huertohogar.huertohogar_app.screen.*
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel
import com.huertohogar.huertohogar_app.viewmodel.UserViewModel

/**
 * Configuración del grafo de navegación
 * Aprendí a usar Navigation Compose para manejar las rutas de la app
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    productViewModel: ProductViewModel
) {
    // ViewModel compartido para datos del usuario
    val userViewModel: UserViewModel = viewModel()

    NavHost(navController, startDestination = "splash") {
        // Pantalla inicial
        composable("splash") { 
            SplashScreen(navController) 
        }
        
        // Introducción
        composable("intro") { 
            IntroScreen(navController) 
        }
        
        // Autenticación (unificada login/register)
        composable("auth") { 
            AuthScreen(navController, initialTab = 0) 
        }
        composable("login") { 
            AuthScreen(navController, initialTab = 0) 
        }
        composable("register") { 
            AuthScreen(navController, initialTab = 1) 
        }
        
        // Pantalla principal
        composable("home") { 
            HomeScreen(navController, productViewModel, userViewModel) 
        }
        
        // Navegación con parámetros
        composable(
            route = "categoria/{categoria}",
            arguments = listOf(navArgument("categoria") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoria = backStackEntry.arguments?.getString("categoria") ?: ""
            CategoriaScreen(navController, categoria, productViewModel)
        }
        
        composable(
            route = "detalle_producto/{productSku}",
            arguments = listOf(navArgument("productSku") { type = NavType.StringType })
        ) { backStackEntry ->
            val productSku = backStackEntry.arguments?.getString("productSku") ?: ""
            DetalleProductoScreen(navController, productSku, productViewModel)
        }
        
        // Carrito y compra
        composable("cart") { 
            CartScreen(navController, productViewModel) 
        }
        composable("checkout") { 
            CheckoutScreen(navController, productViewModel, userViewModel) 
        }
        
        // Perfil y pedidos
        composable("pedidos") { 
            PedidosScreen(navController, productViewModel) 
        }
        composable("profile") { 
            ProfileScreen(navController, userViewModel, productViewModel) 
        }
    }
}
