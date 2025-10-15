package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.levelup.model.local.AppDatabase
import com.example.levelup.model.repository.ProductosRepository

import com.example.levelup.ui.theme.LevelUpTheme
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.ProductosViewModelFactory
import com.example.levelup.ui.ProductosNavHost


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "productos_db").build()
        val repository = ProductosRepository(db.productosDao())
        val viewModelFactory = ProductosViewModelFactory(repository)

        setContent {
            androidx.lifecycle.viewmodel.compose.viewModel(
                factory = viewModelFactory
            ) {
                ProductosNavHost()
            }
        }
    }
}