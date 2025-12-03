package com.huertohogar.huertohogar_app.data.mapper

import com.huertohogar.huertohogar_app.data.db.ProductEntity
import com.huertohogar.huertohogar_app.model.Product

/**
 * Convierte ProductEntity (Room) a Product (modelo de negocio)
 */
fun ProductEntity.toProduct(): Product {
    return Product(
        sku = this.sku,
        name = this.nombre,
        description = this.descripcion,
        price = this.precio.toDouble(),
        category = this.categoria,
        imageUrl = this.imagen,
        stockBySucursal = mapOf(
            "Concepción" to this.stockConcepcion,
            "Talcahuano" to this.stockTalcahuano,
            "Chillán" to this.stockChillan,
            "Los Ángeles" to this.stockLosAngeles
        ),
        stock = this.stockConcepcion.toDouble(),
        stock_unit = "unidades",
        freshness = null,
        origin = null,
        storage = null,
        availability = if (stockConcepcion > 0) "En Stock" else "Agotado"
    )
}

/**
 * Convierte Product (modelo de negocio) a ProductEntity (Room)
 */
fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        sku = this.sku,
        nombre = this.name ?: "Producto",
        categoria = this.category,
        descripcion = this.description,
        precio = this.price.toInt(),
        imagen = this.imageUrl,
        stockConcepcion = this.stockBySucursal?.get("Concepción") ?: this.stock?.toInt() ?: 0,
        stockTalcahuano = this.stockBySucursal?.get("Talcahuano") ?: 0,
        stockChillan = this.stockBySucursal?.get("Chillán") ?: 0,
        stockLosAngeles = this.stockBySucursal?.get("Los Ángeles") ?: 0
    )
}

/**
 * Convierte una lista de ProductEntity a Product
 */
fun List<ProductEntity>.toProductList(): List<Product> {
    return this.map { it.toProduct() }
}

/**
 * Convierte una lista de Product a ProductEntity
 */
fun List<Product>.toEntityList(): List<ProductEntity> {
    return this.map { it.toEntity() }
}

