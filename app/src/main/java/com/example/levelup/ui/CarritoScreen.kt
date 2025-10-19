package com.example.levelup.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelup.model.local.CarritoEntity
import com.example.levelup.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(nav: NavController, viewModel: CarritoViewModel) {
    val carrito = viewModel.carrito.collectAsState()
    val total = carrito.value.sumOf { it.precio * it.cantidad }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "🛒 Tu Carrito",
                        color = Color(0xFF39FF14),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .background(Color.Black)
        ) {
            if (carrito.value.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "El carrito está vacío",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { nav.navigate("productos") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF39FF14),
                                contentColor = Color.Black
                            ),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Añadir productos")
                        }
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(carrito.value) { producto ->
                        CarritoItem(
                            producto = producto,
                            onEliminar = { viewModel.eliminarDelCarrito(producto) }
                        )
                    }
                }

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color.DarkGray
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total:", color = Color.White, fontWeight = FontWeight.Bold)
                    Text(
                        "$${String.format("%.2f", total)}",
                        color = Color(0xFF39FF14),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { /* TODO: Finalizar compra */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF39FF14),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Finalizar compra", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CarritoItem(producto: CarritoEntity, onEliminar: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = producto.imagenRes),
                contentDescription = producto.nombre,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, color = Color.White, fontWeight = FontWeight.Bold)
                Text("$${producto.precio}", color = Color(0xFF39FF14))
            }
            IconButton(onClick = onEliminar) {
                Icon(
                    painter = painterResource(android.R.drawable.ic_delete),
                    contentDescription = "Eliminar",
                    tint = Color(0xFFFF5555)
                )
            }
        }
    }
}
