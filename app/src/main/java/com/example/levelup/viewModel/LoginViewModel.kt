package com.example.levelup_gamerapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup_gamerapp.model.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LoginRepository
    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje

    init {
        val dao = AppDataBase.getDatabase(application).registroUsuarioDao()
        repository = LoginRepository(dao)
    }

    fun login(correo: String, contrasena: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (correo.isBlank() || contrasena.isBlank()) {
                _mensaje.value = "Completa todos los campos."
                return@launch
            }

            val accesoValido = repository.login(correo, contrasena)
            if (accesoValido) {
                _mensaje.value = "Bienvenido!!"
                onSuccess()
            } else {
                _mensaje.value = "Correo o contraseña incorrectos"
            }
        }
    }
}
