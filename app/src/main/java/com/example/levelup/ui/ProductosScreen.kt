package com.example.levelup.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.levelup.model.local.ProductosEntity


@Composable
fun ProductosScreen(viewModel: com.example.levelup.viewmodel.ProductosViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val productos by viewModel.productos.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "🛒 Nuestros Productos", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(productos) { producto->
                ProductoItem(producto) {
                    viewModel.agregarAlCarrito(producto)
                }
            }
        }
    }
}

@Composable
fun ProductoItem(producto: ProductosEntity, onAddToCart: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(250.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            Image(
                painter = rememberAsyncImagePainter(producto.imagenUrl),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .height(130.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(producto.nombre, fontWeight = FontWeight.Bold)
            Text("$${producto.precio}")
            Button(onClick = onAddToCart) {
                Text("Añadir al carrito")
            }
        }
    }
}