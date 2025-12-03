package com.huertohogar.huertohogar_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.graphics.Color as AndroidColor
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.huertohogar.huertohogar_app.repository.LocalProductRepository
import com.huertohogar.huertohogar_app.repository.RemoteProductRepository
import com.huertohogar.huertohogar_app.data.repository.RoomProductRepository
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModelFactory
import com.huertohogar.huertohogar_app.viewmodel.ProductViewModel
import com.huertohogar.huertohogar_app.ui.navigation.AppNavGraph
import com.huertohogar.huertohogar_app.theme.HuertoHogar_AppTheme

/**
 * Activity principal de la app HuertoHogar
 * Configurada con Edge-to-Edge para pantalla completa real
 */
class MainActivity : ComponentActivity(), ImageLoaderFactory {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilitar modo Edge-to-Edge para que las pantallas puedan dibujar detrás de las barras del sistema
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Hacer barras transparentes y forzar iconos en color claro por defecto
        window.statusBarColor = AndroidColor.TRANSPARENT
        window.navigationBarColor = AndroidColor.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars = false

        setContent {
            HuertoHogar_AppTheme {
                val navController = rememberNavController()

                // Inicialización del repositorio y ViewModel
                val local = LocalProductRepository(applicationContext)
                val remote = RemoteProductRepository()
                val roomRepo = RoomProductRepository(applicationContext)
                val factory = ProductViewModelFactory(local, remote)
                val productViewModel: ProductViewModel = viewModel(factory = factory)

                // Inyectar el repositorio Room al ViewModel
                productViewModel.setRoomRepository(roomRepo)

                // No dibujamos un Scaffold global con bottomBar aquí porque
                // muchas pantallas usan `ScaffoldWithBottomNav` internamente.
                // Si dejamos un bottomBar global se duplica la barra.
                Box(modifier = Modifier.fillMaxSize()) {
                    AppNavGraph(
                        navController = navController,
                        productViewModel = productViewModel
                    )
                }
            }
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(applicationContext).build()
    }
}