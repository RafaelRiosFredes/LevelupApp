package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.levelup.ui.LevelUpNavHost
import com.example.levelup.ui.theme.LevelUpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // ✅ Activa el renderizado edge-to-edge (pantalla completa moderna)

        setContent {
            // 🎮 Aplica el tema personalizado de tu app
            LevelUpTheme {
                // 🎨 Define la superficie principal de Material3
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 🧭 Carga el sistema de navegación principal
                    LevelUpNavHost()
                }
            }
        }
    }
}
