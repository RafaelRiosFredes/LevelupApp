package com.example.levelup_gamerapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.levelup_gamerapp.ui.LoginScreen
import com.example.levelup_gamerapp.ui.RegistroUsuarioScreen

@Composable
fun LevelUpNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("registro") { RegistroUsuarioScreen(navController) }
    }
}
