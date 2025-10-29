package com.huertohogar.huertohogar_app.network

import com.huertohogar.huertohogar_app.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {
    @GET("/api/products")
    suspend fun getAllProducts(): List<Product>

    @GET("/api/products/{id}")
    suspend fun getProductById(@Path("id") id: Long): Product
}