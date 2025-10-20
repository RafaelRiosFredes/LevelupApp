package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.model.repository.UsuariosRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UsuariosViewModel(private val repo: UsuariosRepository): ViewModel() {

    val usuarios: StateFlow<List<UsuarioEntity>> =
        repo.todosLosUsuarios().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun insertarUsuario(u: UsuarioEntity) {
        viewModelScope.launch { repo.insertar(u) }
    }

    fun actualizarUsuario(u: UsuarioEntity) {
        viewModelScope.launch { repo.actualizar(u) }
    }

    fun eliminarUsuario(u: UsuarioEntity) {
        viewModelScope.launch { repo.eliminar(u) }
    }

    fun usuarioPorId(id: Int): Flow<UsuarioEntity?> = repo.obtenerPorId(id)
}
