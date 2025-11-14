package com.example.levelup.TestUsuario

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.model.repository.UsuariosRepository
import com.example.levelup.viewmodel.UsuariosViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UsuariosViewModelTest {

    private lateinit var db: AppDatabase
    private lateinit var repo: UsuariosRepository
    private lateinit var vm: UsuariosViewModel

    @Before
    fun setup() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repo = UsuariosRepository(db.usuarioDao())
        vm = UsuariosViewModel(repo)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun registrarUsuario_camposVacíos_daError() = runTest {
        vm.registrarUsuario()

        // 🔥 IMPORTANTE: esperar que la corrutina termine
        advanceUntilIdle()

        assertEquals("Completa todos los campos ", vm.form.value.mensaje)
    }
    @Test
    fun loginCorrecto() = runTest {
        vm.onChangeCorreo("u@duocuc.com")
        vm.onChangeContrasena("123456")

        repo.insertar(
            UsuarioEntity(
                nombres = "User",
                apellidos = "Test",
                correo = "u@mail.com",
                contrasena = "123456",
                telefono = 1,
                fechaNacimiento = "01/01/2000",
                fotoPerfil = null
            )
        )

        val ok = vm.login("u@duocuc.com", "123456")

        Assert.assertTrue(ok)
        Assert.assertEquals("Inicio de sesión exitoso ", vm.mensaje.value)
    }
}

