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
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.model.local.CategoriaEntity

@Composable
fun InventarioAdaptiveScreen(
    productos: List<CategoriaEntity>,
    onAddClick: () -> Unit,
    onEditClick: (CategoriaEntity) -> Unit,
    onDeleteClick: (CategoriaEntity) -> Unit
) {
    val windowInfo = currentWindowAdaptiveInfo()
    val widthClass = windowInfo.windowSizeClass.windowWidthSizeClass

    // Decide layout según ancho
    if (widthClass == WindowWidthSizeClass.EXPANDED) {
        // En pantalla amplia, mostrar tabla estilo web + detalle u opciones
        InventarioExpandedLayout(
            productos = productos,
            onAddClick = onAddClick,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick
        )
    } else {
        // Para teléfonos / pantallas normales: usar diseño de cards (lista) mejorado
        InventarioCompactLayout(
            productos = productos,
            onAddClick = onAddClick,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick
        )
    }
}

@Composable
fun InventarioCompactLayout(
    productos: List<CategoriaEntity>,
    onAddClick: () -> Unit,
    onEditClick: (CategoriaEntity) -> Unit,
    onDeleteClick: (CategoriaEntity) -> Unit
) {
    TODO("Not yet implemented")
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

@Composable
fun InventarioExpandedLayout(
    productos: List<CategoriaEntity>,
    onAddClick: () -> Unit,
    onEditClick: (CategoriaEntity) -> Unit,
    onDeleteClick: (CategoriaEntity) -> Unit
) {
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
        ) {
            // Filtros en una fila
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dropdown categoría (puedes implementar con ExposedDropdownMenu adaptativo)
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = {}
                ) {
                    OutlinedTextField(
                        value = "Todos los productos",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría", color = Color.White) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF39FF14),
                            unfocusedBorderColor = Color(0xFF39FF14),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }

                OutlinedTextField(
                    value = "",
                    onValueChange = { /*...*/ },
                    label = { Text("Buscar producto", color = Color.White) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF39FF14),
                        unfocusedBorderColor = Color(0xFF39FF14),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }

            // Encabezado tipo tabla
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("ID", color = Color.White, modifier = Modifier.weight(0.5f))
                Text("Nombre", color = Color.White, modifier = Modifier.weight(2f))
                Text("Categoría", color = Color.White, modifier = Modifier.weight(1.5f))
                Text("Stock", color = Color.White, modifier = Modifier.weight(1f))
                Text("Precio", color = Color.White, modifier = Modifier.weight(1.5f))
                Text("Estado", color = Color.White, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(60.dp)) // espacio para acciones
            }

            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(horizontal = 12.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                items(productos) { producto ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${producto.id}", color = Color.White, modifier = Modifier.weight(0.5f))
                        Text(producto.nombre, color = Color.White, modifier = Modifier.weight(2f))
                        Text(producto.icon_url, color = Color.Gray, modifier = Modifier.weight(1.5f))
                        Text("10", color = Color.LightGray, modifier = Modifier.weight(1f)) // ejemplo stock
                        Text("$100.000", color = Color.White, modifier = Modifier.weight(1.5f))
                        // Estado con chip
                        EstadoChipMini(/*estado aquí*/, modifier = Modifier.weight(1f))
                        // Acciones
                        Row {
                            IconButton(onClick = { onEditClick(producto) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF00AAFF))
                            }
                            IconButton(onClick = { onDeleteClick(producto) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                            }
                        }
                    }
                    Divider(color = Color.DarkGray, thickness = 0.5.dp)
                }
            }
        }
    }
}

@Composable
fun EstadoChipMini(estadoTexto: String, estadoColor: Color, modifier: Modifier = Modifier) {
    // similar al chip anterior, pero versión compacta
    Surface(
        color = estadoColor,
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            estadoTexto,
            color = Color.Black,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
