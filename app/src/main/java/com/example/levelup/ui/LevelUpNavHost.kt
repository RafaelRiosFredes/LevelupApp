package com.example.levelup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun LevelUpNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "registro" // Pantalla inicial
    ) {
        // Pantalla de registro
        composable("registro") {
            RegistroUsuarioScreen(navController)
        }

        // Pantalla principal
        composable("index") {
             // IndexScreen(navController)
        }
    }
}

