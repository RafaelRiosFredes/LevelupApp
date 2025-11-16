package com.example.levelup

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.levelup.ui.NoticiasScreen
import org.junit.Rule
import org.junit.Test

class NoticiasScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun noticiasScreen_muestraTitulo() {

        lateinit var navController: TestNavHostController

        composeRule.setContent {

            val context = LocalContext.current

            navController = TestNavHostController(context).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            NoticiasScreen(navController = navController)
        }

        // Verifica que el título aparece en pantalla
        composeRule
            .onNodeWithText("Noticias Gamer")
            .assertExists()
    }
}
