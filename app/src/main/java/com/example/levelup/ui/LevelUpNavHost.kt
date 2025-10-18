package com.example.levelup.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.levelup.ui.screens.CarritoScreen
import com.example.levelup.viewmodel.CarritoViewModel

@Composable
fun LevelUpNavHost(navController: NavHostController, carritoViewModel: CarritoViewModel) {
    NavHost(navController = navController, startDestination = "carrito") {
        composable("carrito") {
            CarritoScreen(viewModel = carritoViewModel)
        }
    }
}
