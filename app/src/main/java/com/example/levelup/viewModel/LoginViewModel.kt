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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FormState(
    val id: Int? = null,
    val correo: String = "",
    val contrasena : String = "",
    val error: String? = null
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

    fun limpiarFormulario() = run { _form.value = FormState() }

    fun onChangeCorreo (v: String) =  _form.update { it.copy(correo = v) }

    fun onChangeContrasena (v: String) = _form.update { it.copy(contrasena = v) }


    fun guardar() = viewModelScope.launch {
        val f = _form.value
        if (f.correo.isBlank() || f.contrasena.isBlank()

        ){
            _form.update { it.copy(error = "completa todos los campos.") }
            return@launch
        }
        repo.guardar(f.id, f.correo, f.contrasena)
        limpiarFormulario()
    }

    fun eliminar(login: LoginReporitory) = viewModelScope.launch { repo.eliminar(login) }
}

