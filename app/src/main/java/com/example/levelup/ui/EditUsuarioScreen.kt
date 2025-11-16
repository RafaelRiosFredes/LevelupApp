@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.UsuariosViewModel
import kotlinx.coroutines.launch

// ======================================================
//          PANTALLA PRINCIPAL CON DRAWER
// ======================================================

@Composable
fun EditUsuarioScreen(
    navController: NavController,
    usuariosViewModel: UsuariosViewModel,
    currentUserRol: String,
    userId: Int,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {

    // Seguridad admin
    LaunchedEffect(Unit) {
        if (currentUserRol != "admin") onCancel()
    }

    // Drawer corregido
    DrawerGlobal(navController = navController) {

        EditUsuarioContent(
            usuariosViewModel = usuariosViewModel,
            userId = userId,
            onSaved = onSaved,
            onCancel = onCancel
        )
    }
}

// ======================================================
//               CONTENIDO DE LA PANTALLA
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

    // Cargar datos iniciales
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
        containerColor = JetBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Editar usuario", color = GamerGreen) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = JetBlack
                )
            )
        }
    ) { padding ->

        if (usuario == null) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = GamerGreen)
            }
            return@Scaffold
        }

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // =============== FOTO PERFIL ===============
            Box(
                Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(3.dp, GamerGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (fotoBitmap != null) {
                    Image(
                        bitmap = fotoBitmap!!.asImageBitmap(),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("Foto", color = PureWhite)
                }
            }

            Spacer(Modifier.height(20.dp))

            // =============== CAMPOS ===============

            StyledTextField("Nombres", nombres) { nombres = it }

            StyledTextField("Apellidos", apellidos) { apellidos = it }

            StyledTextField("Correo", correo) { correo = it }

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña", color = PureWhite) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = gamerColors()
            )

            Spacer(Modifier.height(12.dp))

            // =============== DROPDOWN ROL ===============
            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                OutlinedTextField(
                    value = rol,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rol", color = PureWhite) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = gamerColors()
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

            Spacer(Modifier.height(24.dp))

            // =============== BOTONES ===============
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancel) {
                    Text("Cancelar", color = PureWhite)
                }

                Spacer(Modifier.width(12.dp))

                Button(
                    onClick = {
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
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GamerGreen,
                        contentColor = JetBlack
                    )
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}

// ======================================================
//      COMPONENTE REUTILIZABLE PARA CAMPOS GAMER
// ======================================================

@Composable
private fun StyledTextField(
    titulo: String,
    valor: String,
    onValue: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValue,
        label = { Text(titulo, color = PureWhite) },
        modifier = Modifier.fillMaxWidth(),
        textStyle = androidx.compose.ui.text.TextStyle(color = PureWhite),
        colors = gamerColors()
    )

    Spacer(Modifier.height(12.dp))
}

@Composable
private fun gamerColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GamerGreen,
    unfocusedBorderColor = Color.DarkGray,
    cursorColor = GamerGreen,
    focusedTextColor = PureWhite,
    unfocusedTextColor = PureWhite
)
