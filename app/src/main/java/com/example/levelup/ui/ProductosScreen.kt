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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.levelup.R
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.ProductosViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

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
                    onQueryChange = { value -> searchQuery = value },
                    onSearch = { /* TODO: Filtrar productos con searchQuery */ }
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Nuestros Productos",
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
                            onAddToCart = { /*viewModel.agregarAlCarrito(producto)*/ }
                        )
                    }
                }
            }
        }
    }
}

/* ------------------------- ProductoItem ------------------------- */

@Composable
fun ProductoItem(
    producto: ProductosEntity,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val localSnack = remember { SnackbarHostState() }


    // Animación de “brillo” suave del botón
    val infiniteTransition = rememberInfiniteTransition(label = "neonPulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(380.dp)
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


                // Nombre y precio
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
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 6.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$${producto.precio}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 19.sp,
                        color = Color(0xFF39FF14),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón Añadir al carrito
                Button(
                    onClick = {
                        isPressed = true
                        onAddToCart()
                        scope.launch {
                            localSnack.showSnackbar("✅ Producto añadido al carrito")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF39FF14).copy(alpha = glowAlpha),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .graphicsLayer {
                            scaleX = if (isPressed) 1.06f else 1f
                            scaleY = if (isPressed) 1.06f else 1f
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

                // Reset rebote
                LaunchedEffect(isPressed) {
                    if (isPressed) {
                        delay(150)
                        isPressed = false
                    }
                }
            }
        }

        // Snackbar local del item
        SnackbarHost(
            hostState = localSnack,
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

/* ------------------------- DrawerContent ------------------------- */

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
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Menú principal
        DrawerItem("Inicio", Icons.Default.Home, scope, drawerState, snackbarHostState)
        DrawerItem("Juegos de Mesa", null, scope, drawerState, snackbarHostState)
        DrawerItem("Accesorios", null, scope, drawerState, snackbarHostState)
        DrawerItem("Consolas", null, scope, drawerState, snackbarHostState)
        DrawerItem("Contacto", null, scope, drawerState, snackbarHostState)
        DrawerItem("Noticias", null, scope, drawerState, snackbarHostState)
        DrawerItem("Carrito", Icons.Default.ShoppingCart, scope, drawerState, snackbarHostState)

        // Separador
        Divider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.DarkGray,
            thickness = 1.dp
        )

        // Sección de cuenta / usuario
        NavigationDrawerItem(
            label = { Text("Inicia sesión", color = Color.White) },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.close()
                    snackbarHostState.showSnackbar("Inicia sesión seleccionado")
                }
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Regístrate", color = Color.White) },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.close()
                    snackbarHostState.showSnackbar("Regístrate seleccionado")
                }
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Mi cuenta", color = Color.White) },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.close()
                    snackbarHostState.showSnackbar("Mi cuenta seleccionado")
                }
            },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.White) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Puntos LevelUp", color = Color.White) },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.close()
                    snackbarHostState.showSnackbar("Puntos LevelUp seleccionado")
                }
            },
            icon = { Icon(Icons.Default.Star, contentDescription = null, tint = Color.White) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

/* ------------------------- DrawerItem helper ------------------------- */

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
            if (icon != null) {
                Icon(icon, contentDescription = title, tint = Color.White)
            }
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

/* ------------------------- CustomSearchBar ------------------------- */

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
            onValueChange = { value -> onQueryChange(value) },
            placeholder = { Text("Buscar en LEVEL-UP GAMER", color = Color.Gray, fontSize = 14.sp) },
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
