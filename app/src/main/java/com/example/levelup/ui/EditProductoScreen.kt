package com.example.levelup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.ProductosViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductoScreen(
    productosViewModel: ProductosViewModel,
    currentUserRol: String,
    productId: Int,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    // Seguridad
    LaunchedEffect(Unit) {
        if (currentUserRol != "admin") onCancel()
    }

    val productoFlow = remember(productId) {
        productosViewModel.obtenerProductoPorId(productId)
    }
    val producto by productoFlow.collectAsState(initial = null)

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

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
                title = { Text("Editar producto", color = GamerGreen) }
            )
        }
    ) { padding ->

        if (producto == null) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = GamerGreen)
            }
            return@Scaffold
        }

        Column(
            Modifier.fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre", color = PureWhite) },
                textStyle = TextStyle(color = PureWhite),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Precio", color = PureWhite) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = PureWhite)
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = imagenUrl,
                onValueChange = { imagenUrl = it },
                label = { Text("URL Imagen", color = PureWhite) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = PureWhite)
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción", color = PureWhite) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = PureWhite)
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
