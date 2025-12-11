@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.levelup.R
import com.example.levelup.remote.CategoriaRemoteDTO
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.viewmodel.ProductosViewModel // Importamos el ViewModel

data class CategoriaUI(
    val id: Long,
    val nombre: String,
    val icono: ImageVector
)

@Composable
fun PantallaPrincipal(
    navController: NavHostController,
    productosViewModel: ProductosViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    // Carga categorías al iniciar
    LaunchedEffect(Unit) {
        productosViewModel.cargarCategorias()
    }
    val categoriasBackend by productosViewModel.categorias.collectAsState()

    DrawerGlobal(navController = navController) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "LevelUp Gamer",
                            color = GamerGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
                )
            },
            containerColor = Color.Black
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.Black)
            ) {
                BannerPrincipal()

                CategoriasSection(
                    categoriasData = categoriasBackend,
                    onCategoryClick = { idCat ->
                        navController.navigate("productos?categoriaId=$idCat")
                    }
                )
            }
        }
    }
}

@Composable
fun BannerPrincipal() {
    val imagenes = listOf(R.drawable.principal1, R.drawable.principal2)
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(4000L)
            currentIndex = (currentIndex + 1) % imagenes.size
        }
    }

    AnimatedContent(
        targetState = currentIndex,
        transitionSpec = {
            slideInHorizontally { it } + fadeIn() togetherWith
                    slideOutHorizontally { -it } + fadeOut()
        },
        label = "banner"
    ) { index ->
        Box(modifier = Modifier.fillMaxWidth().height(220.dp)) {
            Image(
                painter = rememberAsyncImagePainter(imagenes[index]),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "LEVEL UP TU EXPERIENCIA GAMER",
                color = Color(0xFF00AAFF),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color(0xAA000000))
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun CategoriasSection(
    categoriasData: List<CategoriaRemoteDTO>,
    onCategoryClick: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = "CATEGORÍAS",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        if (categoriasData.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GamerGreen)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(categoriasData) { catBackend ->
                    val catUI = CategoriaUI(
                        id = catBackend.idCategoria,
                        nombre = catBackend.nombreCategoria,
                        icono = obtenerIconoPorNombre(catBackend.nombreCategoria)
                    )

                    CategoriaCard(catUI, onClick = { onCategoryClick(catUI.id) })
                }
            }
        }
    }
}

fun obtenerIconoPorNombre(nombre: String): ImageVector {
    val n = nombre.lowercase()
    return when {
        "mesa" in n -> Icons.Default.Casino
        "accesorio" in n -> Icons.Default.Headphones
        "consola" in n -> Icons.Default.VideogameAsset
        "computador" in n || "pc" in n -> Icons.Default.Computer
        "silla" in n -> Icons.Default.AirlineSeatReclineExtra
        "mouse" in n && "pad" !in n -> Icons.Default.Mouse
        "pad" in n || "alfombrilla" in n -> Icons.Default.TouchApp
        "ropa" in n || "polera" in n || "camiseta" in n -> Icons.Default.Checkroom
        else -> Icons.Default.Category // icono por defecto si no coincide nada
    }
}

@Composable
fun CategoriaCard(categoria: CategoriaUI, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = categoria.icono,
                contentDescription = categoria.nombre,
                tint = GamerGreen,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = categoria.nombre,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}