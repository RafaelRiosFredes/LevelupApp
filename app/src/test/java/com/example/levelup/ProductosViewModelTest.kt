package com.example.levelup


import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.model.repository.ProductosRepository
import com.example.levelup.viewmodel.ProductosViewModel
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
class ProductosViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ProductosViewModel
    private lateinit var repository: ProductosRepository

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = mockk(relaxed = true)
        viewModel = ProductosViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun crearProductoPrueba(
        id: Int = 1,
        backendId: Long? = null
    ): ProductosEntity {
        return ProductosEntity(
            id = id,
            backendId = backendId,
            nombre = "Producto Test",
            precio = 9999.0,                // DOUBLE ✔
            descripcion = "Descripción test",
            imagenUrl = "https://test.com/img.png"
        )
    }

    @Test
    fun `insertarProducto llama al repository`() = runTest {
        val producto = crearProductoPrueba()

        viewModel.insertarProducto(producto)
        dispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.insertarProducto(producto) }
    }

    @Test
    fun `actualizarProducto llama al repository`() = runTest {
        val producto = crearProductoPrueba(id = 2)

        viewModel.actualizarProducto(producto)
        dispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.actualizarProducto(producto) }
    }

    @Test
    fun `eliminarProducto llama al repository`() = runTest {
        val producto = crearProductoPrueba(id = 3)

        viewModel.eliminarProducto(producto)
        dispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.eliminarProducto(producto) }
    }

    @Test
    fun `crearProductoBackend llama al repository`() = runTest {
        val producto = crearProductoPrueba(id = 4)

        viewModel.crearProductoBackend(producto)
        dispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.crearProducto(producto) }
    }

    @Test
    fun `actualizarProductoBackend llama al repository`() = runTest {
        val producto = crearProductoPrueba(id = 5, backendId = 10)

        viewModel.actualizarProductoBackend(producto)
        dispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.actualizarProductoBackend(producto) }
    }

    @Test
    fun `eliminarProductoBackend llama al repository`() = runTest {
        val producto = crearProductoPrueba(id = 6, backendId = 20)

        viewModel.eliminarProductoBackend(producto)
        dispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.eliminarProductoBackend(producto) }
    }

    @Test
    fun `sincronizarProductos llama al repository`() = runTest {
        coEvery { repository.sincronizarProductosDesdeBackend() } returns Unit

        viewModel.sincronizarProductos()
        dispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.sincronizarProductosDesdeBackend() }
    }
}