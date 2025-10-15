package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.local.ProductosEntity
import com.example.levelup.model.repository.ProductosRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class ProductosViewModel (private val repo: ProductosRepository): ViewModel(){
    val productos: StateFlow<List<ProductosEntity>> =
        repo.todosLosProductos().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun insertarProducto(producto: ProductosEntity) {
        viewModelScope.launch {
            repo.insertar(producto)
        }
    }
}