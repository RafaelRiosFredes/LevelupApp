package com.example.levelup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelup.theme.GamerGreen
import com.example.levelup.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    vm: LoginViewModel
) {
    val form by vm.form.collectAsState()

    Surface(
        color = Color.Black,
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Iniciar Sesión", color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = GamerGreen,
                        titleContentColor = Color.White
                    )
                )
            },
            containerColor = Color.Black
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                form.mensaje?.let {
                    Text(
                        it,
                        color = if (form.autenticado) GamerGreen else Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(16.dp))
                }

                OutlinedTextField(
                    value = form.correo,
                    onValueChange = vm::onChangeCorreo,
                    label = { Text("Correo electrónico", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GamerGreen,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = GamerGreen
                    )
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = form.contrasena,
                    onValueChange = vm::onChangeContrasena,
                    label = { Text("Contraseña", color = Color.White) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GamerGreen,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = GamerGreen
                    )
                )

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        vm.iniciarSesion {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GamerGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ingresar", color = Color.White)
                }

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { navController.navigate("registro") },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GamerGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Crear cuenta")
                }
            }
        }
    }
}
