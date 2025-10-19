package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.levelup.ui.LevelUpNavHost
import com.example.levelup.viewmodel.CarritoViewModel
import com.example.levelup.viewmodel.CarritoViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val carritoViewModel: CarritoViewModel = viewModel(
                factory = CarritoViewModelFactory(application)
            )

            LevelUpNavHost(
                navController = navController,
                carritoViewModel = carritoViewModel
            )
        }
    }
}
