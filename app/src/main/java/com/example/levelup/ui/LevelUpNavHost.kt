package com.example.levelup.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun LevelUpNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "productos"
    ) {
        composable("productos") {
            ProductosScreen(navController = navController)
        }

        composable("producto/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            if (id != null) {
                ProductosScreen(
                    id = id,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        composable("carrito") {
            PlaceholderScreen("Carrito de Compras")
        }
    }
}

@Composable
fun PlaceholderScreen(texto: String) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(texto)
        }
    }
}