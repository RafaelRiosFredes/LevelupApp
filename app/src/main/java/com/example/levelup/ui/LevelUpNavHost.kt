package com.example.levelup.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.ProductosViewModelFactory
import com.example.levelup.viewmodel.UsuariosViewModel
import com.example.levelup.viewmodel.UsuariosViewModelFactory
import com.example.levelup_gamerapp.ui.LoginScreen

@Composable
fun LevelUpNavHost() {
    val navController = rememberNavController()
    val app = LocalContext.current.applicationContext as Application

    val productosVM: ProductosViewModel = viewModel(factory = ProductosViewModelFactory(app))
    val usuariosVM: UsuariosViewModel = viewModel(factory = UsuariosViewModelFactory(app))

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            LoginScreen(
                navController = navController,
                onNavigate = {
                    navController.navigate("registro")
                },
                onLogout = {
                    // ejemplo: vuelve al login desde cualquier lado
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }


        // 🟦 REGISTRO DE USUARIO
        composable("registro") {
            FormScreen(
                vm = usuariosVM,
                onSaved = {
                    // una vez registrado, volvemos al login
                    navController.navigate("login") {
                        popUpTo("registro") { inclusive = true }
                    }
                }
            )
        }

        // 🟪 PANTALLA PRINCIPAL DE PRODUCTOS (cliente)
        composable("productos") {
            ProductosScreen(
                nav = navController,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 🟥 INVENTARIO ADMIN
        composable("inventario") {
            InventarioScreen(
                productosViewModel = productosVM,
                onAgregarClick = { navController.navigate("agregarProducto") },
                onEditarClick = { id -> navController.navigate("editarProducto/$id") },
                onNavigate = { destino -> navController.navigate(destino) },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("productos") { inclusive = true }
                    }
                }
            )
        }

        // 🟨 AGREGAR PRODUCTO
        composable("agregarProducto") {
            AddProductScreen(
                productosViewModel = productosVM,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        // 🟧 EDITAR PRODUCTO
        composable(
            "editarProducto/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStack ->
            val id = backStack.arguments?.getInt("id") ?: 0
            EditProductoScreen(
                productosViewModel = productosVM,
                productId = id,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        // 🟫 DETALLE DE PRODUCTO
        composable(
            "producto/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStack ->
            val id = backStack.arguments?.getInt("id") ?: 0
            ProductoScreen(
                id = id,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
