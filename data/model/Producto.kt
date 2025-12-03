package data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Producto(
    val sku: String,
    val nombre: String,
    val precio: Double,
    val descripcion: String?,
    val imagen: String?
) : Parcelable
