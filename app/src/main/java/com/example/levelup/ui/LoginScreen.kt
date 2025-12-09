@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.viewmodel.UsuariosViewModel
import com.example.levelup.viewmodel.UsuariosViewModelFactoryApp

@Composable
fun LoginScreen(
    navController: NavController,
    vm: UsuariosViewModel = viewModel(
        factory = UsuariosViewModelFactoryApp(
            LocalContext.current.applicationContext as Application
        )
    )
) {

    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }

    // 🔥 ESCUCHAR ERROR DEL VIEWMODEL
    val errorVM by vm.error.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Iniciar sesión",
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

            // -------------------- CORREO --------------------
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo", color = Color.White) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GamerGreen,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = GamerGreen
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // -------------------- CONTRASEÑA --------------------
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
                    unfocusedTextColor = Color.White,
                    cursorColor = GamerGreen
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(26.dp))

            // -------------------- BOTÓN INGRESAR --------------------
            Button(
                onClick = {

                    // Validación básica antes de llamar al backend
                    if (correo.isBlank() || contrasena.isBlank()) {
                        vm.setError("Debes completar ambos campos")
                        return@Button
                    }

                    vm.login(correo.trim(), contrasena.trim()) {
                        navController.navigate("PantallaPrincipal") {
                            popUpTo("login") { inclusive = true }
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

            // -------------------- MOSTRAR ERROR --------------------
            if (!errorVM.isNullOrBlank()) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = errorVM!!,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(20.dp))

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
