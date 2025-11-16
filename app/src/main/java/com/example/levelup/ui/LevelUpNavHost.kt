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

    val carritoViewModel: CarritoViewModel =
        viewModel(factory = CarritoViewModelFactory(context.applicationContext as Application))

    val boletaViewModel: BoletaViewModel =
        viewModel(factory = BoletaViewModelFactoryApp(context.applicationContext as Application))

    NavHost(
        navController = navController,
        startDestination = "PantallaPrincipal"
    ) {

        // ----------------------------------------------------
        //                      PRINCIPAL
        // ----------------------------------------------------
        composable("PantallaPrincipal") {
            PantallaPrincipal(
                navController = navController,
                onNavigate = { navController.navigate(it) },
                onLogout = {
                    UserSession.logout()
                    navController.navigate("login") { popUpTo(0) }
                }
            )
        }

        // ----------------------------------------------------
        //                       PRODUCTOS
        // ----------------------------------------------------
        composable("productos") {
            ProductosScreen(
                navController = navController,
                productosViewModel = productosViewModel,
                carritoViewModel = carritoViewModel
            )
        }

        composable("producto/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toIntOrNull() ?: 0
            ProductoScreen(
                navController = navController,
                productosViewModel = productosViewModel,
                carritoViewModel = carritoViewModel,
                id = id
            )
        }

        // ----------------------------------------------------
        //                         CARRITO
        // ----------------------------------------------------
        composable("carrito") {
            CarritoScreen(
                navController = navController,
                carritoViewModel = carritoViewModel,
                boletaViewModel = boletaViewModel,
                usuariosViewModel = usuariosViewModel
            )
        }

        // ----------------------------------------------------
        //                         LOGIN
        // ----------------------------------------------------
        composable("login") {
            LoginScreen(
                navController = navController,
                usuariosViewModel = usuariosViewModel
            )
        }

        // ----------------------------------------------------
        //                      REGISTRO
        // ----------------------------------------------------
        composable("registro") {
            RegistroScreen(
                navController = navController,
                vm = usuariosViewModel
            )
        }

        // ----------------------------------------------------
        //                       NOTICIAS
        // ----------------------------------------------------
        composable("noticias") {
            NoticiasScreen(
                navController = navController
            )
        }

        // ----------------------------------------------------
        //                       CONTACTO
        // ----------------------------------------------------
        composable("contacto") {
            ContactoScreen(navController = navController)
        }

        // ----------------------------------------------------
        //                       INVENTARIO (ADMIN)
        // ----------------------------------------------------
        composable("inventario") {
            InventarioScreen(
                navController = navController,
                productosViewModel = productosViewModel,
                currentUserRol = UserSession.rol ?: "user"
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
                currentUserRol = UserSession.rol ?: "user",
                productId = id,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        // ----------------------------------------------------
        //                    ADMIN: USUARIOS
        // ----------------------------------------------------
        composable("admin_usuarios") {
            UsuariosScreen(
                usuariosViewModel = usuariosViewModel,
                currentUserRol = UserSession.rol ?: "user",
                navController = navController,
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
                navController = navController,
                usuariosViewModel = usuariosViewModel,
                currentUserRol = UserSession.rol ?: "user",
                userId = id,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        // ----------------------------------------------------
        //                    DETALLE BOLETA
        // ----------------------------------------------------
        composable("boleta_detalle/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0L

            BoletaDetalleScreen(
                navController = navController,
                boletaId = id,
                boletaViewModel = boletaViewModel
            )
        }
    }
}
