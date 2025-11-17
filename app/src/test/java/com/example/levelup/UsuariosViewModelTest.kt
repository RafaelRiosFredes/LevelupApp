package com.example.levelup

import android.app.Application
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.model.repository.UsuariosRepository
import com.example.levelup.viewmodel.UsuariosViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsuariosViewModelTest {

    private lateinit var viewModel: UsuariosViewModel
    private lateinit var repository: UsuariosRepository
    private lateinit var app: Application
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = mockk(relaxed = true)
        app = mockk(relaxed = true)
        viewModel = UsuariosViewModel(app, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun crearUsuario(
        backendId: Long? = 100
    ) = UsuarioEntity(
        nombres = "Juan",
        apellidos = "Pérez",
        correo = "correo@test.com",
        contrasena = "1234",
        telefono = 9999,
        fechaNacimiento = "2000-01-01",
        fotoPerfil = null,
        duoc = false,
        descApl = false,
        rol = "user",
        backendId = backendId
    )

    //Test

    @Test
    fun `insertarUsuario llama repository_insertar`() = runTest {
        val user = crearUsuario()

        viewModel.insertarUsuario(user)
        dispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.insertar(user) }
    }

    @Test
    fun `actualizarUsuario llama repository_actualizar`() = runTest {
        val user = crearUsuario()

        viewModel.actualizarUsuario(user)
        dispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.actualizar(user) }
    }

    @Test
    fun `eliminarUsuario llama repository_eliminar`() = runTest {
        val user = crearUsuario()

        viewModel.eliminarUsuario(user)
        dispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.eliminar(user) }
    }

    @Test
    fun `crearUsuarioBackend llama repository`() = runTest {
        val user = crearUsuario()
        coEvery { repository.crearUsuarioBackend(user) } returns user

        viewModel.crearUsuarioBackend(user)

        coVerify { repository.crearUsuarioBackend(user) }
    }

    @Test
    fun `actualizarUsuarioBackend llama repository`() = runTest {
        val user = crearUsuario()
        coEvery { repository.actualizarUsuarioBackend(user) } returns Unit

        viewModel.actualizarUsuarioBackend(user)

        coVerify { repository.actualizarUsuarioBackend(user) }
    }

    @Test
    fun `eliminarUsuarioBackend llama repository`() = runTest {
        val user = crearUsuario()
        coEvery { repository.eliminarUsuarioBackend(user) } returns Unit

        viewModel.eliminarUsuarioBackend(user)

        coVerify { repository.eliminarUsuarioBackend(user) }
    }

    @Test
    fun `sincronizarUsuarios llama repository`() = runTest {
        viewModel.sincronizarUsuarios()
        dispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.sincronizarUsuarios() }
    }

    @Test
    fun `login correcto actualiza usuarioActual y mensaje`() = runTest {
        val user = crearUsuario()
        coEvery { repository.login("a@a.com", "123") } returns user

        val result = viewModel.login("a@a.com", "123")

        assert(result != null)
        assert(viewModel.loginMensaje.value == "Ingreso exitoso")
        assert(viewModel.usuarioActual.value == user)
    }

    @Test
    fun `login incorrecto setea mensaje de error`() = runTest {
        coEvery { repository.login("x@x.com", "000") } returns null

        val result = viewModel.login("x@x.com", "000")

        assert(result == null)
        assert(viewModel.loginMensaje.value == "Credenciales incorrectas")
    }

    @Test
    fun `logout limpia usuarioActual`() = runTest {
        viewModel.logout()
        assert(viewModel.usuarioActual.value == null)
    }
}
