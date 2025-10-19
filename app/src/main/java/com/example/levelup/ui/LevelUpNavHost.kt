package com.example.levelup.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun LevelUpNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "noticias"
    ) {
        composable("noticias") {
            NoticiasScreen()
        }
    }
}
