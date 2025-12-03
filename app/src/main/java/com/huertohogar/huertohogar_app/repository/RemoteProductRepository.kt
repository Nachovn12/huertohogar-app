package com.huertohogar.huertohogar_app.repository

import android.util.Log
import com.huertohogar.huertohogar_app.data.api.RetrofitClient
import com.huertohogar.huertohogar_app.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio que obtiene productos desde el endpoint público (Retrofit)
 * Ahora ApiService devuelve directamente List<Product> con mapeo correcto via @SerializedName
 */
class RemoteProductRepository : ProductDataSource {
    override suspend fun getAllProducts(): List<Product> = withContext(Dispatchers.IO) {
        try {
            // Ya no necesitamos mapeo manual - Gson lo hace automáticamente
            RetrofitClient.apiService.getProductos()
        } catch (e: Exception) {
            Log.e("RemoteProductRepo", "Error al obtener productos desde API", e)
            emptyList()
        }
    }

    override suspend fun getProductBySku(sku: String): Product = withContext(Dispatchers.IO) {
        val all = getAllProducts()
        all.first { it.sku == sku }
    }
}

