package com.example.levelup

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.levelup.ui.ContactoScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContactoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Crea navController de prueba
    private fun navController() = TestNavHostController(
        ApplicationProvider.getApplicationContext()
    )

    @Test
    fun enviarFormulario_correcto_muestraSnackbarExito() {
        composeTestRule.setContent {
            ContactoScreen(navController = navController())
        }

        // Completar campos correctamente
        composeTestRule.onNodeWithText("Nombre completo")
            .performTextInput("Savka Test")

        composeTestRule.onNodeWithText("Correo electrónico")
            .performTextInput("savka@mail.com")

        composeTestRule.onNodeWithText("Mensaje")
            .performTextInput("Hola soy un mensaje válido.")

        // Click en Enviar
        composeTestRule.onNodeWithText("Enviar mensaje").performClick()

        // Verificar snackbar
        composeTestRule
            .onNodeWithText("Mensaje enviado exitosamente")
            .assertExists()
    }

    @Test
    fun enviarFormulario_vacio_muestraSnackbarError() {
        composeTestRule.setContent {
            ContactoScreen(navController = navController())
        }

        // Clic sin llenar nada
        composeTestRule.onNodeWithText("Enviar mensaje").performClick()

        // Verificar snackbar de error
        composeTestRule
            .onNodeWithText("Corrige los campos marcados")
            .assertExists()
    }
}
