package com.example.levelup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.ui.theme.DarkGray
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.UsuariosViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosScreen(
    usuariosViewModel: UsuariosViewModel,
    currentUserRol: String,              // <--- IMPORTANTE: rol del usuario logeado
    onNavigate: (String) -> Unit = {},
    onAgregarClick: () -> Unit,
    onEditarClick: (Int) -> Unit,
    onLogout: () -> Unit = {}
) {
    // 🔥 Si NO es admin → lo sacamos inmediatamente
    LaunchedEffect(Unit) {
        if (currentUserRol != "admin") {
            onNavigate("PantallaPrincipal")
        }
    }

    val usuarios by usuariosViewModel.obtenerUsuarios().collectAsState(initial = emptyList())
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedUser by remember { mutableStateOf<UsuarioEntity?>(null) }
    var showConfirmDelete by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = JetBlack,
                drawerContentColor = PureWhite
            ) {
                Text(
                    "Menú Admin",
                    color = GamerGreen,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                NavigationDrawerItem(
                    label = { Text("Inventario") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigate("inventario")
                    },
                    icon = {
                        Icon(Icons.Default.List, contentDescription = null, tint = GamerGreen)
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Usuarios") },
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = GamerGreen)
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    onClick = onLogout,
                    icon = {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = PureWhite)
                    }
                )
            }
        }
    ) {
        Scaffold(
            containerColor = JetBlack,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Usuarios", color = GamerGreen) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = null,
                                tint = PureWhite
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onAgregarClick) {
                            Icon(Icons.Default.Add, contentDescription = "Agregar usuario", tint = GamerGreen)
                        }
                    }
                )
            }
        ) { padding ->

            if (usuarios.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay usuarios registrados", color = PureWhite)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(8.dp)
                ) {
                    items(usuarios) { u ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                                .clickable {
                                    selectedUser = if (selectedUser?.id == u.id) null else u
                                },
                            colors = CardDefaults.cardColors(containerColor = DarkGray),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "${u.nombres} ${u.apellidos}",
                                    color = PureWhite,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    u.correo,
                                    color = Color.LightGray,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    "Rol: ${u.rol}",
                                    color = if (u.rol == "admin") GamerGreen else Color.Gray
                                )

                                if (selectedUser?.id == u.id) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        modifier = Modifier.padding(top = 10.dp)
                                    ) {
                                        IconButton(onClick = { onEditarClick(u.id) }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = GamerGreen)
                                        }
                                        IconButton(onClick = { showConfirmDelete = true }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (showConfirmDelete && selectedUser != null) {
                AlertDialog(
                    onDismissRequest = { showConfirmDelete = false },
                    title = {
                        Text("Eliminar usuario", color = PureWhite)
                    },
                    text = {
                        Text(
                            "¿Deseas eliminar a ${selectedUser!!.nombres} ${selectedUser!!.apellidos}?",
                            color = PureWhite
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            usuariosViewModel.eliminarUsuario(selectedUser!!)
                            showConfirmDelete = false
                            selectedUser = null
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
}
