package com.huertohogar.huertohogar_app.model

/**
 * Modelo de datos para productos del e-commerce
 * Cambié el modelo para enfocarlo en productos de comida fresca en lugar de jardinería
 */
data class Product(
    val sku: String,              // Código único del producto
    val name: String,             // Nombre del producto
    val description: String?,     // Descripción detallada
    val price: Double,            // Precio en CLP
    val stock: Double?,           // Cantidad disponible
    val stock_unit: String?,      // Unidad de medida (kg, unidades, etc.)
    val category: String?,        // Categoría (Frutas, Verduras, etc.)
    val imageUrl: String?,        // URL o nombre de la imagen
    
    // Campos específicos para productos alimenticios
    val freshness: String?,       // "Extra Fresco", "Fresco", "Del día"
    val origin: String?,          // "Local", "Valle del Maule", "Orgánico"
    val storage: String?,         // "Refrigerar", "Temperatura ambiente"
    val availability: String?     // "En Stock", "Stock limitado"
)
