package com.example.levelup

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.levelup.model.data.CarritoEntity
import com.example.levelup.model.repository.CarritoRepository
import com.example.levelup.viewmodel.CarritoViewModel
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
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
@OptIn(ExperimentalCoroutinesApi::class)
class CarritoViewModelTest {

    private lateinit var repository: CarritoRepository
    private lateinit var viewModel: CarritoViewModel
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        // Mock del repositorio
        repository = mockk(relaxed = true)

        // USAR Application REAL de Robolectric
        val app: Application = ApplicationProvider.getApplicationContext()

        // Crear el ViewModel MANUALMENTE sin que pase por AppDatabase
        viewModel = CarritoViewModel(app)
        viewModel.repository = repository
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun crearItem(cantidad: Int = 1) = CarritoEntity(
        id = 0,
        productoId = 1,
        nombre = "Producto",
        precio = 1000.0,
        cantidad = cantidad,
        imagenUrl = "img"
    )

    @Test
    fun `agregarProducto llama repository_agregar`() = runTest {
        viewModel.agregarProducto(1, "Prod X", 1000.0, "img")
        dispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.agregar(any()) }
    }

    @Test
    fun `aumentarCantidad incrementa`() = runTest {
        val item = crearItem()
        viewModel.aumentarCantidad(item)
        dispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.actualizar(match { it.cantidad == 2 }) }
    }

    @Test
    fun `disminuirCantidad reduce cuando mayor a 1`() = runTest {
        val item = crearItem(3)
        viewModel.disminuirCantidad(item)
        dispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.actualizar(match { it.cantidad == 2 }) }
    }

    @Test
    fun `disminuirCantidad elimina cuando cantidad es 1`() = runTest {
        val item = crearItem(1)
        viewModel.disminuirCantidad(item)
        dispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.eliminar(item) }
    }

    @Test
    fun `eliminar llama repository_eliminar`() = runTest {
        val item = crearItem()
        viewModel.eliminar(item)
        dispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.eliminar(item) }
    }

    @Test
    fun `vaciarCarrito llama eliminarTodo`() = runTest {
        viewModel.vaciarCarrito()
        dispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.eliminarTodo() }
    }
}
