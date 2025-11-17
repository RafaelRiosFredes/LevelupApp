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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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

    DrawerGlobal(navController = navController) {

        val boleta by boletaViewModel.obtenerBoleta(boletaId.toInt())
            .collectAsState(initial = null)

        // ----------- Cargando -----------
        if (boleta == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(JetBlack),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GamerGreen)
            }
            return@DrawerGlobal
        }

        val detalles = boleta!!.detalleTexto.split("\n")

        // ----------- Calcular subtotal (precio unitario * cantidad) -----------
        val subtotalCalculado = detalles.sumOf { linea ->
            val p = linea.split("|")
            if (p.size >= 4) p[3].toDouble() else 0.0
        }

        val descuento = boleta!!.descuento ?: 0
        val totalFinal = boleta!!.total

        Scaffold(
            containerColor = JetBlack,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Boleta #${boleta!!.backendId ?: boleta!!.id}",
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

                // -------------------------------------------------------
                // ---------- Datos del Cliente + Totales Iniciales ------
                // -------------------------------------------------------

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

                        Text("Fecha: ${boleta!!.fechaEmision}", color = PureWhite)

                        Text(
                            "Cliente: ${boleta!!.usuarioNombre ?: ""} ${boleta!!.usuarioApellidos ?: ""}",
                            color = PureWhite
                        )

                        Text(
                            "Correo: ${boleta!!.usuarioCorreo ?: ""}",
                            color = PureWhite
                        )

                        Spacer(Modifier.height(12.dp))

                        // ----- Subtotal calculado -----
                        Text(
                            "Subtotal calculado: $${"%.0f".format(subtotalCalculado)}",
                            color = PureWhite
                        )

                        // ----- Subtotal original (sin descuento) -----
                        Text(
                            "Subtotal original: $${boleta!!.totalSinDescuento}",
                            color = PureWhite
                        )

                        // ----- Descuento DUOC aplicado -----
                        Text(
                            "Descuento DUOC aplicado: ${if (boleta!!.descuentoDuocAplicado) "Sí" else "No"}",
                            color = if (boleta!!.descuentoDuocAplicado) GamerGreen else Color.Red,
                            fontWeight = FontWeight.Medium
                        )

                        // ----- Descuento % -----
                        Text(
                            "Descuento: $descuento%",
                            color = if (descuento > 0) GamerGreen else PureWhite
                        )
                    }
                }

                // -------------------------------------------------------
                // ---------- Detalle de los productos -------------------
                // -------------------------------------------------------

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

                        detalles.forEach { linea ->
                            if (linea.isNotBlank()) {
                                val p = linea.split("|")

                                if (p.size >= 4) {
                                    Text(
                                        "- ${p[1]} x${p[2]} = $${p[3]}",
                                        color = PureWhite,
                                        fontSize = 15.sp
                                    )
                                    Spacer(Modifier.height(4.dp))
                                }
                            }
                        }
                    }
                }

                // -------------------------------------------------------
                // ------------------ Total Final ------------------------
                // -------------------------------------------------------

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
