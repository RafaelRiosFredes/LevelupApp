package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.levelup.ui.LevelUpNavHost
import com.example.levelup.ui.PantallaPrincipal
import com.example.levelup.ui.theme.LevelUpTheme
import com.example.levelup.viewmodel.CategoriaViewModel
import com.example.levelup.viewmodel.CategoriaViewModelFactory


class MainActivity : ComponentActivity() {

    private val vm: CategoriaViewModel by viewModels{
        CategoriaViewModelFactory(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LevelUpTheme {
                LevelUpNavHost()
            }
        }
    }
}

