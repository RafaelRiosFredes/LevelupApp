@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.levelup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

    // Drawer state (for the hamburger menu)
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    //Recibir lo escrito en la barra de búsqueda y recordar aun habiendo rotado el teléfono
    var searchQuery by rememberSaveable { mutableStateOf("") }


    // ModalNavigationDrawer with black background for the sheet
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Make the sheet itself black
            ModalDrawerSheet(
                drawerContainerColor = Color.Black,
                drawerContentColor = Color.White,
                modifier = Modifier
                    .background(Color.Black)
                    .width(300.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { scope.launch { drawerState.close() } }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar menú",
                            tint = Color(0xFF39FF14)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(vertical = 18.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = Color(0xFF39FF14),
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Navigation items that mirror your HTML menu
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { /* TODO: handle search logic here */ }
                )

                Column(modifier = Modifier.fillMaxWidth()) {
                    NavigationDrawerItem(
                        label = { Text("Inicio", color = Color.White) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                snackbarHostState.showSnackbar("Inicio seleccionado")
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = null, tint = Color.White) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Juegos de Mesa", color = Color.White) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                snackbarHostState.showSnackbar("Juegos de Mesa seleccionado")
                            }
                        },
                        // no icon to avoid missing-icon issues
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Accesorios", color = Color.White) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                snackbarHostState.showSnackbar("Accesorios seleccionado")
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Consolas", color = Color.White) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                snackbarHostState.showSnackbar("Consolas seleccionado")
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Contacto", color = Color.White) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                snackbarHostState.showSnackbar("Contacto seleccionado")
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Noticias", color = Color.White) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                snackbarHostState.showSnackbar("Noticias seleccionado")
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Carrito", color = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = MaterialTheme.shapes.small,
                                    tonalElevation = 0.dp,
                                    color = Color(0xFF39FF14)
                                ) {
                                    Text(
                                        text = "0",
                                        color = Color.Black,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                snackbarHostState.showSnackbar("Carrito seleccionado")
                            }
                        },
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.DarkGray)

                    NavigationDrawerItem(
                        label = { Text("Inicia sesión", color = Color.White) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                snackbarHostState.showSnackbar("Inicia sesión seleccionado")
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Regístrate", color = Color.White) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                snackbarHostState.showSnackbar("Regístrate seleccionado")
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Mi cuenta", color = Color.White) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                snackbarHostState.showSnackbar("Mi cuenta seleccionado")
                            }
                        },
                        icon = { Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.White) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Puntos LevelUp", color = Color.White) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                snackbarHostState.showSnackbar("Puntos LevelUp seleccionado")
                            }
                        },
                        icon = { Icon(Icons.Default.Star, contentDescription = null, tint = Color.White) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    ) {
        // ---- BEGIN ORIGINAL SCAFFOLD (unchanged form logic & colors) ----
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color(0xFF39FF14)
                            )
                        }
                    },
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
        // ---- END ORIGINAL SCAFFOLD ----
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.Black),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    "Buscar en LEVEL-UP GAMER",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF39FF14),
                unfocusedBorderColor = Color(0xFF39FF14),
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black,
                cursorColor = Color(0xFF39FF14),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Search
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onSearch,
            modifier = Modifier
                .size(50.dp)
                .background(Color.Transparent)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = Color(0xFF39FF14)
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PreviewPantallaContacto() {
    PantallaContacto()
}
