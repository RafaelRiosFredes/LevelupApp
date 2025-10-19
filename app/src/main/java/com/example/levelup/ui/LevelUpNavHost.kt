package com.example.levelup.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.viewmodel.LoginViewModel

@Composable
fun LevelUpNavHost(vm: LoginViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(vm, navController)
        }


        composable("registro") {
            // RegistroUsuarioScreen(navController)
        }


        composable("index") {
           // IndexScreen()
        }
    }
}
