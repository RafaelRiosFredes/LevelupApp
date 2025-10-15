package com.example.levelup.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.viewmodel.LoginViewModel

@Composable
fun UsuariosNavHost(
    vm: LoginViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController, vm)
        }
        composable("registro") {
            RegistroUsuarioScreen(navController)
        }
        composable("home") {
            PlaceholderScreen(
                texto = "🏠 Bienvenido a LevelUp!",
                onBack = { navController.navigate("login") }
            )
        }
    }
}


