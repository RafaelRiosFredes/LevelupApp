package com.example.levelup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.text.style.TextOverflow
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.ui.theme.DarkGray
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.GamerBlue
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.viewmodel.UsuariosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosScreen(
    usuariosViewModel: UsuariosViewModel,
    onAgregarClick: () -> Unit,
    onEditarClick: (Int) -> Unit,
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val usuarios by usuariosViewModel.usuarios.collectAsState()

    var selectedUser by remember { mutableStateOf<UsuarioEntity?>(null) }
    var showConfirmDelete by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    Scaffold(
        containerColor = JetBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Usuarios", color = GamerGreen) },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = null, tint = PureWhite)
                    }
                },
                actions = {
                    if (selectedUser != null) {
                        IconButton(onClick = { onEditarClick(selectedUser!!.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar usuario", tint = PureWhite)
                        }
                        IconButton(onClick = { showConfirmDelete = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar usuario", tint = PureWhite)
                        }
                    } else {
                        IconButton(onClick = onAgregarClick) {
                            Icon(Icons.Default.Add, contentDescription = "Agregar", tint = GamerGreen)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregarClick, containerColor = GamerGreen, contentColor = JetBlack) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        if (usuarios.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No hay usuarios. Pulsa + para agregar uno.", color = PureWhite)
            }
            return@Scaffold
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(8.dp)) {
            items(usuarios) { u ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clickable { selectedUser = if (selectedUser?.id == u.id) null else u },
                    colors = CardDefaults.cardColors(containerColor = DarkGray)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("${u.nombres} ${u.apellidos}", color = PureWhite)
                        Text("Id: ${u.id} - ${u.correo}", color = PureWhite)
                        if (selectedUser?.id == u.id) {
                            Text("Seleccionado", color = GamerGreen)
                        }
                    }
                }
            }
        }

        if (showConfirmDelete && selectedUser != null) {
            AlertDialog(
                onDismissRequest = { showConfirmDelete = false },
                title = { Text("Eliminar usuario", color = PureWhite) },
                text = { Text("¿Eliminar ${selectedUser!!.nombres} ${selectedUser!!.apellidos}?", color = PureWhite) },
                confirmButton = {
                    TextButton(onClick = {
                        usuariosViewModel.eliminarUsuario(selectedUser!!)
                        showConfirmDelete = false
                        selectedUser = null
                    }) { Text("Eliminar", color = GamerGreen) }
                },
                dismissButton = { TextButton(onClick = { showConfirmDelete = false }) { Text("Cancelar", color = PureWhite) } },
                containerColor = DarkGray
            )
        }
    }
}


@Composable
fun ProductoRow(
    producto: ProductosEntity,
    isSelected: Boolean = false,
    onClick: (ProductosEntity) -> Unit
) {
    val formatter = remember {
        NumberFormat.getNumberInstance(Locale.getDefault()).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
            isGroupingUsed = true
        }
    }
    val precioFormateado = formatter.format(producto.precio)

    val shape = RoundedCornerShape(8.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .then(
                if (isSelected) Modifier.border(width = 2.dp, color = GamerGreen, shape = shape)
                else Modifier
            )
            .clickable { onClick(producto) },
        colors = CardDefaults.cardColors(containerColor = DarkGray),
        shape = shape
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleMedium,
                color = PureWhite,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Id: ${producto.id}", style = MaterialTheme.typography.bodyMedium, color = PureWhite)
            Text(text = "Precio: $precioFormateado", style = MaterialTheme.typography.bodyMedium, color = PureWhite)
            if (isSelected) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Seleccionado", style = MaterialTheme.typography.labelSmall, color = GamerGreen)
            }
        }
    }
}
