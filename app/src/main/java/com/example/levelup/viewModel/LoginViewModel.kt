package com.example.levelup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.local.LoginEntity
import com.example.levelup.model.reporitory.LoginReporitory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

data class FormState(
    val id: Int? = null,
    val correo: String,
    val contrasena : String
)

class LoginViewModel(private val repo: LoginReporitory) : ViewModel() {

    val usuarios: StateFlow<List<LoginReporitory>> =
        repo.observarUsuarios().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed( 5_000),
            initialValue = emptyList()
        )
    private val _form = MutableStateFlow(FormState())

    val form : StateFlow <FormState> = _form.asStateFlow()

    fun cargarParaEditar(login : LoginEntity) {
        _form.value = FormState (
            id = login.id,
            correo = login.correo,
            contrasena = login.contrasena
        )
    }



}

