package com.example.levelup

import com.example.levelup.model.repository.NoticiaRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class NoticiaRepositoryTest {

    private val repository = NoticiaRepository()

    @Test
    fun `obtenerNoticias retorna lista con 3 elementos`() {
        val noticias = repository.obtenerNoticias()
        assertEquals(3, noticias.size)
    }

    @Test
    fun `primera noticia tiene titulo correcto`() {
        val noticia = repository.obtenerNoticias().first()
        assertEquals("Nuevo tráiler de GTA VI", noticia.titulo)
    }
}
