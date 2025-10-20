package com.example.levelup.ui

import android.app.Application
import androidx.compose.material3.*


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.ui.unit.dp

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.viewmodel.CategoriaViewModel
import com.example.levelup.viewmodel.CategoriaViewModelFactory
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.ProductosViewModelFactoryApp
import com.example.levelup.viewmodel.UsuariosViewModel
import com.example.levelup.viewmodel.UsuariosViewModelFactoryApp
import com.example.levelup_gamerapp.ui.LoginScreen





/**

 * NavHost del módulo Productos.

 * Ahora la pantalla principal es ProductosScreen.

 * Además ya incluye rutas para las demás pantallas de otras ramas (placeholders).

 */

@Composable
fun LevelUpNavHost(modifier: Modifier = Modifier,navController: NavHostController = rememberNavController()) {

    val app = LocalContext.current.applicationContext as Application
    val productosViewModel: ProductosViewModel = viewModel(factory = ProductosViewModelFactoryApp(app))
    val usuariosViewModel: UsuariosViewModel = viewModel(factory = UsuariosViewModelFactoryApp(app))

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
                onAgregarClick = { navController.navigate("agregarProducto") },
                onEditarClick = { id -> navController.navigate("editarProducto/$id") },
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

        composable("productos") {
            ProductosScreen(nav = navController)
        }

        composable("producto/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            if (id != null) {
                ProductoScreen(id = id, onNavigateBack = { navController.popBackStack() })
            }
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

        composable("agregarProducto") {
            AddProductScreen(
                productosViewModel = productosViewModel,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        composable("editarProducto/{productId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("productId")?.toIntOrNull() ?: 0
            EditProductoScreen(
                productosViewModel = productosViewModel,
                productId = id,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
                )
        }

        composable("adminCategorias") {
            //PantallaCategorias(vm = categoriaVm)
        }


        // Pantalla de registro
        composable("registro") {
            AddUsuarioScreen(usuariosViewModel = usuariosViewModel,
                onSaved = {navController.popBackStack()},
                onCancel = {navController.popBackStack()}
            )
        }

        composable("login") {
            LoginScreen(navController)
        }


        composable("contacto") {
            // LoginScreen(...)  // si existe
        }

        // 📰 Noticias (placeholder temporal)

        composable("noticias") {
            // LoginScreen(...)  // si existe
            PlaceholderScreen("Pantalla de Noticias (en desarrollo)")

        }
    }
}



/**

 * Pantalla temporal que muestra texto cuando una ruta aún no está lista.

 */

@Composable

fun PlaceholderScreen(texto: String) {

    androidx.compose.material3.Surface {
        androidx.compose.foundation.layout.Column(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .padding(32.dp),

            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally

        ) {

            androidx.compose.material3.Text(texto)

        }

    }

}