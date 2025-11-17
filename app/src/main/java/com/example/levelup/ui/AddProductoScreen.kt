@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.levelup.core.UserSession
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.viewmodel.ProductosViewModel
import kotlinx.coroutines.launch

@Composable
fun AddProductScreen(
    navController: NavHostController,
    productosViewModel: ProductosViewModel
) {

    // 🔐 Solo admin
    LaunchedEffect(Unit) {
        if (UserSession.rol != "admin") {
            navController.navigate("PantallaPrincipal") {
                popUpTo("PantallaPrincipal") { inclusive = true }
            }
        }
    }

    DrawerGlobal(navController = navController) {

        var nombre by remember { mutableStateOf("") }
        var descripcion by remember { mutableStateOf("") }
        var precio by remember { mutableStateOf("") }
        var stock by remember { mutableStateOf("") }
        var categoriaId by remember { mutableStateOf("") }
        var categoriaNombre by remember { mutableStateOf("") }
        var imagenUrl by remember { mutableStateOf("") }

        val scope = rememberCoroutineScope()
        val snackbar = remember { SnackbarHostState() }

        Scaffold(
            containerColor = JetBlack,
            snackbarHost = { SnackbarHost(snackbar) },
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Agregar Producto", color = GamerGreen) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = JetBlack
                    )
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CampoTexto("Nombre", nombre) { nombre = it }
                CampoTexto("Descripción", descripcion) { descripcion = it }
                CampoTexto("Precio", precio, KeyboardType.Number) { precio = it }
                CampoTexto("Stock", stock, KeyboardType.Number) { stock = it }
                CampoTexto("Categoría ID", categoriaId, KeyboardType.Number) { categoriaId = it }
                CampoTexto("Nombre Categoría", categoriaNombre) { categoriaNombre = it }
                CampoTexto("Imagen URL", imagenUrl) { imagenUrl = it }

                Spacer(Modifier.height(25.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Cancelar", color = Color.Gray)
                    }

                    Button(
                        onClick = {
                            if (nombre.isBlank() || precio.isBlank() || stock.isBlank()
                                || categoriaId.isBlank() || categoriaNombre.isBlank()
                            ) {
                                scope.launch { snackbar.showSnackbar("Faltan campos obligatorios") }
                                return@Button
                            }

                            val nuevoProducto = ProductosEntity(
                                id = 0L, // ROOM asigna ID local automático
                                backendId = null,
                                nombre = nombre.trim(),
                                descripcion = descripcion.trim(),
                                precio = precio.toLongOrNull() ?: 0L,
                                stock = stock.toIntOrNull() ?: 0,
                                imagenUrl = imagenUrl.trim().ifBlank { null },
                                categoriaId = categoriaId.toLongOrNull() ?: 0L,
                                categoriaNombre = categoriaNombre.trim()
                            )

                            scope.launch {
                                productosViewModel.insertarProducto(nuevoProducto)
                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GamerGreen)
                    ) {
                        Text("Guardar", color = Color.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun CampoTexto(
    titulo: String,
    valor: String,
    tipo: KeyboardType = KeyboardType.Text,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(titulo, color = Color.White) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = tipo),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GamerGreen,
            unfocusedBorderColor = Color.DarkGray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = GamerGreen
        )
    )

    Spacer(Modifier.height(12.dp))
}
