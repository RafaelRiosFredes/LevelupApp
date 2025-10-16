package com.example.levelup.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.ui.producto.ProductoDetalleScreen
import com.example.levelup.ui.producto.ProductosScreen

@Composable
fun LevelUpNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "productos",
        modifier = modifier // Aquí aplicamos el padding del Scaffold
    ) {
        // Pantalla principal de productos
        composable("productos") {
            ProductosScreen(
                onNavigateBack = { navController.popBackStack() },
                nav = { route ->
                    navController.navigate(route)
                }
            )
        }

        // Pantalla de detalle de producto
        composable("producto/{productoId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("productoId")?.toInt() ?: 0
            ProductoDetalleScreen(
                productoId = id,
                nav = navController
            )
        }
    }
}
