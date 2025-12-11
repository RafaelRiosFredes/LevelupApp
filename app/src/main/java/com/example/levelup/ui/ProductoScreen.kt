@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.levelup.viewmodel.CarritoViewModel
import com.example.levelup.viewmodel.OpinionesViewModel
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.model.data.OpinionEntity
import kotlinx.coroutines.launch

@Composable
fun ProductoScreen(
    navController: NavController,
    productosViewModel: ProductosViewModel,
    carritoViewModel: CarritoViewModel,
    opinionesViewModel: OpinionesViewModel,
    id: Long
) {
    ProductoContent(
        navController = navController,
        productosViewModel = productosViewModel,
        carritoViewModel = carritoViewModel,
        opinionesViewModel = opinionesViewModel,
        id = id
    )
}

@Composable
private fun ProductoContent(
    navController: NavController,
    productosViewModel: ProductosViewModel,
    carritoViewModel: CarritoViewModel,
    opinionesViewModel: OpinionesViewModel,
    id: Long
) {
    val productoFlow = remember(id) { productosViewModel.obtenerProductoPorId(id) }
    val producto by productoFlow.collectAsState(initial = null)

    // 🎯 Opiniones desde Room (Flow<List<OpinionEntity>>)
    val opiniones by opinionesViewModel.obtenerOpiniones(id)
        .collectAsState(initial = emptyList())

    var comentario by remember { mutableStateOf("") }
    var estrellas by remember { mutableStateOf(5) }
    var nombreUsuario by remember { mutableStateOf("Usuario") }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Detalle del producto",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF39FF14)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color(0xFF39FF14)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Black
    ) { padding ->

        producto?.let { prod ->

            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ========================
                // DATOS DEL PRODUCTO
                // ========================
                item {
                    Text(prod.nombre, fontSize = 26.sp, color = Color.White, fontWeight = FontWeight.Bold)

                    Spacer(Modifier.height(16.dp))

                    AsyncImage(
                        model = prod.imagenUrl,
                        contentDescription = prod.nombre,
                        modifier = Modifier.size(250.dp)
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(prod.descripcion, color = Color.LightGray, textAlign = TextAlign.Center)

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "Precio: $${prod.precio}",
                        color = Color(0xFF39FF14),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            carritoViewModel.agregarProducto(
                                productoId = prod.backendId ?: 0,
                                nombre = prod.nombre,
                                precio = prod.precio,
                                imagenUrl = prod.imagenUrl
                            )
                            scope.launch { snackbarHostState.showSnackbar("Agregado al carrito") }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF39FF14),
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Agregar al carrito")
                    }

                    Spacer(Modifier.height(30.dp))

                    Text("Opiniones", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)

                    Spacer(Modifier.height(16.dp))
                }

                // ========================
                // LISTA DE OPINIONES (ROOM)
                // ========================
                items(opiniones) { op ->
                    OpinionCardLocal(op)
                    Spacer(Modifier.height(16.dp))
                }

                // ========================
                // FORMULARIO PARA COMENTAR
                // ========================
                item {
                    Spacer(Modifier.height(20.dp))

                    OutlinedTextField(
                        value = nombreUsuario,
                        onValueChange = { nombreUsuario = it },
                        label = { Text("Tu nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(color = Color.White)
                    )

                    Spacer(Modifier.height(10.dp))

                    OutlinedTextField(
                        value = comentario,
                        onValueChange = { comentario = it },
                        label = { Text("Comentario") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(color = Color.White)
                    )

                    Spacer(Modifier.height(10.dp))

                    StarRating(estrellas) { estrellas = it }

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = {
                            opinionesViewModel.insertarOpinion(
                                OpinionEntity(
                                    productoId = id,
                                    comentario = comentario,
                                    estrellas = estrellas,
                                    nombreUsuario = nombreUsuario
                                )
                            )
                            comentario = ""
                            estrellas = 5
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF39FF14),
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Enviar opinión")
                    }

                    Spacer(Modifier.height(40.dp))
                }
            }

        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF39FF14))
        }
    }
}


// =====================================
// CARD DE OPINIÓN LOCAL
// =====================================
@Composable
fun OpinionCardLocal(op: OpinionEntity) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0B0B0B)
        ),
        border = BorderStroke(1.dp, Color(0xFF39FF14))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = op.nombreUsuario,
                color = Color(0xFF39FF14),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            StarRating(op.estrellas)

            Spacer(Modifier.height(8.dp))

            Text(
                text = op.comentario,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}


// =====================================
// ESTRELLAS (REUTILIZABLE)
// =====================================
@Composable
fun StarRating(selected: Int, onSelect: (Int) -> Unit = {}) {
    Row {
        for (i in 1..5) {
            val color = if (i <= selected) Color(0xFFFFD700) else Color.Gray

            Text(
                text = "★",
                fontSize = 28.sp,
                color = color,
                modifier = Modifier
                    .padding(4.dp)
                    .clickable { onSelect(i) }
            )
        }
    }
}
