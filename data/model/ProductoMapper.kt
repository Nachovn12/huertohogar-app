package data.model

import data.db.ProductoEntity

fun Producto.toEntity(): ProductoEntity = ProductoEntity(
    sku = sku,
    nombre = nombre,
    precio = precio,
    descripcion = descripcion,
    imagen = imagen
)

fun ProductoEntity.toModel(): Producto = Producto(
    sku = sku,
    nombre = nombre,
    precio = precio,
    descripcion = descripcion,
    imagen = imagen
)

