@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelup.core.UserSession
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.ProductosViewModel
import kotlinx.coroutines.launch

// =========================================================
//             PANTALLA PRINCIPAL (CON DRAWER)
// =========================================================

@Composable
fun EditProductoScreen(
    navController: NavController,
    productosViewModel: ProductosViewModel,
    currentUserRol: String,
    productId: Long,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {

    // Permiso admin
    LaunchedEffect(Unit) {
        if (currentUserRol != "admin") {
            navController.navigate("PantallaPrincipal") {
                popUpTo("PantallaPrincipal") { inclusive = true }
            }
        }
    }

    DrawerGlobal(navController = navController) {

        EditProductoContent(
            productosViewModel = productosViewModel,
            productId = productId,
            onSaved = onSaved,
            onCancel = onCancel
        )
    }
}

// =========================================================
//                 CONTENIDO EDITABLE
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProductoContent(
    productosViewModel: ProductosViewModel,
    productId: Long,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {

    val productoFlow = remember(productId) {
        productosViewModel.obtenerProductoPorId(productId)
    }

    val producto by productoFlow.collectAsState(initial = null)

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // Cuando cargue el producto, rellenar campos
    LaunchedEffect(producto) {
        producto?.let {
            nombre = it.nombre
            precio = it.precio.toString()
            imagenUrl = it.imagenUrl
            descripcion = it.descripcion
        }
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = JetBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Editar producto", color = GamerGreen) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = JetBlack
                )
            )
        }
    ) { padding ->

        if (producto == null) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GamerGreen)
            }
            return@Scaffold
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {

            // ========= NOMBRE =========
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre", color = PureWhite) },
                textStyle = TextStyle(color = PureWhite),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GamerGreen,
                    unfocusedBorderColor = Color.DarkGray,
                    cursorColor = GamerGreen,
                    focusedTextColor = PureWhite,
                    unfocusedTextColor = PureWhite
                )
            )

            Spacer(Modifier.height(12.dp))

            // ========= PRECIO =========
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Precio", color = PureWhite) },
                textStyle = TextStyle(color = PureWhite),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GamerGreen,
                    unfocusedBorderColor = Color.DarkGray,
                    cursorColor = GamerGreen,
                    focusedTextColor = PureWhite,
                    unfocusedTextColor = PureWhite
                )
            )

            Spacer(Modifier.height(12.dp))

            // ========= URL IMAGEN =========
            OutlinedTextField(
                value = imagenUrl,
                onValueChange = { imagenUrl = it },
                label = { Text("URL de imagen", color = PureWhite) },
                textStyle = TextStyle(color = PureWhite),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GamerGreen,
                    unfocusedBorderColor = Color.DarkGray,
                    cursorColor = GamerGreen,
                    focusedTextColor = PureWhite,
                    unfocusedTextColor = PureWhite
                )
            )

            Spacer(Modifier.height(12.dp))

            // ========= DESCRIPCIÓN =========
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción", color = PureWhite) },
                textStyle = TextStyle(color = PureWhite),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GamerGreen,
                    unfocusedBorderColor = Color.DarkGray,
                    cursorColor = GamerGreen,
                    focusedTextColor = PureWhite,
                    unfocusedTextColor = PureWhite
                )
            )

            Spacer(Modifier.height(20.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancel) {
                    Text("Cancelar", color = PureWhite)
                }

                Spacer(Modifier.width(12.dp))

                Button(
                    onClick = {
                        val actualizado = ProductosEntity(
                            id = productId,
                            backendId = producto!!.backendId,
                            nombre = nombre,
                            precio = precio.toDoubleOrNull() ?: 0.0,
                            descripcion = descripcion,
                            imagenUrl = imagenUrl
                        )

                        scope.launch {
                            productosViewModel.actualizarProductoBackend(actualizado)
                            productosViewModel.sincronizarProductos()
                            onSaved()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GamerGreen,
                        contentColor = JetBlack
                    )
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}
