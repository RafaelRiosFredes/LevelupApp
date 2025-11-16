package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.model.repository.ProductosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductosViewModel(
    private val repository: ProductosRepository
) : ViewModel() {


    //   LISTA DE PRODUCTOS (ROOM)
    val productos = repository.obtenerProductos()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    // =============================
    //   CRUD LOCAL (Room)
    // =============================

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


    // =============================
    //   CRUD BACKEND (Retrofit)
    // =============================

    fun crearProductoBackend(producto: ProductosEntity) = viewModelScope.launch {
        repository.crearProducto(producto)
    }

    fun actualizarProductoBackend(producto: ProductosEntity) = viewModelScope.launch {
        repository.actualizarProductoBackend(producto)
    }

    fun eliminarProductoBackend(producto: ProductosEntity) = viewModelScope.launch {
        repository.eliminarProductoBackend(producto)
    }

    fun obtenerProductoBackend(id: Long) = viewModelScope.launch {
        repository.obtenerProductoDesdeBackend(id)
    }


    // =============================
    //   SINCRONIZACIÓN OPCIONAL
    // =============================

    fun sincronizarProductos() = viewModelScope.launch {
        try {
            repository.sincronizarProductosDesdeBackend()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        // ❗IMPORTANTE: NO sincronizar automáticamente hasta tener backend funcionando
        // sincronizarProductos()
    }
}
