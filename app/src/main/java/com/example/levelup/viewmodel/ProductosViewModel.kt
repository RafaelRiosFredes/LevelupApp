package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.R
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.model.repository.ProductosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductosViewModel(
    private val repository: ProductosRepository
) : ViewModel() {

    // Lista observable desde la BD
    val productos = repository.obtenerProductos()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        // Insertar productos de prueba solo si la tabla está vacía
        viewModelScope.launch {
            val lista = repository.obtenerProductos().first()
            if (lista.isEmpty()) {
                val productosDemo = listOf(
                    ProductosEntity(nombre = "Teclado HyperX Alloy", precio = 49990.0, imagenRes = R.drawable.ic_launcher_foreground),
                    ProductosEntity(nombre = "Mouse Logitech G502", precio = 39990.0, imagenRes = R.drawable.ic_launcher_foreground),
                    ProductosEntity(nombre = "Audífonos Razer Kraken", precio = 59990.0, imagenRes = R.drawable.ic_launcher_foreground)
                )
                productosDemo.forEach { repository.insertarProducto(it) }
            }
        }
    }

    fun insertarProducto(producto: ProductosEntity) = viewModelScope.launch {
        repository.insertarProducto(producto)
    }

    fun obtenerProductoPorId(id: Int): Flow<ProductosEntity?> {
        return repository.obtenerProductoPorId(id)
    }

    fun actualizarProducto(producto: ProductosEntity) = viewModelScope.launch {
        repository.actualizarProducto(producto)
    }

    fun eliminarProducto(producto: ProductosEntity) = viewModelScope.launch {
        repository.eliminarProducto(producto)
    }
}
