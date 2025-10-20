// EditUsuarioScreen.kt
package com.example.levelup.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.viewmodel.UsuariosViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUsuarioScreen(
    usuariosViewModel: UsuariosViewModel,
    userId: Int,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val usuarioFlow = remember(userId) { usuariosViewModel.usuarioPorId(userId) }
    val usuario by usuarioFlow.collectAsState(initial = null)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Campos locales
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") } // opcional mostrar/editar
    var telefono by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("user") }
    var duoc by remember { mutableStateOf(false) }
    var descApl by remember { mutableStateOf(false) }
    var fotoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var fotoBytes by remember { mutableStateOf<ByteArray?>(null) }

    // Cuando llega el usuario desde DB, rellenamos campos
    LaunchedEffect(usuario) {
        usuario?.let {
            nombres = it.nombres
            apellidos = it.apellidos
            correo = it.correo
            contrasena = it.contrasena ?: ""
            telefono = it.telefono?.toString() ?: ""
            fechaNacimiento = it.fechaNacimiento ?: ""
            rol = it.rol ?: "user"
            duoc = it.duoc ?: false
            descApl = it.descApl ?: false
            fotoBytes = it.fotoPerfil
            fotoBitmap = it.fotoPerfil?.let { bytes -> BitmapFactory.decodeByteArray(bytes, 0, bytes.size) }
        }
    }

    fun validar(): Pair<Boolean, String> {
        if (nombres.isBlank()) return false to "Ingresa nombres"
        if (correo.isBlank()) return false to "Ingresa correo"
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!correo.matches(emailRegex.toRegex())) return false to "Correo inválido"
        if (contrasena.isNotBlank() && contrasena.length < 6) return false to "Contraseña mínima 6 caracteres"
        if (telefono.isNotBlank() && telefono.toLongOrNull() == null) return false to "Teléfono inválido"
        return true to ""
    }

    // Muestra loader si todavía no llegó el usuario
    if (usuario == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Lógica de guardado (reuse)
    fun doSave() {
        val (ok, msg) = validar()
        if (!ok) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            return
        }

        val telefonoLong = telefono.toLongOrNull()
        val actualizado = UsuarioEntity(
            id = userId,
            nombres = nombres,
            apellidos = apellidos,
            correo = correo,
            contrasena = if (contrasena.isBlank()) usuario?.contrasena ?: "" else contrasena,
            telefono = telefonoLong,
            fechaNacimiento = fechaNacimiento,
            fotoPerfil = fotoBytes ?: usuario?.fotoPerfil,
            duoc = duoc,
            descApl = descApl,
            rol = rol
        )

        scope.launch {
            usuariosViewModel.actualizarUsuario(actualizado)
            Toast.makeText(context, "Usuario actualizado", Toast.LENGTH_SHORT).show()
            onSaved()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Editar usuario") }) },
        /*bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = { doSave() }, modifier = Modifier.weight(1f)) {
                    Text("Guardar cambios")
                }
                OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                    Text("Cancelar")
                }
            }
        },*/
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen (si existe)
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (fotoBitmap != null) {
                        Image(
                            bitmap = fotoBitmap!!.asImageBitmap(),
                            contentDescription = "Foto perfil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_camera),
                            contentDescription = "Sin foto",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = nombres,
                    onValueChange = { nombres = it },
                    label = { Text("Nombres") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    label = { Text("Apellidos") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña (dejar vacío = sin cambios)") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Teléfono (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = fechaNacimiento,
                    onValueChange = { fechaNacimiento = it },
                    label = { Text("Fecha de nacimiento (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // rol + checks
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Rol: ")
                    Spacer(modifier = Modifier.width(8.dp))
                    RoleDropdown(selected = rol, onSelected = { rol = it })
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = duoc, onCheckedChange = { duoc = it })
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Duoc")
                    Spacer(modifier = Modifier.width(16.dp))
                    Checkbox(checked = descApl, onCheckedChange = { descApl = it })
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Desc Apl")
                }

                // espacio extra para que no quede pegado al bottomBar
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    )
}

@Composable
private fun RoleDropdown(selected: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("user", "admin")
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selected)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { opt ->
                DropdownMenuItem(text = { Text(opt) }, onClick = { onSelected(opt); expanded = false })
            }
        }
    }
}
