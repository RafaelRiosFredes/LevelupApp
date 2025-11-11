package com.example.levelup.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.UsuariosViewModel
import com.example.levelup_gamerapp.ui.LoginScreen

@Composable
fun LevelUpNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    productosViewModel: ProductosViewModel,
    usuariosViewModel: UsuariosViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "PantallaPrincipal",
        modifier = modifier
    ) {

        // 🏠 Pantalla principal
        composable("PantallaPrincipal") {
            PantallaPrincipal(
                onNavigate = { route -> navController.navigate(route) },
                onLogout = { navController.navigate("login") }
            )
        }

        // 🔐 Login
        composable("login") {
            LoginScreen(navController = navController)
        }

        // 📝 Registro de usuario
        composable("registro") {
            RegistroScreen(
                vm = usuariosViewModel,
                navController = navController,
                onSaved = { navController.navigate("PantallaPrincipal") },

            )
        }

        // 👤 Gestión de usuarios
        composable("add_usuario") {
            AddUsuarioScreen(
                usuariosViewModel = usuariosViewModel,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        composable("edit_usuario/{userId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 0
            EditUsuarioScreen(
                usuariosViewModel = usuariosViewModel,
                userId = id,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        // 🛒 Catálogo de productos
        composable("productos") {
            ProductosScreen(
                productosViewModel = productosViewModel,
                nav = navController,
                onNavigate = { route -> navController.navigate(route) }
            )
        }


        // 🔍 Detalle de producto
        composable("producto/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            ProductoScreen(
                productosViewModel = productosViewModel,
                id = id,
                onNavigateBack = { navController.popBackStack() }
            )

        }

        // ⚙️ Inventario
        composable("inventario") {
            InventarioScreen(
                productosViewModel = productosViewModel,
                onAgregarClick = { navController.navigate("add_producto") },
                onEditarClick = { id -> navController.navigate("edit_producto/$id") }
            )
        }

        composable("add_producto") {
            AddProductScreen(
                productosViewModel = productosViewModel,
                onSaved = { navController.navigate("inventario") },
                onCancel = { navController.popBackStack() }
            )
        }

        composable("edit_producto/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            EditProductoScreen(
                productosViewModel = productosViewModel,
                productId = id,
                onSaved = { navController.navigate("inventario") },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}