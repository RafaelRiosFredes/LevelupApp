package com.example.levelup

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.levelup.ui.PantallaPrincipal
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PantallaPrincipalTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun pantallaPrincipal_muestraCategorias() {
        composeTestRule.setContent {
            PantallaPrincipal()
        }

        // Verificar título de la sección
        composeTestRule.onNodeWithText("CATEGORÍAS").assertIsDisplayed()

        // Verificar algunas categorías
        composeTestRule.onNodeWithText("Juegos de Mesa").assertExists()
        composeTestRule.onNodeWithText("Consolas").assertExists()
        composeTestRule.onNodeWithText("Mouse").assertExists()
    }
}