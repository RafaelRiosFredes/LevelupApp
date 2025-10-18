package com.example.levelup.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.data.RegistroUsuarioEntity
import com.example.levelup.model.repository.RegistroUsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

class RegistroUsuarioViewModel(private val repo: RegistroUsuarioRepository) : ViewModel() {

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

    fun limpiarFormulario() = run { _form.value = RegistroFormState() }


    fun registrarUsuario(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val f = _form.value

            // valida que todos los campos esten completos
            if (f.nombres.isBlank() || f.apellidos.isBlank() ||
                f.correo.isBlank() || f.contrasena.length < 6 || f.fechaNacimiento.isBlank()
            ) {
                _form.update { it.copy(mensaje = "Completa todos los campos!") }
                return@launch
            }

            // 🔹 Validar que ambas contraseñas sean iguales
            if (f.contrasena != f.contrasenaConfirmacion) {
                _form.update { it.copy(mensaje = "Las contraseñas no coinciden") }
                return@launch
            }

            // valida que no haya un usuario con el mismo correo
            val existente = repo.obtenerPorCorreo(f.correo)
            if (existente != null) {
                _form.update { it.copy(mensaje = "Ya existe un usuario con este correo") }
                return@launch
            }

            // valida la fecha de nacimiento en el formato correcto
            val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
            val fechaNac = try {
                LocalDate.parse(f.fechaNacimiento.trim(), formatter)
            } catch (e: Exception) {
                _form.update { it.copy(mensaje = "Formato de fecha inválido usa DD/MM/YYYY") }
                return@launch
            }

            // verifica que el usuario sea +18
            val edad = Period.between(fechaNac, LocalDate.now()).years
            if (edad < 18) {
                _form.update { it.copy(mensaje = "Solo mayores de 18 años se pueden registrarse") }
                return@launch
            }

            // valida que el correo sea del duoc para el beneficio
            val duoc = f.correo.contains("@duocuc.cl", ignoreCase = true)
            if (duoc)
                _form.update { it.copy(mensaje = "Beneficio Duoc aplicado!!") }


            val telefonoInt = f.telefono.toIntOrNull()

            val fotoBytes = f.fotoPerfil?.let {
                val output = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.PNG, 100, output)
                output.toByteArray()
            }

            val usuario = RegistroUsuarioEntity(
                nombres = f.nombres,
                apellidos = f.apellidos,
                correo = f.correo,
                contrasena = f.contrasena,
                telefono = telefonoInt,
                fechaNacimiento = f.fechaNacimiento,
                fotoPerfil = fotoBytes,
                duoc = duoc,
                descApl = duoc
            )

            repo.insertar(usuario)
            _form.update {
                it.copy(
                    mensaje = if (duoc)
                        "Registro Completado, Descuento Duoc aplicado!!"
                    else
                        "Registro exitoso"
                )
            }
            limpiarFormulario()
            onSuccess()
        }
    }
}
