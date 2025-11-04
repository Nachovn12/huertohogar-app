package com.huertohogar.huertohogar_app.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.huertohogar.huertohogar_app.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.Normalizer
import java.util.Locale

class LocalProductRepository(private val context: Context) : ProductDataSource {
    override suspend fun getAllProducts(): List<Product> = withContext(Dispatchers.IO) {
        try {
            val json = context.assets.open("products.json").bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<Product>>() {}.type
            val parsed: List<Product> = Gson().fromJson<List<Product>>(json, listType) ?: emptyList()

            // Mapear productos y convertir nombres de drawable a URIs de recurso
            val mapped = parsed.map { product ->
                try {
                    val imageUrl = product.imageUrl

                    // Si imageUrl no tiene protocolo (http/https), asumimos que es un nombre de drawable
                    if (imageUrl != null && !imageUrl.startsWith("http")) {
                        // Verificar si el recurso existe en drawable
                        val drawableId = context.resources.getIdentifier(
                            imageUrl,
                            "drawable",
                            context.packageName
                        )

                        if (drawableId != 0) {
                            // Usar URI de recurso para Coil
                            val localUri = "android.resource://${context.packageName}/drawable/$imageUrl"
                            product.copy(imageUrl = localUri)
                        } else {
                            // Si no existe el drawable, mantener el URL original
                            Log.w("LocalProductRepo", "Drawable not found: $imageUrl for product ${product.sku}")
                            product
                        }
                    } else {
                        // Es una URL externa, mantenerla
                        product
                    }
                } catch (e: Exception) {
                    Log.w("LocalProductRepo", "Error mapping image for product ${product.sku}", e)
                    product
                }
            }

            val localCount = mapped.count {
                it.imageUrl?.startsWith("android.resource://") == true
            }
            Log.d("LocalProductRepo", "Loaded ${mapped.size} products; local images=$localCount")

            mapped
        } catch (e: Exception) {
            Log.e("LocalProductRepo", "Failed to load products.json from assets", e)
            emptyList()
        }
    }

    override suspend fun getProductBySku(sku: String): Product = withContext(Dispatchers.IO) {
        val all = getAllProducts()
        all.first { it.sku == sku }
    }
}
