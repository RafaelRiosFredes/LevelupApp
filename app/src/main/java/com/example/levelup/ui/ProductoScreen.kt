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
import com.example.levelup.model.local.ProductosEntity
import com.example.levelup.model.local.AppDatabase
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.levelup.model.repository.ProductosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.levelup.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

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
                // 🖼 Imagen principal
                Image(
                    painter = painterResource(id = prod.imagenRes),
                    contentDescription = prod.nombre,
                    modifier = Modifier
                        .size(260.dp)
                        .padding(top = 10.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 🏷 Nombre
                Text(
                    text = prod.nombre,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 📜 Descripción
                Text(
                    text = prod.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                // 💸 Precio centrado visualmente
                Text(
                    text = "Precio: $${prod.precio}",
                    fontSize = 22.sp,
                    color = Color(0xFF39FF14),
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(80.dp)) // ✅ Baja el botón visualmente

                // 🛒 Botón gamer grande y ovalado
                Button(
                    onClick = { /* TODO: Agregar al carrito */ },
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
