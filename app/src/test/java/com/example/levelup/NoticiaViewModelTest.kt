package com.example.levelup

import com.example.levelup.model.data.NoticiaEntity
import com.example.levelup.model.repository.NoticiaRepository
import com.example.levelup.viewmodel.NoticiasViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class NoticiasViewModelTest {

    @Test
    fun `ViewModel carga noticias al iniciar`() {

        val repoMock = mockk<NoticiaRepository>()

        val noticiasFake = listOf(
            NoticiaEntity(1, "Titulo 1", "Desc 1", "img1", "fuente1"),
            NoticiaEntity(2, "Titulo 2", "Desc 2", "img2", "fuente2")
        )

        every { repoMock.obtenerNoticias() } returns noticiasFake

        val viewModel = NoticiasViewModel(repoMock)

        assertEquals(2, viewModel.noticias.value.size)
        assertEquals("Titulo 1", viewModel.noticias.value[0].titulo)
    }
}
