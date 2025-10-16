package com.example.levelup.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levelup.model.local.ProductosEntity
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.ui.theme.DarkGray
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioScreen(
    productosViewModel: ProductosViewModel,
    onAgregarClick: () -> Unit
) {
    val productos by productosViewModel.productos.collectAsState()

    Scaffold(
        // fondo de toda la pantalla
        containerColor = JetBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Inventario", color = GamerGreen) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    // barra casi opaca para verse separada pero sin tapar todo
                    containerColor = DarkGray.copy(alpha = 0.95f)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregarClick,
                containerColor = GamerGreen,
                contentColor = JetBlack
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->

        // aplica el padding que entrega Scaffold para evitar la superposición con el TopAppBar
        if (productos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay productos. Pulsa + para agregar uno.", color = PureWhite)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                // *IMPORTANTE*: aquí sí aplicamos el padding del Scaffold
                .padding(padding)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(productos) { p ->
                ProductoRow(producto = p)
            }
        }
    }
}

@Composable
fun ProductoRow(producto: ProductosEntity) {
    // Formatter que usa la configuración regional del dispositivo, con separador de miles y 2 decimales
    val formatter = remember {
        NumberFormat.getNumberInstance(Locale.getDefault()).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
            isGroupingUsed = true
        }
    }
    val precioFormateado = formatter.format(producto.precio)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGray)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium, color = PureWhite)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Id: ${producto.id}", style = MaterialTheme.typography.bodyMedium, color = PureWhite)
            Text(text = "Precio: $precioFormateado", style = MaterialTheme.typography.bodyMedium, color = PureWhite)
        }
    }
}
