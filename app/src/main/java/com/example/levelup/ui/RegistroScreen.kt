package com.example.levelup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.viewmodel.UsuariosViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color
import com.example.levelup.ui.theme.GamerBlue
import com.example.levelup.ui.theme.GamerGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    navController: NavController,
    vm: UsuariosViewModel,
    onSaved: () -> Unit = {}        // callback opcional
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // ---------- estados locales del formulario ----------
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var contrasena2 by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Registro de usuario", color = GamerGreen) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Black
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ====== NOMBRES ======
            OutlinedTextField(
                value = nombres,
                onValueChange = { nombres = it },
                label = { Text("Nombres", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GamerBlue,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = GamerBlue
                )
            )

            // ====== APELLIDOS ======
            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellidos", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GamerBlue,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = GamerBlue
                )
            )

            // ====== CORREO ======
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GamerBlue,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = GamerBlue
                )
            )

            // ====== CONTRASEÑA ======
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña", color = Color.White) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GamerBlue,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = GamerBlue
                )
            )

            // ====== REPETIR CONTRASEÑA ======
            OutlinedTextField(
                value = contrasena2,
                onValueChange = { contrasena2 = it },
                label = { Text("Repite la contraseña", color = Color.White) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GamerBlue,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = GamerBlue
                )
            )

            // ====== TELÉFONO (OPCIONAL) ======
            OutlinedTextField(
                value = telefono,
                onValueChange = { tel -> telefono = tel.filter { it.isDigit() } },
                label = { Text("Teléfono (opcional)", color = Color.White) },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GamerBlue,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = GamerBlue
                )
            )

            // ====== FECHA NACIMIENTO (OPCIONAL TEXTO LIBRE) ======
            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = { fechaNacimiento = it },
                label = { Text("Fecha nacimiento (YYYY-MM-DD)", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GamerBlue,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = GamerBlue
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ====== BOTÓN REGISTRAR ======
            Button(
                onClick = {
                    scope.launch {
                        // ---- Validaciones simples ----
                        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+".toRegex()

                        val error = when {
                            nombres.isBlank() -> "Ingresa tus nombres"
                            apellidos.isBlank() -> "Ingresa tus apellidos"
                            correo.isBlank() -> "Ingresa tu correo"
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

                        val nuevo = UsuarioEntity(
                            id = 0,
                            nombres = nombres.trim(),
                            apellidos = apellidos.trim(),
                            correo = correo.trim(),
                            contrasena = contrasena,
                            telefono = telLong,
                            fechaNacimiento = if (fechaNacimiento.isBlank()) null else fechaNacimiento.trim(),
                            fotoPerfil = null,
                            duoc = false,
                            descApl = false,
                            rol = "user",          // siempre user, nunca admin
                            backendId = null
                        )

                        vm.insertarUsuario(nuevo)
                        snackbarHostState.showSnackbar("Registro exitoso. Ahora puedes iniciar sesión.")

                        onSaved()

                        navController.navigate("login") {
                            popUpTo("registro") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GamerGreen,
                    contentColor = Color.Black
                )
            ) {
                Text("Registrar", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
        }
    }
}
