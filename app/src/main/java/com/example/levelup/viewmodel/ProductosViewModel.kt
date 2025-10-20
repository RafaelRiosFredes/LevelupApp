package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.R
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.model.repository.ProductosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductosViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductosRepository

    val productos: StateFlow<List<ProductosEntity>>

    init {
        val dao = AppDatabase.getInstance(application).productosDao()
        repository = ProductosRepository(dao)
        productos = repository.obtenerProductos()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun insertarProducto(producto: ProductosEntity){
        viewModelScope.launch {
            repository.insertarProducto(producto)
        }
    }
    fun obtenerProductoPorId(id: Int): Flow<ProductosEntity?> {
        return repository.obtenerProductoPorId(id)
    }

    fun actualizarProducto(producto: ProductosEntity) {
        viewModelScope.launch {
            repository.actualizarProducto(producto)
        }
    }

    fun eliminarProducto(producto: ProductosEntity) {
        viewModelScope.launch { repository.eliminarProducto(producto) }
    }
}

