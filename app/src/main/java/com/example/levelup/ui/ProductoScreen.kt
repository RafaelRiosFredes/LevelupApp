package com.example.levelup.ui.producto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.levelup.local.ProductoEntity
import com.example.levelup.viewmodel.ProductoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.DrawerState

@Composable
fun ProductoScreen(
    navController: NavController,
    viewModel: ProductoViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    val productos = viewModel.productos.collectAsState().value

    Column(modifier = Modifier.fillMaxSize()) {

        // Top bar con botón para abrir Drawer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF39FF14))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
            Text(
                text = "Productos",
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(productos) { producto ->
                ProductoItem(producto) {
                    viewModel.agregarAlCarrito(producto)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("carrito") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = producto.imagenUrl,
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
            Text(producto.descripcion ?: "", style = MaterialTheme.typography.bodyMedium)
            Text("Precio: $${producto.precio}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Button(onClick = onAddToCart, modifier = Modifier.fillMaxWidth()) {
                Text("Agregar al carrito")
            }
        }
    }
}
