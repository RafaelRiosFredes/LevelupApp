package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.model.repository.UsuariosRepository
import com.example.levelup.remote.LoginRequestDTO
import com.example.levelup.remote.LoginResponseDTO
import com.example.levelup.remote.RetrofitBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuariosViewModel(
    application: Application,
    private val repository: UsuariosRepository
) : AndroidViewModel(application) {

    private val _loginMensaje = MutableStateFlow<String?>(null)
    val loginMensaje: StateFlow<String?> = _loginMensaje

    private val _usuarioActual = MutableStateFlow<UsuarioEntity?>(null)
    val usuarioActual: StateFlow<UsuarioEntity?> = _usuarioActual

    fun limpiarLoginMensaje() {
        _loginMensaje.value = null
    }

    fun obtenerUsuarios() = repository.obtenerUsuarios()
    fun usuarioPorId(id: Int) = repository.usuarioPorId(id)

    fun insertarUsuario(usuario: UsuarioEntity) = viewModelScope.launch {
        repository.insertar(usuario)
    }

    fun actualizarUsuario(usuario: UsuarioEntity) = viewModelScope.launch {
        repository.actualizar(usuario)
    }

    fun eliminarUsuario(usuario: UsuarioEntity) = viewModelScope.launch {
        repository.eliminar(usuario)
    }

    //Backend crear

    fun registrarUsuarioPublico(
        nombres: String,
        apellidos: String,
        correo: String,
        contrasena: String,
        telefono: Long,
        fechaNacimiento: String,
        onResult: (Boolean) -> Unit // Callback para saber si funcionó
    ) = viewModelScope.launch {

        // Llamamos a la función del repositorio que conecta con AuthApiService
        val resultado = repository.registrarUsuarioBackendYLocal(
            nombres = nombres,
            apellidos = apellidos,
            correo = correo,
            contrasena = contrasena,
            telefono = telefono,
            fechaNacimiento = fechaNacimiento,
            fotoPerfilBase64 = null, // Opcional por ahora
            duoc = correo.endsWith("@duocuc.cl"),
            descApl = false,
            rol = "user"
        )

        // Notificamos a la vista si fue exitoso o no
        if (resultado.isSuccess) {
            onResult(true)
        } else {
            resultado.exceptionOrNull()?.printStackTrace()
            onResult(false)
        }
    }
    // BACKEND: actualizar
    suspend fun actualizarUsuarioBackend(usuario: UsuarioEntity) =
        repository.actualizarUsuarioBackend(usuario)

    // BACKEND: eliminar
    suspend fun eliminarUsuarioBackend(usuario: UsuarioEntity) =
        repository.eliminarUsuarioBackend(usuario)

    fun sincronizarUsuarios() = viewModelScope.launch {
        repository.sincronizarUsuarios()
    }

    // LOGIN
    suspend fun login(correo: String, contrasena: String): UsuarioEntity? {
        val usuario = repository.login(correo, contrasena)

        _loginMensaje.value =
            if (usuario != null) "Ingreso exitoso"
            else "Credenciales incorrectas"

        _usuarioActual.value = usuario

        return usuario
    }

    // LOGOUT
    fun logout() {
        _usuarioActual.value = null
    }

    // ADMIN POR DEFECTO
    fun crearAdminPorDefecto() = viewModelScope.launch {
        val admin = repository.login("admin@levelup.com", "admin123")
        if (admin == null) {
            repository.insertar(
                UsuarioEntity(
                    nombres = "Admin",
                    apellidos = "LevelUp",
                    correo = "admin@levelup.com",
                    contrasena = "admin123",
                    rol = "admin"
                )
            )
        }
    }

    suspend fun loginBackend(email: String, pass: String): LoginResponseDTO? {
        return try {
            // Llamada a la API usando Retrofit
            // Asegúrate de que tu AuthApiService tenga un método login que devuelva LoginResponseDTO
            val response = RetrofitBuilder.authApi.login(LoginRequestDTO(email, pass))
            response // Retornamos el DTO que trae el token
        } catch (e: Exception) {
            e.printStackTrace()
            null // Si falla, retornamos null
        }
    }
}
