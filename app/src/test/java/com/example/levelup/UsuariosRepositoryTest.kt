package com.example.levelup

import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.model.data.UsuariosDao
import com.example.levelup.model.repository.UsuariosRepository
import com.example.levelup.remote.UsuarioDTO
import com.example.levelup.remote.UsuariosApiService
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsuariosRepositoryTest {

    private lateinit var dao: UsuariosDao
    private lateinit var api: UsuariosApiService
    private lateinit var repository: UsuariosRepository

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        api = mockk(relaxed = true)
        repository = UsuariosRepository(dao, api)
    }

    private fun crearEntity(
        backendId: Long? = 100L
    ) = UsuarioEntity(
        id = 0,
        nombres = "Juan",
        apellidos = "Pérez",
        correo = "juan@test.com",
        contrasena = "1234",
        telefono = 99999999,
        fechaNacimiento = "2000-01-01",
        fotoPerfil = null,
        duoc = true,
        descApl = false,
        rol = "user",
        backendId = backendId
    )

    private fun crearDTO(
        id: Long? = 100L
    ) = UsuarioDTO(
        id = id,
        nombres = "Juan",
        apellidos = "Pérez",
        correo = "juan@test.com",
        contrasena = "1234",
        telefono = 99999999,
        fechaNacimiento = "2000-01-01",
        fotoPerfil = null,
        duoc = true,
        descApl = false,
        rol = "user"
    )

    // Test

    @Test
    fun `crearUsuarioBackend inserta entidad convertida en DAO`() = runTest {
        val user = crearEntity()
        val dto = crearDTO()
        coEvery { api.crearUsuario(any()) } returns dto

        repository.crearUsuarioBackend(user)

        coVerify { dao.insertar(any()) }
    }


    @Test
    fun `actualizarUsuarioBackend actualiza usuario si backendId no es null`() = runTest {
        val usuario = crearEntity(backendId = 55L)
        val dto = crearDTO(id = 55L)

        coEvery { api.actualizarUsuario(55L, any()) } returns dto

        repository.actualizarUsuarioBackend(usuario)

        coVerify { dao.actualizar(any()) }
    }

    @Test
    fun `actualizarUsuarioBackend NO llama API si backendId es null`() = runTest {
        val usuario = crearEntity(backendId = null)

        repository.actualizarUsuarioBackend(usuario)

        coVerify(exactly = 0) { api.actualizarUsuario(any(), any()) }
    }

    @Test
    fun `eliminarUsuarioBackend llama API y DAO si backendId existe`() = runTest {
        val usuario = crearEntity(backendId = 77L)

        coEvery { api.eliminarUsuario(77L) } returns Unit

        repository.eliminarUsuarioBackend(usuario)

        coVerify { dao.eliminar(usuario) }
    }

    @Test
    fun `sincronizarUsuarios elimina y reinserta lista`() = runTest {
        val listaDTO = listOf(
            crearDTO(id = 1),
            crearDTO(id = 2)
        )

        coEvery { api.obtenerUsuarios() } returns listaDTO

        repository.sincronizarUsuarios()

        coVerify { dao.eliminarTodos() }
        coVerify { dao.insertarUsuarios(any()) }
    }

    @Test
    fun `login retorna usuario correcto desde DAO`() = runTest {
        val usuario = crearEntity()
        coEvery { dao.login("a@a.com", "123") } returns usuario

        val result = repository.login("a@a.com", "123")

        assert(result != null)
        assert(result!!.correo == "juan@test.com")
    }
}
