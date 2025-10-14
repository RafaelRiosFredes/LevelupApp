package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.levelup.ui.theme.LevelUpTheme
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.ProductosViewModelFactor


import kotlin.getValue


class MainActivity : ComponentActivity() {


    val vm: ProductosViewModel = viewModel()
    val productos by vm.productos.collectAsState()

    ListaProductosScreen(
    productos = productos,
    onAddToCart = { productos -> vm.agregarAlCarrito(producto) }
    )
}    