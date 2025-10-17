package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.local.ProductoEntity
import com.example.levelup.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductoViewModel(private val repository: ProductoRepository) : ViewModel() {

    val productos: StateFlow<List<ProductoEntity>> = repository.productos
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val carrito: StateFlow<List<ProductoEntity>> = repository.carrito
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun agregarAlCarrito(producto: ProductoEntity) {
        viewModelScope.launch {
            val actualizado = producto.copy(enCarrito = true)
            repository.actualizar(actualizado)
        }
    }

    fun insertarProducto(producto: ProductoEntity) {
        viewModelScope.launch {
            repository.insertar(producto)
        }
    }
}