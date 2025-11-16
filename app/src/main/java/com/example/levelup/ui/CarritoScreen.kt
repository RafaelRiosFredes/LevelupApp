@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.levelup.core.UserSession
import com.example.levelup.model.data.BoletaEntity
import com.example.levelup.model.data.CarritoEntity
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.BoletaViewModel
import com.example.levelup.viewmodel.CarritoViewModel
import com.example.levelup.viewmodel.UsuariosViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CarritoScreen(
    navController: NavController,
    carritoViewModel: CarritoViewModel,
    boletaViewModel: BoletaViewModel,
    usuariosViewModel: UsuariosViewModel
) {

    DrawerGlobal(navController = navController) {

        val carrito by carritoViewModel.carrito.collectAsState()
        val total = carrito.sumOf { it.precio * it.cantidad }
        val usuarioActual = UserSession.id
        val scope = rememberCoroutineScope()

        Scaffold(
            containerColor = JetBlack,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Tu Carrito", color = GamerGreen, fontSize = 20.sp) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = GamerGreen
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = JetBlack
                    )
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {

                if (carrito.isEmpty()) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("El carrito está vacío", color = PureWhite)
                    }

                } else {

                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items(carrito) { item ->
                            CarritoItem(
                                item = item,
                                onEliminar = { carritoViewModel.eliminar(item) },
                                onAumentar = { carritoViewModel.aumentarCantidad(item) },
                                onDisminuir = { carritoViewModel.disminuirCantidad(item) }
                            )
                        }
                    }

                    Divider(color = Color.DarkGray)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total:", color = PureWhite, fontSize = 18.sp)
                        Text(
                            "$${String.format("%.2f", total)}",
                            color = GamerGreen,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // -------- Botón Finalizar Compra --------
                    Button(
                        onClick = {

                            if (usuarioActual == null) {
                                navController.navigate("login")
                                return@Button
                            }

                            val productos = carrito

                            val detalleTexto = productos.joinToString("\n") {
                                "${it.productoId}|${it.nombre}|${it.cantidad}|${it.precio}"
                            }

                            val fecha = SimpleDateFormat(
                                "yyyy-MM-dd HH:mm",
                                Locale.getDefault()
                            ).format(Date())

                            // Obtener correo real desde UserSession
                            val correoUsuario = UserSession.correo ?: ""

                            // Aplicar 20% si es @duocuc.cl
                            val descuentoAplicado = if (correoUsuario.endsWith("duocuc.cl")) 20 else 0

                            // Total con descuento
                            val totalFinal = if (descuentoAplicado > 0) {
                                (total * 0.8).toLong()
                            } else total.toLong()

                            val boleta = BoletaEntity(
                                total = totalFinal,
                                descuento = descuentoAplicado,
                                fechaEmision = fecha,
                                usuarioIdBackend = usuarioActual,
                                usuarioNombre = UserSession.nombre,
                                usuarioApellidos = UserSession.apellidos,
                                usuarioCorreo = UserSession.correo,
                                detalleTexto = detalleTexto
                            )

                            scope.launch {
                                val idLocal = boletaViewModel.crearBoletaRoom(boleta).toInt()
                                carritoViewModel.vaciarCarrito()
                                navController.navigate("boleta_detalle/$idLocal")
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
