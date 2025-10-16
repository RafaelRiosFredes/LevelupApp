package com.example.levelup.ui

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.levelup.R
import com.example.levelup.model.local.AppDatabase
import com.example.levelup.model.local.ProductosEntity
import com.example.levelup.model.repository.ProductosRepository
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.ProductosViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    nav: NavHostController, // ✅ Ahora recibe el navController
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val db = AppDatabase.getInstance(application)
    val repo = ProductosRepository(db.productosDao())
    val factory = ProductosViewModelFactory(application)
    val viewModel: ProductosViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)

    val productos by viewModel.productos.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var searchQuery by rememberSaveable { mutableStateOf("") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(scope, drawerState, snackbarHostState)
        }
    ) {
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
                            text = context.getString(R.string.app_name),
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.Black)
            ) {
                // 🔍 Barra de búsqueda
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { /* TODO: Filtrar productos */ }
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "🛒 Nuestros Productos",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(productos) { producto ->
                        ProductoItem(
                            producto = producto,
                            onClick = {
                                // ✅ Al hacer clic, ir al detalle
                                nav.navigate("producto/${producto.id}")
                            },
                            onAddToCart = {
                                viewModel.agregarAlCarrito(producto)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoItem(
    producto: ProductosEntity,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(250.dp)
            .clickable { onClick() } // ✅ clic lleva al detalle
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = rememberAsyncImagePainter(producto.imagenUrl),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .height(130.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(producto.nombre, fontWeight = FontWeight.Bold)
            Text("$${producto.precio}")
            Button(onClick = onAddToCart) {
                Text("Añadir al carrito")
            }
        }
    }
}

@Composable
fun DrawerContent(
    scope: CoroutineScope,
    drawerState: DrawerState,
    snackbarHostState: SnackbarHostState
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.Black,
        drawerContentColor = Color.White,
        modifier = Modifier
            .background(Color.Black)
            .width(300.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
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
                text = "LEVEL-UP GAMER",
                color = Color(0xFF39FF14),
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        DrawerItem("Inicio", Icons.Default.Home, scope, drawerState, snackbarHostState)
        DrawerItem("Juegos de Mesa", null, scope, drawerState, snackbarHostState)
        DrawerItem("Accesorios", null, scope, drawerState, snackbarHostState)
        DrawerItem("Consolas", null, scope, drawerState, snackbarHostState)
        DrawerItem("Contacto", null, scope, drawerState, snackbarHostState)
        DrawerItem("Noticias", null, scope, drawerState, snackbarHostState)
        DrawerItem("Carrito", Icons.Default.ShoppingCart, scope, drawerState, snackbarHostState)
    }
}

@Composable
fun DrawerItem(
    title: String,
    icon: ImageVector?,
    scope: CoroutineScope,
    drawerState: DrawerState,
    snackbarHostState: SnackbarHostState
) {
    NavigationDrawerItem(
        label = { Text(title, color = Color.White) },
        selected = false,
        onClick = {
            scope.launch {
                drawerState.close()
                snackbarHostState.showSnackbar("$title seleccionado")
            }
        },
        icon = {
            icon?.let {
                Icon(it, contentDescription = title, tint = Color.White)
            }
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

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
            placeholder = { Text("Buscar en LEVEL-UP GAMER", color = Color.Gray, fontSize = 14.sp) },
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
