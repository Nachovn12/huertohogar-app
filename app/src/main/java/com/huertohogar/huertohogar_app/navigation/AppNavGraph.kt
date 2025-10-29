package com.huertohogar.huertohogar_app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import com.huertohogar.huertohogar_app.screen.SplashScreen
import com.huertohogar.huertohogar_app.screen.IntroScreen
import com.huertohogar.huertohogar_app.screen.LoginScreen
import com.huertohogar.huertohogar_app.screen.RegisterScreen
import com.huertohogar.huertohogar_app.screen.HomeScreen
import com.huertohogar.huertohogar_app.screen.CategoriaScreen 
import com.huertohogar.huertohogar_app.screen.DetalleProductoScreen
import com.huertohogar.huertohogar_app.screen.CartScreen
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    productViewModel: ProductViewModel
) {
    NavHost(navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("intro") { IntroScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController, productViewModel) }
        composable(
            "categoria/{categoria}",
            arguments = listOf(navArgument("categoria") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoria = backStackEntry.arguments?.getString("categoria") ?: ""
            CategoriaScreen(navController, categoria, productViewModel)
        }
        composable(
            "detalle_producto/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
            DetalleProductoScreen(navController, productId, productViewModel)
        }
        composable("cart") { CartScreen(navController, productViewModel) } 
    }
}
