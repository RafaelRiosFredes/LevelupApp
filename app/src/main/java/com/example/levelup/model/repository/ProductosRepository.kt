package com.example.levelup.model.repository

import com.example.levelup.model.data.ProductosDao
import com.example.levelup.model.data.ProductosEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProductosRepository(private val dao: ProductosDao) {

    fun obtenerProductoPorId(id: Int): Flow<ProductosEntity?> = dao.obtenerPorId(id)

    fun obtenerProductos(): Flow<List<ProductosEntity>> = dao.obtenerTodos()

    suspend fun insertarProducto(producto: ProductosEntity) = dao.insertar(producto)

    suspend fun actualizarProducto(producto: ProductosEntity) = dao.actualizar(producto)

    suspend fun eliminarProducto(producto: ProductosEntity) = dao.eliminar(producto)
}

