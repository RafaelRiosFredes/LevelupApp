@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.levelup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.levelup.R
import com.example.levelup.ui.theme.GamerGreen
import kotlinx.coroutines.launch

// -------------------------
// MODELO DE DATOS
// -------------------------
data class Categoria(
    val nombre: String,
    val icono: ImageVector
)

// -------------------------
// PANTALLA PRINCIPAL
// -------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    onNavigate: (String) -> Unit = {},   // recibe rutas ("categorias", "usuarios", "inventario", ...)
    onLogout: () -> Unit = {}   ) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
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
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { /* TODO: handle search logic here */ }
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
        // Contenido principal con la TopBar que abre el drawer
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
                            color = Color(0xFF39FF14),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            containerColor = Color.Black
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.Black)
            ) {
                BannerPrincipal()

                CategoriasSection()
            }
        }
    }
}

// -------------------------
// BANNER PRINCIPAL
// -------------------------
@Composable
fun BannerPrincipal() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
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
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 12.dp)
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit ) {
    Row( modifier = Modifier
        .fillMaxWidth(1f)
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .background(Color.Black),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically ) {
        // Campo de texto para buscar contenido
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text( "Buscar en LEVEL-UP GAMER", color = Color.Gray, fontSize = 14.sp ) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GamerGreen,
                unfocusedBorderColor = GamerGreen,
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black,
                cursorColor = GamerGreen,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White ),
            modifier = Modifier .weight(1f)
                .height(50.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Search ) )
        Spacer(modifier = Modifier.width(8.dp))
        // Botón de búsqueda con ícono
        IconButton( onClick = onSearch,
            modifier = Modifier
                .size(50.dp)
                .background(Color.Transparent) ) {
            Icon( imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = GamerGreen
            )
        }
    }
}
@Composable
fun CategoriasSection() {

    // 🔹 Lista ESTÁTICA de categorías (agregada)
    // Cada elemento tiene un nombre y un ícono de Material Icons.
    val categorias = listOf(
        Categoria("Juegos de Mesa", Icons.Default.Casino),
        Categoria("Accesorios", Icons.Default.Headphones),
        Categoria("Consolas", Icons.Default.VideogameAsset),
        Categoria("Computadores Gamer", Icons.Default.Computer),
        Categoria("Sillas Gamer", Icons.Default.AirlineSeatReclineExtra),
        Categoria("Mouse", Icons.Default.Mouse),
        Categoria("Mousepad", Icons.Default.TouchApp),
        Categoria("Poleras Personalizadas", Icons.Default.Checkroom),
        Categoria("Poleras Gamer Personalizadas", Icons.Default.CatchingPokemon)
    )

    // 🔹 Contenedor principal de la sección
    // Tiene padding y título “CATEGORÍAS”
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // 🔸 Título principal de la sección
        Text(
            text = "CATEGORÍAS",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        // 🔹 Grilla vertical (2 columnas)
        // LazyVerticalGrid permite mostrar un listado en cuadrícula.
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 columnas fijas
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp), // separación entre filas
            horizontalArrangement = Arrangement.spacedBy(12.dp), // separación entre columnas
            content = {
                // 🔸 Iteramos sobre la lista de categorías
                items(categorias) { categoria ->
                    CategoriaCard(categoria) // llamada al composable de tarjeta
                }
            }
        )
    }
}
@Composable
fun CategoriaCard(categoria: Categoria) {
    // 🔹 Card = contenedor con fondo oscuro y bordes redondeados
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp), // alto fijo
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A) // gris muy oscuro gamer
        )
    ) {
        // 🔸 Contenido centrado dentro de la tarjeta
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 🔸 Ícono verde gamer
            Icon(
                imageVector = categoria.icono,
                contentDescription = categoria.nombre,
                tint = GamerGreen,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))

            // 🔸 Texto del nombre de la categoría
            Text(
                text = categoria.nombre,
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

