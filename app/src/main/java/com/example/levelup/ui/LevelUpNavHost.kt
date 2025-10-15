package com.example.levelup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.ui.RegistroUsuarioScreen


@Composable
fun LevelUpNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "registro" // 🚀 Pantalla principal temporal
    ) {

        // 🟢 Registro de usuario
        composable("registro") {
            RegistroUsuarioScreen(navController) // ✅ Aquí pasamos el controlador
        }

        // 🔵 Pantalla Login (placeholder por ahora)
        composable("login") {
            PlaceholderScreen(
                texto = "Pantalla de Login (en desarrollo)",
                onBack = { navController.navigate("registro") }
            )
        }

        // 🏠 Pantalla principal o Home (placeholder temporal)
        composable("home") {
            PlaceholderScreen(
                texto = "Pantalla Principal (Home) — En desarrollo",
                onBack = { navController.navigate("registro") }
            )
        }
    }
}


@Composable
fun PlaceholderScreen(
    texto: String,
    onBack: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = texto,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(24.dp))

            onBack?.let {
                OutlinedButton(onClick = it) {
                    Text("Volver")
                }
            }
        }
    }
}
