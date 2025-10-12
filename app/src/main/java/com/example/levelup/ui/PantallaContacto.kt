@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.levelup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.levelup.R
import kotlinx.coroutines.launch
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun PantallaContacto(
    onEnviar: (nombre: String, email: String, mensaje: String) -> Unit = { _, _, _ -> }
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = Color(0xFF39FF14)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Black,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        // Form state
        var nombre by rememberSaveable { mutableStateOf("") }
        var email by rememberSaveable { mutableStateOf("") }
        var mensaje by rememberSaveable { mutableStateOf("") }

        var emailError by remember { mutableStateOf(false) }
        var nombreError by remember { mutableStateOf(false) }
        var mensajeError by remember { mutableStateOf(false) }

        // Scrollable column so keyboard doesn't hide content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(Color.Black)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Banner (responsive height)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        "https://www.azernews.az/media/2023/11/27/2023_rog_zephyrus_duo_16_gx650_scenario_photo_01.jpg?v=1701092248"
                    ),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = stringResource(R.string.slogan),
                    color = Color(0xFF00AAFF),
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Formulario de Contacto",
                fontSize = 22.sp,
                color = Color(0xFF00AAFF),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Nombre
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it; nombreError = it.isBlank() },
                        label = { Text("Nombre Completo") },
                        placeholder = { Text("Ingresa tu nombre") },
                        singleLine = true,
                        isError = nombreError,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.LightGray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (nombreError) {
                        Text(
                            text = "El nombre es requerido",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                        },
                        label = { Text("Correo Electrónico") },
                        placeholder = { Text("ejemplo@correo.com") },
                        singleLine = true,
                        isError = emailError,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.LightGray,
                            errorTextColor = Color.Red
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (emailError) {
                        Text(
                            text = "Ingresa un email válido",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Mensaje
                    OutlinedTextField(
                        value = mensaje,
                        onValueChange = { mensaje = it; mensajeError = it.isBlank() },
                        label = { Text("Contenido") },
                        placeholder = { Text("Escribe tu mensaje") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.LightGray
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        isError = mensajeError,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )
                    if (mensajeError) {
                        Text(
                            text = "El mensaje no puede estar vacío",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // Validación simple
                            nombreError = nombre.isBlank()
                            emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                            mensajeError = mensaje.isBlank()

                            if (!nombreError && !emailError && !mensajeError) {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                onEnviar(nombre, email, mensaje)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Mensaje enviado")
                                }
                                // limpiar campos si quieres:
                                nombre = ""
                                email = ""
                                mensaje = ""
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Corrige los errores antes de enviar")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(text = "Enviar Mensaje")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer
            Text(
                text = stringResource(R.string.footer),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PreviewPantallaContacto() {
    PantallaContacto()
}
