package com.example.levelup.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import com.example.levelup.core.UserSession
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerGlobal(
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
                Text(
                    text = "LEVEL UP GAMER",
                    color = Color(0xFF39FF14),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = androidx.compose.ui.Modifier.padding(16.dp)
                )

                Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))

                // --------- CLIENTE ----------
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("PantallaPrincipal") {
                            popUpTo("PantallaPrincipal") { inclusive = false }
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    modifier = androidx.compose.ui.Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Productos") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("productos")
                    },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                    modifier = androidx.compose.ui.Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Carrito") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("carrito")
                    },
                    icon = { Icon(Icons.Default.ShoppingBasket, contentDescription = null) },
                    modifier = androidx.compose.ui.Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Noticias") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("Noticias")
                    },
                    icon = { Icon(Icons.Default.Article, contentDescription = null) },
                    modifier = androidx.compose.ui.Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Contacto") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("contacto")
                    },
                    icon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = androidx.compose.ui.Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Inicia sesión") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("login")
                    },
                    icon = { Icon(Icons.Default.Login, contentDescription = null) },
                    modifier = androidx.compose.ui.Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Regístrate") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("registro")
                    },
                    icon = { Icon(Icons.Default.AppRegistration, contentDescription = null) },
                    modifier = androidx.compose.ui.Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Perfil") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("perfil")
                    },
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                    modifier = androidx.compose.ui.Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                // --------- ADMIN ----------
                if (UserSession.rol.lowercase() == "admin") {
                    Spacer(modifier = androidx.compose.ui.Modifier.height(12.dp))
                    Divider(color = Color.DarkGray)

                    NavigationDrawerItem(
                        label = { Text("Inventario") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("inventario")
                        },
                        icon = { Icon(Icons.Default.Inventory, contentDescription = null) },
                        modifier = androidx.compose.ui.Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Gestión de usuarios") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("usuarios")
                        },
                        icon = { Icon(Icons.Default.Groups, contentDescription = null) },
                        modifier = androidx.compose.ui.Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }

                Spacer(modifier = androidx.compose.ui.Modifier.height(12.dp))
                Divider(color = Color.DarkGray)

                // --------- CERRAR SESIÓN ----------
                NavigationDrawerItem(
                    label = { Text("Cerrar sesión") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        UserSession.logout()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Default.Logout, contentDescription = null) },
                    modifier = androidx.compose.ui.Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        // El contenido real de la pantalla (cada Screen trae su propio Scaffold)
        content()
    }
}
