package com.example.levelup.repository

import com.example.levelup.model.local.CarritoDao
import com.example.levelup.model.local.CarritoEntity
import kotlinx.coroutines.flow.Flow

class CarroRepository(private val carritoDao: CarritoDao) {

    val carritoItems: Flow<List<CarritoEntity>> = carritoDao.getAllCarrito()

    suspend fun agregarProducto(producto: CarritoEntity) {
        carritoDao.insertItem(producto)
    }

    suspend fun eliminarProducto(producto: CarritoEntity) {
        carritoDao.deleteItem(producto)
    }

    suspend fun actualizarProducto(producto: CarritoEntity) {
        carritoDao.updateItem(producto)
    }

    suspend fun limpiarCarrito() {
        carritoDao.clearCarrito()
    }
}
