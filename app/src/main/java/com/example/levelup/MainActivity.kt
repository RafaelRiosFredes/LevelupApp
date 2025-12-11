package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.levelup.core.UserSession
import com.example.levelup.ui.LevelUpNavHost
import com.example.levelup.ui.theme.LevelUpTheme
import com.example.levelup.viewmodel.OpinionesViewModel
import com.example.levelup.viewmodel.OpinionesViewModelFactoryApp
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.ProductosViewModelFactoryApp
import com.example.levelup.viewmodel.UsuariosViewModel
import com.example.levelup.viewmodel.UsuariosViewModelFactoryApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            LevelUpTheme {

                // -------------------------
                // Crear ViewModels globales
                // -------------------------
                val productosViewModel: ProductosViewModel = viewModel(
                    factory = ProductosViewModelFactoryApp(application)
                )

                val usuariosViewModel: UsuariosViewModel = viewModel(
                    factory = UsuariosViewModelFactoryApp(application)
                )

                val opinionesViewModel: OpinionesViewModel = viewModel(
                    factory = OpinionesViewModelFactoryApp(application)
                )


                // -------------------------
                // NavController global
                // -------------------------
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LevelUpNavHost(
                        navController = navController,
                        productosViewModel = productosViewModel,
                        usuariosViewModel = usuariosViewModel,
                        opinionesViewModel = opinionesViewModel
                    )
                }
            }
        }
    }
}
