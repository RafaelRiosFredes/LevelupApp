package com.example.levelup

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.levelup.ui.LoginScreen
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    // ---------------------------------------
    // TEST ÚNICO: Login incorrecto muestra error
    // ---------------------------------------
    @Test
    fun loginIncorrecto_muestraError() {

        lateinit var navController: TestNavHostController

        composeRule.setContent {

            val context = LocalContext.current   // ← Necesario para TestNavHostController

            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            LoginScreen(navController = navController)
        }

        // Ingresar datos incorrectos
        composeRule.onNodeWithText("Correo").performTextInput("fake@mail.com")
        composeRule.onNodeWithText("Contraseña").performTextInput("1234")
        composeRule.onNodeWithText("Ingresar").performClick()

        // Verificar que aparece el mensaje de error
        composeRule.onNodeWithText("Credenciales incorrectas")
            .assertExists()
    }
}