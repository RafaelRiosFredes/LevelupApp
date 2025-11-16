package com.example.levelup.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.viewmodel.UsuariosViewModel
import kotlinx.coroutines.launch
import com.example.levelup.ui.components.DrawerGlobal   // ← IMPORTANTE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUsuarioScreen(
    navController: NavController,
    usuariosViewModel: UsuariosViewModel,
    currentUserRol: String,
    userId: Int,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {

    // --- Seguridad admin ---
    LaunchedEffect(Unit) {
        if (currentUserRol != "admin") {
            onCancel()
        }
    }

    // ============================
    //     ENVOLVER EN DRAWER
    // ============================
    DrawerGlobal() {
        EditUsuarioContent(
            usuariosViewModel = usuariosViewModel,
            userId = userId,
            onSaved = onSaved,
            onCancel = onCancel
        )
    }
}


// ======================================================
//     CONTENIDO REAL DE LA PANTALLA (NO SE ROMPE NADA)
// ======================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditUsuarioContent(
    usuariosViewModel: UsuariosViewModel,
    userId: Int,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {

    val usuarioFlow = remember(userId) {
        usuariosViewModel.usuarioPorId(userId)
    }
    val usuario by usuarioFlow.collectAsState(initial = null)

    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("user") }

    var fotoBytes by remember { mutableStateOf<ByteArray?>(null) }
    var fotoBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    LaunchedEffect(usuario) {
        usuario?.let {
            nombres = it.nombres
            apellidos = it.apellidos
            correo = it.correo
            contrasena = it.contrasena ?: ""
            rol = it.rol
            fotoBytes = it.fotoPerfil
            fotoBitmap = it.fotoPerfil?.let { b ->
                BitmapFactory.decodeByteArray(b, 0, b.size)
            }
        }
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Editar Usuario") }) }
    ) { padding ->

        if (usuario == null) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Box(
                Modifier.size(80.dp).clip(CircleShape),
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

            Spacer(Modifier.height(16.dp))

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
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Rol dropdown
            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                OutlinedTextField(
                    value = rol,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rol") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
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

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancel) { Text("Cancelar") }
                Spacer(Modifier.width(12.dp))

                Button(onClick = {
                    val actualizado = UsuarioEntity(
                        id = userId,
                        nombres = nombres,
                        apellidos = apellidos,
                        correo = correo,
                        contrasena = contrasena,
                        telefono = usuario?.telefono,
                        fechaNacimiento = usuario?.fechaNacimiento,
                        fotoPerfil = fotoBytes,
                        duoc = usuario?.duoc ?: false,
                        descApl = usuario?.descApl ?: false,
                        rol = rol
                    )

                    scope.launch {
                        usuariosViewModel.actualizarUsuario(actualizado)
                        onSaved()
                    }
                }) {
                    Text("Guardar")
                }
            }
        }
    }
}
