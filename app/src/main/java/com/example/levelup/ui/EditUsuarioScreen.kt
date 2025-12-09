@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelup.remote.UsuarioDTO
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.UsuariosViewModel

@Composable
fun EditUsuarioScreen(
    navController: NavController,
    vm: UsuariosViewModel,
    userId: Long,
    currentUserRol: String,
    onCancel: () -> Unit
) {
    // Solo admin
    LaunchedEffect(Unit) {
        if (!currentUserRol.contains("ADMIN", ignoreCase = true)) {
            onCancel()
        }
    }

    DrawerGlobal(navController = navController) {
        EditUsuarioContent(vm = vm, userId = userId, onCancel = onCancel)
    }
}

@Composable
private fun EditUsuarioContent(
    vm: UsuariosViewModel,
    userId: Long,
    onCancel: () -> Unit
) {
    val usuario by vm.usuarioActual.collectAsState()
    val error by vm.error.collectAsState()

    // Cargar desde backend
    LaunchedEffect(userId) {
        vm.cargarUsuario(userId)
    }

    Scaffold(
        containerColor = JetBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle de usuario", color = GamerGreen) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = JetBlack
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            if (usuario == null) {
                CircularProgressIndicator(color = GamerGreen)
                return@Column
            }

            val u: UsuarioDTO = usuario!!

            Text("ID: ${u.id}", color = PureWhite)
            Text("Nombre: ${u.nombres} ${u.apellidos}", color = PureWhite)
            Text("Correo: ${u.correo}", color = PureWhite)
            Text("Teléfono: ${u.telefono}", color = PureWhite)
            Text("Nacimiento: ${u.fechaNacimiento}", color = PureWhite)
            Text("Rol: ${u.rol}", color = PureWhite)

            Spacer(modifier = Modifier.height(24.dp))

            if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = { onCancel() },
                colors = ButtonDefaults.buttonColors(containerColor = GamerGreen)
            ) {
                Text("Volver", color = JetBlack)
            }
        }
    }
}
