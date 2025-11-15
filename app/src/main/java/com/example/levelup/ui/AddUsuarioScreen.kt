package com.example.levelup.ui

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.viewmodel.UsuariosViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUsuarioScreen(
    usuariosViewModel: UsuariosViewModel,
    currentUserRol: String,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    if (currentUserRol != "admin") {
        onCancel()
        return
    }

    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("user") }

    var fotoBytes by remember { mutableStateOf<ByteArray?>(null) }
    var fotoBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val bytes = ctx.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            fotoBytes = bytes
            fotoBitmap = bytes?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Agregar usuario") }) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (fotoBitmap != null) {
                    Image(
                        bitmap = fotoBitmap!!.asImageBitmap(),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                } else Text("Foto")
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = nombres, onValueChange = { nombres = it },
                label = { Text("Nombres") }, modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = apellidos, onValueChange = { apellidos = it },
                label = { Text("Apellidos") }, modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = correo, onValueChange = { correo = it },
                label = { Text("Correo") }, modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = contrasena, onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // rol
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                OutlinedTextField(
                    value = rol,
                    onValueChange = {},
                    label = { Text("Rol") },
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf("user", "admin").forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                rol = it
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onCancel) { Text("Cancelar") }
                Spacer(Modifier.width(12.dp))
                Button(onClick = {

                    if (nombres.isBlank() || correo.isBlank() || contrasena.isBlank()) {
                        Toast.makeText(ctx, "Completa todos los campos", Toast.LENGTH_SHORT)
                            .show()
                        return@Button
                    }

                    val nuevo = UsuarioEntity(
                        id = 0,
                        nombres = nombres,
                        apellidos = apellidos,
                        correo = correo,
                        contrasena = contrasena,
                        telefono = null,
                        fechaNacimiento = null,
                        fotoPerfil = fotoBytes,
                        duoc = false,
                        descApl = false,
                        rol = rol
                    )

                    scope.launch {
                        usuariosViewModel.insertarUsuario(nuevo)
                        onSaved()
                    }

                }) { Text("Guardar") }
            }
        }
    }
}
