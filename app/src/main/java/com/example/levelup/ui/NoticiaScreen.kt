package com.example.levelup.ui
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.R
import com.example.levelup.viewmodel.NoticiasViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticiasScreen(vm: NoticiasViewModel = viewModel()) {
    val noticias = vm.noticias.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var searchQuery by rememberSaveable { mutableStateOf("") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.Black,
                drawerContentColor = Color.White,
                modifier = Modifier
                    .background(Color.Black)
                    .width(300.dp)
            ) {
                // Botón cerrar
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

                //  Título Drawer
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

                // Barra de búsqueda (decorativa)
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { }
                )

                // Ítems del drawer
                Column(modifier = Modifier.fillMaxWidth()) {
                    val items = listOf(
                        "Inicio" to Icons.Default.Home,
                        "Juegos de Mesa" to Icons.Default.SportsEsports,
                        "Accesorios" to Icons.Default.Headphones,
                        "Consolas" to Icons.Default.VideogameAsset,
                        "Contacto" to Icons.Default.Phone,
                        "Noticias" to Icons.Default.Newspaper,
                        "Carrito" to Icons.Default.ShoppingCart,
                        "Inicia sesión" to Icons.Default.Login,
                        "Regístrate" to Icons.Default.Person,
                        "Mi cuenta" to Icons.Default.AccountCircle,
                        "Puntos LevelUp" to Icons.Default.Star
                    )

                    items.forEach { (label, icon) ->
                        NavigationDrawerItem(
                            label = { Text(label, color = Color.White) },
                            selected = label == "Noticias",
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                    snackbarHostState.showSnackbar("$label seleccionado")
                                }
                            },
                            icon = { Icon(icon, contentDescription = null, tint = Color.White) },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = Color(0xFF39FF14)
                            )
                        }
                    },
                    title = {
                        Text(
                            text = "Noticias Gamer",
                            color = Color(0xFF39FF14),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Black
                    )
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            containerColor = Color.Black
        ) { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(16.dp)
            ) {
                items(noticias) { noticia ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                        ) {
                            // Imagen local desde drawable
                            Image(
                                painter = painterResource(id = noticia.imagen.toInt()),
                                contentDescription = noticia.titulo,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = noticia.titulo,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Text(
                                text = noticia.descripcion,
                                color = Color.LightGray,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = " ${noticia.fuente}",
                                color = Color(0xFF00AAFF),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

// Barra de búsqueda
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search)
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