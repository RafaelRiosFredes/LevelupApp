package com.example.levelup.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.levelup.core.UserSession
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.UsuariosViewModel
import com.example.levelup.viewmodel.CarritoViewModel
import com.example.levelup.viewmodel.BoletaViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.levelup.viewmodel.BoletaViewModelFactoryApp
import com.example.levelup.viewmodel.CarritoViewModelFactory

@Composable
fun LevelUpNavHost(
    navController: NavHostController,
    productosViewModel: ProductosViewModel,
    usuariosViewModel: UsuariosViewModel
) {

    val context = LocalContext.current

    val carritoViewModel: CarritoViewModel = viewModel(factory = CarritoViewModelFactory(context.applicationContext as Application))
    val boletaViewModel: BoletaViewModel = viewModel(factory = BoletaViewModelFactoryApp(context.applicationContext as Application))

    NavHost(
        navController = navController,
        startDestination = "PantallaPrincipal"
    ) {

        // -------------------------------
        // PANTALLAS DEL USUARIO NORMAL
        // -------------------------------

        composable("PantallaPrincipal") {
            PantallaPrincipal(
                navController = navController,
                onNavigate = { navController.navigate(it) },
                onLogout = {
                    UserSession.clear()
                    navController.navigate("login") { popUpTo(0) }
                }
            )
        }

        composable("productos") {
            ProductosScreen(
                navController = navController,
                productosViewModel = productosViewModel
            )
        }

        composable("carrito") {
            CarritoScreen(
                navController = navController,
                carritoViewModel = carritoViewModel,
                boletaViewModel = boletaViewModel,
                usuariosViewModel = usuariosViewModel
            )
        }

        composable("login") {
            LoginScreen(
                navController = navController,
                usuariosViewModel = usuariosViewModel
            )
        }

        composable("registro") {
            RegistroScreen(
                navController = navController,
                usuariosViewModel = usuariosViewModel
            )
        }

        composable("noticias") {
            NoticiasScreen(navController = navController)
        }

        composable("contacto") {
            ContactoScreen(navController = navController)
        }



        // -------------------------------------
        // ADMIN: INVENTARIO DE PRODUCTOS
        // -------------------------------------

        composable("inventario") {
            InventarioScreen(
                productosViewModel = productosViewModel,
                currentUserRol = UserSession.rol ?: "user",
                onAgregarClick = { navController.navigate("add_producto") },
                onEditarClick = { id -> navController.navigate("edit_producto/$id") }
            )
        }

        composable("add_producto") {
            AddProductScreen(
                navController = navController,
                productosViewModel = productosViewModel
            )
        }

        composable("edit_producto/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toIntOrNull() ?: 0
            EditProductoScreen(
                navController = navController,
                productosViewModel = productosViewModel,
                productoId = id
            )
        }

        // -------------------------------------
        // ADMIN: GESTIÓN DE USUARIOS
        // -------------------------------------

        composable("admin_usuarios") {
            UsuariosAdminScreen(
                navController = navController,
                usuariosViewModel = usuariosViewModel,
                onAgregarClick = { navController.navigate("add_usuario") },
                onEditarClick = { id -> navController.navigate("edit_usuario/$id") }
            )
        }

        composable("add_usuario") {
            AddUsuarioScreen(
                navController = navController,
                usuariosViewModel = usuariosViewModel
            )
        }

        composable("edit_usuario/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toIntOrNull() ?: 0
            EditUsuarioScreen(
                usuariosViewModel = usuariosViewModel,
                currentUserRol = UserSession.rol ?: "user",
                userId = id,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        // -------------------------------------
        // DETALLE DE BOLETA
        // -------------------------------------
        composable("boleta_detalle/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toIntOrNull() ?: 0

            BoletaDetalleScreen(
                navController = navController,
                boletaViewModel = boletaViewModel,
                boletaId = id
            )
        }
    }
}
