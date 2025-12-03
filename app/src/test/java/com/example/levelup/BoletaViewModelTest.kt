package com.example.levelup


import android.app.Application
import com.example.levelup.model.data.BoletaEntity
import com.example.levelup.viewmodel.BoletaViewModel
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
class BoletaViewModelTest {

    private lateinit var viewModel: BoletaViewModel
    private lateinit var repository: BoletaRepository
    private lateinit var app: Application
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = mockk(relaxed = true)
        app = mockk(relaxed = true)
        viewModel = BoletaViewModel(app, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun crearBoleta(
        backendId: Long? = 100
    ) = BoletaEntity(
        id = 0,
        backendId = backendId,
        total = 20000,
        descuento = 5,
        fechaEmision = "2024-01-02",
        usuarioIdBackend = 15,
        usuarioNombre = "Ana",
        usuarioApellidos = "Lopez",
        usuarioCorreo = "ana@test.com",
        detalleTexto = "Producto x1 = 20000"
    )

    @Test
    fun `crearBoletaRoom llama a repository_insertarLocal`() = runTest {
        val boleta = crearBoleta()

        viewModel.crearBoletaRoom(boleta)
        dispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.insertarLocal(boleta) }
    }

    @Test
    fun `crearBoletaBackend llama a repository_crearBoletaBackend`() = runTest {
        val boleta = crearBoleta()
        coEvery { repository.crearBoletaBackend(boleta) } returns boleta

        viewModel.crearBoletaBackend(boleta)
        dispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.crearBoletaBackend(boleta) }
    }

    @Test
    fun `sincronizar llama a repository_obtenerBoletasBackend`() = runTest {
        viewModel.sincronizar()
        dispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.obtenerBoletasBackend() }
    }
}