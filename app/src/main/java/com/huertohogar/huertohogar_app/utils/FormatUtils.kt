package com.huertohogar.huertohogar_app.utils

import java.util.Locale

/**
 * Función de extensión para formatear precios en CLP
 * Uso separador de miles con punto (formato chileno)
 * Si el precio es entero, no muestro decimales
 */
fun Double.formatPrecio(): String {
    return if (this % 1.0 == 0.0) {
        // Precio sin decimales - formato manual con puntos cada 3 dígitos
        this.toInt().toString().reversed().chunked(3).joinToString(".").reversed()
    } else {
        // Precio con decimales - uso formato regional
        String.format(Locale.getDefault(), "%,.0f", this)
    }
}
