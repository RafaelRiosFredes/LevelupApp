@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
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
import coil.compose.rememberAsyncImagePainter
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

    // Bloqueo por rol
    LaunchedEffect(Unit) {
        if (UserSession.rol != "admin") {
            navController.navigate("PantallaPrincipal") {
                popUpTo("PantallaPrincipal") { inclusive = true }
            }
        }
        productosViewModel.cargarCategorias()
    }

    val listaCategorias by productosViewModel.categorias.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    DrawerGlobal(navController = navController) {

        // ======================================================
        // 🔄 FORM STATES
        // ======================================================
        var nombre by remember { mutableStateOf("") }
        var descripcion by remember { mutableStateOf("") }
        var precio by remember { mutableStateOf("") }
        var stock by remember { mutableStateOf("") }

        var categoriaSeleccionadaId by remember { mutableStateOf<Long?>(null) }
        var categoriaSeleccionadaNombre by remember { mutableStateOf("") }
        var expandirMenu by remember { mutableStateOf(false) }


        // ======================================================
        // 📌 FOTO: CÁMARA + GALERÍA (CAMBIO #1)
        // ======================================================
        var fotoBitmap by remember { mutableStateOf<Bitmap?>(null) }
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
        var fotoBase64 by remember { mutableStateOf<String?>(null) }


        // ======================================================
        // 📸 CAMERA LAUNCHER (YA EXISTENTE, SOLO ORDENADO)
        // ======================================================
        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { originalBitmap ->
            if (originalBitmap != null) {

                // Redimensionado
                val maxWidth = 1024
                val maxHeight = 1024
                val ratio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()

                val finalWidth: Int
                val finalHeight: Int

                if (ratio > 1) {
                    finalWidth = maxWidth
                    finalHeight = (maxWidth / ratio).toInt()
                } else {
                    finalHeight = maxHeight
                    finalWidth = (maxHeight * ratio).toInt()
                }

                val resized = Bitmap.createScaledBitmap(originalBitmap, finalWidth, finalHeight, true)

                fotoBitmap = resized
                selectedImageUri = null // Foto por cámara reemplaza galería

                // Convertimos a Base64
                val stream = ByteArrayOutputStream()
                resized.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                val byteArray = stream.toByteArray()
                fotoBase64 = Base64.encodeToString(byteArray, Base64.NO_WRAP)
            }
        }

        // ======================================================
        // 🔒 PERMISSION LAUNCHER (YA EXISTENTE)
        // ======================================================
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) cameraLauncher.launch(null)
            else scope.launch { snackbar.showSnackbar("Permiso de cámara denegado") }
        }


        // ======================================================
        // 🖼 GALERÍA LAUNCHER (CAMBIO #2: NUEVO)
        // ======================================================
        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                fotoBitmap = null // La imagen de cámara se desactiva

                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                fotoBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
            }
        }


        // ======================================================
        // UI
        // ======================================================
        Scaffold(
            containerColor = JetBlack,
            snackbarHost = { SnackbarHost(snackbar) },
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Agregar Producto", color = GamerGreen) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = JetBlack)
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

                Text("Toca para tomar foto o usa galería", color = Color.Gray)
                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .border(2.dp, GamerGreen, MaterialTheme.shapes.medium)
                        .clickable {
                            val permission = ContextCompat.checkSelfPermission(
                                context, Manifest.permission.CAMERA
                            )
                            if (permission == PackageManager.PERMISSION_GRANTED) {
                                cameraLauncher.launch(null)
                            } else {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        selectedImageUri != null -> {
                            Image(
                                painter = rememberAsyncImagePainter(selectedImageUri),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        fotoBitmap != null -> {
                            Image(
                                bitmap = fotoBitmap!!.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        else -> {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = GamerGreen, modifier = Modifier.size(40.dp))
                        }
                    }
                }

                // ======================================================
                // 📌 BOTÓN PARA GALERÍA (CAMBIO #3: NUEVO)
                // ======================================================
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = GamerGreen)
                ) {
                    Text("Seleccionar desde galería", color = Color.Black)
                }


                Spacer(Modifier.height(25.dp))

                // --- CAMPOS ---
                CampoTexto("Nombre", nombre, KeyboardType.Text) { nombre = it }
                CampoTexto("Descripción", descripcion,KeyboardType.Text) { descripcion = it }
                CampoTexto("Precio", precio, KeyboardType.Number) { precio = it }
                CampoTexto("Stock", stock, KeyboardType.Number) { stock = it }


                Spacer(Modifier.height(12.dp))

                // CATEGORÍAS
                ExposedDropdownMenuBox(
                    expanded = expandirMenu,
                    onExpandedChange = { expandirMenu = !expandirMenu }
                ) {
                    OutlinedTextField(
                        value = categoriaSeleccionadaNombre.ifEmpty { "Seleccionar categoría" },
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirMenu) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandirMenu,
                        onDismissRequest = { expandirMenu = false }
                    ) {
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

                Spacer(Modifier.height(25.dp))


                // ======================================================
                // 📦 BOTÓN GUARDAR FINAL
                // ======================================================
                Button(
                    onClick = {
                        if (nombre.isBlank() || precio.isBlank() || categoriaSeleccionadaId == null) {
                            scope.launch { snackbar.showSnackbar("Faltan datos obligatorios") }
                            return@Button
                        }

                        if (fotoBase64 == null) {
                            scope.launch { snackbar.showSnackbar("Debes agregar una imagen") }
                            return@Button
                        }

                        val productoNuevo = ProductosEntity(
                            id = 0,
                            backendId = null,
                            nombre = nombre,
                            descripcion = descripcion,
                            precio = precio.toLong(),
                            stock = stock.toIntOrNull() ?: 0,
                            categoriaId = categoriaSeleccionadaId!!,
                            categoriaNombre = categoriaSeleccionadaNombre,
                            imagenUrl = null
                        )

                        productosViewModel.crearProductoConImagen(
                            productoNuevo,
                            fotoBase64!!,
                            "imagen_${System.currentTimeMillis()}.jpg",
                            "image/jpeg"
                        ) { ok ->
                            if (ok) {
                                Toast.makeText(context, "Producto creado", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                scope.launch { snackbar.showSnackbar("Error al crear producto") }
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
