package com.example.levelup.TestUsuario

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.model.repository.UsuariosRepository
import com.example.levelup.ui.UsuariosScreen
import com.example.levelup.viewmodel.UsuariosViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class UsuariosScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun vmConUsuarios(lista: List<UsuarioEntity>): UsuariosViewModel {
        val ctx = ApplicationProvider.getApplicationContext<Context>()

        val db = Room.inMemoryDatabaseBuilder(
            ctx,
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        val repo = UsuariosRepository(db.usuarioDao())

        // Insertamos los usuarios iniciales en la BD de prueba
        runBlocking {
            lista.forEach { repo.insertar(it) }
        }

        return UsuariosViewModel(repo)
    }

    @Test
    fun muestraMensajeSiListaVacia() {
        val vm = vmConUsuarios(emptyList())

        composeTestRule.setContent {
            UsuariosScreen(vm, {}, {}, {})
        }

        composeTestRule.onNodeWithText("No hay usuarios. Pulsa + para agregar uno.")
            .assertIsDisplayed()
    }

    @Test
    fun muestraUsuariosEnLista() {
        val vm = vmConUsuarios(
            listOf(
                UsuarioEntity(
                    nombres = "Juan",
                    apellidos = "Perez",
                    correo = "j@mail.com",
                    contrasena = "123456",
                    telefono = 0,
                    fechaNacimiento = "01/01/2000",
                    fotoPerfil = null
                ),
                UsuarioEntity(
                    nombres = "Ana",
                    apellidos = "Lopez",
                    correo = "a@mail.com",
                    contrasena = "123456",
                    telefono = 0,
                    fechaNacimiento = "01/01/2000",
                    fotoPerfil = null
                )
            )
        )

        composeTestRule.setContent {
            UsuariosScreen(vm, {}, {}, {})
        }

        composeTestRule.onNodeWithText("Juan Perez").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ana Lopez").assertIsDisplayed()
    }
}