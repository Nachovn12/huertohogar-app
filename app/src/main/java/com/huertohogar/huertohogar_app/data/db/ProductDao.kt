package com.huertohogar.huertohogar_app.data.db

import androidx.room.*

/**
 * DAO (Data Access Object) para operaciones de base de datos sobre productos
 * Define las consultas SQL que Room ejecutará
 */
@Dao
interface ProductDao {
    @Query("SELECT * FROM productos")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM productos WHERE sku = :sku LIMIT 1")
    suspend fun getProductBySku(sku: String): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("DELETE FROM productos")
    suspend fun deleteAllProducts()

    @Query("SELECT COUNT(*) FROM productos")
    suspend fun getProductCount(): Int
}

