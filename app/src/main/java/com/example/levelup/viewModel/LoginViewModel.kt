package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginFormState(
    val correo: String = "",
    val contrasena: String = "",
    val mensaje: String? = null,
    val exito: Boolean = false
)

class LoginViewModel(private val repo: LoginRepository) : ViewModel() {
    private val _form = MutableStateFlow(LoginFormState())
    val form: StateFlow<LoginFormState> = _form.asStateFlow()

    fun onChangeCorreo(v: String) {
        _form.value = _form.value.copy(correo = v)
    }

    fun onChangeContrasena(v: String) {
        _form.value = _form.value.copy(contrasena = v)
    }


    fun validarUsuario() {
        viewModelScope.launch {
            val f = _form.value
            if (f.correo.isBlank() || f.contrasena.isBlank()) {
                _form.value = f.copy(mensaje = "Completa todos los campos")
                return@launch
            }

            val esValido = repo.validarUsuario(f.correo, f.contrasena)
            _form.value = if (esValido)
                f.copy(exito = true, mensaje = "Inicio de sesión exitoso")
            else
                f.copy(mensaje = "Correo o contraseña incorrectos")
        }
    }

}
