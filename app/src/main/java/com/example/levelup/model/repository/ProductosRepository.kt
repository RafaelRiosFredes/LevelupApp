package com.example.levelup.model.repository


import com.example.levelup.model.local.ProductosDao
import com.example.levelup.model.local.ProductosEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductosRepository(private val dao: ProductosDao) {

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
