package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.levelup.ui.LevelUpNavHost
import com.example.levelup.ui.theme.LevelUpTheme





class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //Define el contenido de la interfaz usando compose
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpTheme {//aplica el tema visual
                LevelUpNavHost()//Controla la navegacion entre pantallas
            }
        }
    }
}