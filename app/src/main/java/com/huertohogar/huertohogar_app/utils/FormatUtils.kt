package com.huertohogar.huertohogar_app.utils

import java.util.Locale

/**
 * Formatea un precio Double a String con separador de miles (.)
 * Si es un número entero, no muestra decimales
 * Si tiene decimales, los muestra con el formato regional
 */
fun Double.formatPrecio(): String {
    return if (this % 1.0 == 0.0) {
        this.toInt().toString().reversed().chunked(3).joinToString(".").reversed()
    } else {
        String.format(Locale.getDefault(), "%,.0f", this)
    }
}
