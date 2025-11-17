@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.viewmodel.CarritoViewModel
import com.example.levelup.viewmodel.ProductosViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductoScreen(
    navController: NavController,
    productosViewModel: ProductosViewModel,
    carritoViewModel: CarritoViewModel,
    id: Long
) {

    DrawerGlobal(navController = navController) {
        ProductoContent(
            navController = navController,
            productosViewModel = productosViewModel,
            carritoViewModel = carritoViewModel,
            id = id
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductoContent(
    navController: NavController,
    productosViewModel: ProductosViewModel,
    carritoViewModel: CarritoViewModel,
    id: Long
) {

    val productoFlow = remember(id) { productosViewModel.obtenerProductoPorId(id) }
    val producto by productoFlow.collectAsState(initial = null)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Detalle del producto",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF39FF14)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
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

            val imagenSegura = (prod.imagenUrl ?: "").ifBlank {
                "https://placehold.co/300x300/000000/FFFFFF"
            }

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = prod.nombre,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Image(
                    painter = rememberAsyncImagePainter(imagenSegura),
                    contentDescription = prod.nombre,
                    modifier = Modifier
                        .size(250.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = prod.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Precio: $${prod.precio}",
                    fontSize = 22.sp,
                    color = Color(0xFF39FF14),
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(80.dp))

                Button(
                    onClick = {
                        carritoViewModel.agregarProducto(
                            productoId = prod.id,
                            nombre = prod.nombre,
                            precio = prod.precio,
                            imagenUrl = prod.imagenUrl
                        )

                        scope.launch {
                            snackbarHostState.showSnackbar("Producto agregado")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF39FF14),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(60.dp)
                ) {
                    Text(
                        "Añadir al carrito",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
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
