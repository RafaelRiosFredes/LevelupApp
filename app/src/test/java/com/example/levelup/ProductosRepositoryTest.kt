package com.example.levelup


import com.example.levelup.model.data.ProductosDao
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.model.repository.ProductosRepository
import com.example.levelup.remote.ProductosApiService
import com.example.levelup.remote.ProductosDTO
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductosRepositoryTest {

    private lateinit var dao: ProductosDao
    private lateinit var api: ProductosApiService
    private lateinit var repository: ProductosRepository

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        api = mockk(relaxed = true)
        repository = ProductosRepository(dao, api)
    }

    //  Funciones auxiliares para crear DTO y Entity

    private fun crearDTO(
        id: Long = 1L,
        nombre: String = "Test Producto",
        precio: Double = 999.0,
        descripcion: String = "Descripción de prueba",
        imagenUrl: String = "https://test.com/img.png"
    ): ProductosDTO {
        return ProductosDTO(
            id = id,
            nombre = nombre,
            precio = precio,
            descripcion = descripcion,
            imagenUrl = imagenUrl
        )
    }

    private fun crearEntity(
        id: Int = 1,
        backendId: Long? = null
    ): ProductosEntity {
        return ProductosEntity(
            id = id,
            backendId = backendId,
            nombre = "Producto Test",
            precio = 999.0,
            descripcion = "Descripción test",
            imagenUrl = "https://test.com/img.png"
        )
    }

    // Test

    @Test
    fun `obtenerProductoDesdeBackend guarda la entidad convertida en dao`() = runTest {
        val dto = crearDTO(id = 10)
        coEvery { api.obtenerProductoPorId(10) } returns dto

        repository.obtenerProductoDesdeBackend(10)

        coVerify {
            dao.insertar(
                match {
                    it.nombre == dto.nombre &&
                            it.precio == dto.precio &&
                            it.descripcion == dto.descripcion
                }
            )
        }
    }

    @Test
    fun `crearProducto llama API y luego inserta entidad`() = runTest {
        val entity = crearEntity()
        val dto = crearDTO(id = 55)

        coEvery { api.crearProducto(any()) } returns dto

        repository.crearProducto(entity)

        coVerify { dao.insertar(any()) }
    }

    @Test
    fun `actualizarProductoBackend actualiza en dao si backendId existe`() = runTest {
        val entity = crearEntity(backendId = 20)
        val dto = crearDTO(id = 20)

        coEvery { api.actualizarProducto(20, any()) } returns dto

        repository.actualizarProductoBackend(entity)

        coVerify { dao.actualizar(any()) }
    }

    @Test
    fun `actualizarProductoBackend NO llama API si backendId es null`() = runTest {
        val entity = crearEntity(backendId = null)

        repository.actualizarProductoBackend(entity)

        coVerify(exactly = 0) { api.actualizarProducto(any(), any()) }
    }


    @Test
    fun `eliminarProductoBackend elimina en API y luego en dao`() = runTest {
        val entity = crearEntity(backendId = 44)

        coEvery { api.eliminarProducto(44) } returns Unit

        repository.eliminarProductoBackend(entity)

        coVerify { dao.eliminar(entity) }
    }


    @Test
    fun `sincronizarProductosDesdeBackend elimina todo y guarda lista nueva`() = runTest {
        val listaDTO = listOf(
            crearDTO(id = 1, nombre = "A"),
            crearDTO(id = 2, nombre = "B")
        )

        coEvery { api.obtenerProductos() } returns listaDTO

        repository.sincronizarProductosDesdeBackend()

        coVerify { dao.eliminarTodos() }
        coVerify { dao.insertarProductos(any()) }
    }
}