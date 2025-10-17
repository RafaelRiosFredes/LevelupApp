package com.example.levelup.repository

import com.example.levelup.local.ProductoDao
import com.example.levelup.local.ProductoEntity
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val productoDao: ProductoDao) {
    val productos: Flow<List<ProductoEntity>> = productoDao.obtenerProductos()
    val carrito: Flow<List<ProductoEntity>> = productoDao.obtenerCarrito()

    suspend fun insertar(producto: ProductoEntity) {
        productoDao.insertar(producto)
    }

    suspend fun actualizar(producto: ProductoEntity) {
        productoDao.actualizar(producto)
    }
}