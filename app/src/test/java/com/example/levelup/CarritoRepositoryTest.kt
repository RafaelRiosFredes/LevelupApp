package com.example.levelup

import com.example.levelup.model.data.CarritoDao
import com.example.levelup.model.data.CarritoEntity
import com.example.levelup.model.repository.CarritoRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CarritoRepositoryTest {

    private lateinit var dao: CarritoDao
    private lateinit var repository: CarritoRepository

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = CarritoRepository(dao)
    }

    private fun crearItem() = CarritoEntity(
        id = 0,
        productoId = 1,
        nombre = "Mouse Gamer",
        precio = 15000.0,
        cantidad = 1,
        imagenUrl = "imagen.png"
    )

    @Test
    fun `agregar llama a dao_insertar`() = runTest {
        val item = crearItem()

        repository.agregar(item)

        coVerify { dao.insertar(item) }
    }

    @Test
    fun `actualizar llama a dao_actualizar`() = runTest {
        val item = crearItem()

        repository.actualizar(item)

        coVerify { dao.actualizar(item) }
    }

    @Test
    fun `eliminar llama a dao_eliminar`() = runTest {
        val item = crearItem()

        repository.eliminar(item)

        coVerify { dao.eliminar(item) }
    }

    @Test
    fun `eliminarTodo llama a dao_eliminarTodo`() = runTest {
        repository.eliminarTodo()

        coVerify { dao.eliminarTodo() }
    }
}
