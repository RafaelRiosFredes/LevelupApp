package com.example.levelup.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.local.AppDatabase
import com.example.levelup.model.local.CarritoEntity
import com.example.levelup.repository.CarroRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CarritoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CarroRepository
    val carrito: StateFlow<List<CarritoEntity>>

    init {
        val dao = AppDatabase.Companion.getDatabase(application).carritoDao()
        repository = CarroRepository(dao)
        carrito = repository.carritoItems.stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5000),
            emptyList()
        )
    }

    fun agregarProducto(nombre: String, precio: Double, imagenRes: Int) {
        viewModelScope.launch {
            val producto =
                CarritoEntity(nombre = nombre, precio = precio, cantidad = 1, imagenRes = imagenRes)
            repository.agregarProducto(producto)
        }
    }

    fun eliminarDelCarrito(item: CarritoEntity) {
        viewModelScope.launch { repository.eliminarProducto(item) }
    }

    fun aumentarCantidad(item: CarritoEntity) {
        viewModelScope.launch {
            repository.actualizarProducto(item.copy(cantidad = item.cantidad + 1))
        }
    }

    fun disminuirCantidad(item: CarritoEntity) {
        viewModelScope.launch {
            if (item.cantidad > 1) {
                repository.actualizarProducto(item.copy(cantidad = item.cantidad - 1))
            } else {
                repository.eliminarProducto(item)
            }
        }
    }

    fun obtenerTotal(): StateFlow<Double> {
        return carrito
            .map { lista -> lista.sumOf { it.precio * it.cantidad } }
            .stateIn(
                viewModelScope,
                SharingStarted.Companion.WhileSubscribed(5000),
                0.0
            )
    }
}