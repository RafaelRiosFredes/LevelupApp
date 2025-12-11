@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelup.core.UserSession
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.UsuariosViewModel
import kotlinx.coroutines.launch

@Composable
fun AddUsuarioScreen(
    navController: NavController,
    vm: UsuariosViewModel
) {
    // Solo admin
    LaunchedEffect(Unit) {
        if (!UserSession.rol.toString().contains("ADMIN", ignoreCase = true)) {
            navController.navigate("PantallaPrincipal") {
                popUpTo("PantallaPrincipal") { inclusive = true }
            }
        }
    }

    DrawerGlobal(navController = navController) {
        AddUsuarioContent(
            vm = vm,
            onSaved = { navController.popBackStack() },
            onCancel = { navController.popBackStack() }
        )
    }
}

@Composable
private fun AddUsuarioContent(
    vm: UsuariosViewModel,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    // FORMULARIO
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("user") }

    Scaffold(
        containerColor = JetBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agregar usuario", color = GamerGreen) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = JetBlack)
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // CAMPOS sin foto
            CampoTexto("Nombres", nombres, KeyboardType.Number) { nombres = it }
            CampoTexto("Apellidos", apellidos, KeyboardType.Number) { apellidos = it }
            CampoTexto("Correo", correo, KeyboardType.Number) { correo = it }
            CampoTexto("Contraseña", contrasena, KeyboardType.Number) { contrasena = it }
            CampoTexto("Teléfono", telefono, KeyboardType.Number) { telefono = it }
            CampoTexto(
                "Fecha nacimiento (YYYY-MM-DD)",
                fechaNacimiento,
                KeyboardType.Number
            ) { fechaNacimiento = it }

            Spacer(Modifier.height(12.dp))

            // SELECTOR DE ROL
            RolSelector(rolActual = rol) { rol = it }

            Spacer(Modifier.height(24.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

                TextButton(onClick = onCancel) {
                    Text("Cancelar", color = PureWhite)
                }

                Spacer(Modifier.width(12.dp))

                Button(
                    onClick = {
                        if (nombres.isBlank() || correo.isBlank() || contrasena.isBlank()) {
                            Toast.makeText(ctx, "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        scope.launch {
                            vm.registrar(
                                nombres = nombres,
                                apellidos = apellidos,
                                correo = correo,
                                contrasena = contrasena,
                                telefono = telefono.toLongOrNull() ?: 0,
                                fechaNacimiento = fechaNacimiento
                            ) {
                                Toast.makeText(ctx, "Usuario creado", Toast.LENGTH_SHORT).show()
                                onSaved()
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


// ----------------------------------------------------------
// REUSABLE FIELDS
// ----------------------------------------------------------
@Composable
fun CampoTexto(
    titulo: String,
    valor: String,
    onChange1: KeyboardType,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(titulo, color = PureWhite) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GamerGreen,
            unfocusedBorderColor = Color.DarkGray,
            focusedTextColor = PureWhite,
            unfocusedTextColor = PureWhite,
            cursorColor = GamerGreen
        )
    )

    Spacer(Modifier.height(12.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RolSelector(rolActual: String, onChange: (String) -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        OutlinedTextField(
            value = rolActual,
            onValueChange = {},
            readOnly = true,
            label = { Text("Rol", color = PureWhite) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GamerGreen,
                unfocusedBorderColor = Color.DarkGray,
                focusedTextColor = PureWhite,
                unfocusedTextColor = PureWhite,
                cursorColor = GamerGreen
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf("user", "admin").forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onChange(it)
                        expanded = false
                    }
                )
            }
        }
    }
}
