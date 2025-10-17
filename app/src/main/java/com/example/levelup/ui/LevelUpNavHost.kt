package com.example.levelup.ui

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.levelup.ui.producto.CarritoScreen
import com.example.levelup.ui.producto.ProductoScreen
import com.example.levelup.viewmodel.ProductoViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun LevelUpNavHost(
    navController: NavHostController,
    viewModel: ProductoViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    NavHost(navController = navController, startDestination = "productos") {
        composable("productos") {
            ProductoScreen(
                navController = navController,
                viewModel = viewModel,
                drawerState = drawerState,
                scope = scope
            )
        }
        composable("carrito") {
            CarritoScreen(
                navController = navController,
                viewModel = viewModel,
                drawerState = drawerState,
                scope = scope
            )
        }
    }
}
