package com.example.levelup.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.viewmodel.ProductosViewModel


@Composable
fun ProductosNavHost () {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "productos"
    ) {
        composable("productos") {
            ProductosScreen()
        }
    }
}
