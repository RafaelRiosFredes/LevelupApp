package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.levelup.ui.LevelUpNavHost
import com.example.levelup.ui.theme.LevelUpTheme
import com.example.levelup.viewmodel.CarritoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpTheme {
                val navController = rememberNavController()
                val carritoViewModel: CarritoViewModel = viewModel()
                LevelUpNavHost(navController = navController, carritoViewModel = carritoViewModel)
            }
        }
    }
}
