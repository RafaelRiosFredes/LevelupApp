@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavController
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.*
import com.example.levelup.viewmodel.ProductosViewModel
import kotlinx.coroutines.launch

// ======================================================
//              PANTALLA PRINCIPAL
// ======================================================

@Composable
fun InventarioScreen(
    navController: NavController,
    productosViewModel: ProductosViewModel,
    currentUserRol: String
) {

    // Protección admin
    LaunchedEffect(Unit) {
        if (currentUserRol != "admin") {
            navController.navigate("PantallaPrincipal") {
                popUpTo("PantallaPrincipal") { inclusive = true }
            }
        }
    }

    // Drawer corregido
    DrawerGlobal(navController = navController) {

        InventarioContent(
            productosViewModel = productosViewModel,
            navController = navController
        )
    }
}

// ======================================================
//              CONTENIDO DEL INVENTARIO
// ======================================================

@Composable
private fun InventarioContent(
    productosViewModel: ProductosViewModel,
    navController: NavController
) {

    val productos by productosViewModel.productos.collectAsState(initial = emptyList())
    var selectedProduct by remember { mutableStateOf<ProductosEntity?>(null) }
    var showConfirmDelete by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = JetBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Inventario", color = GamerGreen) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkGray),
                actions = {

                    // Si hay un producto seleccionado → mostrar acciones
                    if (selectedProduct != null) {

                        IconButton(onClick = {
                            navController.navigate("edit_producto/${selectedProduct!!.id}")
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = PureWhite)
                        }

                        IconButton(onClick = { showConfirmDelete = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = PureWhite)
                        }

                    } else {
                        // Ninguno seleccionado → mostrar botón agregar
                        IconButton(onClick = {
                            navController.navigate("add_producto")
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Agregar", tint = GamerGreen)
                        }
                    }
                }
            )
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

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                items(productos) { p ->

                    ProductoRow(
                        producto = p,
                        isSelected = selectedProduct?.id == p.id,
                        onClick = { clicked ->
                            selectedProduct =
                                if (selectedProduct?.id == clicked.id) null else clicked
                        }
                    )
                }
            }
        }
    }

    // ======================================================
    //        CONFIRMACIÓN DE ELIMINACIÓN (GAMER)
    // ======================================================

    if (showConfirmDelete && selectedProduct != null) {

        AlertDialog(
            onDismissRequest = { showConfirmDelete = false },
            title = { Text("Eliminar producto", color = PureWhite) },
            text = { Text("¿Eliminar ${selectedProduct!!.nombre}?", color = PureWhite) },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        productosViewModel.eliminarProducto(selectedProduct!!)
                        productosViewModel.sincronizarProductos()
                    }
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

// ======================================================
//              TARJETA DE PRODUCTO
// ======================================================

@Composable
fun ProductoRow(
    producto: ProductosEntity,
    isSelected: Boolean = false,
    onClick: (ProductosEntity) -> Unit
) {
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
                color = PureWhite
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "ID: ${producto.id}", color = PureWhite)
            Text(text = "Precio: $${producto.precio}", color = PureWhite)

            if (isSelected) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Seleccionado", color = GamerGreen)
            }
        }
    }
}
