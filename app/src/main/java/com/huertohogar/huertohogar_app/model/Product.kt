package com.huertohogar.huertohogar_app.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para productos del e-commerce
 * Soporta tanto JSON local (inglés) como API remota (español)
 */
data class Product(
    @SerializedName("sku")
    val sku: String = "",         // Código único del producto

    @SerializedName(value = "nombre", alternate = ["name"])
    val name: String? = null,     // Nombre del producto (soporta "nombre" o "name")

    @SerializedName(value = "descripcion", alternate = ["description"])
    val description: String? = null,     // Descripción detallada

    @SerializedName(value = "precio", alternate = ["price"])
    val price: Double = 0.0,      // Precio en CLP (soporta "precio" o "price")

    @SerializedName(value = "categoria", alternate = ["category"])
    val category: String?,        // Categoría (soporta "categoria" o "category")

    @SerializedName(value = "imagen", alternate = ["imageUrl"])
    val imageUrl: String?,        // URL de la imagen (soporta "imagen" o "imageUrl")

    @SerializedName("stock_por_sucursal")
    val stockBySucursal: Map<String, Int>? = null,  // Stock por sucursal

    // Campos adicionales del JSON local
    val stock: Double? = null,
    val stock_unit: String? = null,
    val freshness: String? = null,
    val origin: String? = null,
    val storage: String? = null,
    val availability: String? = null
)
