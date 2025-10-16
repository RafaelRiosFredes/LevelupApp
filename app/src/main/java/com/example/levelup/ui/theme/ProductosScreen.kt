package com.example.levelup.ui.producto

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.levelup.local.AppDatabase
import com.example.levelup.repository.ProductosRepository
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.ProductosViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    onNavigateBack: () -> Unit = {},
    nav: (String) -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val factory = ProductosViewModelFactory(application)
    val viewModel: ProductosViewModel = viewModel(factory = factory)

    val productos by viewModel.productos.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LEVEL-UP GAMER") },
                navigationIcon = {
                    IconButton(onClick = { /* abrir drawer */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color(0xFF39FF14))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Black
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(productos) { producto ->
                ProductoItem(producto) {
                    nav("producto/${producto.id}")
                }
            }
        }
    }
}

@Composable
fun ProductoItem(producto: com.example.levelup.local.ProductosEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(250.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_gallery),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .height(130.dp)
                    .fillMaxWidth()
            )
            Text(producto.nombre, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text("$${producto.precio}")
            Button(onClick = onClick) {
                Text("Añadir al carrito")
            }
        }
    }
}
