// AddUsuarioScreen.kt
package com.example.levelup.ui

import android.R.attr.padding
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.net.toFile
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.viewmodel.UsuariosViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUsuarioScreen(
    usuariosViewModel: UsuariosViewModel,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Campos del formulario
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("user") }
    var duoc by remember { mutableStateOf(false) }
    var descApl by remember { mutableStateOf(false) }

    // Foto (ByteArray para la entidad)
    var fotoBytes by remember { mutableStateOf<ByteArray?>(null) }
    var fotoBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Launcher para seleccionar imagen desde galería
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                try {
                    context.contentResolver.openInputStream(it).use { input ->
                        val bytes = input?.readBytes()
                        if (bytes != null) {
                            fotoBytes = bytes
                            fotoBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error al leer imagen", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    // Validación mínima
    fun validar(): Pair<Boolean, String> {
        if (nombres.isBlank()) return false to "Ingresa nombres"
        if (correo.isBlank()) return false to "Ingresa correo"
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!correo.matches(emailRegex.toRegex())) return false to "Correo inválido"
        if (contrasena.length < 6) return false to "Contraseña mínima 6 caracteres"
        // teléfono opcional pero si hay algo, debe ser numérico
        if (telefono.isNotBlank() && telefono.toLongOrNull() == null) return false to "Teléfono inválido"
        return true to ""
    }
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Agregar usuario") })
        }/*,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        // mover la lógica de validación/guardado a una lambda separada
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Guardar") }

                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) { Text("Cancelar") }
            }
        }*/,
        content = { innerPadding ->
            val bottomInset = innerPadding.calculateBottomPadding()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
                    .padding(16.dp)
                    .padding(bottom = bottomInset + 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Foto (preview)
                Box(modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") },
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
                        // placeholder; usa recurso si quieres
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_camera),
                            contentDescription = "Seleccionar foto",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(value = nombres, onValueChange = { nombres = it }, label = { Text("Nombres") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = apellidos, onValueChange = { apellidos = it }, label = { Text("Apellidos") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = contrasena, onValueChange = { contrasena = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = telefono, onValueChange = { telefono = it.filter { ch -> ch.isDigit() } }, label = { Text("Teléfono (opcional)") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = fechaNacimiento, onValueChange = { fechaNacimiento = it }, label = { Text("Fecha nacimiento (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Rol: ")
                    Spacer(Modifier.width(8.dp))
                    DropdownMenuSample(selected = rol, onSelected = { rol = it })
                }

                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = duoc, onCheckedChange = { duoc = it })
                    Text("Duoc")
                    Spacer(Modifier.width(16.dp))
                    Checkbox(checked = descApl, onCheckedChange = { descApl = it })
                    Text("Desc Apl")
                }

                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = {
                        val (ok, msg) = validar()
                        if (!ok) {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        // Convertir telefono si corresponde
                        val telefonoLong = telefono.toLongOrNull()

                        // Recomiendo hashear la contraseña; aquí la guardamos como texto (mala práctica)
                        val nuevo = UsuarioEntity(
                            id = 0, // Room autogenerate expected
                            nombres = nombres,
                            apellidos = apellidos,
                            correo = correo,
                            contrasena = contrasena, // ver nota de seguridad
                            telefono = telefonoLong,
                            fechaNacimiento = fechaNacimiento,
                            fotoPerfil = fotoBytes,
                            duoc = duoc,
                            descApl = descApl,
                            rol = rol
                        )

                        scope.launch {
                            usuariosViewModel.insertarUsuario(nuevo)
                            Toast.makeText(context, "Usuario creado", Toast.LENGTH_SHORT).show()
                            onSaved()
                        }

                    }) {
                        Text("Guardar")
                    }

                    OutlinedButton(onClick = onCancel) {
                        Text("Cancelar")
                    }
                }
            }
        }
    )
}

// Pequeño dropdown de ejemplo — sustituye por tu propio control si ya lo tienes
@Composable
private fun DropdownMenuSample(selected: String, onSelected: (String) -> Unit) {
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
