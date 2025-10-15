package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.local.AppDatabase
import com.example.levelup.model.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginFormState(
    val correo: String = "",
    val contrasena: String = "",
    val mensaje: String? = null,
    val autenticado: Boolean = false
)

class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = LoginRepository(AppDatabase.get(app))

    private val _form = MutableStateFlow(LoginFormState())
    val form: StateFlow<LoginFormState> = _form

    fun onChangeCorreo(value: String) {
        _form.value = _form.value.copy(correo = value)
    }

    fun onChangeContrasena(value: String) {
        _form.value = _form.value.copy(contrasena = value)
    }

    fun iniciarSesion(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val f = _form.value

            if (f.correo.isBlank() || f.contrasena.isBlank()) {
                _form.value = f.copy(mensaje = "Debes ingresar correo y contraseña")
                return@launch
            }

            val usuario = repo.validarUsuario(f.correo, f.contrasena)
            if (usuario == null) {
                _form.value = f.copy(mensaje = "Correo o contraseña incorrectos")
            } else {
                _form.value = f.copy(
                    mensaje = "Bienvenido ${usuario.nombres}!",
                    autenticado = true
                )
                onSuccess()
            }
        }
    }
}
