package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.viewModels
import com.example.levelup.ui.LevelUpNavHost
import com.example.levelup.ui.PantallaPrincipal
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.ui.LevelUpNavHost
import com.example.levelup.ui.LevelUpNavHost
import com.example.levelup.ui.theme.LevelUpTheme

import androidx.navigation.compose.rememberNavController
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.ProductosViewModelFactory
import com.example.levelup.viewmodel.UsuariosViewModel
import com.example.levelup.viewmodel.UsuariosViewModelFactoryApp


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LevelUpTheme {

                // ✅ ViewModels
                val productosViewModel: ProductosViewModel = viewModel(
                    factory = ProductosViewModelFactory(application)
                )

                val usuariosViewModel: UsuariosViewModel = viewModel(
                    factory = UsuariosViewModelFactoryApp(application)
                )

                // ✅ NavHost principal
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LevelUpNavHost(
                        productosViewModel = productosViewModel,
                        usuariosViewModel = usuariosViewModel
                    )
                }
            }
        }
    }
}

