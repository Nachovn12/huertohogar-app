package data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey val sku: String,
    val nombre: String,
    val precio: Double,
    val descripcion: String?,
    val imagen: String?
)
