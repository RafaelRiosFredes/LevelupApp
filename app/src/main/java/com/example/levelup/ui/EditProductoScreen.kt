@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image // Icono Galeria
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.remote.ProductoImagenCreateDTO
import com.example.levelup.remote.RetrofitBuilder
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.ProductosViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@Composable
fun EditProductoScreen(
    navController: NavController,
    productosViewModel: ProductosViewModel,
    currentUserRol: String,
    productId: Long, // ESTE ES EL ID LOCAL (Room)
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    // Protección de ruta: Solo admin
    LaunchedEffect(Unit) {
        if (currentUserRol != "admin") {
            navController.navigate("PantallaPrincipal") {
                popUpTo("PantallaPrincipal") { inclusive = true }
            }
        }
    }

    DrawerGlobal(navController = navController) {
        EditProductoContent(
            productosViewModel = productosViewModel,
            localProductId = productId, // Pasamos el ID local
            onSaved = onSaved,
            onCancel = onCancel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProductoContent(
    productosViewModel: ProductosViewModel,
    localProductId: Long,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    // Estado del producto remoto (desde Backend)
    var productoRemoto by remember { mutableStateOf<com.example.levelup.remote.ProductoRemoteDTO?>(null) }

    // Estados de carga y error
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // --- CORRECCIÓN: Puente Room -> Backend ---
    LaunchedEffect(localProductId) {
        isLoading = true
        errorMessage = null
        try {
            // 1. Buscamos el producto en Room para obtener el ID real del Backend
            val productoLocal = productosViewModel.obtenerProductoPorId(localProductId).firstOrNull()

            if (productoLocal != null && productoLocal.backendId != null) {
                // 2. Si tiene backendId, consultamos la API con ESE id
                val idRemoto = productoLocal.backendId!!
                productoRemoto = RetrofitBuilder.productosApi.obtenerProductoPorId(idRemoto)
            } else {
                errorMessage = "Error: El producto no está sincronizado con el servidor (Falta Backend ID)."
            }
        } catch (e: retrofit2.HttpException) {
            e.printStackTrace()
            if (e.code() == 404) {
                errorMessage = "El producto no existe en el servidor (Error 404)."
            } else {
                errorMessage = "Error del servidor: ${e.message()}"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessage = "Error de conexión: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    // Estados del formulario
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    // Imagen actual (URL) vs Nueva Imagen (Bitmap)
    var currentImageUrl by remember { mutableStateOf<String?>(null) }
    var newFotoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var newFotoBase64 by remember { mutableStateOf<String?>(null) }

    // Rellenar formulario cuando llegue el producto remoto exitosamente
    LaunchedEffect(productoRemoto) {
        productoRemoto?.let {
            nombre = it.nombreProducto
            precio = it.precio.toString()
            descripcion = it.descripcion
            stock = it.stock.toString()

            // Tomamos la primera imagen si existe
            if (it.imagenes.isNotEmpty()) {
                val urlRelativa = it.imagenes[0].url
                currentImageUrl = if (urlRelativa.startsWith("http")) urlRelativa
                else RetrofitBuilder.BASE_URL.removeSuffix("/") + urlRelativa
            }
        }
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    // --- LOGICA DE CAMARA Y GALERIA ---
    fun procesarBitmap(originalBitmap: Bitmap) {
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

        newFotoBitmap = resizedBitmap

        val stream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteArray = stream.toByteArray()
        newFotoBase64 = Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) procesarBitmap(bitmap)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
                procesarBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
                scope.launch { snackbar.showSnackbar("Error al cargar imagen") }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) cameraLauncher.launch(null)
        else scope.launch { snackbar.showSnackbar("Permiso denegado") }
    }


    Scaffold(
        containerColor = JetBlack,
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Editar producto", color = GamerGreen) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = JetBlack)
            )
        }
    ) { padding ->

        // 1. ESTADO DE CARGA
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GamerGreen)
            }
            return@Scaffold
        }

        // 2. ESTADO DE ERROR
        if (errorMessage != null) {
            Column(
                Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red, modifier = Modifier.size(50.dp))
                Spacer(Modifier.height(16.dp))
                Text(errorMessage!!, color = PureWhite)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onCancel, colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)) {
                    Text("Volver", color = PureWhite)
                }
            }
            return@Scaffold
        }

        // 3. CONTENIDO (Si productoRemoto != null)
        if (productoRemoto != null) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // --- SECCIÓN FOTO ---
                Text("Imagen del producto", color = PureWhite)
                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .border(2.dp, GamerGreen, MaterialTheme.shapes.medium),
                    contentAlignment = Alignment.Center
                ) {
                    if (newFotoBitmap != null) {
                        Image(
                            bitmap = newFotoBitmap!!.asImageBitmap(),
                            contentDescription = "Nueva Foto",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else if (currentImageUrl != null) {
                        AsyncImage(
                            model = currentImageUrl,
                            contentDescription = "Foto Actual",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.Gray)
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = {
                            val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                            if (permissionCheck == PackageManager.PERMISSION_GRANTED) cameraLauncher.launch(null)
                            else permissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = GamerGreen)
                        Spacer(Modifier.width(8.dp))
                        Text("Cámara", color = PureWhite)
                    }

                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    ) {
                        Icon(Icons.Default.Image, contentDescription = null, tint = GamerGreen)
                        Spacer(Modifier.width(8.dp))
                        Text("Galería", color = PureWhite)
                    }
                }

                Spacer(Modifier.height(20.dp))

                // --- CAMPOS DE TEXTO ---
                CampoTextoEdit("Nombre", nombre) { nombre = it }
                CampoTextoEdit("Precio", precio, KeyboardType.Number) { precio = it.filter { c -> c.isDigit() } }
                CampoTextoEdit("Stock", stock, KeyboardType.Number) { stock = it.filter { c -> c.isDigit() } }
                CampoTextoEdit("Descripción", descripcion) { descripcion = it }

                Spacer(Modifier.height(24.dp))

                // --- BOTONES ACCIÓN ---
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCancel) {
                        Text("Cancelar", color = Color.Gray)
                    }

                    Spacer(Modifier.width(12.dp))

                    Button(
                        onClick = {
                            if (nombre.isBlank() || precio.isBlank()) {
                                scope.launch { snackbar.showSnackbar("Nombre y precio obligatorios") }
                                return@Button
                            }

                            // Creamos la entidad actualizada
                            // OJO: backendId debe ser el del producto remoto, no 0 ni null.
                            val productoActualizado = ProductosEntity(
                                id = localProductId, // ID Local (para actualizar room si fuera necesario)
                                backendId = productoRemoto!!.idProducto, // ID Backend IMPORTANTE
                                nombre = nombre,
                                descripcion = descripcion,
                                precio = precio.toLongOrNull() ?: 0L,
                                stock = stock.toIntOrNull() ?: 0,
                                imagenUrl = null, // Se maneja en la lógica del VM
                                categoriaId = productoRemoto!!.categoriaId,
                                categoriaNombre = productoRemoto!!.categoriaNombre
                            )

                            // IDs de imágenes existentes para borrarlas si hay foto nueva
                            val idsAntiguas = productoRemoto!!.imagenes.map { it.idImagen }

                            scope.launch {
                                productosViewModel.editarProductoCompleto(
                                    producto = productoActualizado,
                                    nuevaImagenBase64 = newFotoBase64,
                                    imagenesAntiguasIds = idsAntiguas
                                ) { exito ->
                                    if (exito) {
                                        Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show()
                                        onSaved()
                                    } else {
                                        scope.launch { snackbar.showSnackbar("Error al actualizar") }
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GamerGreen,
                            contentColor = JetBlack
                        )
                    ) {
                        Text("Guardar Cambios")
                    }
                }
            }
        }
    }
}

@Composable
fun CampoTextoEdit(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = PureWhite) },
        textStyle = TextStyle(color = PureWhite),
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GamerGreen,
            unfocusedBorderColor = Color.DarkGray,
            cursorColor = GamerGreen,
            focusedTextColor = PureWhite,
            unfocusedTextColor = PureWhite
        )
    )
    Spacer(Modifier.height(12.dp))
}