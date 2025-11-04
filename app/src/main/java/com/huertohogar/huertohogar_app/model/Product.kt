package com.huertohogar.huertohogar_app.model

data class Product(
    val sku: String,
    val name: String,
    val description: String?,
    val price: Double,
    val stock: Double?,
    val stock_unit: String?,
    val category: String?,
    val imageUrl: String?,
    val season: String?,
    val difficulty: String?,
    val plantingDepth: String?,
    val spacing: String?,
    val harvestTime: String?
)
