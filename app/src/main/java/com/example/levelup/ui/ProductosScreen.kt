package com.example.levelup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.levelup.R
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.viewmodel.CarritoViewModel
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.ui.theme.GamerGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    productosViewModel: ProductosViewModel,
    nav: NavHostController,
    carritoViewModel: CarritoViewModel,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val productos by productosViewModel.productos.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var searchQuery by rememberSaveable { mutableStateOf("") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.Black,
                drawerContentColor = Color.White,
                modifier = Modifier
                    .background(Color.Black)
                    .width(300.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 1.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { scope.launch { drawerState.close() } }) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = GamerGreen)
                    }
                }

                Text(
                    text = stringResource(R.string.app_name),
                    color = GamerGreen,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )

                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = {}
                )

                NavigationDrawerItem(
                    label = { Text("Carrito", color = Color.White) },
                    selected = false,
                    icon = { Icon(Icons.Default.ShoppingCart, null, tint = Color.White) },
                    onClick = { scope.launch { drawerState.close(); onNavigate("carrito") } }
                )

                NavigationDrawerItem(
                    label = { Text("Inicio", color = Color.White) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close(); onNavigate("PantallaPrincipal") } }
                )

                NavigationDrawerItem(
                    label = { Text("Productos", color = Color.White) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close(); onNavigate("productos") } }
                )

                NavigationDrawerItem(
                    label = { Text("Inicia sesión", color = Color.White) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close(); onNavigate("login") } }
                )

                NavigationDrawerItem(
                    label = { Text("Regístrate", color = Color.White) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close(); onNavigate("registro") } }
                )
            }
        }
    ) {

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = null, tint = GamerGreen)
                        }
                    },
                    title = { Text(stringResource(R.string.app_name), color = GamerGreen, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color.Black
        ) { innerPadding ->
            Column(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.Black)
            ) {

                Text(
                    text = "Nuestros Productos",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(productos) { producto ->
                        ProductoItem(
                            producto = producto,
                            onClick = { nav.navigate("producto/${producto.id}") },
                            onAddToCart = {
                                carritoViewModel.agregarProducto(
                                    productoId = producto.id,
                                    nombre = producto.nombre,
                                    precio = producto.precio,
                                    imagenRes = R.drawable.ic_launcher_foreground
                                )

                                scope.launch {
                                    snackbarHostState.showSnackbar("✅ Producto agregado al carrito")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

/* ---------------- ITEM PRODUCTO ------------------ */
@Composable
fun ProductoItem(
    producto: ProductosEntity,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(350.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(Modifier.height(10.dp))

            Text(
                text = producto.nombre,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "$${producto.precio}",
                color = GamerGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = onAddToCart,
                colors = ButtonDefaults.buttonColors(containerColor = GamerGreen, contentColor = Color.Black),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Añadir al carrito", fontWeight = FontWeight.Bold)
            }
        }
    }
}
