package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.R
import com.example.levelup.model.local.AppDatabase
import com.example.levelup.model.local.ProductosEntity
import com.example.levelup.model.repository.ProductosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ProductosViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProductosRepository
    private val _productos = MutableStateFlow<List<ProductosEntity>>(emptyList())
    val productos: StateFlow<List<ProductosEntity>> = _productos

    // ✅ Agregar StateFlow para el producto seleccionado
    private val _productoSeleccionado = MutableStateFlow<ProductosEntity?>(null)
    val productoSeleccionado: StateFlow<ProductosEntity?> = _productoSeleccionado

    init {
        val dao = AppDatabase.getInstance(application).productosDao()
        repository = ProductosRepository(dao)

        viewModelScope.launch {
            try {
                val lista = repository.obtenerProductos()
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
                    _productos.value = productosIniciales
                } else {
                    _productos.value = lista
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ✅ Nuevo método para obtener producto por ID
    fun obtenerProductoPorId(id: Int) {
        viewModelScope.launch {
            try {
                val producto = repository.obtenerProductoPorId(id)
                _productoSeleccionado.value = producto
            } catch (e: Exception) {
                e.printStackTrace()
                _productoSeleccionado.value = null
            }
        }
    }

    fun agregarAlCarrito(producto: ProductosEntity) {
        // TODO: Lógica futura para carrito
    }
}