package com.example.levelup


import com.example.levelup.model.data.BoletaEntity
import com.example.levelup.model.data.BoletasDao
import com.example.levelup.model.repository.BoletaRepository
import com.example.levelup.remote.*
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BoletaRepositoryTest {

    private lateinit var dao: BoletasDao
    private lateinit var api: BoletaApiService
    private lateinit var repository: BoletaRepository

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        api = mockk(relaxed = true)
        repository = BoletaRepository(dao, api)
    }


    // Funciones auxiliares


    private fun crearUsuarioDTO() = UsuarioBoletaDTO(
        id = 10,
        nombres = "Juan",
        apellidos = "Pérez",
        correo = "juan@test.com"
    )

    private fun crearDetalleDTO() = listOf(
        DetalleBoletaDTO(
            productoId = 1L,
            nombreProducto = "Mouse",
            cantidad = 2,
            precioUnitario = 5000
        )
    )

    private fun crearDTO(
        idBoleta: Long? = 100
    ): BoletaDTO {
        return BoletaDTO(
            idBoleta = idBoleta,
            descuento = 10,
            total = 10000,
            fechaEmision = "2024-01-01",
            usuario = crearUsuarioDTO(),
            detalles = crearDetalleDTO()
        )
    }

    private fun crearEntity(
        id: Int = 0,
        backendId: Long? = 100
    ) = BoletaEntity(
        id = id,
        backendId = backendId,
        total = 10000,
        descuento = 10,
        fechaEmision = "2024-01-01",
        usuarioIdBackend = 10,
        usuarioNombre = "Juan",
        usuarioApellidos = "Pérez",
        usuarioCorreo = "juan@test.com",
        detalleTexto = "Mouse x2 = 10000"
    )

    // TESTS

    @Test
    fun `crearBoletaBackend - inserta entidad convertida en DAO`() = runTest {
        val boleta = crearEntity()
        val dto = crearDTO()

        coEvery { api.crearBoleta(any()) } returns dto

        repository.crearBoletaBackend(boleta)

        coVerify {
            dao.insertar(any())
        }
    }

    @Test
    fun `obtenerBoletasBackend - elimina todo y guarda lista nueva`() = runTest {
        val listaDTO = listOf(crearDTO(idBoleta = 1), crearDTO(idBoleta = 2))
        coEvery { api.obtenerBoletas() } returns listaDTO

        repository.obtenerBoletasBackend()

        coVerify { dao.eliminarTodas() }
        coVerify { dao.insertarBoletas(any()) }
    }

    @Test
    fun `obtenerBoletaBackendPorId - inserta entity convertida`() = runTest {
        val dto = crearDTO(200)
        coEvery { api.obtenerBoletaId(200) } returns dto

        repository.obtenerBoletaBackendPorId(200)

        coVerify { dao.insertar(any()) }
    }

    @Test
    fun `obtenerBoletaBackendPorId - si API falla retorna null`() = runTest {
        coEvery { api.obtenerBoletaId(any()) } throws RuntimeException("crash")

        val result = repository.obtenerBoletaBackendPorId(99)

        assert(result == null)
    }
}