package com.example.levelup.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.ui.producto.ProductoDetalleScreen
import com.example.levelup.ui.producto.ProductosScreen

@Composable
fun LevelUpNavHost() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "productos") {

        composable("productos") {
            ProductosScreen(onNavigateBack = { nav.popBackStack() }) { route ->
                nav.navigate(route)
            }
        }

        composable("producto/{productoId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("productoId")?.toInt() ?: 0
            ProductoDetalleScreen(productoId = id, nav = nav)
        }
    }
}
