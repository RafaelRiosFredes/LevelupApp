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
    currentUserRol: String,        //  admin
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    //   Admin
    LaunchedEffect(Unit) {
        if (currentUserRol != "admin") {
            onCancel()  // vuelve atrás
        }
    }

    val scope = rememberCoroutineScope()
    var nombre by remember { mutableStateOf("") }
    var precioText by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    val numeroRegex = Regex("""^\d+(\.\d+)?$""")
    val isValid = nombre.isNotBlank() && numeroRegex.matches(precioText)

    Scaffold(
        containerColor = JetBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agregar producto", color = GamerGreen) }
            )
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
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = precioText,
                onValueChange = { input ->
                    val clean = input.filter { it.isDigit() || it == '.' }
                    precioText = clean
                },
                label = { Text("Precio", color = PureWhite) },
                textStyle = TextStyle(color = PureWhite),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = imagenUrl,
                onValueChange = { imagenUrl = it },
                label = { Text("Imagen URL", color = PureWhite) },
                placeholder = { Text("https://...", color = PureWhite.copy(alpha = 0.6f)) },
                singleLine = true,
                textStyle = TextStyle(color = PureWhite),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (opcional)", color = PureWhite) },
                textStyle = TextStyle(color = PureWhite),
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                TextButton(onClick = onCancel) {
                    Text("Cancelar", color = PureWhite)
                }

                Spacer(Modifier.width(12.dp))

                Button(
                    onClick = {
                        val precio = precioText.toDoubleOrNull() ?: 0.0

                        val nuevo = ProductosEntity(
                            id = 0,
                            backendId = null,
                            nombre = nombre.trim(),
                            precio = precio,
                            descripcion = descripcion.trim(),
                            imagenUrl = if (imagenUrl.isNotBlank())
                                imagenUrl.trim()
                            else
                                "https://placehold.co/600x400/000000/FFFFFF/png"
                        )

                        scope.launch {
                            productosViewModel.crearProductoBackend(nuevo)
                            productosViewModel.sincronizarProductos()
                            onSaved()
                        }
                    },
                    enabled = isValid,
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
