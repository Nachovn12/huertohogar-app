package com.huertohogar.huertohogar_app.repository

import com.huertohogar.huertohogar_app.model.Product
import com.huertohogar.huertohogar_app.network.ProductApiService

class ProductRepository(private val api: ProductApiService) {
    suspend fun getAllProducts(): List<Product> = api.getAllProducts()
    suspend fun getProductById(id: Long): Product = api.getProductById(id)
}