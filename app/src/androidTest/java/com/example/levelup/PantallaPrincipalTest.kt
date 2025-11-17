package com.example.levelup

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.levelup.ui.PantallaPrincipal
import org.junit.Rule
import org.junit.Test

class PantallaPrincipalTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun pantallaPrincipal_muestraCategorias() {

        lateinit var navController: TestNavHostController

        composeRule.setContent {

            val context = LocalContext.current

            navController = TestNavHostController(context).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            PantallaPrincipal(
                navController = navController,
                onLogout = {},
                onNavigate = {}
            )
        }

        // Verificar título
        composeRule.onNodeWithText("CATEGORÍAS").assertIsDisplayed()

        // Verificar categorías
        composeRule.onNodeWithText("Juegos de Mesa").assertExists()
        composeRule.onNodeWithText("Consolas").assertExists()
        composeRule.onNodeWithText("Mouse").assertExists()
    }
}
