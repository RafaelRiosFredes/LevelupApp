package com.example.levelup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.BoletaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoletaDetalleScreen(
    boletaId: Long,
    boletaViewModel: BoletaViewModel,
    onVolver: () -> Unit
) {

    val boleta by boletaViewModel.obtenerBoleta(boletaId).collectAsState(initial = null)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle Boleta", color = GamerGreen, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = GamerGreen
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = JetBlack)
            )
        },
        containerColor = JetBlack
    ) { padding ->

        if (boleta == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GamerGreen)
            }
            return@Scaffold
        }

        val fechaFormateada = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            .format(Date(boleta!!.fecha))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🌟 TARJETA ELEGANTE
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF111111)),
                shape = RoundedCornerShape(16.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(20.dp)
                ) {

                    // ====== ENCABEZADO DE LA BOLETA ======
                    Text(
                        text = "Boleta #${boleta!!.id}",
                        color = GamerGreen,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    Divider(color = GamerGreen.copy(alpha = 0.4f), thickness = 1.dp)

                    Spacer(Modifier.height(12.dp))


                    // ====== FECHA ======
                    Text("📅 Fecha", color = GamerGreen, fontWeight = FontWeight.SemiBold)
                    Text(fechaFormateada, color = PureWhite, fontSize = 15.sp)

                    Spacer(Modifier.height(16.dp))

                    // ====== TOTAL ======
                    Text("💵 Total", color = GamerGreen, fontWeight = FontWeight.SemiBold)
                    Text(
                        "$${boleta!!.total}",
                        color = PureWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(16.dp))


                    // ====== PRODUCTOS ======
                    Text("🛒 Productos comprados", color = GamerGreen, fontWeight = FontWeight.SemiBold)

                    Spacer(Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1A1A1A), RoundedCornerShape(10.dp))
                            .border(1.dp, GamerGreen.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = boleta!!.detalle.replace("\n", "\n• "),
                            color = PureWhite,
                            fontSize = 15.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(30.dp))

            // BOTÓN VOLVER ELEGANTE
            Button(
                onClick = onVolver,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GamerGreen,
                    contentColor = JetBlack
                )
            ) {
                Text("Volver", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
