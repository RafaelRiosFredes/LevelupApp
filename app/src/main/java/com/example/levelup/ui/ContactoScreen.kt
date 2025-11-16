

@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.levelup.ui

// Importación de librerías necesarias para UI, manejo de estado, diseño y utilidades
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
import androidx.navigation.NavController

// Pantalla principal de contacto con formulario y navegación lateral
@Composable
fun ContactoScreen(
    navController: NavController,
    onEnviar: (nombre: String, email: String, mensaje: String) -> Unit = { _, _, _ -> },
    onNavigate: (String) -> Unit ={}
) {
    // Estado para manejar notificaciones tipo Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Estado del Drawer (menú lateral)
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Estado del texto de búsqueda (recordado incluso tras rotar la pantalla)
    var searchQuery by rememberSaveable { mutableStateOf("") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Contenido del menú lateral (idéntico a PantallaContacto)
            ModalDrawerSheet(
                drawerContainerColor = Color.Black,
                drawerContentColor = Color.White,
                modifier = Modifier
                    .background(Color.Black)
                    .width(300.dp)
            ) {
                // Header: botón para cerrar
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

                // Título de la app en el drawer
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

                // SearchBar (mismo componente reutilizable que tienes en PantallaContacto)
                CustomSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = {  }
                )

                // Items del drawer (idénticos)
                Column(modifier = Modifier.fillMaxWidth()) {

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
                                onNavigate("carrito")
                            }
                        },
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = DividerDefaults.Thickness,
                        color = Color.DarkGray
                    )

                    NavigationDrawerItem(
                        label = { Text("Inicio", color = Color.White) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                onNavigate("PantallaPrincipal")
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Productos", color = Color.White) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                onNavigate("productos")
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
                                onNavigate("Noticias")
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Inicia sesión", color = Color.White) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                onNavigate("login")
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
                                onNavigate("registro")
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
                                onNavigate("contacto")
                            }
                        },
                        icon = { Icon(Icons.Default.Email, contentDescription = "Contacto", tint = Color.White) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )




                    NavigationDrawerItem(
                        label = { Text("Mi cuenta", color = Color.White) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                onNavigate("login")
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
        // Contenido principal de la pantalla con barra superior y formulario
        Scaffold(
            topBar = {
                // Barra superior con título y botón de menú
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
            // Manejo del teclado y foco
            val focusManager = LocalFocusManager.current
            val keyboardController = LocalSoftwareKeyboardController.current

            // Estados de los campos del formulario
            var nombre by rememberSaveable { mutableStateOf("") }
            var email by rememberSaveable { mutableStateOf("") }
            var mensaje by rememberSaveable { mutableStateOf("") }

            // Variables de error para validación
            var emailError by remember { mutableStateOf(false) }
            var nombreError by remember { mutableStateOf(false) }
            var mensajeError by remember { mutableStateOf(false) }

            // Contenedor del formulario
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .background(Color.Black)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Banner superior con imagen y eslogan
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

                // Título del formulario
                Text(
                    text = "Formulario de Contacto",
                    fontSize = 22.sp,
                    color = Color(0xFF00AAFF),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Tarjeta que contiene el formulario
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Campo: Nombre completo
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

                        // Campo: Correo electrónico
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

                        // Campo: Mensaje
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

                        // Botón de envío con validación de campos
                        Button(
                            onClick = {
                                // Validación de campos
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
                                    // Limpiar campos tras enviar
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

                // Pie de página (footer)
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
}

// Composable para la barra de búsqueda reutilizable
@Composable
fun CustomSearchBar(
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
        // Campo de texto para buscar contenido
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

        // Botón de búsqueda con ícono
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

