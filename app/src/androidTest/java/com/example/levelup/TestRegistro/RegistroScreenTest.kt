package com.example.levelup.TestRegistro

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.model.repository.UsuariosRepository
import com.example.levelup.ui.RegistroScreen
import com.example.levelup.viewmodel.UsuariosViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class RegistroScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    // 🔥 ViewModel REAL usando Room In-Memory
    private fun vmReal(): UsuariosViewModel {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        val repo = UsuariosRepository(db.usuarioDao()

        return UsuariosViewModel(repo)
    }

    // 🔥 NavController REAL para pruebas
    private fun navController(): TestNavHostController {
        val nav = TestNavHostController(ApplicationProvider.getApplicationContext())
        nav.navigatorProvider.addNavigator(ComposeNavigator())
        return nav
    }

    // ===========================================================================================
    // 1) TEST — Se muestran todos los campos del formulario
    // ===========================================================================================
    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun muestraCamposDelFormulario() {
        composeRule.setContent {
            RegistroScreen(
                navController = navController(),
                vm = vmReal()
            )
        }

        composeRule.onNodeWithText("Nombres").assertExists()
        composeRule.onNodeWithText("Apellidos").assertExists()
        composeRule.onNodeWithText("Correo").assertExists()
        composeRule.onNodeWithText("Contraseña").assertExists()
        composeRule.onNodeWithText("Repite la contraseña").assertExists()
        composeRule.onNodeWithText("Registrar").assertExists()
    }

    // ===========================================================================================
    // 2) TEST — Error si faltan campos (Snackbar)
    // ===========================================================================================
    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun muestraErrorSiFaltanCampos() {
        composeRule.setContent {
            RegistroScreen(
                navController = navController(),
                vm = vmReal()
            )
        }

        composeRule.onNodeWithText("Registrar").performClick()

        // Primer error: falta Nombres
        composeRule.onNodeWithText("Ingresa tus nombres").assertIsDisplayed()
    }

    // ===========================================================================================
    // 3) TEST — Error cuando las contraseñas no coinciden
    // ===========================================================================================
    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun errorCuandoContrasenasNoCoinciden() {
        composeRule.setContent {
            RegistroScreen(
                navController = navController(),
                vm = vmReal()
            )
        }

        composeRule.onNodeWithText("Nombres").performTextInput("Juan")
        composeRule.onNodeWithText("Apellidos").performTextInput("Perez")
        composeRule.onNodeWithText("Correo").performTextInput("juan@mail.com")
        composeRule.onNodeWithText("Contraseña").performTextInput("123456")
        composeRule.onNodeWithText("Repite la contraseña").performTextInput("999999")

        composeRule.onNodeWithText("Registrar").performClick()

        composeRule.onNodeWithText("Las contraseñas no coinciden").assertIsDisplayed()
    }

    // ===========================================================================================
    // 4) TEST — Cuando el registro es exitoso se llama al callback `onSaved()`
    // ===========================================================================================
    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun registroExitosoLlamaOnSaved() {
        var fueLlamado = false

        composeRule.setContent {
            RegistroScreen(
                navController = navController(),
                vm = vmReal(),
                onSaved = { fueLlamado = true }
            )
        }

        composeRule.onNodeWithText("Nombres").performTextInput("Ana")
        composeRule.onNodeWithText("Apellidos").performTextInput("Lopez")
        composeRule.onNodeWithText("Correo").performTextInput("ana@mail.com")
        composeRule.onNodeWithText("Contraseña").performTextInput("123456")
        composeRule.onNodeWithText("Repite la contraseña").performTextInput("123456")

        composeRule.onNodeWithText("Registrar").performClick()

        composeRule.waitUntil(timeoutMillis = 3000) { fueLlamado }

        assert(fueLlamado)
    }

    // ===========================================================================================
    // 5) TEST — Navega a login después de registrarse correctamente
    // ===========================================================================================
    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun navegaALoginDespuesDeRegistrar() {
        val nav = navController()

        composeRule.setContent {
            RegistroScreen(
                navController = nav,
                vm = vmReal()
            )
        }

        composeRule.onNodeWithText("Nombres").performTextInput("Carlos")
        composeRule.onNodeWithText("Apellidos").performTextInput("Rojas")
        composeRule.onNodeWithText("Correo").performTextInput("carlos@mail.com")
        composeRule.onNodeWithText("Contraseña").performTextInput("123456")
        composeRule.onNodeWithText("Repite la contraseña").performTextInput("123456")

        composeRule.onNodeWithText("Registrar").performClick()

        composeRule.waitUntil(timeoutMillis = 4000) {
            nav.currentDestination?.route == "login"
        }

        assert(nav.currentDestination?.route == "login")
    }
}
