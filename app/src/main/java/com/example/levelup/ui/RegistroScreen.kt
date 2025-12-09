@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.UsuariosViewModel
import kotlinx.coroutines.launch

@Composable
fun RegistroScreen(
    navController: NavController,
    vm: UsuariosViewModel
) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var contrasena2 by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }

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

            RegistroCampo("Nombres", nombres) { nombres = it }
            RegistroCampo("Apellidos", apellidos) { apellidos = it }
            RegistroCampo("Correo", correo) { correo = it }
            RegistroCampoPassword("Contraseña", contrasena) { contrasena = it }
            RegistroCampoPassword("Repetir contraseña", contrasena2) { contrasena2 = it }
            RegistroCampo("Teléfono", telefono, KeyboardType.Number) { telefono = it }
            RegistroCampo("Fecha nacimiento (YYYY-MM-DD)", fechaNacimiento) { fechaNacimiento = it }

            Spacer(Modifier.height(26.dp))

            Button(
                onClick = {
                    scope.launch {

                        val emailRegex = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.[a-zA-Z]+".toRegex()

                        val error = when {
                            nombres.isBlank() -> "Ingresa nombre"
                            apellidos.isBlank() -> "Ingresa apellido"
                            correo.isBlank() -> "Ingresa correo"
                            !correo.matches(emailRegex) -> "Correo inválido"
                            contrasena.length < 6 -> "Contraseña mínima 6 caracteres"
                            contrasena != contrasena2 -> "Las contraseñas no coinciden"
                            telefono.isBlank() -> "Ingresa un teléfono válido"
                            fechaNacimiento.isBlank() -> "Ingresa fecha de nacimiento"
                            else -> null
                        }

                        if (error != null) {
                            snackbarHostState.showSnackbar(error)
                            return@launch
                        }

                        vm.registrar(
                            nombres.trim(),
                            apellidos.trim(),
                            correo.trim(),
                            contrasena.trim(),
                            telefono.toLong(),
                            fechaNacimiento.trim()
                        ) {
                            // On success
                            scope.launch {
                                snackbarHostState.showSnackbar("Usuario registrado exitosamente")
                                kotlinx.coroutines.delay(800)
                                navController.navigate("login") {
                                    popUpTo("registro") { inclusive = true }
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

// ------------------------ CAMPOS ------------------------

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
