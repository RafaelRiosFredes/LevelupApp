package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.local.ProductosEntity
import com.example.levelup.model.repository.ProductosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FormState(
    val id: Int? = null,
    val descripcion: String = "",
    val monto: String = "",
    val categoria: String = "",
    val error: String? = null
)

class ProductosViewModel(private val repository: ProductosRepository) : ViewModel() {

    private val _productos = MutableStateFlow<List<ProductosEntity>>(emptyList())
    val productos: StateFlow<List<ProductosEntity>> = _productos

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        viewModelScope.launch {
            _productos.value = repository.obtenerProductos()
        }
    }

    fun agregarAlCarrito(producto: ProductosEntity) {
        // Aquí podrías guardar el producto en tabla "carrito" o mostrar un Toast
        println("🛍️ Producto añadido: ${producto.nombre}")
    }
}
