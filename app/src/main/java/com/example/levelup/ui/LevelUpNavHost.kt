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

@Composable
fun LevelUpNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // create the ProductosViewModel using our Application-backed factory
    val app = androidx.compose.ui.platform.LocalContext.current.applicationContext as Application
    val productosViewModel: ProductosViewModel = viewModel(factory = ProductosViewModelFactoryApp(app))

    NavHost(
        navController = navController,
        startDestination = "inventario",
        modifier = modifier
    ) {
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

        composable("agregar") {
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

        composable("categorias") {
            //CategoriasScreen()
        }

        composable("usuarios") {
            //UsuariosScreen()
        }
    }
}
