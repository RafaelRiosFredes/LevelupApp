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

    // 🔐 Protección: solo admin puede entrar
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
        var imagenUrl by remember { mutableStateOf("") }

        val scope = rememberCoroutineScope()
        val snackbar = remember { SnackbarHostState() }

        Scaffold(
            containerColor = JetBlack,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Agregar Producto", color = GamerGreen) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = JetBlack
                    )
                )
            },
            snackbarHost = { SnackbarHost(snackbar) }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CampoTexto("Nombre", nombre) { nombre = it }
                CampoTexto("Descripción", descripcion) { descripcion = it }
                CampoTexto("Precio", precio, KeyboardType.Number) { precio = it }
                CampoTexto("Imagen URL", imagenUrl) { imagenUrl = it }

                Spacer(Modifier.height(25.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Cancelar", color = Color.Gray)
                    }

                    Button(
                        onClick = {
                            if (nombre.isBlank() || precio.isBlank()) {
                                scope.launch { snackbar.showSnackbar("Faltan campos obligatorios") }
                                return@Button
                            }

                            val nuevoProducto = ProductosEntity(
                                id = 0,
                                nombre = nombre.trim(),
                                descripcion = descripcion.trim(),
                                precio = precio.toDoubleOrNull() ?: 0.0,
                                imagenUrl = imagenUrl.trim()
                            )

                            productosViewModel.insertarProducto(nuevoProducto)
                            navController.popBackStack()
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

// ========================================================
//                 ✨ CAMPO TEXTO REUTILIZABLE
// ========================================================
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
        label = { Text(titulo) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
            keyboardType = tipo
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF39FF14),
            unfocusedBorderColor = Color.DarkGray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color(0xFF39FF14)
        )
    )
}
