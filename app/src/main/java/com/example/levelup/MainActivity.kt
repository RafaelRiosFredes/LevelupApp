package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.model.local.AppDatabase
import com.example.levelup.model.repository.ProductosRepository
import com.example.levelup.ui.AddProductScreen
import com.example.levelup.ui.EditProductScreen
import com.example.levelup.ui.InventarioScreen
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.ProductosViewModelFactory
import com.example.levelup.ui.theme.LevelUpTheme

class MainActivity : ComponentActivity() {

    private val db by lazy { AppDatabase.get(this) }
    private val repo by lazy { ProductosRepository(db.productoDao()) }
    private val vmFactory by lazy { ProductosViewModelFactory(repo) }
    private val productosViewModel: ProductosViewModel by viewModels { vmFactory }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "inventario") {
                    composable("inventario") {
                        InventarioScreen(
                            productosViewModel = productosViewModel,
                            onAgregarClick = { navController.navigate("agregar") },
                            onEditarClick = {id -> navController.navigate("editar/$id")}
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
                        EditProductScreen(
                            productosViewModel = productosViewModel,
                            productId = id,
                            onSaved = { navController.popBackStack() },
                            onCancel = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
