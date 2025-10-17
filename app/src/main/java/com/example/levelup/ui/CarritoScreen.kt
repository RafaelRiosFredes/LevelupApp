package com.example.levelup.ui.producto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.levelup.local.ProductoEntity
import com.example.levelup.viewmodel.ProductoViewModel
import kotlinx.coroutines.CoroutineScope
import androidx.compose.material3.DrawerState
import kotlinx.coroutines.launch

@Composable
fun CarritoScreen(
    navController: NavController,
    viewModel: ProductoViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    val carrito by viewModel.carrito.collectAsState()

    // Total del carrito
    val total = carrito.sumOf { it.precio }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(carrito) { producto ->
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
                        Button(
                            onClick = {
                                // Quitar del carrito correctamente
                                viewModel.quitarDelCarrito(producto)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Quitar del carrito")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar total
        Text(
            text = "Total: $${"%.2f".format(total)}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}
