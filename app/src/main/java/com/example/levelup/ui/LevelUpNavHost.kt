package com.example.levelup.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.levelup.viewmodel.CarritoViewModel
import com.example.levelup.ui.screens.CarritoScreen

@Composable
fun LevelUpNavHost(navController: NavHostController, carritoViewModel: CarritoViewModel) {
    NavHost(navController = navController, startDestination = "productos") {

        // 🟢 Pantalla de Productos
        composable("productos") {
            ProductosScreen(nav = navController)
        }

        // 🛒 Pantalla del Carrito
        composable("carrito") {
            CarritoScreen(nav = navController, viewModel = carritoViewModel)
        }
    }
}
