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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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

    DrawerGlobal(navController = navController) {   // ← 🔥 DRAWER GLOBAL CORRECTO

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

        val scrollState = rememberScrollState()

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
                    .padding(22.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Spacer(Modifier.height(25.dp))

                // CAMPOS
                RegistroCampo("Nombres", nombres) { nombres = it }
                RegistroCampo("Apellidos", apellidos) { apellidos = it }
                RegistroCampo("Correo", correo) { correo = it }
                RegistroCampoPassword("Contraseña", contrasena) { contrasena = it }
                RegistroCampoPassword("Repetir contraseña", contrasena2) { contrasena2 = it }
                RegistroCampo("Teléfono", telefono, KeyboardType.Number) { telefono = it }
                RegistroCampo("Fecha nacimiento (YYYY-MM-DD)", fechaNacimiento) { fechaNacimiento = it }

                Spacer(Modifier.height(26.dp))


                // BOTÓN REGISTRAR
                Button(
                    onClick = {
                        scope.launch {
                            // --- VALIDACIONES EXISTENTES ---
                            val emailRegex = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.[a-zA-Z]+".toRegex()

                            // Corrección: El backend exige teléfono positivo, validamos eso también
                            val error = when {
                                nombres.isBlank() -> "Ingresa nombre"
                                apellidos.isBlank() -> "Ingresa apellido"
                                correo.isBlank() -> "Ingresa correo"
                                !correo.matches(emailRegex) -> "Correo inválido"
                                contrasena.length < 6 -> "Contraseña mínima 6 caracteres"
                                contrasena != contrasena2 -> "Las contraseñas no coinciden"
                                telefono.isBlank() -> "Ingresa un teléfono" // Backend @NotNull
                                else -> null
                            }

                            if (error != null) {
                                snackbarHostState.showSnackbar(error)
                                return@launch
                            }

                            // Convertir teléfono (Backend pide Long positivo)
                            val telLong = telefono.toLongOrNull() ?: 0L

                            // --- CAMBIO PRINCIPAL AQUÍ ---
                            // En lugar de crear la Entity manual, llamamos al ViewModel
                            vm.registrarUsuarioPublico(
                                nombres = nombres.trim(),
                                apellidos = apellidos.trim(),
                                correo = correo.trim(),
                                contrasena = contrasena.trim(),
                                telefono = telLong,
                                fechaNacimiento = fechaNacimiento // Debe ser formato "YYYY-MM-DD"
                            ) { exito ->
                                scope.launch {
                                    if (exito) {
                                        snackbarHostState.showSnackbar("Usuario registrado exitosamente")
                                        onSaved() // Callback opcional
                                        // Esperar un poco para que se vea el mensaje
                                        kotlinx.coroutines.delay(1000)
                                        navController.navigate("login") {
                                            popUpTo("registro") { inclusive = true }
                                        }
                                    } else {
                                        snackbarHostState.showSnackbar("Error al registrar: Revisa los datos o conexión")
                                    }
                                }
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
    }
}


// ---------------------------------------------------------------------------------
// CAMPOS
// ---------------------------------------------------------------------------------

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
