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

    internal var repository: CarritoRepository
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

    constructor(repositoryTest: CarritoRepository) : this(Application()) {
        repository = repositoryTest
    }

    fun agregarProducto(productoId: Long, nombre: String, precio: Long, imagenUrl: String?) {
        viewModelScope.launch {
            // Buscamos si ya existe en el carrito
            val productoExistente = repository.buscarPorBackendId(productoId)

            if (productoExistente != null) {
                // si existe: Solo aumentamos la cantidad
                val copiaActualizada = productoExistente.copy(
                    cantidad = productoExistente.cantidad + 1
                )
                repository.actualizar(copiaActualizada)
            } else {
                // 3. NO EXISTE: Lo creamos desde cero
                val nuevoProducto = CarritoEntity(
                    backendId = productoId,
                    nombre = nombre,
                    precio = precio,
                    cantidad = 1,
                    imagenUrl = imagenUrl
                )
                repository.agregar(nuevoProducto)
            }
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
        viewModelScope.launch { repository.eliminar(item) }
    }

    fun total(): StateFlow<Double> =
        carrito
            .map { lista ->
                lista.sumOf { (it.precio * it.cantidad).toDouble() }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                0.0
            )

    fun vaciarCarrito() = viewModelScope.launch {
        repository.eliminarTodo()
    }
}