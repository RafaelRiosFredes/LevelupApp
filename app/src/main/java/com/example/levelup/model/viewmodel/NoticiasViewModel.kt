package com.example.levelup.model.viewmodel

import androidx.lifecycle.ViewModel
import com.example.levelup.model.data.NoticiaEntity
import com.example.levelup.model.repository.NoticiaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoticiasViewModel(
    private val repo: NoticiaRepository = NoticiaRepository()
) : ViewModel() {

    private val _noticias = MutableStateFlow<List<NoticiaEntity>>(emptyList())
    val noticias: StateFlow<List<NoticiaEntity>> = _noticias.asStateFlow()

    init {
        cargarNoticias()
    }

    private fun cargarNoticias() {
        _noticias.value = repo.obtenerNoticias()
    }
}
