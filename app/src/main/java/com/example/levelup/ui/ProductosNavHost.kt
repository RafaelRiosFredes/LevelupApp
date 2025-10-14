package com.example.levelup.ui

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.viewmodel.ProductosViewModel

fun ProductosNavHost (vm: ProductosViewModel) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "lista") {
        composable("lista") {
            ProductosScreen()
        }
    }
}
