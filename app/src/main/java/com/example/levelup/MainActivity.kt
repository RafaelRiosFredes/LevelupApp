package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.levelup.local.AppDatabase
import com.example.levelup.local.ProductoEntity
import com.example.levelup.repository.ProductoRepository
import com.example.levelup.theme.LevelUpTheme
import com.example.levelup.ui.LevelUpNavHost
import com.example.levelup.viewmodel.ProductoViewModel
import com.example.levelup.viewmodel.ProductoViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val repo = ProductoRepository(db.ProductoDao())
        val vmFactory = ProductoViewModelFactory(repo)
        val viewModel = ViewModelProvider(this, vmFactory)[ProductoViewModel::class.java]

        CoroutineScope(Dispatchers.IO).launch {
            val currentProducts = viewModel.productos.value
            if (currentProducts.isEmpty()) {
                viewModel.insertarProducto(
                    ProductoEntity(
                        nombre = "Pantalón",
                        precio = 29.99,
                        imagenUrl = "https://picsum.photos/seed/pantalon/400/300",
                        descripcion = "Pantalón de mezclilla"
                    )
                )
                viewModel.insertarProducto(
                    ProductoEntity(
                        nombre = "Zapatos",
                        precio = 49.99,
                        imagenUrl = "https://picsum.photos/seed/zapatos/400/300",
                        descripcion = "Zapatos de cuero"
                    )
                )
            }
        }

        setContent {
            LevelUpTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerContent(scope, drawerState, snackbarHostState)
                    }
                ) {
                    // Le pasamos drawerState y scope para abrirlo desde cualquier pantalla
                    LevelUpNavHost(navController, viewModel, drawerState, scope)
                }
            }
        }
    }

    // Drawer Content
    @Composable
    fun DrawerContent(
        scope: CoroutineScope,
        drawerState: DrawerState,
        snackbarHostState: SnackbarHostState
    ) {
        ModalDrawerSheet(
            drawerContainerColor = Color.Black,
            drawerContentColor = Color.White,
            modifier = Modifier
                .background(Color.Black)
                .width(300.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { scope.launch { drawerState.close() } }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar menú",
                        tint = Color(0xFF39FF14)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = 18.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "LEVEL-UP GAMER",
                    color = Color(0xFF39FF14),
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            DrawerItem("Inicio", Icons.Default.Home, scope, drawerState, snackbarHostState)
            DrawerItem("Juegos de Mesa", null, scope, drawerState, snackbarHostState)
            DrawerItem("Accesorios", null, scope, drawerState, snackbarHostState)
            DrawerItem("Consolas", null, scope, drawerState, snackbarHostState)
            DrawerItem("Contacto", null, scope, drawerState, snackbarHostState)
            DrawerItem("Noticias", null, scope, drawerState, snackbarHostState)
            DrawerItem("Carrito", Icons.Default.ShoppingCart, scope, drawerState, snackbarHostState)

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.DarkGray
            )

            DrawerItem("Inicia sesión", null, scope, drawerState, snackbarHostState)
            DrawerItem("Regístrate", null, scope, drawerState, snackbarHostState)
            DrawerItem("Mi cuenta", Icons.Default.AccountCircle, scope, drawerState, snackbarHostState)
            DrawerItem("Puntos LevelUp", Icons.Default.Star, scope, drawerState, snackbarHostState)
        }
    }

    @Composable
    fun DrawerItem(
        title: String,
        icon: ImageVector?,
        scope: CoroutineScope,
        drawerState: DrawerState,
        snackbarHostState: SnackbarHostState
    ) {
        NavigationDrawerItem(
            label = { Text(title, color = Color.White) },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.close()
                    snackbarHostState.showSnackbar("$title seleccionado")
                }
            },
            icon = {
                icon?.let {
                    Icon(it, contentDescription = title, tint = Color.White)
                }
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}
