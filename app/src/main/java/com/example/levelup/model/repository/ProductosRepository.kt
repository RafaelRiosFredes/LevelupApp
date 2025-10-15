package com.example.levelup.model.repository

import com.example.levelup.model.local.ProductosDao
import com.example.levelup.model.local.ProductosEntity
import kotlinx.coroutines.flow.Flow

class ProductosRepository(private val dao: ProductosDao) {

    val todosLosProductos: Flow<List<ProductosEntity>> = dao.obtenerTodos()

    suspend fun insertar(producto: ProductosEntity) {
        dao.insertar(producto)
    }

    suspend fun insertarTodos(productos: List<ProductosEntity>) {
        dao.insertarTodos(productos)
    }

    suspend fun actualizar(producto: ProductosEntity) {
        dao.actualizar(producto)
    }

    suspend fun eliminar(producto: ProductosEntity) {
        dao.eliminar(producto)
    }

    fun obtenerPorId(id: Int): Flow<ProductosEntity?> {
        return dao.obtenerPorId(id)
    }
}
