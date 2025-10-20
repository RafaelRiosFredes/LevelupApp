package com.example.levelup.model.repository

import com.example.levelup.model.data.ProductosDao
import com.example.levelup.model.data.ProductosEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProductosRepository(private val dao: ProductosDao) {

    fun todosLosProductos(): Flow<List<ProductosEntity>> = dao.obtenerTodos()

    suspend fun insertar(producto: ProductosEntity) = withContext(Dispatchers.IO) { dao.insertar(producto) }

    suspend fun actualizar(producto: ProductosEntity) = withContext(Dispatchers.IO) { dao.actualizar(producto) }

    suspend fun eliminar(producto: ProductosEntity) = withContext(Dispatchers.IO) { dao.eliminar(producto) }

    suspend fun obtenerProductoPorId(id: Int): ProductosEntity? = withContext(Dispatchers.IO) { dao.obtenerPorId(id) }

    suspend fun insertarProductos(productos: List<ProductosEntity>) = withContext(Dispatchers.IO) { dao.insertarProductos(productos) }
}
