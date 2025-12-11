@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.viewmodel.NoticiasViewModel

@Composable
fun NoticiasScreen(
    navController: NavController,
    vm: NoticiasViewModel = viewModel(),
) {

    val noticias by vm.noticias.collectAsState()


    DrawerGlobal(navController = navController) {

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Noticias Gamer",
                            color = GamerGreen,
                            fontSize = 20.sp
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Black
                    )
                )
            },
            containerColor = Color.Black
        ) { padding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(noticias) { noticia ->

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                        ) {


                            Text(
                                text = noticia.titulo,
                                color = Color.White,
                                fontSize = 17.sp
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = noticia.descripcion,
                                color = Color.LightGray,
                                fontSize = 13.sp,
                                maxLines = 2
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                text = "Fuente: ${noticia.fuente}",
                                color = Color(0xFF00AAFF),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
