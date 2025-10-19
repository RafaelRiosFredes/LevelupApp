package com.example.levelup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.ProductosViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductoScreen(
    productosViewModel: ProductosViewModel,
    productId: Int,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val scope = rememberCoroutineScope()

    // Obtener el producto desde el ViewModel (Flow)
    val productoFlow = remember(productId) { productosViewModel.productoPorId(productId) }
    val producto by productoFlow.collectAsState(initial = null)

    // Campos locales para edición
    var nombre by remember { mutableStateOf("") }
    var precioText by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }

    // Cuando cargue el producto, inicializar campos (solo una vez por carga)
    LaunchedEffect(producto) {
        producto?.let {
            nombre = it.nombre
            precioText = BigDecimal.valueOf(it.precio).setScale(2,java.math.RoundingMode.HALF_UP).toPlainString()
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
                placeholder = { Text("Ej: Teclado mecánico", color = PureWhite.copy(alpha = 0.6f)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = PureWhite),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = precioText,
                onValueChange = { raw ->
                    var puntoVisto = false
                    val filtered = StringBuilder()
                    for (ch in raw) {
                        if (ch == '.') {
                            if (!puntoVisto) {
                                if (filtered.isEmpty()) filtered.append('0')
                                filtered.append('.')
                                puntoVisto = true
                            }
                        } else if (ch.isDigit()) {
                            filtered.append(ch)
                        }
                    }
                    precioText = filtered.toString()
                },
                label = { Text("Precio (ej: 9.99)", color = PureWhite) },
                placeholder = { Text("9.99", color = PureWhite.copy(alpha = 0.6f)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = PureWhite),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = imagenUrl,
                onValueChange = { imagenUrl = it },
                label = { Text("URL imagen (opcional)", color = PureWhite) },
                placeholder = { Text("https://...", color = PureWhite.copy(alpha = 0.6f)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = PureWhite),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onCancel) {
                    Text("Cancelar", color = PureWhite)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        val precio = precioText.toDoubleOrNull() ?: 0.0
                        val actualizado = ProductosEntity(
                            id = productId,
                            nombre = nombre.trim(),
                            precio = precio,
                            imagenUrl = imagenUrl.trim()
                        )
                        scope.launch {
                            productosViewModel.actualizarProducto(actualizado)
                            onSaved()
                        }
                    },
                    enabled = isValid,
                    colors = ButtonDefaults.buttonColors(containerColor = GamerGreen, contentColor = JetBlack)
                ) {
                    Text("Guardar cambios")
                }
            }
        }
    }
}
