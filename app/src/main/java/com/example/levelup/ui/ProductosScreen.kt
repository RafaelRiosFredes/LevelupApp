package com.example.levelup.ui

import android.widget.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.room.parser.Section
import com.example.levelup.model.local.ProductosEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    productos: List<ProductosEntity>,
    onAddToCart: (ProductosEntity) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Productos") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(productos) { producto ->
                ListItem(
                    headlineContent = {
                        Text(producto.descripcion, fontWeight = FontWeight.Bold)
                    },
                    supportingContent = {
                        Text("Categoría: ${producto.categoria}")
                    },
                    trailingContent = {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(String.format("$%.2f", producto.monto))
                            Spacer(modifier = Modifier.height(4.dp))
                            Button(
                                onClick = { onAddToCart(producto) },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("Añadir", fontSize = 12.sp)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Divider()
            }
        }
    }
}