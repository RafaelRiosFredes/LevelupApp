package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.repository.OpinionesRepository
import com.example.levelup.remote.OpinionRemoteDTO
import com.example.levelup.core.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OpinionesViewModel(
    private val repo: OpinionesRepository
) : ViewModel() {

    private val _opiniones = MutableStateFlow<List<OpinionRemoteDTO>>(emptyList())
    val opiniones = _opiniones.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    // ⭐ PROMEDIO DE ESTRELLAS
    private val _promedio = MutableStateFlow(0.0)
    val promedio = _promedio.asStateFlow()

    // ⭐ MI OPINIÓN (si el usuario ya opinó antes)
    private val _miOpinion = MutableStateFlow<OpinionRemoteDTO?>(null)
    val miOpinion = _miOpinion.asStateFlow()


    fun cargarOpiniones(idProducto: Long) = viewModelScope.launch {
        _loading.value = true

        val lista = repo.obtenerOpiniones(idProducto)
        _opiniones.value = lista

        // ⭐ CALCULAR PROMEDIO
        _promedio.value =
            if (lista.isEmpty()) 0.0 else lista.map { it.estrellas }.average()

        // ⭐ BUSCAR LA OPINIÓN DEL USUARIO ACTUAL
        val idUsuarioActual = UserSession.id ?: -1
        _miOpinion.value = lista.find { it.idUsuario == idUsuarioActual.toLong() }

        _loading.value = false
    }


    fun enviarOpinion(idProducto: Long, comentario: String, estrellas: Int) =
        viewModelScope.launch {
            repo.enviarOpinion(idProducto, comentario, estrellas)
            cargarOpiniones(idProducto)
        }


    fun eliminarOpinion(idProducto: Long) = viewModelScope.launch {
        repo.eliminarOpinion(idProducto)
        cargarOpiniones(idProducto)
    }
}
