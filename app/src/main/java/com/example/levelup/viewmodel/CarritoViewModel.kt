package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.data.CarritoEntity
import com.example.levelup.model.repository.CarritoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CarritoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CarritoRepository
    val carrito: StateFlow<List<CarritoEntity>>

    init {
        val dao = AppDatabase.getInstance(application).carritoDao()
        repository = CarritoRepository(dao)

        carrito = repository.carrito.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    fun agregarProducto(productoId: Int, nombre: String, precio: Double, imagenUrl: String) {
        viewModelScope.launch {
            val producto = CarritoEntity(
                productoId = productoId,
                nombre = nombre,
                precio = precio,
                cantidad = 1,
                imagenUrl = imagenUrl
            )
            repository.agregar(producto)
        }
    }

    fun aumentarCantidad(item: CarritoEntity) {
        viewModelScope.launch {
            repository.actualizar(item.copy(cantidad = item.cantidad + 1))
        }
    }

    fun disminuirCantidad(item: CarritoEntity) {
        viewModelScope.launch {
            if (item.cantidad > 1) {
                repository.actualizar(item.copy(cantidad = item.cantidad - 1))
            } else {
                repository.eliminar(item)
            }
        }
    }

    fun eliminar(item: CarritoEntity) {
        viewModelScope.launch {
            repository.eliminar(item)
        }
    }

    fun total(): StateFlow<Double> =
        carrito.map { lista -> lista.sumOf { it.precio * it.cantidad } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun vaciarCarrito() = viewModelScope.launch {
        repository.eliminarTodo()
    }

}
