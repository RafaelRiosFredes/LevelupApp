package com.example.levelup.repository

import com.example.levelup.local.ProductoDao
import com.example.levelup.local.ProductosEntity
import kotlinx.coroutines.flow.Flow

class ProductosRepository(private val dao: ProductoDao) {

    val productos: Flow<List<ProductosEntity>> = dao.getAllProductos()

    suspend fun agregarProducto(producto: ProductosEntity) {
        dao.insertProducto(producto)
    }
}
