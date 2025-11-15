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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.layout.ContentScale
import com.example.levelup.model.data.CarritoEntity
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.BoletaViewModel
import com.example.levelup.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    nav: NavController,
    carritoViewModel: CarritoViewModel,
    boletaViewModel: BoletaViewModel
) {
    val carrito = carritoViewModel.carrito.collectAsState()
    val total = carrito.value.sumOf { it.precio * it.cantidad }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = JetBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Tu Carrito",
                        color = GamerGreen,
                        fontSize = 20.sp)
                },
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

            /* si no hay productos */
            if (carrito.value.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("El carrito está vacío", color = PureWhite)
                }
            }

            /* si hay productos */
            else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(carrito.value) { item ->
                        CarritoItem(
                            item = item,
                            onEliminar = { carritoViewModel.eliminar(item) },
                            onAumentar = { carritoViewModel.aumentarCantidad(item) },
                            onDisminuir = { carritoViewModel.disminuirCantidad(item) }
                        )
                    }
                }

                Divider(color = Color.DarkGray)

                /* total */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total:",
                        color = PureWhite,
                        fontSize = 18.sp)
                    Text(
                        "$${String.format("%.2f", total)}",
                        color = GamerGreen,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                /* botón comprar */
                Button(
                    onClick = {
                        val productos = carrito.value

                        // -------- detalle de productos --------
                        val detalle = productos.joinToString("\n") {
                            "- ${it.nombre} x${it.cantidad} = $${it.precio * it.cantidad}"
                        }

                        val cantidad = productos.sumOf { it.cantidad }

                        scope.launch {
                            // crea boleta en room y da id
                            val boletaId = boletaViewModel.generarBoleta(
                                total = total,
                                cantidadProductos = cantidad,
                                detalle = detalle
                            )

                            // navegar a detalle boleta
                            nav.navigate("boleta_detalle/$boletaId")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GamerGreen,
                        contentColor = JetBlack
                    )
                ) {
                    Text("Finalizar compra", fontSize = 16.sp)
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
                Text(item.nombre, color = PureWhite)
                Text("Precio: $${item.precio}", color = GamerGreen)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDisminuir) { Text("-", color = PureWhite) }
                Text("${item.cantidad}", color = PureWhite, modifier = Modifier.padding(4.dp))
                IconButton(onClick = onAumentar) { Text("+", color = PureWhite) }
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
