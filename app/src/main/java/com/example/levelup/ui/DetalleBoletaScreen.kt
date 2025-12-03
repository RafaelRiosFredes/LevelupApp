@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
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
fun BoletaDetalleScreen(
    navController: NavController,
    boletaId: Long,
    boletaViewModel: BoletaViewModel
) {
    var boleta by remember { mutableStateOf<BoletaRemoteDTO?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(boletaId) {
        try {
            loading = true
            boleta = boletaViewModel.obtenerBoletaId(boletaId)
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    DrawerGlobal(navController = navController) {

        when {
            loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(JetBlack),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GamerGreen)
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(JetBlack),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error al cargar boleta: $error",
                        color = Color.Red
                    )
                }
            }

            boleta != null -> {
                val b = boleta!!
                val subtotalCalculado = b.detalles.sumOf { it.subtotal }
                val descuento = b.descuento
                val totalFinal = b.total

                Scaffold(
                    containerColor = JetBlack,
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    "Boleta #${b.idBoleta}",
                                    color = GamerGreen,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = JetBlack
                            )
                        )
                    }
                ) { padding ->

                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {

                        // Datos cliente / totales
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Datos del Cliente",
                                    color = GamerGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(Modifier.height(8.dp))

                                Text("Fecha: ${b.fechaEmision}", color = PureWhite)
                                Text("Cliente: ${b.nombreUsuario}", color = PureWhite)

                                Spacer(Modifier.height(12.dp))

                                Text(
                                    "Subtotal calculado: $${subtotalCalculado}",
                                    color = PureWhite
                                )
                                Text(
                                    "Subtotal original: $${b.totalSinDescuento}",
                                    color = PureWhite
                                )

                                Text(
                                    "Descuento DUOC aplicado: ${if (b.descuentoDuocAplicado) "Sí" else "No"}",
                                    color = if (b.descuentoDuocAplicado) GamerGreen else Color.Red,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    "Descuento: $descuento%",
                                    color = if (descuento > 0) GamerGreen else PureWhite
                                )
                            }
                        }

                        // Detalle productos
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Productos",
                                    color = GamerGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(Modifier.height(8.dp))

                                b.detalles.forEach { d ->
                                    Text(
                                        "- ${d.nombreProducto} x${d.cantidad} = $${d.subtotal}",
                                        color = PureWhite,
                                        fontSize = 15.sp
                                    )
                                    Spacer(Modifier.height(4.dp))
                                }
                            }
                        }

                        Text(
                            "TOTAL FINAL: $${totalFinal}",
                            color = GamerGreen,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(Modifier.height(20.dp))

                        Button(
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GamerGreen,
                                contentColor = JetBlack
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .height(48.dp)
                        ) {
                            Text("Volver", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
