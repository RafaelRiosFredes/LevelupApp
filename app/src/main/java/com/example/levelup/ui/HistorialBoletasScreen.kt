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
import com.example.levelup.model.data.BoletaEntity
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.BoletaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialBoletasScreen(
    navController: NavController,
    boletaViewModel: BoletaViewModel
) {

    DrawerGlobal(navController = navController) {

        val historial by boletaViewModel.obtenerBoletas()
            .collectAsState(initial = emptyList())

        Scaffold(
            containerColor = JetBlack,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Historial de Boletas",
                            color = GamerGreen,
                            fontSize = 22.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = GamerGreen
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = JetBlack
                    )
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
                        Text(
                            "No hay boletas registradas",
                            color = PureWhite
                        )
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
                                    navController.navigate("boleta_detalle/${boleta.id}")
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
    boleta: BoletaEntity,
    onClick: () -> Unit
) {

    val subtotal = boleta.detalleTexto.split("\n").sumOf {
        val p = it.split("|")
        if (p.size >= 4) p[3].toDouble() else 0.0
    }

    val descuento = boleta.descuento ?: 0
    val totalFinal = boleta.total

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
                    "Boleta #${boleta.backendId ?: boleta.id}",
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

            Text(
                "Cliente: ${boleta.usuarioNombre ?: ""} ${boleta.usuarioApellidos ?: ""}",
                color = PureWhite
            )

            Text(
                "Correo: ${boleta.usuarioCorreo ?: ""}",
                color = PureWhite
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "Subtotal: $${"%.0f".format(subtotal)}",
                color = PureWhite
            )

            Text(
                "Descuento: $descuento%",
                color = if (descuento > 0) GamerGreen else PureWhite
            )

            Text(
                "Total final: $${totalFinal}",
                color = GamerGreen,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "Fecha: ${boleta.fechaEmision}",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}
