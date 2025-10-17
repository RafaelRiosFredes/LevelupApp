package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.levelup.local.AppDatabase
import com.example.levelup.local.ProductoEntity
import com.example.levelup.repository.ProductoRepository
import com.example.levelup.theme.LevelUpTheme
import com.example.levelup.ui.LevelUpNavHost
import com.example.levelup.viewmodel.ProductoViewModel
import com.example.levelup.viewmodel.ProductoViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val repo = ProductoRepository(db.ProductoDao())
        val vmFactory = ProductoViewModelFactory(repo)
        val viewModel = ViewModelProvider(this, vmFactory)[ProductoViewModel::class.java]

        // Inserta algunos productos de ejemplo
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.insertarProducto(
                ProductoEntity(
                    nombre = "Camisa",
                    precio = 19.99,
                    imagenUrl = "",
                    descripcion = "Camisa de algodón"
                )
            )
            viewModel.insertarProducto(
                ProductoEntity(nombre = "Pantalón", precio = 29.99, imagenUrl = "https://tusitio.com/imagenes/pantalon.png", descripcion = "Pantalón de mezclilla")
            )
            viewModel.insertarProducto(
                ProductoEntity(nombre = "Zapatos", precio = 49.99, imagenUrl = "https://tusitio.com/imagenes/pantalon.png", descripcion = "Zapatos de cuero")
            )
        }

        setContent {
            LevelUpTheme {
                val navController = rememberNavController()
                LevelUpNavHost(navController, viewModel)
            }
        }
    }

}

