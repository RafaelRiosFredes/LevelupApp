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
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.viewmodel.ProductosViewModel
import kotlinx.coroutines.launch

@Composable
fun AddProductScreen(
    productosViewModel: ProductosViewModel,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    navController: NavHostController,
    currentUserRol: String
) {

    DrawerGlobal() {

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
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = JetBlack)
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

                // BOTONES
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {

                    TextButton(onClick = onCancel) {
                        Text("Cancelar", color = Color.Gray)
                    }

                    Button(
                        onClick = {
                            if (nombre.isBlank() || precio.isBlank()) {
                                scope.launch { snackbar.showSnackbar("Faltan campos obligatorios") }
                                return@Button
                            }

                            val p = ProductosEntity(
                                id = 0,
                                nombre = nombre.trim(),
                                descripcion = descripcion.trim(),
                                precio = precio.toDoubleOrNull() ?: 0.0,
                                imagenUrl = imagenUrl.trim()
                            )

                            productosViewModel.insertarProducto(p)
                            onSaved()
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
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GamerGreen,
            unfocusedBorderColor = Color.Gray,
            focusedTextColor = Color.White,
            cursorColor = GamerGreen
        ),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = tipo)
    )
}
