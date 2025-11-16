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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLoginScreen(
    navController: NavController
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
                        text = "Acceso Administrador",
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

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Admin", color = Color.White) },
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

            Button(
                onClick = {
                    scope.launch {

                        if (correo.trim() == "admin@levelup.com" &&
                            contrasena.trim() == "admin123") {

                            UserSession.login(
                                id = -1,
                                correo = "admin@levelup.com",
                                rol = "admin"
                            )

                            navController.navigate("admin_home") {
                                popUpTo("admin_login") { inclusive = true }
                            }
                        } else {
                            error = "Credenciales de administrador incorrectas"
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
        }
    }
}
