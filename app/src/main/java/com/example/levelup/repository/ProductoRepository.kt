package com.example.levelup.repository

import com.example.levelup.local.ProductoDao
import com.example.levelup.local.ProductosEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProductoRepository(private val dao: ProductoDao) {

    suspend fun obtenerProductos(): List<ProductosEntity> = withContext(Dispatchers.IO) {
        dao.getAllProductos()
    }

    suspend fun obtenerProductoPorId(id: Int): ProductosEntity? = withContext(Dispatchers.IO) {
        dao.getProductoPorId(id)
    }

    suspend fun insertarProductos(productos: List<ProductosEntity>) = withContext(Dispatchers.IO) {
        dao.insertarProductos(productos)
    }
}