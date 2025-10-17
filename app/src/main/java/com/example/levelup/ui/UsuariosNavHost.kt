package com.example.levelup.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.viewmodel.LoginViewModel

@Composable
fun UsuariosNavHost(vm: LoginViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // 🔹 Pantalla de Login
        composable("login") {
            LoginScreen(vm) {
                // 👉 Acción cuando el login sea exitoso
                navController.navigate("registro") {
                    popUpTo("registro") { inclusive = true }
                }
            }
        }

        // 🔹 Pantalla Home (después de iniciar sesión)
        composable("registro") {
            //RegistroUsuarioScreen()
        }
    }
}
