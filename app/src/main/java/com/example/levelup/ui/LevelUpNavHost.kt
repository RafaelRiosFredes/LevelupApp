package com.example.levelup.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.levelup.viewmodel.*
import com.example.levelup_gamerapp.ui.LoginScreen

@Composable
fun LevelUpNavHost(
    navController: NavHostController,
    productosViewModel: ProductosViewModel,
    usuariosViewModel: UsuariosViewModel,
    modifier: Modifier = Modifier
) {

    // ✅ CarritoViewModel global (no se recrea, mantiene el carrito estable)
    val carritoViewModel: CarritoViewModel = viewModel(
        factory = CarritoViewModelFactory(navController.context.applicationContext as Application)
    )

    NavHost(
        navController = navController,
        startDestination = "PantallaPrincipal",
        modifier = modifier
    ) {

        composable("PantallaPrincipal") {
            PantallaPrincipal(
                onNavigate = { route -> navController.navigate(route) },
                onLogout = { navController.navigate("login") }
            )
        }

        composable("login") {
            LoginScreen(navController = navController)
        }

        composable("registro") {
            RegistroScreen(
                vm = usuariosViewModel,
                navController = navController,
                onSaved = { navController.navigate("PantallaPrincipal") }
            )
        }

        composable("productos") {
            ProductosScreen(
                productosViewModel = productosViewModel,
                nav = navController,
                carritoViewModel = carritoViewModel,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable("producto/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            ProductoScreen(
                productosViewModel = productosViewModel,
                carritoViewModel = carritoViewModel,
                id = id,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("carrito") {
            CarritoScreen(
                nav = navController,
                carritoViewModel = carritoViewModel
            )
        }

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
