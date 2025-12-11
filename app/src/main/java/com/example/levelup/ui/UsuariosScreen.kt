@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelup.remote.UsuarioDTO
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.DarkGray
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.UsuariosViewModel

@Composable
fun UsuariosScreen(
    vm: UsuariosViewModel,
    currentUserRol: String,
    navController: NavController,
    onEditarClick: (Long) -> Unit,
) {

    // ADMIN
    LaunchedEffect(Unit) {
        if (!currentUserRol.contains("ADMIN", ignoreCase = true)) {
            navController.navigate("PantallaPrincipal") {
                popUpTo("PantallaPrincipal") { inclusive = true }
            }
        }
    }

    val usuarios by vm.listaUsuarios.collectAsState()


    LaunchedEffect(Unit) {
        vm.cargarUsuarios()
    }

    var selectedUser by remember { mutableStateOf<UsuarioDTO?>(null) }
    var showConfirmDelete by remember { mutableStateOf(false) }

    DrawerGlobal(navController = navController) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(JetBlack)
                .padding(16.dp)
        ) {

            Text(
                text = "Gestión de Usuarios",
                color = GamerGreen,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(16.dp))

            if (usuarios.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay usuarios registrados", color = PureWhite)
                }
            } else {

                LazyColumn(modifier = Modifier.fillMaxSize()) {

                    items(usuarios) { u ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable {
                                    selectedUser =
                                        if (selectedUser?.id == u.id) null else u
                                },
                            colors = CardDefaults.cardColors(containerColor = DarkGray)
                        ) {

                            Column(modifier = Modifier.padding(16.dp)) {

                                Text(
                                    "${u.nombres} ${u.apellidos}",
                                    color = PureWhite,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(u.correo, color = Color.LightGray)

                                Text(
                                    "Rol: ${u.rol}",
                                    color = if (u.rol.contains("ADMIN", ignoreCase = true)) GamerGreen else Color.Gray
                                )

                                if (selectedUser?.id == u.id) {

                                    Row(
                                        modifier = Modifier.padding(top = 10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {

                                        IconButton(onClick = { onEditarClick(u.id!!) }) {
                                            Icon(
                                                Icons.Default.Edit,
                                                contentDescription = "Editar",
                                                tint = GamerGreen
                                            )
                                        }

                                        IconButton(onClick = {
                                            selectedUser = u
                                            showConfirmDelete = true
                                        }) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Eliminar",
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // confirma eliminacion
        if (showConfirmDelete && selectedUser != null) {

            AlertDialog(
                onDismissRequest = { showConfirmDelete = false },
                title = { Text("Eliminar usuario", color = PureWhite) },
                text = {
                    Text(
                        "¿Seguro que deseas eliminar a ${selectedUser!!.nombres} ${selectedUser!!.apellidos}?",
                        color = PureWhite
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        vm.eliminarUsuarioBackend(selectedUser!!.id!!)
                        selectedUser = null
                        showConfirmDelete = false
                    }) {
                        Text("Eliminar", color = GamerGreen)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDelete = false }) {
                        Text("Cancelar", color = PureWhite)
                    }
                },
                containerColor = DarkGray
            )
        }
    }
}
