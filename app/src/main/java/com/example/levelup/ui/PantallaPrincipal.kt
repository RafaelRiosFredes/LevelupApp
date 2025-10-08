@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.levelup.ui



import androidx.compose.foundation.Image

import androidx.compose.foundation.background

import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.grid.GridCells

import androidx.compose.foundation.lazy.grid.LazyVerticalGrid

import androidx.compose.foundation.lazy.grid.items

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.stringResource

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp

import coil.compose.rememberAsyncImagePainter

import com.example.levelup.R



// -------------------------

// MODELO DE DATOS

// -------------------------

 data class Categoria(

    val nombre: String,

    val iconUrl: String

)



// -------------------------

// PANTALLA PRINCIPAL

// -------------------------

@OptIn(ExperimentalMaterial3Api::class)

@Composable

fun PantallaPrincipal() {

    Scaffold(

        topBar = { BarraSuperior() },

        containerColor = Color.Black

    ) { innerPadding ->

        Column(

            modifier = Modifier

                .fillMaxSize()

                .padding(innerPadding)

                .background(Color.Black)

        ) {

            BannerPrincipal()

            CategoriasGrid()

            PieDePagina()

        }

    }

}



// -------------------------

// BARRA SUPERIOR

// -------------------------

@Composable

fun BarraSuperior() {

    CenterAlignedTopAppBar(

        title = {

            Text(

                text = stringResource(R.string.app_name),

                color = Color(0xFF39FF14),

                fontWeight = FontWeight.Bold

            )

        },

        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(

            containerColor = Color.Black

        )

    )

}



// -------------------------

// BANNER PRINCIPAL

// -------------------------

@Composable

fun BannerPrincipal() {

    Box(

        modifier = Modifier

            .fillMaxWidth()

            .height(220.dp)

    ) {

        Image(

            painter = rememberAsyncImagePainter(

                "https://www.azernews.az/media/2023/11/27/2023_rog_zephyrus_duo_16_gx650_scenario_photo_01.jpg?v=1701092248"

            ),

            contentDescription = null,

            modifier = Modifier.fillMaxSize(),

            contentScale = ContentScale.Crop

        )



        Text(

            text = stringResource(R.string.slogan),

            color = Color(0xFF00AAFF),

            fontWeight = FontWeight.Bold,

            fontSize = 18.sp,

            textAlign = TextAlign.Center,

            modifier = Modifier

                .align(Alignment.Center)

                .padding(horizontal = 12.dp)

        )

    }

}



// -------------------------

// GRID DE CATEGORÍAS

// -------------------------

@Composable

fun CategoriasGrid() {

    val categorias = listOf(

        Categoria("Juegos de Mesa", "https://img.icons8.com/ios-filled/100/dice.png"),

        Categoria("Accesorios", "https://img.icons8.com/ios-filled/100/controller.png"),

        Categoria("Consolas", "https://img.icons8.com/ios-filled/100/device.png"),

        Categoria("Computadores Gamer", "https://img.icons8.com/ios-filled/100/computer.png"),

        Categoria("Sillas Gamer", "https://img.icons8.com/?size=100&id=cnYTrlcPnC0e&format=png&color=39ff14"),

        Categoria("Mouse", "https://img.icons8.com/ios-filled/100/mouse.png"),

        Categoria("Mousepad", "https://img.icons8.com/?size=100&id=k2sKvYhF2GC8&format=png&color=39ff14"),

        Categoria("Poleras Personalizadas", "https://img.icons8.com/?size=100&id=N757ereBOFWm&format=png&color=39ff14"),

        Categoria("Poleras Gamer Personalizadas", "https://img.icons8.com/?size=100&id=aprOfFsRz9MN&format=png&color=39ff14")

    )



    Text(

        text = stringResource(R.string.categorias),

        fontSize = 22.sp,

        fontWeight = FontWeight.Bold,

        color = Color.White,

        modifier = Modifier.padding(16.dp)

    )



    LazyVerticalGrid(

        columns = GridCells.Fixed(2),

        contentPadding = PaddingValues(12.dp),

        horizontalArrangement = Arrangement.spacedBy(12.dp),

        verticalArrangement = Arrangement.spacedBy(12.dp)

    ) {

        items(categorias) { categoria ->

            CategoriaCard(categoria)

        }

    }

}



// -------------------------

// CARD DE CATEGORÍA

// -------------------------

@Composable

fun CategoriaCard(categoria: Categoria) {

    Card(

        modifier = Modifier

            .fillMaxWidth()

            .height(140.dp),

        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))

    ) {

        Column(

            modifier = Modifier

                .fillMaxSize()

                .padding(8.dp),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Center

        ) {

            Image(

                painter = rememberAsyncImagePainter(categoria.iconUrl),

                contentDescription = categoria.nombre,

                modifier = Modifier.size(55.dp)

            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(

                text = categoria.nombre,

                color = Color.White,

                fontSize = 14.sp,

                textAlign = TextAlign.Center

            )

        }

    }

}


// -------------------------

// FOOTER

// -------------------------

@Composable

fun PieDePagina() {

    Box(

        modifier = Modifier

            .fillMaxWidth()

            .background(Color.Black)

            .padding(16.dp)

    ) {

        Text(

            text = stringResource(R.string.footer),

            color = Color.White,

            textAlign = TextAlign.Center,

            fontSize = 12.sp,

            modifier = Modifier.fillMaxWidth()

        )

    }

}