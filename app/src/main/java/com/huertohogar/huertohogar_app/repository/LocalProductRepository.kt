package com.huertohogar.huertohogar_app.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.huertohogar.huertohogar_app.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio que carga productos desde el archivo JSON en assets
 * Implementé la carga asíncrona con coroutines para no bloquear la UI
 */
class LocalProductRepository(private val context: Context) : ProductDataSource {
    
    /**
     * Carga todos los productos del archivo products.json
     * Convierte los nombres de imágenes drawable a URIs de recursos Android
     */
    override suspend fun getAllProducts(): List<Product> = withContext(Dispatchers.IO) {
        try {
            // Leer el archivo JSON de assets
            val json = context.assets.open("products.json").bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<Product>>() {}.type
            val parsed: List<Product> = Gson().fromJson<List<Product>>(json, listType) ?: emptyList()

            // Mapear rutas de imágenes a URIs de recursos Android
            val mapped = parsed.map { product ->
                try {
                    val imageUrl = product.imageUrl

                    // Si no es una URL HTTP, asumir que es un drawable local
                    if (imageUrl != null && !imageUrl.startsWith("http")) {
                        // Verificar que el drawable existe
                        val drawableId = context.resources.getIdentifier(
                            imageUrl,
                            "drawable",
                            context.packageName
                        )

                        if (drawableId != 0) {
                            // Construir URI para Coil
                            val localUri = "android.resource://${context.packageName}/drawable/$imageUrl"
                            product.copy(imageUrl = localUri)
                        } else {
                            Log.w("LocalProductRepo", "Drawable no encontrado: $imageUrl para ${product.sku}")
                            product
                        }
                    } else {
                        // Es URL externa, mantener tal cual
                        product
                    }
                } catch (e: Exception) {
                    Log.w("LocalProductRepo", "Error mapeando imagen para ${product.sku}", e)
                    product
                }
            }

            val localCount = mapped.count {
                it.imageUrl?.startsWith("android.resource://") == true
            }
            Log.d("LocalProductRepo", "Cargados ${mapped.size} productos; ${localCount} imágenes locales")

            mapped
        } catch (e: Exception) {
            Log.e("LocalProductRepo", "Error al cargar products.json desde assets", e)
            emptyList()
        }
    }

    /**
     * Busca un producto específico por su SKU
     */
    override suspend fun getProductBySku(sku: String): Product = withContext(Dispatchers.IO) {
        val all = getAllProducts()
        all.first { it.sku == sku }
    }
}
