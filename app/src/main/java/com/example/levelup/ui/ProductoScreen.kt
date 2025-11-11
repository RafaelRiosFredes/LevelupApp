package com.example.levelup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.model.data.AppDatabase
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.levelup.model.repository.ProductosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.room.util.TableInfo
import com.example.levelup.R
import com.example.levelup.viewmodel.CarritoViewModel
import com.example.levelup.viewmodel.ProductosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen(
    productosViewModel: ProductosViewModel,
    carritoViewModel: CarritoViewModel,
    id: Int,
    onNavigateBack: () -> Unit
) {

    val productoFlow = remember(id) { productosViewModel.obtenerProductoPorId(id) }
    val producto by productoFlow.collectAsState(initial = null)

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
                    IconButton(onClick = onNavigateBack) {
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
        containerColor = Color.Black
    ) { padding ->
        producto?.let { prod ->
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

                Spacer(modifier = Modifier.height(10.dp))

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
                            imagenRes = R.drawable.ic_launcher_foreground // luego lo haremos dinámico
                        )
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

