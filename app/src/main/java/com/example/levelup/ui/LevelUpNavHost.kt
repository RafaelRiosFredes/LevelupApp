package com.example.levelup.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.levelup.viewmodel.*
import com.example.levelup.ui.LoginScreen
import com.example.levelup.core.UserSession

fun requireAdmin(): Boolean {
    return UserSession.rol.lowercase() == "admin"
}

@Composable
fun LevelUpNavHost(
    navController: NavHostController,
    productosViewModel: ProductosViewModel,
    usuariosViewModel: UsuariosViewModel,
    modifier: Modifier = Modifier
) {

    // ViewModels globales
    val carritoViewModel: CarritoViewModel = viewModel(
        factory = CarritoViewModelFactory(navController.context.applicationContext as Application)
    )

    val boletaViewModel: BoletaViewModel = viewModel(
        factory = BoletaViewModelFactory(navController.context.applicationContext as Application)
    )


    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {

        // =====================================================
        // ===============  PANTALLAS PÚBLICAS  =================
        // =====================================================

        composable("login") {
            LoginScreen(navController = navController)
        }

        composable("PantallaPrincipal") {
            PantallaPrincipal(
                onNavigate = { route -> navController.navigate(route) },
                onLogout = {
                    UserSession.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
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

        composable("contacto") {
            ContactoScreen(onNavigate = { navController.navigate(it) })
        }

        composable("noticias") {
            NoticiasScreen(onNavigate = { navController.navigate(it) })
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
                carritoViewModel = carritoViewModel,
                boletaViewModel = boletaViewModel
            )
        }

        composable("boleta_detalle/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0L
            BoletaDetalleScreen(
                boletaId = id,
                boletaViewModel = boletaViewModel,
                onVolver = { navController.navigate("PantallaPrincipal") }
            )
        }


        // ======================================================
        // ===================  ZONA ADMIN  ======================
        // ======================================================

        composable("inventario") {
            if (!requireAdmin()) {
                navController.navigate("PantallaPrincipal")
            } else {
                InventarioScreen(
                    productosViewModel = productosViewModel,
                    currentUserRol = UserSession.rol,
                    onAgregarClick = { navController.navigate("add_producto") },
                    onEditarClick = { id -> navController.navigate("edit_producto/$id") }
                )
            }
        }

        composable("add_producto") {
            if (!requireAdmin()) {
                navController.navigate("PantallaPrincipal")
            } else {
                AddProductScreen(
                    productosViewModel = productosViewModel,
                    currentUserRol = UserSession.rol,
                    onSaved = { navController.navigate("inventario") },
                    onCancel = { navController.popBackStack() }
                )
            }
        }

        composable("edit_producto/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0

            if (!requireAdmin()) {
                navController.navigate("PantallaPrincipal")
            } else {
                EditProductoScreen(
                    productosViewModel = productosViewModel,
                    currentUserRol = UserSession.rol,
                    productId = id,
                    onSaved = { navController.navigate("inventario") },
                    onCancel = { navController.popBackStack() }
                )
            }
        }

        // === CRUD USUARIOS SOLO ADMIN ===

        composable("usuarios") {
            if (!requireAdmin()) {
                navController.navigate("PantallaPrincipal")
            } else {
                UsuariosScreen(
                    usuariosViewModel = usuariosViewModel,
                    currentUserRol = UserSession.rol,
                    onNavigate = { navController.navigate(it) },
                    onAgregarClick = { navController.navigate("add_usuario") },
                    onEditarClick = { id -> navController.navigate("edit_usuario/$id") }
                )
            }
        }

        composable("add_usuario") {
            if (!requireAdmin()) {
                navController.navigate("PantallaPrincipal")
            } else {
                AddUsuarioScreen(
                    usuariosViewModel = usuariosViewModel,
                    currentUserRol = UserSession.rol,
                    onSaved = { navController.popBackStack() },
                    onCancel = { navController.popBackStack() }
                )
            }
        }

        composable("edit_usuario/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0

            if (!requireAdmin()) {
                navController.navigate("PantallaPrincipal")
            } else {
                EditUsuarioScreen(
                    usuariosViewModel = usuariosViewModel,
                    currentUserRol = UserSession.rol,
                    userId = id,
                    onSaved = { navController.popBackStack() },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
    }
}
