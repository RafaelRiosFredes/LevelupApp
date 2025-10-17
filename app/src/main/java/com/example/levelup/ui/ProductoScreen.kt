package com.example.levelup.ui.producto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.levelup.local.ProductoDao
import com.example.levelup.local.ProductoEntity
import com.example.levelup.viewmodel.ProductoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun ProductoScreen(navController: NavController, viewModel: ProductoViewModel) {
    val productos = viewModel.productos.collectAsState().value

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(productos) { producto ->
                ProductoItem(producto) {
                    viewModel.agregarAlCarrito(producto)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("carrito") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ir al carrito")
        }
    }
}




@Composable
fun ProductoItem(producto: ProductoEntity, onAddToCart: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Imagen desde URL
            AsyncImage(
                model = producto.imagenUrl,
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Text(text = producto.nombre)
            Text(text = producto.descripcion ?: "")
            Text(text = "Precio: $${producto.precio}")
            Button(onClick = onAddToCart) {
                Text("Agregar al carrito")
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
