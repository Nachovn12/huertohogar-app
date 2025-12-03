package com.huertohogar.huertohogar_app.data.model

import java.io.Serializable

data class Producto(
    val sku: String,
    val nombre: String,
    val precio: Double,
    val descripcion: String? = null,
    val imagen: String? = null
) : Serializable

