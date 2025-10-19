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

            // Validación de campos vacíos
            if (f.correo.isBlank() || f.contrasena.isBlank()) {
                _form.value = f.copy(mensaje = "Completa todos los campos", exito = false)
                return@launch
            }

            try {
                val usuario = repo.obtenerUsuarioPorCorreo(f.correo)

                when {
                    usuario == null -> {
                        _form.value = f.copy(
                            mensaje = "Usuario no encontrado",
                            exito = false
                        )
                    }

                    usuario.contrasena != f.contrasena -> {
                        _form.value = f.copy(
                            mensaje = "Contraseña incorrecta",
                            exito = false
                        )
                    }

                    else -> {
                        _form.value = f.copy(
                            mensaje = "Inicio de sesión exitoso",
                            exito = true
                        )
                    }
                }

            } catch (e: Exception) {
                _form.value = f.copy(
                    mensaje = "Error al validar usuario",
                    exito = false
                )
            }
        }
    }


}
