@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelup.core.UserSession
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.UsuariosViewModel
import kotlinx.coroutines.launch

// ----------------------------------------------------------
//              PANTALLA PRINCIPAL (ADMIN)
// ----------------------------------------------------------
@Composable
fun AddUsuarioScreen(
    navController: NavController,
    usuariosViewModel: UsuariosViewModel
) {

    // 🔐 Protección admin
    LaunchedEffect(Unit) {
        if (UserSession.rol != "admin") {
            navController.navigate("PantallaPrincipal") {
                popUpTo("PantallaPrincipal") { inclusive = true }
            }
        }
    }

    DrawerGlobal(navController = navController) {
        AddUsuarioContent(
            usuariosViewModel = usuariosViewModel,
            onSaved = { navController.popBackStack() },
            onCancel = { navController.popBackStack() }
        )
    }
}

// ----------------------------------------------------------
//                  CONTENIDO DE LA VISTA
// ----------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddUsuarioContent(
    usuariosViewModel: UsuariosViewModel,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
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
        uri?.let {
            val bytes = ctx.contentResolver.openInputStream(it)?.use { stream -> stream.readBytes() }
            fotoBytes = bytes
            fotoBitmap = bytes?.let { b -> BitmapFactory.decodeByteArray(b, 0, b.size) }
        }
    }

    Scaffold(
        containerColor = JetBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agregar Usuario", color = GamerGreen) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = JetBlack)
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // FOTO PERFIL
            Box(
                modifier = Modifier
                    .size(100.dp)
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
                } else Text("Foto", color = PureWhite)
            }

            Spacer(Modifier.height(16.dp))

            // CAMPOS
            CampoTexto("Nombres", nombres) { nombres = it }
            CampoTexto("Apellidos", apellidos) { apellidos = it }
            CampoTexto("Correo", correo) { correo = it }
            CampoTextoPassword("Contraseña", contrasena) { contrasena = it }

            // ROL
            Spacer(Modifier.height(16.dp))
            RolSelector(rol) { rol = it }

            Spacer(Modifier.height(25.dp))

            // BOTONES
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onCancel) {
                    Text("Cancelar", color = PureWhite)
                }
                Spacer(Modifier.width(12.dp))
                Button(
                    onClick = {
                        if (nombres.isBlank() || correo.isBlank() || contrasena.isBlank()) {
                            Toast.makeText(ctx, "Completa todos los campos", Toast.LENGTH_SHORT).show()
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

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GamerGreen)
                ) {
                    Text("Guardar", color = Color.Black)
                }
            }
        }
    }
}

// ----------------------------------------------------------
//                   CAMPOS REUTILIZABLES
// ----------------------------------------------------------
@Composable
fun CampoTexto(
    titulo: String,
    valor: String,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(titulo, color = PureWhite) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GamerGreen,
            unfocusedBorderColor = Color.DarkGray,
            focusedTextColor = PureWhite,
            unfocusedTextColor = PureWhite,
            cursorColor = GamerGreen
        )
    )
}

@Composable
fun CampoTextoPassword(
    titulo: String,
    valor: String,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(titulo, color = PureWhite) },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GamerGreen,
            unfocusedBorderColor = Color.DarkGray,
            focusedTextColor = PureWhite,
            unfocusedTextColor = PureWhite,
            cursorColor = GamerGreen
        )
    )
}

// ----------------------------------------------------------
//                   SELECTOR DE ROL
// ----------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RolSelector(rolActual: String, onChange: (String) -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {

        OutlinedTextField(
            value = rolActual,
            onValueChange = {},
            readOnly = true,
            label = { Text("Rol", color = PureWhite) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GamerGreen,
                unfocusedBorderColor = Color.DarkGray,
                focusedTextColor = PureWhite,
                unfocusedTextColor = PureWhite,
                cursorColor = GamerGreen
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf("user", "admin").forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onChange(it)
                        expanded = false
                    }
                )
            }
        }
    }
}
