package com.example.levelup.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.levelup.R
import com.example.levelup.ui.theme.GamerBlue
import com.example.levelup.viewmodel.UsuariosViewModel
import com.example.levelup.ui.theme.GamerGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    vm: UsuariosViewModel,
    onSaved: () -> Unit  // se ejecuta si el usuario apreta "Registrar"
) {
    val form by vm.form.collectAsState()
    val context = LocalContext.current //Da el acceso a android
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    // muestra los mensajes
    LaunchedEffect(form.mensaje) {
        form.mensaje?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            vm.limpiarMensaje()
        }
    }

    // carga la imagen desde la camara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? -> bitmap?.let { vm.onChangeFoto(it) } } // guarda la fto en el viewModel

    // carga la imagen desde la galeria
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val thumb = context.contentResolver.loadThumbnail(it, Size(200, 200), null)
            vm.onChangeFoto(thumb)
        }
    }

    // permisos de camara
    val requestCameraPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) cameraLauncher.launch()
        }

    // permisos de galeria
    val requestGalleryPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                galleryLauncher.launch("image/*")
            } else {
                vm.onChangeCorreo("Permiso de galería denegado")
            }
        }

    // fuerza permiso con galeria
    fun abrirGaleriaConPermiso() {
        val permiso =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_IMAGES
            else
                Manifest.permission.READ_EXTERNAL_STORAGE

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, permiso) -> {
                galleryLauncher.launch("image/*")
            }

            else -> {
                requestGalleryPermission.launch(permiso)
            }
        }
    }

    // Colores dinámicos del botón Registrar
    val botonColor = if (
        form.mensaje?.contains("Completa") == true ||
        form.mensaje?.contains("Ya existe") == true ||
        form.mensaje?.contains("inválido") == true ||
        form.mensaje?.contains("mayores") == true
    ) MaterialTheme.colorScheme.error else GamerGreen

    // --------------------------
    // BARRA LATERAL
    // --------------------------
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var searchQuery by rememberSaveable { mutableStateOf("") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.Black,
                drawerContentColor = Color.White,
                modifier = Modifier
                    .background(Color.Black)
                    .width(300.dp)
            ) {
                // Botón cerrar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { scope.launch { drawerState.close() } }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar menú",
                            tint = GamerGreen
                        )
                    }
                }

                // Título app
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(vertical = 18.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = "LevelUp Gamer",
                        color = GamerGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Barra de búsqueda gamer
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { /* TODO búsqueda futura */ }
                )

                // Ítems del drawer
                Column(modifier = Modifier.fillMaxWidth()) {
                    val items = listOf(
                        "Inicio" to Icons.Default.Home,
                        "Noticias" to Icons.Default.Newspaper,
                        "Contacto" to Icons.Default.Phone,
                        "Inicia sesión" to Icons.Default.Login,
                    )

                    items.forEach { (label, icon) ->
                        NavigationDrawerItem(
                            label = { Text(label, color = Color.White) },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                    snackbarHostState.showSnackbar("$label seleccionado")
                                }
                            },
                            icon = { Icon(icon, contentDescription = null, tint = Color.White) },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        }
    ) {

        // empieza la pagina

        Surface(
            color = Color.Black,
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = GamerGreen
                                )
                            }
                        },
                        title = {
                            Text(
                                text = "Registro de Usuario",
                                color = Color(0xFF39FF14),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Black,
                            titleContentColor = Color(0xFF39FF14),
                        )
                    )
                },

                // tipos de mensajes dinamicos
                snackbarHost = {
                    SnackbarHost(snackbarHostState) { data ->
                        val isSuccess = data.visuals.message.contains("exitoso", true) ||
                                data.visuals.message.contains("Completado", true) ||
                                data.visuals.message.contains("Duoc", true)

                        Snackbar(
                            containerColor = if (isSuccess) GamerGreen else Color.Red,
                            contentColor = Color.Black,
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = data.visuals.message,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                containerColor = Color.Black
            ) { padding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // circulo de foto de perfil
                    Box(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .size(140.dp)
                            .clip(CircleShape)
                            .border(
                                width = 3.dp,
                                color = if (form.fotoPerfil != null) GamerGreen else Color.Gray,
                                shape = CircleShape
                            )
                            .shadow(
                                elevation = if (form.fotoPerfil != null) 12.dp else 4.dp,
                                shape = CircleShape,
                                ambientColor = GamerGreen.copy(alpha = 0.6f),
                                spotColor = GamerGreen.copy(alpha = 0.8f)
                            )
                            .clickable { requestCameraPermission.launch(Manifest.permission.CAMERA) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (form.fotoPerfil != null) {
                            Image(
                                bitmap = form.fotoPerfil!!.asImageBitmap(),
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.fillMaxSize(),
                                alignment = Alignment.Center
                            )
                        } else {
                            Text(
                                text = "Agregar foto",
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // boton "tomar una foto"
                        Button(
                            onClick = { requestCameraPermission.launch(Manifest.permission.CAMERA) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                            border = BorderStroke(2.dp, GamerGreen),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Toma una foto", color = Color.White)
                        }

                        // boton "Carga una foto"
                        Button(
                            onClick = { abrirGaleriaConPermiso() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                            border = BorderStroke(2.dp, GamerGreen),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Carga una foto", color = Color.White)
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    // caja de texto de Nombre
                    OutlinedTextField(
                        value = form.nombres,
                        onValueChange = vm::onChangeNombres,
                        label = { Text("Ingresa tu nombre", color = Color.White) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GamerBlue,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = GamerBlue
                        )
                    )

                    Spacer(Modifier.height(10.dp))

                    // caja de texto de apellidos
                    OutlinedTextField(
                        value = form.apellidos,
                        onValueChange = vm::onChangeApellidos,
                        label = { Text("Ingresa tu apellido", color = Color.White) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GamerBlue,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = GamerBlue
                        )
                    )

                    Spacer(Modifier.height(10.dp))

                    // caja de texto de Correo
                    OutlinedTextField(
                        value = form.correo,
                        onValueChange = vm::onChangeCorreo,
                        label = { Text("Ingresa tu correo electrónico", color = Color.White) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GamerBlue,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = GamerBlue
                        )
                    )

                    Spacer(Modifier.height(10.dp))

                    // caja de texto de Contraseña
                    OutlinedTextField(
                        value = form.contrasena,
                        onValueChange = vm::onChangeContrasena,
                        label = { Text("Ingresa tu contraseña", color = Color.White) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GamerBlue,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = GamerBlue
                        )
                    )
                    Spacer(Modifier.height(10.dp))

                    // caja de texto de ContraeñaConfirmacion
                    OutlinedTextField(
                        value = form.contrasenaConfirmacion,
                        onValueChange = vm::onChangeContrasenaConfirmacion,
                        label = { Text("Vuelve a ingresar tu contraseña", color = Color.White) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GamerBlue,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = GamerBlue
                        )
                    )

                    Spacer(Modifier.height(10.dp))

                    // caja de texto de telefono
                    OutlinedTextField(
                        value = form.telefono,
                        onValueChange = vm::onChangeTelefono,
                        label = { Text("Teléfono (opcional)", color = Color.White) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GamerBlue,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = GamerBlue
                        )
                    )

                    Spacer(Modifier.height(10.dp))

                    // caja de texto de fecha de nacimiento
                    OutlinedTextField(
                        value = form.fechaNacimiento,
                        onValueChange = vm::onChangeFechaNacimiento,
                        label = { Text("Fecha de nacimiento (dd/MM/yyyy)", color = Color.White) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GamerBlue,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = GamerBlue
                        )
                    )

                    Spacer(Modifier.height(10.dp))

                    // boton registrar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = onSaved,
                            modifier = Modifier
                                .width(200.dp)
                                .height(45.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = botonColor),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Registrar", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(Modifier.height(30.dp))
                }
            }
        }
    }
}


