@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.levelup.core.UserSession
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.viewmodel.UsuariosViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    usuariosViewModel: UsuariosViewModel
) {

    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Iniciar sesión",
                        color = GamerGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = Color.Black
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // -------------------------------
            // CAMPO CORREO
            // -------------------------------
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo", color = Color.White) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GamerGreen,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    cursorColor = GamerGreen
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // -------------------------------
            // CAMPO CONTRASEÑA
            // -------------------------------
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña", color = Color.White) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GamerGreen,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    cursorColor = GamerGreen
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(26.dp))

            // -------------------------------
            // BOTÓN INGRESAR
            // -------------------------------
            Button(
                onClick = {
                    scope.launch {

                        // ADMIN FIJO
                        if (correo.trim() == "admin@levelup.com" &&
                            contrasena.trim() == "admin123"
                        ) {

                            UserSession.login(
                                id = -1,
                                correo = "admin@levelup.com",
                                rol = "admin",
                                nombre = "Administrador",
                                apellidos = "LevelUp"
                            )

                            navController.navigate("PantallaPrincipal") {
                                popUpTo("login") { inclusive = true }
                            }
                            return@launch
                        }

                        // LOGIN DESDE ROOM
                        val usuario = usuariosViewModel.login(
                            correo.trim(),
                            contrasena.trim()
                        )

                        if (usuario != null) {

                            UserSession.login(
                                id = usuario.id,
                                correo = usuario.correo,
                                rol = usuario.rol,
                                nombre = usuario.nombres,
                                apellidos = usuario.apellidos
                            )

                            navController.navigate("PantallaPrincipal") {
                                popUpTo("login") { inclusive = true }
                            }

                        } else {
                            error = "Credenciales incorrectas"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GamerGreen)
            ) {
                Text("Ingresar", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            if (error.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = error,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(20.dp))

            // --------------------------------
            // REGISTRO
            // --------------------------------
            TextButton(
                onClick = { navController.navigate("registro") }
            ) {
                Text(
                    "¿No tienes cuenta? Regístrate aquí",
                    color = GamerGreen,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
