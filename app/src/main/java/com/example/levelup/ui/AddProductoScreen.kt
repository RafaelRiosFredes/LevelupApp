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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    productosViewModel: ProductosViewModel,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var nombre by remember { mutableStateOf("") }
    var precioText by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // Regex que acepta números enteros o con un solo punto decimal
    val numeroRegex = Regex("""^\d+(\.\d+)?$""")

    // isValid exige nombre y precio con formato numérico válido
    val isValid = nombre.isNotBlank() && numeroRegex.matches(precioText)

    Scaffold(
        containerColor = JetBlack,
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Agregar producto", color = GamerGreen) })
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

            // --- Campo Precio: filtra la entrada para aceptar solo dígitos y un punto máximo ---
            OutlinedTextField(
                value = precioText,
                onValueChange = { raw ->
                    // Filtrado: sólo permite dígitos y un único punto decimal.
                    var puntoVisto = false
                    val filtered = StringBuilder()
                    for (ch in raw) {
                        if (ch == '.') {
                            if (!puntoVisto) {
                                // evitar punto al inicio
                                if (filtered.isEmpty()) {
                                    // si el primer carácter es '.', preasignamos '0' para que quede "0."
                                    filtered.append('0')
                                }
                                filtered.append('.')
                                puntoVisto = true
                            }
                        } else if (ch.isDigit()) {
                            filtered.append(ch)
                        }
                        // cualquier otro carácter se ignora (letras, 'f', comas, etc.)
                    }
                    precioText = filtered.toString()
                },
                label = { Text("Precio (ej: 9.99)", color = PureWhite) },
                placeholder = { Text("9.99", color = PureWhite.copy(alpha = 0.6f)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = PureWhite),
                singleLine = true
            )
            // -------------------------------------------------------------------------------

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
                        // aquí parseamos sabiendo que el formato fue controlado por el filtro
                        val precio = precioText.toDoubleOrNull() ?: 0.0
                        val nuevo = ProductosEntity(
                            id = 0,
                            nombre = nombre.trim(),
                            precio = precio,
                            imagenUrl = imagenUrl.trim(),
                            descripcion =descripcion,
                        )
                        scope.launch {
                            productosViewModel.insertarProducto(nuevo)
                            onSaved()
                        }
                    },
                    enabled = isValid,
                    colors = ButtonDefaults.buttonColors(containerColor = GamerGreen, contentColor = JetBlack)
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}
