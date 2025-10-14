package com.example.levelup



import android.os.Bundle

import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent

import com.example.levelup.ui.PantallaPrincipal

import com.example.levelup.ui.theme.LevelUpTheme
import com.example.levelup.viewmodel.UserViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val userViewModel = viewModel<UserViewModel>
            LevelUpTheme {
                PantallaPrincipal(userViewModel)
            }
        }
    }
}

