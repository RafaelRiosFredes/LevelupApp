package com.example.levelup.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.R
import com.example.levelup.local.AppDatabase
import com.example.levelup.local.ProductosEntity
import com.example.levelup.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ProductoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProductoRepository
    private val _productos = MutableStateFlow<List<ProductosEntity>>(emptyList())
    val productos: StateFlow<List<ProductosEntity>> = _productos

    init {
        val dao = AppDatabase.getInstance(application).productosDao()
        repository = ProductoRepository(dao)

        viewModelScope.launch {
            try {
                val lista = repository.obtenerProductos()
                if (lista.isEmpty()) {
                    val productosIniciales = listOf(
                        ProductosEntity(
                            nombre = "Teclado Gamer",
                            precio = 12990.0,
                            descripcion = "Teclado mecánico RGB para gaming",
                            imagenRes = R.drawable.teclado
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

    fun agregarAlCarrito(producto: ProductosEntity) {
        // Lógica futura para carrito
    }
}