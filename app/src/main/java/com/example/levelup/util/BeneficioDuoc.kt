package com.example.levelup.util

object BeneficioDuoc {

    // Regla: Si el correo contiene "@duocuc", recibe 20% de descuento.
    fun calcularTotalConBeneficio(correoUsuario: String, totalCompra: Int): Int {
        val esAlumnoDuoc = correoUsuario.lowercase().contains("@duocuc")

        return if (esAlumnoDuoc) {
            val descuento = (totalCompra * 0.20).toInt() // 20% Off
            totalCompra - descuento
        } else {
            totalCompra // Precio normal
        }
    }
}