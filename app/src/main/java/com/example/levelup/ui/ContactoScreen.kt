@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import kotlinx.coroutines.launch

@Composable
fun ContactoScreen(
    navController: NavController
) {

    DrawerGlobal(navController = navController) {

        val snackbar = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        val focusManager = LocalFocusManager.current
        val keyboard = LocalSoftwareKeyboardController.current

        var nombre by rememberSaveable { mutableStateOf("") }
        var email by rememberSaveable { mutableStateOf("") }
        var mensaje by rememberSaveable { mutableStateOf("") }

        var errorNombre by remember { mutableStateOf(false) }
        var errorEmail by remember { mutableStateOf(false) }
        var errorMensaje by remember { mutableStateOf(false) }

        Scaffold(
            containerColor = JetBlack,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Contacto", color = GamerGreen) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = JetBlack
                    )
                )
            },
            snackbarHost = { SnackbarHost(snackbar) }
        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                // IMAGEN SUPERIOR
                Image(
                    painter = rememberAsyncImagePainter(
                        "https://www.azernews.az/media/2023/11/27/2023_rog_zephyrus_duo_16_gx650_scenario_photo_01.jpg"
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    "Formulario de Contacto",
                    color = GamerGreen,
                    fontSize = 22.sp,
                )

                Spacer(Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                    shape = MaterialTheme.shapes.medium
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        // ---------------- NOMBRE ----------------
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = {
                                nombre = it
                                errorNombre = it.isBlank()
                            },
                            label = { Text("Nombre completo", color = PureWhite) },
                            modifier = Modifier.fillMaxWidth(),
                            isError = errorNombre,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GamerGreen,
                                unfocusedBorderColor = Color.Gray,
                                focusedTextColor = PureWhite,
                                unfocusedTextColor = PureWhite,
                                cursorColor = GamerGreen
                            )
                        )
                        if (errorNombre)
                            Text("Ingresa tu nombre", color = Color.Red, fontSize = 12.sp)

                        Spacer(Modifier.height(12.dp))

                        // ---------------- EMAIL ----------------
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                errorEmail = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                            },
                            label = { Text("Correo electrónico", color = PureWhite) },
                            modifier = Modifier.fillMaxWidth(),
                            isError = errorEmail,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GamerGreen,
                                unfocusedBorderColor = Color.Gray,
                                focusedTextColor = PureWhite,
                                unfocusedTextColor = PureWhite,
                                cursorColor = GamerGreen
                            )
                        )
                        if (errorEmail)
                            Text("Email inválido", color = Color.Red, fontSize = 12.sp)

                        Spacer(Modifier.height(12.dp))

                        // ---------------- MENSAJE ----------------
                        OutlinedTextField(
                            value = mensaje,
                            onValueChange = {
                                mensaje = it
                                errorMensaje = it.isBlank()
                            },
                            label = { Text("Mensaje", color = PureWhite) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            isError = errorMensaje,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GamerGreen,
                                unfocusedBorderColor = Color.Gray,
                                focusedTextColor = PureWhite,
                                unfocusedTextColor = PureWhite,
                                cursorColor = GamerGreen
                            )
                        )
                        if (errorMensaje)
                            Text("El mensaje no puede estar vacío", color = Color.Red, fontSize = 12.sp)

                        Spacer(Modifier.height(20.dp))

                        // ---------------- BOTÓN ENVIAR ----------------
                        Button(
                            onClick = {
                                errorNombre = nombre.isBlank()
                                errorEmail = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                                errorMensaje = mensaje.isBlank()

                                if (!errorNombre && !errorEmail && !errorMensaje) {
                                    keyboard?.hide()
                                    focusManager.clearFocus()

                                    scope.launch {
                                        snackbar.showSnackbar("Mensaje enviado exitosamente")
                                    }

                                    nombre = ""
                                    email = ""
                                    mensaje = ""

                                } else {
                                    scope.launch {
                                        snackbar.showSnackbar("Corrige los campos marcados")
                                    }
                                }

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GamerGreen,
                                contentColor = JetBlack
                            )
                        ) {
                            Text("Enviar mensaje", fontSize = 16.sp)
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

            }
        }
    }
}
