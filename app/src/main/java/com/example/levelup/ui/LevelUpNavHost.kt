package com.example.levelup.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.ProductosViewModelFactoryApp
import com.example.levelup.viewmodel.UsuariosViewModel
import com.example.levelup.viewmodel.UsuariosViewModelFactoryApp

@Composable
fun LevelUpNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // create the ProductosViewModel using our Application-backed factory
    val app = androidx.compose.ui.platform.LocalContext.current.applicationContext as Application
    val productosViewModel: ProductosViewModel = viewModel(factory = ProductosViewModelFactoryApp(app))
    val usuariosViewModel: UsuariosViewModel = viewModel(factory = UsuariosViewModelFactoryApp(app))

    NavHost(
        navController = navController,
        startDestination = "usuarios",
        modifier = modifier
    ) {

        composable("categorias") {
            //CategoriasScreen()
        }

        composable("usuarios") {
            UsuariosScreen(
                usuariosViewModel = usuariosViewModel,
                onAgregarClick = { navController.navigate("agregarUsuario") },
                onEditarClick = { id -> navController.navigate("editarUsuario/$id") },
                onNavigate = { route ->
                    navController.navigate(route){
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    navController.navigate("login"){
                        popUpTo(navController.graph.startDestinationId){inclusive = true}
                    }
                }
            )
        }

        composable("editarUsuario/{userId}") { back ->
            val id = back.arguments?.getString("userId")?.toIntOrNull() ?: 0
            EditUsuarioScreen(
                usuariosViewModel = usuariosViewModel,
                userId = id,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        composable("agregarUsuario") {
            AddUsuarioScreen(
                usuariosViewModel = usuariosViewModel,
                onSaved = {navController.popBackStack()},
                onCancel = {navController.popBackStack()}
            )
        }

    }
}
