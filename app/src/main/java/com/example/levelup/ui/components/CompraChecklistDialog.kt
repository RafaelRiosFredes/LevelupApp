package com.example.levelup.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import kotlinx.coroutines.delay

@Composable
fun CompraChecklistDialog(
    onDismiss: () -> Unit,
    onAnimationFinished: () -> Unit
) {
    var paso1 by remember { mutableStateOf(false) } // Verifica datos
    var paso2 by remember { mutableStateOf(false) } // Procesa en servidor
    var paso3 by remember { mutableStateOf(false) } // Compra Exitosa

    // secuencia de tiempo para la animación
    LaunchedEffect(Unit) {
        delay(600) // Simular tiempo de validación
        paso1 = true

        delay(800) //  conexión segura
        paso2 = true

        delay(800) // Finalizando
        paso3 = true

        delay(1000)
        onAnimationFinished()
    }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = JetBlack),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, GamerGreen, RoundedCornerShape(16.dp))
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Procesando Compra...",
                    color = GamerGreen,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp).align(Alignment.CenterHorizontally)
                )


                ChecklistItem(text = "Verificando stock y datos...", isCompleted = paso1)

                Spacer(modifier = Modifier.height(16.dp))

                ChecklistItem(text = "Conectando con servidor seguro...", isCompleted = paso2)

                Spacer(modifier = Modifier.height(16.dp))

                ChecklistItem(text = "Generando boleta electrónica...", isCompleted = paso3)

                // Mensaje final
                AnimatedVisibility(
                    visible = paso3,
                    enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "¡TODO LISTO!",
                            color = GamerGreen,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChecklistItem(text: String, isCompleted: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isCompleted) GamerGreen else Color.Gray,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))

        val colorTexto = if (isCompleted) PureWhite else Color.Gray

        Text(
            text = text,
            color = colorTexto,
            fontSize = 16.sp,
            fontWeight = if (isCompleted) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}