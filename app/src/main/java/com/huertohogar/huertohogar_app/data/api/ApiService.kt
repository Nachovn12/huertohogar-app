package com.huertohogar.huertohogar_app.data.api

import com.huertohogar.huertohogar_app.model.Product
import retrofit2.http.GET

interface ApiService {
    // Ruta relativa al dominio raw.githubusercontent.com
    @GET("chalalo1533/ServicioRest/refs/heads/master/productos.json")
    suspend fun getProductos(): List<Product>
}
