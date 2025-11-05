package com.huertohogar.huertohogar_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.huertohogar.huertohogar_app.repository.LocalProductRepository
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModelFactory
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel
import com.huertohogar.huertohogar_app.navigation.AppNavGraph
import com.huertohogar.huertohogar_app.theme.HuertoHogar_AppTheme

/**
 * Activity principal de la app HuertoHogar
 * Configuré Edge-to-Edge para aprovechar toda la pantalla
 */
class MainActivity : ComponentActivity(), ImageLoaderFactory {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Cambio al tema normal después del splash
        setTheme(R.style.Theme_HuertoHogar_App)

        // Modo Edge-to-Edge para mejor uso de pantalla
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            HuertoHogar_AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val navController = rememberNavController()
                    
                    // Inicialización del repositorio y ViewModel
                    val dataSource = LocalProductRepository(applicationContext)
                    val factory = ProductViewModelFactory(dataSource)
                    val productViewModel: ProductViewModel = viewModel(factory = factory)

                    AppNavGraph(
                        navController = navController,
                        productViewModel = productViewModel
                    )
                }
            }
        }
    }

    // Configuración de Coil para carga de imágenes
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(applicationContext).build()
    }
}