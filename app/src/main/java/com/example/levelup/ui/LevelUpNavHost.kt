package com.example.levelup.ui

import android.app.Application
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
fun LevelUpNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // create the ProductosViewModel using our Application-backed factory
    val app = androidx.compose.ui.platform.LocalContext.current.applicationContext as Application
    val productosViewModel: ProductosViewModel = viewModel(factory = ProductosViewModelFactoryApp(app))
    // Instancia el ViewModel con la factory que usa Application (igual que en la rama login)
    val app = LocalContext.current.applicationContext as Application
    val categoriaVm: CategoriaViewModel = viewModel(factory = CategoriaViewModelFactory(app))

    NavHost(
        navController = navController,
        startDestination = "inventario",
        startDestination = "index",
        modifier = modifier
    ) {
        composable("index") {
            // Paso lambdas para que PantallaPrincipal pueda navegar usando este NavController
            PantallaPrincipal(
                vm = categoriaVm,
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
                    // si tienes login en otra branch, navega a "login" o haz lo que haga falta
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
        composable("adminInventario") {
            // InventarioScreen(productosViewModel = ..., onAgregarClick = ..., onEditarClick = ...)
        }

        composable("adminUsuarios") {
            // PantallaUsuarios() // implementa en otra branch
        }

        composable("adminCategorias") {
            //PantallaCategorias(vm = categoriaVm)
        composable("editar/{productId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("productId")?.toIntOrNull() ?: 0
            EditProductoScreen(
                productosViewModel = productosViewModel,
                productId = id,
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        composable("registro") {
            // RegistroScreen
        composable("categorias") {
            //CategoriasScreen()
        }

        composable("login") {
            // LoginScreen(...)  // si existe
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
