package com.example.levelup.ui

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.example.levelup.viewmodel.RegistroUsuarioViewModel
import com.example.levelup.ui.theme.GamerGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    vm: RegistroUsuarioViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val form by vm.form.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // ✅ Launchers para cámara y galería
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? -> bitmap?.let { vm.onChangeFoto(it) } }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val thumb = context.contentResolver.loadThumbnail(it, Size(200, 200), null)
            vm.onChangeFoto(thumb)
        }
    }

    // ✅ Pedir permisos
    val requestCameraPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) cameraLauncher.launch()
        }

    val requestGalleryPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) galleryLauncher.launch("image/*")
        }

    val botonColor = if (form.mensaje?.contains("Completa") == true ||
        form.mensaje?.contains("Ya existe") == true ||
        form.mensaje?.contains("inválido") == true ||
        form.mensaje?.contains("menores") == true
    ) MaterialTheme.colorScheme.error else GamerGreen

    // 🖤 Fondo negro general
    Surface(
        color = Color.Black,
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Registro de Usuario", color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = GamerGreen,
                        titleContentColor = Color.White
                    )
                )
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

                // 🖼️ Imagen o botones de cámara/galería
                form.fotoPerfil?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .clickable { cameraLauncher.launch() }
                    )
                } ?: Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { requestCameraPermission.launch(Manifest.permission.CAMERA) },
                        colors = ButtonDefaults.buttonColors(containerColor = GamerGreen)
                    ) { Text("📷 Tomar foto", color = Color.White) }

                    Button(
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                requestGalleryPermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
                            else
                                requestGalleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GamerGreen)
                    ) { Text("🖼️ Galería", color = Color.White) }
                }

                Divider(Modifier.padding(vertical = 8.dp), color = Color.DarkGray)

                form.mensaje?.let {
                    Text(
                        it,
                        color = if (it.contains("exitoso", true)) GamerGreen else Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // 🧾 Campos del formulario
                OutlinedTextField(
                    value = form.nombres,
                    onValueChange = vm::onChangeNombres,
                    label = { Text("Nombres", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GamerGreen,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = GamerGreen
                    )
                )

                OutlinedTextField(
                    value = form.apellidos,
                    onValueChange = vm::onChangeApellidos,
                    label = { Text("Apellidos", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GamerGreen,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = GamerGreen
                    )
                )

                OutlinedTextField(
                    value = form.correo,
                    onValueChange = vm::onChangeCorreo,
                    label = { Text("Correo electrónico", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GamerGreen,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = GamerGreen
                    )
                )

                OutlinedTextField(
                    value = form.contrasena,
                    onValueChange = vm::onChangeContrasena,
                    label = { Text("Contraseña", color = Color.White) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GamerGreen,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = GamerGreen
                    )
                )

                OutlinedTextField(
                    value = form.telefono,
                    onValueChange = vm::onChangeTelefono,
                    label = { Text("Teléfono (opcional)", color = Color.White) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GamerGreen,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = GamerGreen
                    )
                )

                OutlinedTextField(
                    value = form.fechaNacimiento,
                    onValueChange = vm::onChangeFechaNacimiento,
                    label = { Text("Fecha de nacimiento (dd/MM/yyyy)", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GamerGreen,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = GamerGreen
                    )
                )

                Spacer(Modifier.height(20.dp))

                // ✅ Botones dentro del formulario
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onSaved,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = botonColor)
                    ) {
                        Text("Registrar", color = Color.White)
                    }

                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GamerGreen)
                    ) {
                        Text("Cancelar")
                    }
                }

                Spacer(Modifier.height(30.dp))
            }
        }
    }
}
