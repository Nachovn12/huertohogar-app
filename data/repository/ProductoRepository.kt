package com.huertohogar.huertohogar_app.data.repository

import android.content.Context
import com.huertohogar.huertohogar_app.data.api.RetrofitClient
import com.huertohogar.huertohogar_app.data.db.ProductoDatabase
import com.huertohogar.huertohogar_app.data.db.ProductoEntity
import com.huertohogar.huertohogar_app.data.model.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductoRepository(private val context: Context) {
    private val productoDao = ProductoDatabase.getDatabase(context).productoDao()

    suspend fun getProductosFromApi(): List<Producto> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService.getProductos()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveProductosToDb(productos: List<Producto>) = withContext(Dispatchers.IO) {
        val entities = productos.map { it.toEntity() }
        productoDao.clearAll()
        productoDao.insertAll(entities)
    }

    suspend fun getProductosFromDb(): List<Producto> = withContext(Dispatchers.IO) {
        productoDao.getAll().map { it.toModel() }
    }
}

// Extensiones para transformar entre modelos
fun Producto.toEntity() = ProductoEntity(
    sku = sku,
    nombre = nombre,
    precio = precio,
    descripcion = descripcion,
    imagen = imagen
)

fun ProductoEntity.toModel() = Producto(
    sku = sku,
    nombre = nombre,
    precio = precio,
    descripcion = descripcion,
    imagen = imagen
)
