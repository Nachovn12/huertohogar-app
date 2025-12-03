package com.huertohogar.huertohogar_app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para almacenar productos en la base de datos local
 * Mapea los campos del JSON del endpoint público
 */
@Entity(tableName = "productos")
data class ProductEntity(
    @PrimaryKey val sku: String = "",
    val nombre: String? = null,
    val categoria: String? = null,
    val descripcion: String? = null,
    val precio: Int = 0,
    val imagen: String? = null,
    val stockConcepcion: Int = 0,
    val stockTalcahuano: Int = 0,
    val stockChillan: Int = 0,
    val stockLosAngeles: Int = 0
)

