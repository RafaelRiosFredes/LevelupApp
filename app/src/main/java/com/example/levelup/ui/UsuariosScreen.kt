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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavController
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.DarkGray
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.UsuariosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosScreen(
    usuariosViewModel: UsuariosViewModel,
    currentUserRol: String,
    navController: NavController,
    onAgregarClick: () -> Unit,
    onEditarClick: (Int) -> Unit,
    onLogout: () -> Unit = {}
) {

    // 🔐 Seguridad: solo admin puede ver esta pantalla
    LaunchedEffect(Unit) {
        if (currentUserRol != "admin") {
            navController.navigate("PantallaPrincipal")
        }
    }

    // Datos
    val usuarios by usuariosViewModel.obtenerUsuarios().collectAsState(initial = emptyList())

    // Selección y eliminación
    var selectedUser by remember { mutableStateOf<UsuarioEntity?>(null) }
    var showConfirmDelete by remember { mutableStateOf(false) }

    // ===============================
    // 🚀 ENVUELTA EN DRAWERGLOBAL
    // ===============================
    DrawerGlobal({

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(JetBlack)
                .padding(16.dp)
        ) {

            Text(
                text = "Usuarios",
                color = GamerGreen,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Si no hay usuarios
            if (usuarios.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay usuarios registrados", color = PureWhite)
                }
            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(usuarios) { u ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable {
                                    selectedUser =
                                        if (selectedUser?.id == u.id) null else u
                                },
                            colors = CardDefaults.cardColors(containerColor = DarkGray),
                            shape = RoundedCornerShape(12.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    "${u.nombres} ${u.apellidos}",
                                    color = PureWhite,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    u.correo,
                                    color = Color.LightGray
                                )

                                Text(
                                    "Rol: ${u.rol}",
                                    color = if (u.rol == "admin") GamerGreen else Color.Gray
                                )

                                // ACCIONES SOLO SI ESTÁ SELECCIONADO
                                if (selectedUser?.id == u.id) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        modifier = Modifier.padding(top = 10.dp)
                                    ) {

                                        IconButton(onClick = { onEditarClick(u.id) }) {
                                            Icon(
                                                Icons.Default.Edit,
                                                contentDescription = "Editar",
                                                tint = GamerGreen
                                            )
                                        }

                                        IconButton(onClick = {
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

        // ===============================
        // ALERTA DE CONFIRMAR ELIMINADO
        // ===============================
        if (showConfirmDelete && selectedUser != null) {

            AlertDialog(
                onDismissRequest = { showConfirmDelete = false },
                title = { Text("Eliminar usuario", color = PureWhite) },
                text = {
                    Text(
                        "¿Deseas eliminar a ${selectedUser!!.nombres} ${selectedUser!!.apellidos}?",
                        color = PureWhite
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        usuariosViewModel.eliminarUsuario(selectedUser!!)
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
    })
}
