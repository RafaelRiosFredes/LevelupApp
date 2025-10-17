package com.example.levelup.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.levelup.ui.producto.ProductoScreen
import com.example.levelup.viewmodel.ProductoViewModel

@Composable
fun LevelUpNavHost(navController: NavHostController, viewModel: ProductoViewModel) {
    NavHost(navController = navController, startDestination = "productos") {
        composable("productos") {
            ProductoScreen(navController = navController, viewModel = viewModel)
        }
        composable("carrito") {
            CarritoScreen(navController = navController, viewModel = viewModel)
        }
    }
}