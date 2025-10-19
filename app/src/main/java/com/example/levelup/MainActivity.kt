package com.example.levelup_gamerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.levelup.theme.LevelUpTheme
import com.example.levelup_gamerapp.navigation.LevelUpNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpTheme {
                val navController = rememberNavController()
                LevelUpNavHost(navController)
            }
        }
    }
}
