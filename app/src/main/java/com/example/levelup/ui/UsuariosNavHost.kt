package com.example.levelup.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.viewModel.LoginViewModel
import com.example.levelup.viewModel.LoginViewModelFactory


@Composable
fun UsuariosNavHost (vm: LoginViewModel) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "form") {
        composable("form") {
            LoginScreen(
                vm = vm
            )
        }
    }
}