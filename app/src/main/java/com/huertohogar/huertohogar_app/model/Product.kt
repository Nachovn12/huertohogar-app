package com.huertohogar.huertohogar_app.model

data class Product(
    val id: Long,
    val name: String,
    val description: String?,
    val price: Double,
    val stock: Int?,
    val stock_unit: String?, // Corregido para que coincida con la BD
    val imageUrl: String?,
    val categoryId: Long?,
    val category: Category?,
    val season: String?,
    val difficulty: String?,
    val plantingDepth: String?,
    val spacing: String?,
    val harvestTime: String?,
    val createdAt: String?,
    val updatedAt: String?
)

data class Category(
    val id: Long,
    val name: String
)
