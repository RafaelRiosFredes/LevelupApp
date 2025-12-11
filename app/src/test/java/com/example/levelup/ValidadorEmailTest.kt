
package com.example.levelup.util

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ValidadorEmailTest(
    private val emailEntrada: String,
    private val resultadoEsperado: Boolean
) {


    //test de prueba que incluye correos válidos, vacíos y mal formados
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "Caso {index}: El email ''{0}'' es valido? {1}")
        fun datos(): Collection<Array<Any>> {
            return listOf(
                // Array: [Email a probar, Resultado Esperado]
                arrayOf("usuario@duoc.cl", true),    // Caso 1: Correcto institucional
                arrayOf("juan.perez", false),        // Caso 2: Sin dominio (Falso)
                arrayOf("", false),                  // Caso 3: Vacío (Falso)
                arrayOf("admin@level.up", true),     // Caso 4: Otro dominio (Correcto)
                arrayOf("sin_arroba.com", false),    // Caso 5: Sin arroba (Falso)
                arrayOf("nombre@dominio", false)     // Caso 6: Dominio incompleto (Falso)
            )
        }
    }

    @Test
    fun `validar multiples formatos de email`() {
        // 1. WHEN: Llamamos a la función real de la app
        val resultadoReal = ValidadorUtil.esEmailValido(emailEntrada)

        // 2. THEN: Verificamos que coincida con lo esperado
        assertEquals("Error al validar: $emailEntrada", resultadoEsperado, resultadoReal)
    }
}