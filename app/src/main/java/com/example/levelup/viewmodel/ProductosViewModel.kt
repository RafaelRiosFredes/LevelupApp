package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.R
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.model.repository.ProductosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductosViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProductosRepository

    private val _productos = MutableStateFlow<List<ProductosEntity>>(emptyList())
    val productos: StateFlow<List<ProductosEntity>> = _productos

    private val _productoSeleccionado = MutableStateFlow<ProductosEntity?>(null)
    val productoSeleccionado: StateFlow<ProductosEntity?> = _productoSeleccionado

    init {
        val dao = AppDatabase.getInstance(application).productoDao()
        repository = ProductosRepository(dao)

        viewModelScope.launch {
            repository.todosLosProductos().collect { lista ->
                if (lista.isEmpty()) {
                    val productosIniciales = listOf(
                        ProductosEntity(
                            nombre = "Teclado Gamer",
                            precio = 12990.0,
                            descripcion = "Teclado ideal para gamers",
                            imagenRes = R.drawable.producto1
                        ),
                        ProductosEntity(
                            nombre = "Mouse Gamer",
                            precio = 39990.0,
                            descripcion = "Mouse cómodo ideal para tus juegos",
                            imagenRes = R.drawable.mouse
                        ),
                        ProductosEntity(
                            nombre = "Camiseta personalizada",
                            precio = 7990.0,
                            descripcion = "Elige el diseño que quieras",
                            imagenRes = R.drawable.descarga
                        )
                    )
                    repository.insertarProductos(productosIniciales)
                }
                _productos.value = lista
            }
        }
    }

    fun insertarProducto(producto: ProductosEntity) {
        viewModelScope.launch { repository.insertar(producto) }
    }

    fun eliminarProducto(producto: ProductosEntity) {
        viewModelScope.launch { repository.eliminar(producto) }
    }

    fun actualizarProducto(producto: ProductosEntity) {
        viewModelScope.launch { repository.actualizar(producto) }
    }

    fun obtenerProductoPorId(id: Int) {
        viewModelScope.launch {
            _productoSeleccionado.value = repository.obtenerProductoPorId(id)
        }
    }
}
