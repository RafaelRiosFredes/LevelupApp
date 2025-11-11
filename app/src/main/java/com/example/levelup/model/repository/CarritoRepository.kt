package com.example.levelup.model.repository

import com.example.levelup.model.data.CarritoDao
import com.example.levelup.model.data.CarritoEntity
import kotlinx.coroutines.flow.Flow

class CarritoRepository(private val carritoDao: CarritoDao) {

    val carrito: Flow<List<CarritoEntity>> = carritoDao.obtenerCarrito()

    suspend fun agregar(producto: CarritoEntity) {
        carritoDao.insertar(producto)
    }

    suspend fun eliminar(producto: CarritoEntity) {
        carritoDao.eliminar(producto)
    }

    suspend fun actualizar(producto: CarritoEntity) {
        carritoDao.actualizar(producto)
    }

    suspend fun vaciar() {
        carritoDao.limpiarCarrito()
    }
}
