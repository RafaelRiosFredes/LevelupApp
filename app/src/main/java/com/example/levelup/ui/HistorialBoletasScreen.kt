@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.levelup.remote.BoletaRemoteDTO
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.BoletaViewModel

@Composable
fun HistorialBoletasScreen(
    navController: NavController,
    boletaViewModel: BoletaViewModel
) {
    DrawerGlobal(navController = navController) {

        // 🔥 Observar historial desde el ViewModel
        val historial by boletaViewModel.historial.collectAsState()

        // 🔥 Llamar al backend cuando entras a esta pantalla
        LaunchedEffect(Unit) {
            boletaViewModel.obtenerBoletas()
        }

        Scaffold(
            containerColor = JetBlack,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text("Historial de Boletas", color = GamerGreen, fontSize = 22.sp)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = GamerGreen)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = JetBlack)
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {

                if (historial.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay boletas registradas", color = PureWhite)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {

                        items(historial) { boleta ->
                            BoletaHistorialCard(
                                boleta = boleta,
                                onClick = {
                                    navController.navigate("boleta_detalle/${boleta.idBoleta}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BoletaHistorialCard(
    boleta: BoletaRemoteDTO,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Boleta #${boleta.idBoleta}",
                    color = GamerGreen,
                    fontSize = 18.sp
                )
                Icon(
                    Icons.Default.ReceiptLong,
                    contentDescription = null,
                    tint = GamerGreen
                )
            }

            Spacer(Modifier.height(6.dp))

            Text("Cliente: ${boleta.nombreUsuario}", color = PureWhite)
            Text("Fecha: ${boleta.fechaEmision}", color = Color.Gray, fontSize = 13.sp)

            Spacer(Modifier.height(6.dp))

            Text(
                "Total: $${boleta.total}",
                color = GamerGreen,
                fontSize = 18.sp
            )
        }
    }
}
