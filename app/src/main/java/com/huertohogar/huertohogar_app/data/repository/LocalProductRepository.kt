package com.huertohogar.huertohogar_app.data.repository


import android.content.Context
import com.huertohogar.huertohogar_app.data.model.Producto

class LocalProductRepository(private val context: Context) {
    fun getProductos(): List<Producto> = emptyList()
}
