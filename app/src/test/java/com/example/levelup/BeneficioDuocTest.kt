package com.example.levelup.util

import org.junit.Assert.assertEquals
import org.junit.Test

class BeneficioDuocTest {

    @Test
    fun `usuario con correo duoc recibe 20 porciento de descuento`() {
        // Un usuario registrado con duocuc  comprando algo de $10.000
        val correo = "alumno@duocuc.cl"
        val totalOriginal = 10000

        // Calculamos su total
        val totalFinal = BeneficioDuoc.calcularTotalConBeneficio(correo, totalOriginal)

        // Debería pagar $8.000
        assertEquals(8000, totalFinal)
    }

    @Test
    fun `usuario gmail paga precio normal sin descuento`() {
        // otra persona sin correo duoc comprando lo mismo
        val correo = "cliente@gmail.com"
        val totalOriginal = 10000

        // Calculamos
        val totalFinal = BeneficioDuoc.calcularTotalConBeneficio(correo, totalOriginal)

        // Paga los $10.000 completos
        assertEquals(10000, totalFinal)
    }
}