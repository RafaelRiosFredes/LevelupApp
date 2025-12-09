@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.levelup.core.UserSession
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.viewmodel.ProductosViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@Composable
fun AddProductScreen(
    navController: NavHostController,
    productosViewModel: ProductosViewModel
) {
    // 🔒 Seguridad: Solo admin
    LaunchedEffect(Unit) {
        if (UserSession.rol != "admin") {
            navController.navigate("PantallaPrincipal") {
                popUpTo("PantallaPrincipal") { inclusive = true }
            }
        }
        // Recargar categorías al entrar
        productosViewModel.cargarCategorias()
    }

    // Obtenemos las categorías del ViewModel
    val listaCategorias by productosViewModel.categorias.collectAsState()

    DrawerGlobal(navController = navController) {

        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val snackbar = remember { SnackbarHostState() }

        // --- ESTADOS DEL FORMULARIO ---
        var nombre by remember { mutableStateOf("") }
        var descripcion by remember { mutableStateOf("") }
        var precio by remember { mutableStateOf("") }
        var stock by remember { mutableStateOf("") }

        // Categoría seleccionada (guardamos el objeto completo o ID y nombre por separado)
        var categoriaSeleccionadaId by remember { mutableStateOf<Long?>(null) }
        var categoriaSeleccionadaNombre by remember { mutableStateOf("") }
        var expandirMenu by remember { mutableStateOf(false) }

        // --- ESTADOS PARA LA FOTO ---
        var fotoBitmap by remember { mutableStateOf<Bitmap?>(null) }
        var fotoBase64 by remember { mutableStateOf<String?>(null) }

        // 1. LAUNCHER DE CÁMARA (Con redimensionamiento para evitar error 500)
        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { originalBitmap ->
            if (originalBitmap != null) {
                // Redimensionar
                val maxWidth = 1024
                val maxHeight = 1024
                val width = originalBitmap.width
                val height = originalBitmap.height
                val ratioBitmap = width.toFloat() / height.toFloat()
                val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
                var finalWidth = maxWidth
                var finalHeight = maxHeight
                if (ratioMax > ratioBitmap) {
                    finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
                } else {
                    finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
                }
                val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, finalWidth, finalHeight, true)

                fotoBitmap = resizedBitmap

                val stream = ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                val byteArray = stream.toByteArray()
                fotoBase64 = Base64.encodeToString(byteArray, Base64.NO_WRAP)
            }
        }

        // 2. LAUNCHER DE PERMISO
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) cameraLauncher.launch(null)
            else scope.launch { snackbar.showSnackbar("Se necesita permiso de cámara") }
        }

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
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // --- FOTO ---
                Text("Toca para tomar foto", color = Color.Gray)
                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .border(2.dp, GamerGreen, MaterialTheme.shapes.medium)
                        .clickable {
                            val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                cameraLauncher.launch(null)
                            } else {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (fotoBitmap != null) {
                        Image(
                            bitmap = fotoBitmap!!.asImageBitmap(),
                            contentDescription = "Foto",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = GamerGreen, modifier = Modifier.size(40.dp))
                    }
                }

                Spacer(Modifier.height(25.dp))

                // --- CAMPOS ---
                CampoTexto("Nombre", nombre) { nombre = it }
                CampoTexto("Descripción", descripcion) { descripcion = it }
                CampoTexto("Precio", precio, KeyboardType.Number) { precio = it }
                CampoTexto("Stock", stock, KeyboardType.Number) { stock = it }

                // --- DROPDOWN DE CATEGORÍA ---
                Spacer(Modifier.height(12.dp))
                ExposedDropdownMenuBox(
                    expanded = expandirMenu,
                    onExpandedChange = { expandirMenu = !expandirMenu }
                ) {
                    OutlinedTextField(
                        value = categoriaSeleccionadaNombre.ifEmpty { "Selecciona Categoría" },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría", color = Color.White) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirMenu) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GamerGreen,
                            unfocusedBorderColor = Color.DarkGray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = GamerGreen
                        ),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandirMenu,
                        onDismissRequest = { expandirMenu = false }
                    ) {
                        if (listaCategorias.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Cargando o sin datos...") },
                                onClick = { expandirMenu = false }
                            )
                        } else {
                            listaCategorias.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat.nombreCategoria) },
                                    onClick = {
                                        categoriaSeleccionadaId = cat.idCategoria
                                        categoriaSeleccionadaNombre = cat.nombreCategoria
                                        expandirMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(25.dp))

                // --- BOTONES ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Cancelar", color = Color.Gray)
                    }

                    Button(
                        onClick = {
                            if (nombre.isBlank() || precio.isBlank() || categoriaSeleccionadaId == null) {
                                scope.launch { snackbar.showSnackbar("Falta nombre, precio o categoría") }
                                return@Button
                            }

                            val nuevoProducto = ProductosEntity(
                                id = 0L,
                                backendId = null,
                                nombre = nombre.trim(),
                                descripcion = descripcion.trim(),
                                precio = precio.toLongOrNull() ?: 0L,
                                stock = stock.toIntOrNull() ?: 0,
                                imagenUrl = null,
                                categoriaId = categoriaSeleccionadaId!!, // Usamos el ID seleccionado del menú
                                categoriaNombre = categoriaSeleccionadaNombre
                            )

                            productosViewModel.crearProductoConImagen(nuevoProducto, fotoBase64) { exito ->
                                scope.launch {
                                    if (exito) {
                                        Toast.makeText(context, "Producto creado", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    } else {
                                        snackbar.showSnackbar("Error al crear. Revisa la conexión.")
                                    }
                                }
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