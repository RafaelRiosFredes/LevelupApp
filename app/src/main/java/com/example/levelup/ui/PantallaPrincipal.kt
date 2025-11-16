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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineSeatReclineExtra
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Mouse
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.levelup.R
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen


data class Categoria(
    val nombre: String,
    val icono: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun PantallaPrincipal(
    navController: NavHostController,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {

    // =====================================================
    //       DRAWER GLOBAL (maneja el menú lateral)
    // =====================================================
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black
                    )
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
                CategoriasSection()
            }
        }
    }
}

@Composable
fun BannerPrincipal() {
    val imagenes = listOf(
        R.drawable.principal1,
        R.drawable.principal2
    )

    var currentIndex by remember { mutableStateOf(0) }

    // Rotación automática
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {

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
fun CategoriasSection() {

    val categorias = listOf(
        Categoria("Juegos de Mesa", Icons.Default.Casino),
        Categoria("Accesorios", Icons.Default.Headphones),
        Categoria("Consolas", Icons.Default.VideogameAsset),
        Categoria("Computadores Gamer", Icons.Default.Computer),
        Categoria("Sillas Gamer", Icons.Default.AirlineSeatReclineExtra),
        Categoria("Mouse", Icons.Default.Mouse),
        Categoria("Mousepad", Icons.Default.TouchApp),
        Categoria("Poleras Personalizadas", Icons.Default.Checkroom),
        Categoria("Poleras Gamer", Icons.Default.CatchingPokemon)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "CATEGORÍAS",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(categorias) { categoria ->
                CategoriaCard(categoria)
            }
        }
    }
}

@Composable
fun CategoriaCard(categoria: Categoria) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
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
