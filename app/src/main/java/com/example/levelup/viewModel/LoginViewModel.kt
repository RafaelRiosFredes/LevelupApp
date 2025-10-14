package com.example.levelup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.reporitory.LoginReporitory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FormState(
    val correo: String = "",
    val contrasena: String = "",
    val puntos: Int = 0,
    val loginSuccess: Boolean = false,
    val errorMessage: String? = null
)

class LoginViewModel(private val repo: LoginReporitory) : ViewModel() {

    private val _form = MutableStateFlow(FormState())
    val form: StateFlow<FormState> = _form.asStateFlow()

    fun onCorreoChanged(newCorreo: String) {
        _form.value = _form.value.copy(correo = newCorreo)
    }

    fun onContrasenaChanged(newContrasena: String) {
        _form.value = _form.value.copy(contrasena = newContrasena)
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            val user = repo.getUserByCorreoAndContrasena(
                correo = _form.value.correo,
                contrasena = _form.value.contrasena
            )

            if (user != null) {
                _form.value = _form.value.copy(
                    loginSuccess = true,
                    puntos = user.puntos,
                    errorMessage = null
                )
            } else {
                _form.value = _form.value.copy(
                    loginSuccess = false,
                    errorMessage = "Correo o contraseña incorrectos"
                )
            }
        }
    }
}
