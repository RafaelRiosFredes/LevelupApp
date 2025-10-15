package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        val dao = AppDatabase.getInstance(application).productosDao()
        repository = ProductosRepository(dao)

        viewModelScope.launch {
            // Si la base está vacía, agregamos datos iniciales
            val lista = repository.obtenerProductos()
            if (lista.isEmpty()) {
                val iniciales = listOf(
                    ProductosEntity(nombre = "Camiseta LevelUp", precio = 12990.0, imagenUrl = "https://picsum.photos/200"),
                    ProductosEntity(nombre = "Zapatillas Training", precio = 39990.0, imagenUrl = "https://picsum.photos/201"),
                    ProductosEntity(nombre = "Botella LevelUp", precio = 7990.0, imagenUrl = "https://picsum.photos/202")
                )
                repository.insertarProductos(iniciales)
                _productos.value = iniciales
            } else {
                _productos.value = lista
            }
        }
    }

    fun agregarAlCarrito(producto: ProductosEntity) {
        // lógica futura para carrito
    }
}
