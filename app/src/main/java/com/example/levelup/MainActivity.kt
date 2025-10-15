package com.example.levelup


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.example.levelup.ui.InventarioScreen
import com.example.levelup.ui.theme.LevelUpTheme
import com.example.levelup.viewmodel.CategoriaViewModel
import com.example.levelup.viewmodel.CategoriaViewModelFactory


class MainActivity : ComponentActivity() {

    private val vm: CategoriaViewModel by viewModels{
        CategoriaViewModelFactory(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpTheme {
                InventarioScreen(
                    productos = vm.categorias.collectAsState().value,
                    onAddClick = { /* abrir pantalla de nuevo producto */ },
                    onEditClick = { /* editar */ },
                    onDeleteClick = { /* eliminar */ }
                )

            }
        }
    }
}

