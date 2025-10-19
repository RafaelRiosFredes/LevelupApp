package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.repository.ProductosRepository
import com.example.levelup.ui.AddProductScreen
import com.example.levelup.ui.EditProductoScreen
import com.example.levelup.ui.InventarioScreen
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.ProductosViewModelFactory
import com.example.levelup.ui.theme.LevelUpTheme
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.levelup.ui.theme.GamerBlue
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val db by lazy { AppDatabase.get(this) }
    private val repo by lazy { ProductosRepository(db.productoDao()) }
    private val vmFactory by lazy { ProductosViewModelFactory(repo) }
    private val productosViewModel: ProductosViewModel by viewModels { vmFactory }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(JetBlack)
                            .padding(top = 8.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Spacer(modifier = Modifier.height(12.dp))

                                // Header
                                Text(
                                    text = "Admin",
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    color = PureWhite,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Colores para items del drawer
                                val drawerItemColors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = GamerGreen.copy(alpha = 0.20f),
                                    selectedTextColor = GamerBlue,
                                    selectedIconColor = GamerBlue,
                                    unselectedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                                    unselectedTextColor = PureWhite,
                                    unselectedIconColor = PureWhite
                                )

                                NavigationDrawerItem(
                                    label = { Text("Inventario") },
                                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                                    selected = currentRoute == "inventario",
                                    onClick = {
                                        scope.launch {
                                            drawerState.close()
                                            navController.navigate("inventario") { launchSingleTop = true }
                                        }
                                    },
                                    colors = drawerItemColors,
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                )

                                NavigationDrawerItem(
                                    label = { Text("Categorías") },
                                    icon = { Icon(Icons.Default.Category, contentDescription = null) },
                                    selected = currentRoute == "categorias",
                                    onClick = {
                                        scope.launch {
                                            drawerState.close()
                                            navController.navigate("categorias") { launchSingleTop = true }
                                        }
                                    },
                                    colors = drawerItemColors,
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                )

                                NavigationDrawerItem(
                                    label = { Text("Usuarios") },
                                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                                    selected = currentRoute == "usuarios",
                                    onClick = {
                                        scope.launch {
                                            drawerState.close()
                                            navController.navigate("usuarios") { launchSingleTop = true }
                                        }
                                    },
                                    colors = drawerItemColors,
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                )

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    thickness = DividerDefaults.Thickness,
                                    color = PureWhite.copy(alpha = 0.12f)
                                )

                                NavigationDrawerItem(
                                    label = { Text("Cerrar sesión") },
                                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
                                    selected = false,
                                    onClick = {
                                        scope.launch {
                                            drawerState.close()
                                            this@MainActivity.finish()
                                        }
                                    },
                                    colors = drawerItemColors,
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                )

                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                    }
                ) {
                    Scaffold(
                        containerColor = PureWhite,
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(
                                        "LevelUp Admin",
                                        color = GamerGreen,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                        Icon(Icons.Default.Menu, contentDescription = "Open drawer", tint = PureWhite)
                                    }
                                },
                                actions = {
                                    IconButton(onClick = { navController.navigate("agregar") }) {
                                        Icon(Icons.Default.Add, contentDescription = "Agregar", tint = GamerGreen)
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = JetBlack,               // barra oscura
                                    navigationIconContentColor = PureWhite,  // nav icon visible
                                    titleContentColor = GamerGreen,           // título en verde (seguro)
                                    actionIconContentColor = GamerGreen     // acciones en verde
                                )
                            )
                        },
                        content = { innerPadding ->
                            NavHost(
                                navController = navController,
                                startDestination = "inventario",
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable("inventario") {
                                    InventarioScreen(
                                        productosViewModel = productosViewModel,
                                        onAgregarClick = { navController.navigate("agregar") },
                                        onEditarClick = { id -> navController.navigate("editar/$id") }
                                    )
                                }
                                composable("agregar") {
                                    AddProductScreen(
                                        productosViewModel = productosViewModel,
                                        onSaved = { navController.popBackStack() },
                                        onCancel = { navController.popBackStack() }
                                    )
                                }
                                composable("editar/{productId}") { backStackEntry ->
                                    val id = backStackEntry.arguments?.getString("productId")?.toIntOrNull() ?: 0
                                    EditProductoScreen(
                                        productosViewModel = productosViewModel,
                                        productId = id,
                                        onSaved = { navController.popBackStack() },
                                        onCancel = { navController.popBackStack() }
                                    )
                                }
                                composable("categorias") {
                                    CategoriasScreen()
                                }
                                composable("usuarios") {
                                    UsuariosScreen()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
// CategoriasScreen.kt
@Composable
fun CategoriasScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla Categorías (implementa CRUD en otra branch)", color = PureWhite)
    }
}

// UsuariosScreen.kt
@Composable
fun UsuariosScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla Usuarios (implementa CRUD en otra branch)", color = PureWhite)
    }
}
