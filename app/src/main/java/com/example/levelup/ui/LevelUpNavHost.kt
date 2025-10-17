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
            //   onNavigateToRegistroUsuario() {
                    navController.navigate("registro")
                }
            }
            // ruta de "registro" cuando apreta "¿aun sin cuenta?"
        composable("registro") {
           // RegistroUsuarioScreen(navController)
            }
        }
        // pantalla que muestra al iniciar sesion
       // composable("index") {
            //paginadeindex()
         }
    // }
 //}
