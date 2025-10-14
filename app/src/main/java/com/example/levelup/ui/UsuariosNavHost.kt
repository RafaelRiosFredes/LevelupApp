package com.example.levelup.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.viewModel.LoginViewModel


@Composable
fun UsuariosNavHost (vm : LoginViewModel) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "lista") {
        composable("lista") {
            LoginScreen()
        }
    }
}