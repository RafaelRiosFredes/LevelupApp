package com.example.levelup.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import java.math.BigDecimal
import java.math.RoundingMode
import com.example.levelup.R

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductoScreen(
    productosViewModel: ProductosViewModel,
    productId: Int,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val productoFlow = remember(productId) { productosViewModel.obtenerProductoPorId(productId) }
    val producto by productoFlow.collectAsState(initial = null)

    var nombre by remember { mutableStateOf("") }
    var precioText by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }

    LaunchedEffect(producto) {
        producto?.let {
            nombre = it.nombre
            precioText = BigDecimal.valueOf(it.precio).setScale(2, RoundingMode.HALF_UP).toPlainString()
            descripcion = it.descripcion
            imagenUrl = it.imagenUrl
        }
    }

    val numeroRegex = Regex("""^\d+(\.\d+)?$""")
    val isValid = nombre.isNotBlank() && numeroRegex.matches(precioText)

    Scaffold(
        containerColor = JetBlack,
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Editar producto", color = GamerGreen) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding)
        ) {

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre", color = PureWhite) },
                textStyle = TextStyle(color = PureWhite),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = precioText,
                onValueChange = { precioText = it },
                label = { Text("Precio", color = PureWhite) },
                textStyle = TextStyle(color = PureWhite),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = imagenUrl,
                onValueChange = { imagenUrl = it },
                label = { Text("URL de imagen", color = PureWhite) },
                placeholder = { Text("https://...", color = PureWhite.copy(alpha = 0.6f)) },
                textStyle = TextStyle(color = PureWhite),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción", color = PureWhite) },
                textStyle = TextStyle(color = PureWhite),
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))



            // ============================
            // BOTONES DEL CRUD BACKEND
            // ============================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancel) {
                    Text("Cancelar", color = PureWhite)
                }

                Spacer(modifier = Modifier.width(8.dp))

                // ----------- ACTUALIZAR  -------------
                Button(
                    onClick = {
                        val precio = precioText.toDoubleOrNull() ?: 0.0
                        val actualizado = ProductosEntity(
                            id = productId,
                            backendId = producto?.backendId,
                            nombre = nombre.trim(),
                            precio = precio,
                            descripcion = descripcion,
                            imagenUrl = if (imagenUrl.isNotBlank()) imagenUrl.trim()
                            else "https://via.placeholder.com/300x300.png?text=Producto+sin+imagen"
                        )

                        scope.launch {
                            productosViewModel.actualizarProductoBackend(actualizado)
                            productosViewModel.sincronizarProductos()
                            onSaved()
                        }
                    },
                    enabled = isValid,
                    colors = ButtonDefaults.buttonColors(containerColor = GamerGreen, contentColor = JetBlack)
                ) {
                    Text("Guardar cambios")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ----------- ELIMINAR  -------------
            Button(
                onClick = {
                    scope.launch {
                        producto?.let {
                            productosViewModel.eliminarProductoBackend(it)
                            productosViewModel.sincronizarProductos()
                            onSaved()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = PureWhite),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar producto")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ----------- RECARGAR (obtener) -------------
            Button(
                onClick = {
                    producto?.backendId?.let { backendId ->
                        scope.launch {
                            productosViewModel.obtenerProductoBackend(backendId)
                            productosViewModel.sincronizarProductos()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray, contentColor = PureWhite),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Recargar")
            }
        }
    }
}
