package data.api

import data.model.Producto
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("chalalo1533/ServicioRest/refs/heads/master/productos.json")
    suspend fun getProductos(): Response<List<Producto>>
}
