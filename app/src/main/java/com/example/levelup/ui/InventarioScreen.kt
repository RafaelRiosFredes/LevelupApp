@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.levelup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.model.local.CategoriaEntity

@Composable
fun InventarioScreen(
    productos: List<CategoriaEntity>,
    onAddClick: () -> Unit,
    onEditClick: (CategoriaEntity) -> Unit,
    onDeleteClick: (CategoriaEntity) -> Unit
) {
    var filtro by remember { mutableStateOf("Todos los productos") }
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventario", color = Color(0xFF39FF14), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
                actions = {
                    IconButton(onClick = onAddClick) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White)
                    }
                }
            )
        },
        containerColor = Color.Black
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .background(Color.Black)
                .padding(12.dp)
        ) {
            // Filtros
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = {}
                ) {
                    OutlinedTextField(
                        value = filtro,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría", color = Color.White) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF39FF14),
                            unfocusedBorderColor = Color(0xFF39FF14),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Buscar producto", color = Color.White) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF39FF14),
                        unfocusedBorderColor = Color(0xFF39FF14),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Lista
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(productos) { producto ->
                    InventarioItem(
                        producto = producto,
                        onEditClick = { onEditClick(producto) },
                        onDeleteClick = { onDeleteClick(producto) }
                    )
                }
            }
        }
    }
}

@Composable
fun InventarioItem(
    producto: CategoriaEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, color = Color.White, fontWeight = FontWeight.Bold)
                Text("Categoría: ${producto.icon_url}", color = Color.Gray, fontSize = 12.sp)
                Text("Stock: ${listOf(15, 5, 0).random()}", color = Color.LightGray, fontSize = 12.sp)
                Text("Precio: $${listOf("29.990", "59.990", "549.990").random()}", color = Color.White)
                EstadoChip(producto)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF00AAFF))
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
fun EstadoChip(producto: CategoriaEntity) {
    val estado = when (listOf(15, 5, 0).random()) {
        0 -> "Agotado" to Color.Red
        in 1..5 -> "Stock Bajo" to Color.Yellow
        else -> "Disponible" to Color(0xFF39FF14)
    }
    Surface(
        color = estado.second,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            estado.first,
            color = Color.Black,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}
