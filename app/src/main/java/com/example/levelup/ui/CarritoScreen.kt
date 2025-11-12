package com.example.levelup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.levelup.viewmodel.CarritoViewModel
import com.example.levelup.model.data.CarritoEntity
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    nav: NavController,
    carritoViewModel: CarritoViewModel
) {
    val carrito = carritoViewModel.carrito.collectAsState()
    val total = carrito.value.sumOf { it.precio * it.cantidad }

    Scaffold(
        containerColor = JetBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tu Carrito", color = GamerGreen, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = GamerGreen)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = JetBlack)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            if (carrito.value.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("El carrito está vacío", color = PureWhite)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(carrito.value) { item ->
                        CarritoItem(
                            item = item,
                            onEliminar = { carritoViewModel.eliminar(item) },
                            onAumentar = { carritoViewModel.aumentarCantidad(item) },
                            onDisminuir = { carritoViewModel.disminuirCantidad(item) }
                        )
                    }
                }

                Divider(color = Color.DarkGray, thickness = 1.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total:", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(
                        "$${String.format("%.2f", total)}",
                        color = GamerGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { /* Aquí más adelante haremos la compra ✅ */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GamerGreen, contentColor = JetBlack)
                ) {
                    Text("Finalizar compra", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CarritoItem(
    item: CarritoEntity,
    onEliminar: () -> Unit,
    onAumentar: () -> Unit,
    onDisminuir: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(item.imagenUrl),
                contentDescription = item.nombre,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.nombre, color = PureWhite, fontWeight = FontWeight.Bold)
                Text("Precio: $${item.precio}", color = GamerGreen)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDisminuir) { Text("-") }
                Text("${item.cantidad}", color = PureWhite, modifier = Modifier.padding(4.dp))
                IconButton(onClick = onAumentar) { Text("+") }
            }

            IconButton(onClick = onEliminar) {
                Icon(
                    painterResource(android.R.drawable.ic_delete),
                    contentDescription = "Eliminar",
                    tint = Color(0xFFFF5555)
                )
            }
        }
    }
}
