package com.example.levelup.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.ui.producto.ProductoDetalleScreen
import com.example.levelup.ui.producto.ProductosScreen

@Composable
fun LevelUpNavHost(modifier: Modifier) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "productos") {
        composable("productos") {
            ProductosScreen(nav = { route -> nav.navigate(route) })
        }
        composable("producto/{productoId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("productoId")?.toInt() ?: 0
            ProductoDetalleScreen(productoId = id, nav = nav)
        }
    }
}
