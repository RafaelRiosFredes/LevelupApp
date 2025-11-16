@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.viewmodel.CarritoViewModel
import com.example.levelup.viewmodel.ProductosViewModel
import kotlinx.coroutines.launch


@Composable
fun ProductosScreen(
    navController: NavHostController,
    productosViewModel: ProductosViewModel,
    carritoViewModel: CarritoViewModel
) {

    DrawerGlobal(navController = navController) {  // ← USO CORRECTO DEL DRAWER

        val productos by productosViewModel.productos.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "LevelUp Gamer",
                            color = GamerGreen,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Black
                    )
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color.Black
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.Black)
            ) {

                Text(
                    text = "Nuestros Productos",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {

                    if (productos.isEmpty()) {

                        item(span = { GridItemSpan(2) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = GamerGreen)
                            }
                        }

                    } else {

                        items(productos) { producto ->

                            val idSeguro = producto.id.takeIf { it > 0 } ?: return@items

                            ProductoItem(
                                producto = producto,
                                onClick = { navController.navigate("producto/$idSeguro") },
                                onAddToCart = {
                                    carritoViewModel.agregarProducto(
                                        productoId = producto.id,
                                        nombre = producto.nombre,
                                        precio = producto.precio,
                                        imagenUrl = producto.imagenUrl
                                    )

                                    scope.launch {
                                        snackbarHostState.showSnackbar("Producto agregado")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ProductoItem(
    producto: ProductosEntity,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {

    val imagenSegura = producto.imagenUrl.ifBlank {
        "https://placehold.co/300x300/000000/FFFFFF"
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(350.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(Modifier.height(10.dp))

            Text(
                text = producto.nombre,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(Modifier.height(10.dp))

            Image(
                painter = rememberAsyncImagePainter(imagenSegura),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(150.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "$${producto.precio}",
                color = GamerGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = onAddToCart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GamerGreen,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Añadir al carrito", fontWeight = FontWeight.Bold)
            }
        }
    }
}
