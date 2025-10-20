package com.example.levelup.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.model.repository.UsuariosRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

data class RegistroFormState(
    val nombres: String = "",
    val apellidos: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val telefono: String = "",
    val fechaNacimiento: String = "",
    val fotoPerfil: Bitmap? = null,
    val mensaje: String? = null,
    val contrasenaConfirmacion: String = ""
)

class UsuariosViewModel(private val repo: UsuariosRepository): ViewModel() {

    private val _form = MutableStateFlow(RegistroFormState())
    val form: StateFlow<RegistroFormState> = _form.asStateFlow()

    fun onChangeNombres(v: String) = _form.update { it.copy(nombres = v) }
    fun onChangeApellidos(v: String) = _form.update { it.copy(apellidos = v) }
    fun onChangeCorreo(v: String) = _form.update { it.copy(correo = v) }
    fun onChangeContrasena(v: String) = _form.update { it.copy(contrasena = v) }
    fun onChangeTelefono(v: String) = _form.update { it.copy(telefono = v) }
    fun onChangeFechaNacimiento(v: String) = _form.update { it.copy(fechaNacimiento = v) }
    fun onChangeFoto(bitmap: Bitmap) = _form.update { it.copy(fotoPerfil = bitmap) }
    fun onChangeContrasenaConfirmacion(v: String) = _form.update { it.copy(contrasenaConfirmacion = v) }

    fun limpiarMensaje() {
        _form.update { it.copy(mensaje = null) }
    }
    val usuarios: StateFlow<List<UsuarioEntity>> =
        repo.todosLosUsuarios().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun limpiarFormulario() {
        _form.value = RegistroFormState()
    }

    fun registrarUsuario(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {

            limpiarMensaje()

            val f = _form.value

            //  Validar campos vacíos
            if (f.nombres.isBlank() || f.apellidos.isBlank() ||
                f.correo.isBlank() || f.contrasena.isBlank() ||
                f.contrasenaConfirmacion.isBlank()
            ) {
                _form.update { it.copy(mensaje = "Completa todos los campos ") }
                return@launch
            }

            //  Validar longitud mínima de contraseña
            if (f.contrasena.length < 6) {
                _form.update { it.copy(mensaje = "La contraseña debe tener al menos 6 caracteres") }
                return@launch
            }

            //  Validar que contraseñas coincidan
            if (f.contrasena != f.contrasenaConfirmacion) {
                _form.update { it.copy(mensaje = "Las contraseñas no coinciden") }
                return@launch
            }

            //  Validar fecha vacía
            if (f.fechaNacimiento.isBlank()) {
                _form.update { it.copy(mensaje = "Completa todos los campos") }
                return@launch
            }

            //  Validar formato de fecha y edad
            val edad: Int = try {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val fechaNac = LocalDate.parse(f.fechaNacimiento.trim(), formatter)
                Period.between(fechaNac, LocalDate.now()).years
            } catch (e: Exception) {
                _form.update { it.copy(mensaje = "Formato de fecha inválido (usa dd/MM/yyyy)") }
                return@launch
            }
            // valida que sea +18
            if (edad < 18) {
                _form.update { it.copy(mensaje = "Solo mayores de 18 años pueden registrarse") }
                return@launch
            }

            //elimina registros anteriores con el mismo correo (sirve siempre que el usuario escribe mal un dato)
            repo.eliminarPorCorreo(f.correo)

            //  Validar correo existente
            val existente = repo.obtenerPorCorreo(f.correo)
            if (existente != null) {
                _form.update { it.copy(mensaje = "Ya existe un usuario con este correo") }
                return@launch
            }

            //  Detectar correo DUOC y si es ADMIN
            val duoc = f.correo.contains("@duocuc.cl", ignoreCase = true)
            val esAdmin = f.correo.contains("admin@", ignoreCase = true) ||
                    f.correo.endsWith("@admin.duocuc.cl", ignoreCase = true)

            //  Convertir teléfono a Long
            val telefonoLong = f.telefono.toLongOrNull()

            //  Convertir imagen a bytes
            val fotoBytes = f.fotoPerfil?.let {
                val output = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.PNG, 100, output)
                output.toByteArray()
            }

            //  Crear entidad de usuario
            val usuario = UsuarioEntity(
                nombres = f.nombres,
                apellidos = f.apellidos,
                correo = f.correo,
                contrasena = f.contrasena,
                telefono = telefonoLong,
                fechaNacimiento = f.fechaNacimiento,
                fotoPerfil = fotoBytes,
                duoc = duoc,
                descApl = duoc,
                rol = if (esAdmin) "admin" else "user"
            )

            //  insertar un usuario
            runCatching {
                repo.insertar(usuario)
            }.onFailure { e ->
                _form.update { it.copy(mensaje = "Error al guardar") }
                return@launch
            }

            // muestra un mensaje según el  tipo de correo
            _form.update {
                it.copy(
                    mensaje = when {
                        duoc -> "Registro Completado 🎓 ¡Descuento Duoc aplicado!"
                        esAdmin -> "Hola admin!"
                        else -> "Registro exitoso ✅"
                    }
                )
            }

            delay(2000)

            limpiarFormulario()
            limpiarMensaje()

            onSuccess()
        }
    }
    suspend fun obtenerUltimoUsuarioRegistrado(): UsuarioEntity? {
        return repo.obtenerPorCorreo(_form.value.correo)
    }
    fun insertarUsuario(u: UsuarioEntity) {
        viewModelScope.launch { repo.insertar(u) }
    }

    fun actualizarUsuario(u: UsuarioEntity) {
        viewModelScope.launch { repo.actualizar(u) }
    }

    fun eliminarUsuario(u: UsuarioEntity) {
        viewModelScope.launch { repo.eliminar(u) }
    }

    fun usuarioPorId(id: Int): Flow<UsuarioEntity?> = repo.obtenerPorId(id)
}
