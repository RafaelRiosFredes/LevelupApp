package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.core.UserSession
import com.example.levelup.model.repository.UsuariosRepository
import com.example.levelup.remote.RegistroUsuarioRemoteDTO
import com.example.levelup.remote.UsuarioDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuariosViewModel(
    application: Application,
    private val repo: UsuariosRepository
) : AndroidViewModel(application) {

    // ---------- LISTA COMPLETA DE USUARIOS ----------
    private val _listaUsuarios = MutableStateFlow<List<UsuarioDTO>>(emptyList())
    val listaUsuarios: StateFlow<List<UsuarioDTO>> = _listaUsuarios

    // ---------- USUARIO INDIVIDUAL ----------
    private val _usuarioActual = MutableStateFlow<UsuarioDTO?>(null)
    val usuarioActual: StateFlow<UsuarioDTO?> = _usuarioActual

    // ---------- ERRORES ----------
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    // ---------- LOGIN ----------
// ---------- LOGIN ----------
    fun login(correo: String, contrasena: String, onSuccess: () -> Unit) =
        viewModelScope.launch {
            try {
                // 1) LOGIN
                val resp = repo.login(correo, contrasena)

                val rawRoles = resp.roles.toString()

                val rolNormalizado =
                    rawRoles
                        .substringAfter("authority=")
                        .substringBefore("}")
                        .replace("ROLE_", "", true)
                        .lowercase()
                        .trim()

                UserSession.login(
                    id = resp.idUsuario,
                    correo = resp.username,
                    rol = rolNormalizado,
                    nombre = "",
                    apellidos = "",
                    jwt = resp.token
                )


                // 4) Obtener datos completos
                val usuario = repo.obtenerUsuario(resp.idUsuario)

                UserSession.nombre = usuario.nombres
                UserSession.apellidos = usuario.apellidos

                _error.value = null
                onSuccess()

            } catch (e: Exception) {
                _error.value = "Correo o contraseña incorrectos"
            }
        }


    // ---------- REGISTRO ----------
    fun registrar(
        nombres: String,
        apellidos: String,
        correo: String,
        contrasena: String,
        telefono: Long,
        fechaNacimiento: String,
        onSuccess: () -> Unit
    ) = viewModelScope.launch {

        try {

            val dto = RegistroUsuarioRemoteDTO(
                nombres = nombres,
                apellidos = apellidos,
                correo = correo,
                contrasena = contrasena,
                telefono = telefono,
                fechaNacimiento = fechaNacimiento
            )

            repo.registrar(dto)

            onSuccess()

        } catch (e: Exception) {
            _error.value = "Error al registrar usuario"
        }
    }


    // ---------- CARGAR LISTA COMPLETA DESDE BACKEND ----------
    fun cargarUsuarios() = viewModelScope.launch {
        try {
            _listaUsuarios.value = repo.obtenerUsuarios()
        } catch (e: Exception) {
            _error.value = "No se pudieron cargar usuarios"
        }
    }

    // ---------- CARGAR UN SOLO USUARIO ----------
    fun cargarUsuario(id: Long) = viewModelScope.launch {
        try {
            _usuarioActual.value = repo.obtenerUsuario(id)
        } catch (e: Exception) {
            _error.value = "No se pudo cargar usuario"
        }
    }


    // ---------- ELIMINAR USUARIO DESDE BACKEND ----------
    fun eliminarUsuarioBackend(id: Long) = viewModelScope.launch {
        try {
            repo.eliminarUsuario(id)
            cargarUsuarios()
        } catch (e: Exception) {
            _error.value = "No se pudo eliminar usuario"
        }
    }

    // ---------- SETEAR ERROR MANUAL DESDE UI ----------
    fun setError(msg: String) {
        _error.value = msg
    }

}
