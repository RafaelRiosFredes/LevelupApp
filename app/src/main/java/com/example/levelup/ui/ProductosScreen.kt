package com.example.levelup.ui

import android.app.Application
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.levelup.R
import com.example.levelup.model.local.ProductosEntity
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.ProductosViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    nav: NavHostController,
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val factory = ProductosViewModelFactory(application)
    val viewModel: ProductosViewModel = viewModel(factory = factory)

    val productos by viewModel.productos.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var searchQuery by rememberSaveable { mutableStateOf("") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(scope, drawerState, snackbarHostState)
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color(0xFF39FF14)
                            )
                        }
                    },
                    title = {
                        Text(
                            text = context.getString(R.string.app_name),
                            color = Color(0xFF39FF14),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Black
                    )
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            containerColor = Color.Black
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.Black)
            ) {
                CustomSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { /* TODO: Filtrar productos */ }
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "🛒 Nuestros Productos",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
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
                            onAddToCart = { viewModel.agregarAlCarrito(producto) }
                        )
                    }
                }
            }
        }
    }
}

// 🟢 PRODUCTO ITEM
@Composable
fun ProductoItem(
    producto: ProductosEntity,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // 💡 Animación para el brillo "neón pulsante"
    val infiniteTransition = rememberInfiniteTransition(label = "neonPulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(370.dp)
                .clickable { onClick() }
                .graphicsLayer {
                    shadowElevation = 12f
                    shape = RoundedCornerShape(18.dp)
                    clip = true
                },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A1A1A),
                contentColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                // 🖼 Imagen
                Image(
                    painter = painterResource(id = producto.imagenRes),
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .size(140.dp)
                        .padding(top = 5.dp),
                    contentScale = ContentScale.Fit
                )

                // 🏷 Nombre y precio centrados
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = producto.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color.White,
                        lineHeight = 20.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 6.dp),
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$${producto.precio}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 19.sp,
                        color = Color(0xFF39FF14), // ✅ Verde sólido sin brillo
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                }

                Spacer(modifier = Modifier.height(10.dp))

                // 🛒 Botón animado gamer con brillo, rebote y snackbar
                Button(
                    onClick = {
                        isPressed = true
                        onAddToCart()
                        scope.launch {
                            snackbarHostState.showSnackbar("✅ Producto añadido al carrito")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF39FF14).copy(alpha = glowAlpha),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .graphicsLayer {
                            scaleX = if (isPressed) 1.08f else 1f
                            scaleY = if (isPressed) 1.08f else 1f
                            shadowElevation = if (isPressed) 24f else 8f
                        }
                ) {
                    Text(
                        text = "Añadir al carrito",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // 🔄 Restablecer animación del rebote
                LaunchedEffect(isPressed) {
                    if (isPressed) {
                        kotlinx.coroutines.delay(150)
                        isPressed = false
                    }
                }
            }
        }

        // 🧃 Snackbar gamer
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
        ) { data ->
            Snackbar(
                containerColor = Color(0xFF39FF14),
                contentColor = Color.Black,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = data.visuals.message,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
// 🟢 DRAWER CONTENT
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
    }
}

// 🟢 DRAWER ITEM
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

// 🟢 CUSTOM SEARCH BAR
@Composable
fun CustomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.Black),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    "Buscar en LEVEL-UP GAMER",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF39FF14),
                unfocusedBorderColor = Color(0xFF39FF14),
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black,
                cursorColor = Color(0xFF39FF14),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Search
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onSearch,
            modifier = Modifier
                .size(50.dp)
                .background(Color.Transparent)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = Color(0xFF39FF14)
            )
        }
    }
}
