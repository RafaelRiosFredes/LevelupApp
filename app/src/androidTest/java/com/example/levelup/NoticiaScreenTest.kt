package com.example.levelup


import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.ui.NoticiasScreen
import org.junit.Rule
import org.junit.Test

class NoticiasScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun noticiasScreen_muestraTitulo() {

        composeRule.setContent {
            NoticiasScreen()
        }

        // Verifica que el título está en pantalla
        composeRule
            .onNodeWithText("Noticias Gamer")
            .assertExists()
    }
}