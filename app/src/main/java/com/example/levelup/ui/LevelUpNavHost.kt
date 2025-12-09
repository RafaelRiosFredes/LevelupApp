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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.levelup.viewmodel.BoletaViewModelFactoryApp
import com.example.levelup.viewmodel.CarritoViewModelFactory
import com.example.levelup.viewmodel.OpinionesViewModel
import com.example.levelup.model.repository.OpinionesRepository
import com.example.levelup.remote.RetrofitBuilder
import com.example.levelup.viewmodel.OpinionesViewModelFactory


@Composable
fun LevelUpNavHost(
    navController: NavHostController,
    productosViewModel: ProductosViewModel,
    usuariosViewModel: UsuariosViewModel
) {

    val context = LocalContext.current

    // ============================
    // CARRO / BOLETAS / OPINIONES
    // ============================
    val carritoViewModel: CarritoViewModel =
        viewModel(factory = CarritoViewModelFactory(context.applicationContext as Application))

    val boletaViewModel: BoletaViewModel =
        viewModel(factory = BoletaViewModelFactoryApp(context.applicationContext as Application))

    val opinionesRepository = OpinionesRepository(RetrofitBuilder.opinionesApi)
    val opinionViewModel: OpinionesViewModel =
        viewModel(factory = OpinionesViewModelFactory(opinionesRepository))


    NavHost(
        navController = navController,
        startDestination = "PantallaPrincipal"
    ) {

        // --------------------------
        // PRINCIPAL
        // --------------------------
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

        // --------------------------
        // PRODUCTOS
        // --------------------------
        composable("productos") {
            ProductosScreen(
                navController = navController,
                productosViewModel = productosViewModel,
                carritoViewModel = carritoViewModel
            )
        }

        composable("producto/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0
            ProductoScreen(
                navController = navController,
                productosViewModel = productosViewModel,
                carritoViewModel = carritoViewModel,
                opinionesViewModel = opinionViewModel,
                id = id
            )
        }

        // --------------------------
        // CARRITO
        // --------------------------
        composable("carrito") {
            CarritoScreen(
                navController = navController,
                carritoViewModel = carritoViewModel,
                boletaViewModel = boletaViewModel,
                usuariosViewModel = usuariosViewModel
            )
        }

        // --------------------------
        // LOGIN
        // --------------------------
        composable("login") {
            LoginScreen(navController = navController)
        }

        // --------------------------
        // REGISTRO (público)
        // --------------------------
        composable("registro") {
            RegistroScreen(
                navController = navController,
                vm = usuariosViewModel
            )
        }

        // --------------------------
        // NOTICIAS / CONTACTO
        // --------------------------
        composable("noticias") { NoticiasScreen(navController = navController) }
        composable("contacto") { ContactoScreen(navController = navController) }

        // --------------------------
        // INVENTARIO ADMIN
        // --------------------------
        composable("inventario") {
            InventarioScreen(
                navController = navController,
                productosViewModel = productosViewModel,
                currentUserRol = UserSession.rol ?: "USER"
            )
        }

        composable("add_producto") {
            AddProductScreen(
                navController = navController,
                productosViewModel = productosViewModel
            )
        }

        composable("edit_producto/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0

            EditProductoScreen(
                navController = navController,
                productosViewModel = productosViewModel,
                currentUserRol = UserSession.rol ?: "USER",
                productId = id,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        // --------------------------
        // ADMIN → USUARIOS
        // --------------------------
        composable("admin_usuarios") {
            UsuariosScreen(
                vm = usuariosViewModel,
                currentUserRol = UserSession.rol ?: "USER",
                navController = navController,
                onEditarClick = { id -> navController.navigate("edit_usuario/$id") }
            )
        }

        composable("add_usuario") {
            AddUsuarioScreen(
                navController = navController,
                vm = usuariosViewModel
            )
        }

        composable("edit_usuario/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0L

            EditUsuarioScreen(
                navController = navController,
                vm = usuariosViewModel,
                currentUserRol = UserSession.rol ?: "USER",
                userId = id,
                onCancel = { navController.popBackStack() }
            )
        }

        // --------------------------
        // BOLETAS
        // --------------------------
        composable("historial_boletas") {
            HistorialBoletasScreen(
                navController = navController,
                boletaViewModel = boletaViewModel
            )
        }

        composable("boleta_detalle/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0L

            DetalleBoletaScreen(
                navController = navController,
                boletaId = id,
                boletaViewModel = boletaViewModel
            )
        }

        // --------------------------
        // DETALLE COMPRA
        // --------------------------
        composable(
            route = "detalle_compra/{totalFinal}/{descuentoAplicado}",
            arguments = listOf(
                navArgument("totalFinal") { type = NavType.LongType },
                navArgument("descuentoAplicado") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val totalFinal = backStackEntry.arguments?.getLong("totalFinal") ?: 0L
            val descuentoAplicado = backStackEntry.arguments?.getInt("descuentoAplicado") ?: 0

            DetalleCompraScreen(
                navController = navController,
                carritoViewModel = carritoViewModel,
                boletaViewModel = boletaViewModel,
                totalFinal = totalFinal,
                descuentoAplicado = descuentoAplicado
            )
        }
    }
}

