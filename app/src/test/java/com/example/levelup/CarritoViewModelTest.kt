package com.example.levelup

import com.example.levelup.model.data.CarritoEntity
import com.example.levelup.model.repository.CarritoRepository
import com.example.levelup.viewmodel.CarritoViewModel
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CarritoViewModelTest {

    private lateinit var repository: CarritoRepository
    private lateinit var viewModel: CarritoViewModel
    private val dispatcher = StandardTestDispatcher()

    private val fakeFlow = MutableStateFlow<List<CarritoEntity>>(emptyList())

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        repository = mockk(relaxed = true)

        // Constructor especial para tests
        viewModel = CarritoViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun crearItem(
        cantidad: Int = 1
    ) = CarritoEntity(
        id = 0,
        productoId = 1,
        nombre = "Producto",
        precio = 19990.0,
        cantidad = cantidad,
        imagenUrl = "imagen.png"
    )

    // -------------------------------------------------------------

    @Test
    fun `agregarProducto llama repository_agregar`() = runTest {
        viewModel.agregarProducto(
            productoId = 1,
            nombre = "Producto X",
            precio = 1000.0,
            imagenUrl = "img"
        )

        dispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.agregar(any()) }
    }

    @Test
    fun `aumentarCantidad incrementa cantidad`() = runTest {
        val item = crearItem()

        viewModel.aumentarCantidad(item)
        dispatcher.scheduler.advanceUntilIdle()

        coVerify {
            repository.actualizar(
                match { it.cantidad == item.cantidad + 1 }
            )
        }
    }

    @Test
    fun `disminuirCantidad reduce si cantidad mayor a 1`() = runTest {
        val item = crearItem(cantidad = 3)

        viewModel.disminuirCantidad(item)
        dispatcher.scheduler.advanceUntilIdle()

        coVerify {
            repository.actualizar(
                match { it.cantidad == 2 }
            )
        }
    }

    @Test
    fun `disminuirCantidad elimina si cantidad es 1`() = runTest {
        val item = crearItem(cantidad = 1)

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
