@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerBlue
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.UsuariosViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@Composable
fun RegistroScreen(
    navController: NavController,
    vm: UsuariosViewModel,
    onSaved: () -> Unit = {}
) {

    DrawerGlobal() {   // ------------------ 🔥 INICIO DEL DRAWER GLOBAL -------------------

        val contexto = LocalContext.current
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        var nombres by remember { mutableStateOf("") }
        var apellidos by remember { mutableStateOf("") }
        var correo by remember { mutableStateOf("") }
        var contrasena by remember { mutableStateOf("") }
        var contrasena2 by remember { mutableStateOf("") }
        var telefono by remember { mutableStateOf("") }
        var fechaNacimiento by remember { mutableStateOf("") }

        // FOTO PERFIL
        var fotoBitmap by remember { mutableStateOf<Bitmap?>(null) }
        var fotoBytes by remember { mutableStateOf<ByteArray?>(null) }

        // Permiso cámara
        val permisoCamara = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) {}

        // Cámara
        val launcherCamara = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val data = result.data?.extras?.get("data") as? Bitmap
            data?.let {
                fotoBitmap = it
                fotoBytes = bitmapToByteArray(it)
            }
        }

        // Galería
        val launcherGaleria = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(contexto.contentResolver, it)
                fotoBitmap = bitmap
                fotoBytes = bitmapToByteArray(bitmap)
            }
        }

        Scaffold(
            containerColor = JetBlack,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Crear cuenta", color = GamerGreen) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = JetBlack)
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // -----------------------
                // FOTO PERFIL
                // -----------------------
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(4.dp, GamerGreen, CircleShape)
                        .background(Color(0xFF1A1A1A)),
                    contentAlignment = Alignment.Center
                ) {
                    if (fotoBitmap != null) {
                        Image(
                            bitmap = fotoBitmap!!.asImageBitmap(),
                            contentDescription = "Foto perfil",
                            modifier = Modifier.size(140.dp).clip(CircleShape)
                        )
                    } else {
                        Text("Sin foto", color = PureWhite)
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                    IconButton(
                        onClick = {
                            permisoCamara.launch(Manifest.permission.CAMERA)
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            launcherCamara.launch(intent)
                        },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Camara", tint = GamerGreen)
                    }

                    IconButton(
                        onClick = { launcherGaleria.launch("image/*") },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.Image, contentDescription = "Galeria", tint = GamerBlue)
                    }
                }

                Spacer(Modifier.height(25.dp))

                // CAMPOS
                RegistroCampo("Nombres", nombres) { nombres = it }
                RegistroCampo("Apellidos", apellidos) { apellidos = it }
                RegistroCampo("Correo", correo) { correo = it }
                RegistroCampoPassword("Contraseña", contrasena) { contrasena = it }
                RegistroCampoPassword("Repetir contraseña", contrasena2) { contrasena2 = it }
                RegistroCampo("Teléfono (opcional)", telefono, KeyboardType.Number) { telefono = it }
                RegistroCampo("Fecha nacimiento (YYYY-MM-DD)", fechaNacimiento) { fechaNacimiento = it }

                Spacer(Modifier.height(26.dp))

                // -----------------------
                // BOTÓN REGISTRAR
                // -----------------------
                Button(
                    onClick = {
                        scope.launch {

                            val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+".toRegex()

                            val error = when {
                                nombres.isBlank() -> "Ingresa nombre"
                                apellidos.isBlank() -> "Ingresa apellido"
                                correo.isBlank() -> "Ingresa correo"
                                !correo.matches(emailRegex) -> "Correo inválido"
                                contrasena.length < 6 -> "Contraseña mínima 6 caracteres"
                                contrasena != contrasena2 -> "Las contraseñas no coinciden"
                                else -> null
                            }

                            if (error != null) {
                                snackbarHostState.showSnackbar(error)
                                return@launch
                            }

                            val telLong = telefono.toLongOrNull()

                            val usuario = UsuarioEntity(
                                id = 0,
                                nombres = nombres.trim(),
                                apellidos = apellidos.trim(),
                                correo = correo.trim(),
                                contrasena = contrasena.trim(),
                                telefono = telLong,
                                fechaNacimiento = fechaNacimiento.ifBlank { null },
                                fotoPerfil = fotoBytes,
                                duoc = false,
                                descApl = false,
                                rol = "user",
                                backendId = null
                            )

                            vm.insertarUsuario(usuario)

                            snackbarHostState.showSnackbar("Usuario registrado exitosamente")

                            onSaved()

                            navController.navigate("login") {
                                popUpTo("registro") { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GamerGreen,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Registrarme", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }   // ------------------ 🔥 CIERRE DEL DRAWER GLOBAL -------------------
}


// -------------------------------------
// CAMPOS
// -------------------------------------
@Composable
fun RegistroCampo(
    titulo: String,
    valor: String,
    tipo: KeyboardType = KeyboardType.Text,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(titulo, color = PureWhite) },
        textStyle = TextStyle(color = PureWhite),
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = tipo),
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
fun RegistroCampoPassword(
    titulo: String,
    valor: String,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(titulo, color = PureWhite) },
        textStyle = TextStyle(color = PureWhite),
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GamerGreen,
            unfocusedBorderColor = Color.DarkGray,
            focusedTextColor = PureWhite,
            unfocusedTextColor = PureWhite,
            cursorColor = GamerGreen
        )
    )
}

fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
