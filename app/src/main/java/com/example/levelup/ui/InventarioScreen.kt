package com.example.levelup.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextOverflow
import com.example.levelup.model.data.ProductosEntity
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
    onAgregarClick: () -> Unit,
    onEditarClick: (Int) -> Unit
) {
    val productos by productosViewModel.productos.collectAsState()

    // Estado para seleccionar producto
    var selectedProduct by remember { mutableStateOf<ProductosEntity?>(null) }
    var showConfirmDelete by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = JetBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Inventario", color = GamerGreen) },
                actions = {
                    if (selectedProduct != null) {
                        // EDIT
                        IconButton(onClick = { onEditarClick(selectedProduct!!.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar producto", tint = PureWhite)
                        }
                        // DELETE
                        IconButton(onClick = { showConfirmDelete = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar producto", tint = PureWhite)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
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
                .padding(padding)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(productos) { p ->
                ProductoRow(
                    producto = p,
                    isSelected = selectedProduct?.id == p.id,
                    onClick = { clicked ->
                        // seleccionar / deseleccionar
                        selectedProduct = if (selectedProduct?.id == clicked.id) null else clicked
                    }
                )
            }
        }

        // Dialogo de confirmación para eliminar
        if (showConfirmDelete && selectedProduct != null) {
            AlertDialog(
                onDismissRequest = { showConfirmDelete = false },
                title = { Text("Eliminar producto", color = PureWhite) },
                text = {
                    Text(
                        "¿Estás seguro que quieres eliminar \"${selectedProduct!!.nombre}\" (id ${selectedProduct!!.id})? Esta acción no tiene deshacer.",
                        color = PureWhite
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        // Llamar al ViewModel para eliminar
                        productosViewModel.eliminarProducto(selectedProduct!!)
                        // limpiar estados
                        showConfirmDelete = false
                        selectedProduct = null
                    }) {
                        Text("Eliminar", color = GamerGreen)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDelete = false }) {
                        Text("Cancelar", color = PureWhite)
                    }
                },
                containerColor = DarkGray
            )
        }
    }
}

@Composable
fun ProductoRow(
    producto: ProductosEntity,
    isSelected: Boolean = false,
    onClick: (ProductosEntity) -> Unit
) {
    val formatter = remember {
        NumberFormat.getNumberInstance(Locale.getDefault()).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
            isGroupingUsed = true
        }
    }
    val precioFormateado = formatter.format(producto.precio)

    val shape = RoundedCornerShape(8.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .then(
                if (isSelected) Modifier.border(width = 2.dp, color = GamerGreen, shape = shape)
                else Modifier
            )
            .clickable { onClick(producto) },
        colors = CardDefaults.cardColors(containerColor = DarkGray),
        shape = shape
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleMedium,
                color = PureWhite,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Id: ${producto.id}", style = MaterialTheme.typography.bodyMedium, color = PureWhite)
            Text(text = "Precio: $precioFormateado", style = MaterialTheme.typography.bodyMedium, color = PureWhite)
            if (isSelected) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Seleccionado", style = MaterialTheme.typography.labelSmall, color = GamerGreen)
            }
        }
    }
}
