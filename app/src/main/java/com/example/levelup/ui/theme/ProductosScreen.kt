package com.example.levelup.ui.producto

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.local.ProductosEntity
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.ProductosViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    onNavigateBack: () -> Unit = {},
    nav: (String) -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val factory = ProductosViewModelFactory(application)
    val viewModel: ProductosViewModel = viewModel(factory = factory)

    val productos by viewModel.productos.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(scope, drawerState, snackbarHostState)
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("LEVEL-UP GAMER") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color(0xFF39FF14))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            containerColor = Color.Black
        ) { padding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(productos) { producto ->
                    ProductoItem(producto) {
                        nav("producto/${producto.id}")
                    }
                }
            }
        }
    }
}

// Mueve estas funciones fuera de ProductosScreen:

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

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.DarkGray
        )

        DrawerItem("Inicia sesión", null, scope, drawerState, snackbarHostState)
        DrawerItem("Regístrate", null, scope, drawerState, snackbarHostState)
        DrawerItem("Mi cuenta", Icons.Default.AccountCircle, scope, drawerState, snackbarHostState)
        DrawerItem("Puntos LevelUp", Icons.Default.Star, scope, drawerState, snackbarHostState)
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
fun ProductoItem(producto: ProductosEntity, onClick: () -> Unit){
Card(
modifier = Modifier
.padding(8.dp)
.fillMaxWidth()
.height(250.dp),
elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_gallery),
            contentDescription = producto.nombre,
            modifier = Modifier
                .height(130.dp)
                .fillMaxWidth()
        )
        Text(producto.nombre, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Text("$${producto.precio}")
        Button(onClick = onClick) {
            Text("Añadir al carrito")
        }
    }
}
}
