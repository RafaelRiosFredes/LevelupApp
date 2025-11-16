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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoletaDetalleScreen(
    navController: NavController,    // ← AGREGADO
    boletaId: Long,
    boletaViewModel: BoletaViewModel,
    onVolver: () -> Unit
) {
    DrawerGlobal({   // ← ENVUELTO EN DRAWER

        val boleta by boletaViewModel.obtenerBoleta(boletaId.toInt())
            .collectAsState(initial = null)

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

                // ---------- Datos del cliente ----------
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
                        Text("Cliente: ${boleta!!.usuarioNombre} ${boleta!!.usuarioApellidos}", color = PureWhite)
                        Text("Correo: ${boleta!!.usuarioCorreo}", color = PureWhite)
                    }
                }

                // ---------- Detalle ----------
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

                        detalles.forEach {
                            if (it.isNotBlank()) {
                                val p = it.split("|")
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

                // ---------- Total ----------
                Text(
                    "TOTAL: $${boleta!!.total}",
                    color = GamerGreen,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(Modifier.height(20.dp))

                // ---------- Volver ----------
                Button(
                    onClick = onVolver,
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
    })
}
