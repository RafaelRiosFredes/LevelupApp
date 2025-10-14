@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.levelup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.levelup.R
import com.example.levelup.model.local.CategoriaEntity
import com.example.levelup.viewmodel.CategoriaViewModel
import kotlinx.coroutines.launch

// -------------------------
// MODELO DE DATOS
// -------------------------
data class Categoria(
    val nombre: String,
    val iconUrl: String
)

// -------------------------
// PANTALLA PRINCIPAL
// -------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(vm: CategoriaViewModel) {
    // Copiado de PantallaContacto: snackbar, scope, estado del drawer y searchQuery
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
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black)
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
                CategoriasGrid(vm = vm) // ahora contiene el footer como último ítem
            }
        }
    }
}

// -------------------------
// GRID DE CATEGORÍAS (implementado con LazyColumn 2x por fila para que el footer sea parte del scroll)
// -------------------------
@Composable
fun CategoriasGrid(
    vm: CategoriaViewModel
) {
//    val categorias = listOf(
//        Categoria("Juegos de Mesa", "https://img.icons8.com/?size=100&id=Rz3NTZkvlexz&format=png&color=39ff14"),
//        Categoria("Accesorios", "https://img.icons8.com/?size=100&id=MlGniXnp6gP1&format=png&color=39ff14"),
//        Categoria("Consolas", "https://img.icons8.com/?size=100&id=Tb5XGbRvSX2v&format=png&color=39ff14"),
//        Categoria("Computadores Gamer", "https://img.icons8.com/?size=100&id=5QX8hl5ld2od&format=png&color=39ff14"),
//        Categoria("Sillas Gamer", "https://img.icons8.com/?size=100&id=cnYTrlcPnC0e&format=png&color=39ff14"),
//        Categoria("Mouse", "https://img.icons8.com/?size=100&id=nzxaVHSTEeb8&format=png&color=39ff14"),
//        Categoria("Mousepad", "https://img.icons8.com/?size=100&id=xZ83Lf2CSaVj&format=png&color=39ff14"),
//        Categoria("Poleras Personalizadas", "https://img.icons8.com/?size=100&id=N757ereBOFWm&format=png&color=39ff14"),
//        Categoria("Poleras Gamer Personalizadas", "https://img.icons8.com/?size=100&id=aprOfFsRz9MN&format=png&color=39ff14")
//    )

    val categorias by vm.categorias.collectAsState()

    // Título
    Text(
        text = stringResource(R.string.categorias),
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp)
    )

    // LazyColumn que renderiza filas con 2 cards cada una, y añade el footer al final
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categorias.chunked(2)) { fila ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    CategoriaCard(fila[0])
                }
                if (fila.size > 1) {
                    Box(modifier = Modifier.weight(1f)) {
                        CategoriaCard(fila[1])
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f)) // mantener la alineación si la última fila tiene 1 item
                }
            }
        }

        // Footer como último ítem: forma parte del scroll ahora
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = stringResource(R.string.footer),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


// ------------------------- // CARD DE CATEGORÍA // -------------------------
@Composable
fun CategoriaCard(categoria: CategoriaEntity) {
    Card( modifier = Modifier
        .fillMaxWidth()
        .height(140.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)) ) {
        Column( modifier = Modifier
            .fillMaxSize() .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center ) {
            Image( painter = rememberAsyncImagePainter(categoria.icon_url),
                contentDescription = categoria.nombre,
                modifier = Modifier.size(55.dp) )
            Spacer(modifier = Modifier.height(8.dp))
            Text( text = categoria.nombre, color = Color.White, fontSize = 14.sp, textAlign = TextAlign.Center ) } } }



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
                focusedBorderColor = Color(0xFF39FF14),
                unfocusedBorderColor = Color(0xFF39FF14),
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black,
                cursorColor = Color(0xFF39FF14),
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
                tint = Color(0xFF39FF14)
            )
        }
    }
}