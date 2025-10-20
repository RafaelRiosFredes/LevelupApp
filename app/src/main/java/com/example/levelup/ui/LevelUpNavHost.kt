package com.example.levelup.ui

import android.app.Application
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.viewmodel.CategoriaViewModel
import com.example.levelup.viewmodel.CategoriaViewModelFactory
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.ProductosViewModelFactoryApp

@Composable
fun LevelUpNavHost(modifier: Modifier = Modifier,navController: NavHostController = rememberNavController()) {

    val app = LocalContext.current.applicationContext as Application
    val productosViewModel: ProductosViewModel = viewModel(factory = ProductosViewModelFactoryApp(app))

    val categoriaVm: CategoriaViewModel = viewModel(factory = CategoriaViewModelFactory(app))

    NavHost(
        navController = navController,
        startDestination = "index",
        modifier = modifier
    ) {
        composable("index") {
            // Paso lambdas para que PantallaPrincipal pueda navegar usando este NavController
            PantallaPrincipal(
                vm = categoriaVm,
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    // si tienes login en otra branch, navega a "login" o haz lo que haga falta
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }

        composable("inventario") {
            InventarioScreen(
                productosViewModel = productosViewModel,
                onAgregarClick = { navController.navigate("agregar") },
                onEditarClick = { id -> navController.navigate("editar/$id") },
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }

        composable("agregarProducto") {
            AddProductScreen(
                productosViewModel = productosViewModel,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        composable("editar/{productId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("productId")?.toIntOrNull() ?: 0
            EditProductoScreen(
                productosViewModel = productosViewModel,
                productId = id,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
                )
        }

        composable("adminUsuarios") {
            // PantallaUsuarios() // implementa en otra branch
        }

        composable("adminCategorias") {
            //PantallaCategorias(vm = categoriaVm)
        }


        // Pantalla de registro
        composable("registro") {
            RegistroUsuarioScreen(navController)
        }


        composable("login") {
            // LoginScreen(...)  // si existe
        }
        composable("usuarios") {
            //UsuariosScreen()
        }

        composable("productos") {
            // LoginScreen(...)  // si existe
        }

        composable("contacto") {
            // LoginScreen(...)  // si existe
        }

        composable("noticias") {
            // LoginScreen(...)  // si existe
        }
    }
}

