package com.example.levelup.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelup.core.UserSession
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerGlobal(
    navController: NavController,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.Black,
                drawerContentColor = Color.White
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Título de la App
                    Text(
                        text = "LEVEL UP GAMER",
                        color = Color(0xFF39FF14), // GamerGreen
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    // Lógica para mostrar el saludo si está logueado
                    if (UserSession.isLogged()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Hola, ${UserSession.nombre}!",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    } else {
                        // Mensaje opcional para invitados
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Modo Invitado",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // -----------------------
                // CLIENTE NORMAL
                // -----------------------
                DrawerItem(
                    label = "Inicio",
                    icon = Icons.Default.Home,
                    onClick = {
                        navController.navigate("PantallaPrincipal") {
                            popUpTo("PantallaPrincipal") { inclusive = false }
                        }
                        scope.launch { drawerState.close() }
                    }
                )

                    // -----------------------
                    // OPCIONES PARA INVITADOS
                    // -----------------------
                if (!UserSession.isLogged()) {

                    DrawerItem(
                        label = "Iniciar sesión",
                        icon = Icons.Default.Login,
                        onClick = {
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        label = "Regístrate",
                        icon = Icons.Default.PersonAdd,
                        onClick = {
                            navController.navigate("registro")
                            scope.launch { drawerState.close() }
                        }
                    )

                    Divider(color = Color.DarkGray)
                }

                DrawerItem(
                    label = "Productos",
                    icon = Icons.Default.ShoppingCart,
                    onClick = {
                        navController.navigate("productos")
                        scope.launch { drawerState.close() }
                    }
                )

                DrawerItem(
                    label = "Carrito",
                    icon = Icons.Default.ShoppingBag,
                    onClick = {
                        navController.navigate("carrito")
                        scope.launch { drawerState.close() }
                    }
                )

                DrawerItem(
                    label = "Noticias Gamer",
                    icon = Icons.Default.Article,
                    onClick = {
                        navController.navigate("noticias")
                        scope.launch { drawerState.close() }
                    }
                )

                DrawerItem(
                    label = "Contáctanos",
                    icon = Icons.Default.Email,
                    onClick = {
                        navController.navigate("contacto")
                        scope.launch { drawerState.close() }
                    }
                )

                // -----------------------
                // ADMIN EXTRAS
                // -----------------------
                if (UserSession.rol == "admin") {
                    Divider(color = Color.DarkGray)

                    DrawerItem(
                        label = "Inventario",
                        icon = Icons.Default.Warehouse,
                        onClick = {
                            navController.navigate("inventario")
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        label = "Gestión de Usuarios",
                        icon = Icons.Default.Person,
                        onClick = {
                            navController.navigate("admin_usuarios")
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        label = "Agregar Usuario",
                        icon = Icons.Default.PersonAdd,
                        onClick = {
                            navController.navigate("add_usuario")
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        label = "Historial de Boletas",
                        icon = Icons.Default.List,
                        onClick = {
                            navController.navigate("historial_boletas")
                            scope.launch { drawerState.close() }
                        }
                    )
                }



                // -----------------------
                // CERRAR SESIÓN (solo si está logueado)
                // -----------------------
                if (UserSession.isLogged()) {
                    DrawerItem(
                        label = "Cerrar sesión",
                        icon = Icons.Default.ExitToApp,
                        onClick = {
                            UserSession.logout()
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                            scope.launch { drawerState.close() }
                        }
                    )
                }

            }
        }
    ) {
        Box { content() }
    }
}

@Composable
private fun DrawerItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(label) },
        selected = false,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = null) }
    )
}
