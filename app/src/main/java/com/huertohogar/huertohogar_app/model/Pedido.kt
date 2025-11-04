package com.huertohogar.huertohogar_app.model

import com.huertohogar.huertohogar_app.viewmodel.CartItem
import java.text.SimpleDateFormat
import java.util.*

data class Pedido(
    val id: String = UUID.randomUUID().toString(),
    val numeroPedido: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val envio: Double,
    val total: Double,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val estado: EstadoPedido = EstadoPedido.PROCESANDO,
    val datosEntrega: DatosEntrega,
    val metodoPago: String
) {
    fun getFechaFormateada(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(fechaCreacion))
    }

    fun getCantidadProductos(): Int {
        return items.sumOf { it.quantity }
    }
}

data class DatosEntrega(
    val nombre: String,
    val email: String,
    val telefono: String,
    val direccion: String,
    val ciudad: String,
    val comuna: String
)

enum class EstadoPedido(val displayName: String, val color: Long) {
    PROCESANDO("Procesando", 0xFFFFA726),
    EN_PREPARACION("En Preparación", 0xFF29B6F6),
    EN_CAMINO("En Camino", 0xFF66BB6A),
    ENTREGADO("Entregado", 0xFF23AA49),
    CANCELADO("Cancelado", 0xFFEF5350)
}

