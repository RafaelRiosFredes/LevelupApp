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
import coil.compose.rememberAsyncImagePainter
import com.example.levelup.model.local.ProductosEntity
import com.example.levelup.model.local.AppDatabase
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

import com.example.levelup.model.repository.ProductosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.levelup.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen(
    id: Int,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val repo = ProductosRepository(db.productosDao())

    var producto by remember { mutableStateOf<ProductosEntity?>(null) }

    // 🔹 Cargar producto desde la base de datos
    LaunchedEffect(id) {
        withContext(Dispatchers.IO) {
            producto = repo.obtenerProductoPorId(id)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle del producto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        producto?.let { prod ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = prod.imagenRes), //poner nombre de imagen despues del punto
                    contentDescription = "producto 1",
                    modifier = Modifier
                        .size(250.dp)
                        .padding(top = 5.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = prod.nombre,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = prod.descripcion,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Precio: $${prod.precio}",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = { /* TODO: Agregar al carrito */ }) {
                    Text("Añadir al carrito")
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
