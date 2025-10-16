package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.local.ProductosEntity
import com.example.levelup.repository.ProductosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductosViewModel(
    application: Application,
    private val repository: ProductosRepository
) : AndroidViewModel(application) {

    private val _productos = MutableStateFlow<List<ProductosEntity>>(emptyList())
    val productos: StateFlow<List<ProductosEntity>> get() = _productos

    init {
        viewModelScope.launch {
            repository.productos.collect { lista ->
                _productos.value = lista
            }
        }
    }

    fun agregarAlCarrito(producto: ProductosEntity) {
        // Lógica de carrito
        println("Producto agregado: ${producto.nombre}")
    }
}
