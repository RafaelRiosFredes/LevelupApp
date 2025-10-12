package com.example.levelup.viewModel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.app.Application
import com.example.levelup.viewmodel.LoginDataStore

data class LoginUiState(
    val correo: String = "",
    val contrasena: String = "",
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val cargando: Boolean = false,
    val ingresoExitoso: Boolean = false,
    val sesionActiva: Boolean = false
)

class LoginViewModel (application : Application) : AndroidViewModel(application) {

    private val dataStore = LoginDataStore(application)

    private val _estado = MutableStateFlow(LoginUiState())
    val estado : StateFlow<LoginUiState> = _estado

    init {
        verificarSesionActiva()
    }

    //Verifica si ya esxiste una sesion guardada
    private fun verificarSesionActiva() {
        viewModelScope.launch {
            dataStore.obtenerSesionActiva().collect{ active ->
                _estado.update { it.copy(sesionActiva = active) }
            }
        }
    }

    fun onCorreoChange (nuevo:  String) {
        _estado.update { it.copy(correo = nuevo, errorCorreo = null) }
    }

    fun onContrasenaChange (nuevo:  String) {
        _estado.update { it.copy(contrasena = nuevo, errorContrasena = null) }
    }

    //funcion para validar campos
    private fun camposValidos(): Boolean{
        val emailValido = _estado.value.correo.contains("@")
        val contValida  = _estado.value.contrasena.length >=6

        _estado.update {
            it.copy(
                errorCorreo = if (!emailValido) "Correo Invalido" else null,
                errorContrasena = if (!contValida) "La contraseña debe tener minimo 6 caracteres" else null
            )
        }
        return emailValido && contValida
    }

    fun iniciarSesion() {
        if (!camposValidos()) return

        viewModelScope.launch {
            _estado.update { it.copy(cargando = true) }
            delay(1500)

            //login exitoso
            dataStore.guardarSesionActiva(true)
            _estado.update {
                it.copy(
                    cargando = false,
                    ingresoExitoso = true,
                    sesionActiva = true
                )
            }

        }

    }

    fun cerrarSesion(){
        viewModelScope.launch {
            dataStore.cerrarSesion()
            _estado.update {it.copy(sesionActiva = false, ingresoExitoso = false) }
        }
    }

}