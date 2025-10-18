package com.example.levelup.repository


import com.example.levelup.model.local.CarritoDao
import com.example.levelup.model.local.CarritoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CarroRepository(private val dao: CarritoDao) {

    suspend fun obtenerCarrito() = withContext(Dispatchers.IO) {
        dao.getCarrito()
    }

    suspend fun agregarAlCarrito(item: CarritoEntity) = withContext(Dispatchers.IO) {
        dao.agregarAlCarrito(item)
    }

    suspend fun eliminarDelCarrito(productoId: Int) = withContext(Dispatchers.IO) {
        dao.eliminarDelCarrito(productoId)
    }

    suspend fun limpiarCarrito() = withContext(Dispatchers.IO) {
        dao.limpiarCarrito()
    }

    suspend fun actualizarCantidad(productoId: Int, cantidad: Int) = withContext(Dispatchers.IO) {
        dao.actualizarCantidad(productoId, cantidad)
    }
}