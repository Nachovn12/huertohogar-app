package com.huertohogar.huertohogar_app.repository

import com.huertohogar.huertohogar_app.model.Product

interface ProductDataSource {
    suspend fun getAllProducts(): List<Product>
    suspend fun getProductBySku(sku: String): Product
}
