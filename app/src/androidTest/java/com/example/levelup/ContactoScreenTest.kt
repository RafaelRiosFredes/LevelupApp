package com.example.levelup

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.levelup.ui.ContactoScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContactoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun enviarMensaje_conDatosCorrectos_llamaOnEnviar() {

        var enviado = false
        var enviadoNombre = ""
        var enviadoEmail = ""
        var enviadoMensaje = ""

        composeTestRule.setContent {
            ContactoScreen(
                onEnviar = { nombre, email, mensaje ->
                    enviado = true
                    enviadoNombre = nombre
                    enviadoEmail = email
                    enviadoMensaje = mensaje
                }
            )
        }

        // Completar formulario
        composeTestRule.onNodeWithText("Nombre Completo")
            .performTextInput("Savka Test")

        composeTestRule.onNodeWithText("Correo Electrónico")
            .performTextInput("test@mail.com")

        composeTestRule.onNodeWithText("Contenido")
            .performTextInput("Hola, este es un mensaje de prueba.")

        // Enviar formulario
        composeTestRule.onNodeWithText("Enviar Mensaje").performClick()

        // Validar que se llamó a onEnviar()
        assert(enviado)
        assert(enviadoNombre == "Savka Test")
        assert(enviadoEmail == "test@mail.com")
        assert(enviadoMensaje == "Hola, este es un mensaje de prueba.")
    }

    @Test
    fun enviarMensaje_conCamposVacios_muestraError() {

        composeTestRule.setContent {
            ContactoScreen()
        }

        // Click sin llenar nada
        composeTestRule.onNodeWithText("Enviar Mensaje").performClick()

        // Verifica que aparece snackbar de error
        composeTestRule
            .onNodeWithText("Corrige los errores antes de enviar")
            .assertExists()
    }
}