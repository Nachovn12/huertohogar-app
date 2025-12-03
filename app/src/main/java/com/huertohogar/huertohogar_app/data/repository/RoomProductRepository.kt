package com.huertohogar.huertohogar_app.data.repository

import android.content.Context
import android.util.Log
import com.huertohogar.huertohogar_app.data.db.ProductDao
import com.huertohogar.huertohogar_app.data.db.ProductDatabase
import com.huertohogar.huertohogar_app.data.mapper.toEntity
import com.huertohogar.huertohogar_app.data.mapper.toEntityList
import com.huertohogar.huertohogar_app.data.mapper.toProduct
import com.huertohogar.huertohogar_app.data.mapper.toProductList
import com.huertohogar.huertohogar_app.model.Product
import com.huertohogar.huertohogar_app.repository.ProductDataSource

/**
 * Repositorio que gestiona el acceso a la base de datos local Room
 * Implementa ProductDataSource para mantener consistencia con la arquitectura
 */
class RoomProductRepository(context: Context) : ProductDataSource {

    private val productDao: ProductDao = ProductDatabase.getDatabase(context).productDao()

    /**
     * Obtiene todos los productos de la base de datos local
     */
    override suspend fun getAllProducts(): List<Product> {
        return try {
            val entities = productDao.getAllProducts()
            Log.d("RoomProductRepository", "Productos cargados desde Room: ${entities.size}")
            entities.toProductList()
        } catch (e: Exception) {
            Log.e("RoomProductRepository", "Error al cargar productos desde Room", e)
            emptyList()
        }
    }

    /**
     * Busca un producto específico por su SKU
     */
    override suspend fun getProductBySku(sku: String): Product {
        return try {
            val entity = productDao.getProductBySku(sku)
            entity?.toProduct() ?: throw NoSuchElementException("Producto no encontrado: $sku")
        } catch (e: Exception) {
            Log.e("RoomProductRepository", "Error al buscar producto por SKU: $sku", e)
            throw e
        }
    }

    /**
     * Guarda una lista de productos en la base de datos local
     * Reemplaza productos existentes si tienen el mismo SKU
     */
    suspend fun saveProducts(products: List<Product>): Boolean {
        return try {
            val entities = products.toEntityList()
            productDao.insertProducts(entities)
            Log.d("RoomProductRepository", "Productos guardados en Room: ${entities.size}")
            true
        } catch (e: Exception) {
            Log.e("RoomProductRepository", "Error al guardar productos en Room", e)
            false
        }
    }

    /**
     * Guarda un producto individual en la base de datos
     */
    suspend fun saveProduct(product: Product): Boolean {
        return try {
            productDao.insertProduct(product.toEntity())
            Log.d("RoomProductRepository", "Producto guardado: ${product.sku}")
            true
        } catch (e: Exception) {
            Log.e("RoomProductRepository", "Error al guardar producto", e)
            false
        }
    }

    /**
     * Elimina todos los productos de la base de datos
     */
    suspend fun clearAllProducts() {
        try {
            productDao.deleteAllProducts()
            Log.d("RoomProductRepository", "Todos los productos eliminados de Room")
        } catch (e: Exception) {
            Log.e("RoomProductRepository", "Error al limpiar productos", e)
        }
    }

    /**
     * Verifica si hay productos almacenados localmente
     */
    suspend fun hasProducts(): Boolean {
        return try {
            productDao.getProductCount() > 0
        } catch (e: Exception) {
            Log.e("RoomProductRepository", "Error al verificar productos", e)
            false
        }
    }
}

