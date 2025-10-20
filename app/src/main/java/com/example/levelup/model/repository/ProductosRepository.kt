package com.example.levelup.model.repository


import com.example.levelup.model.data.ProductosDao
import com.example.levelup.model.data.ProductosEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProductosRepository(private val dao: ProductosDao) {

    fun obtenerProductos(): Flow<List<ProductosEntity>> = dao.getAllProductos()
    //Devuelve todos los productos en tiempo real
    suspend fun obtenerProductoPorId(id: Int): ProductosEntity? =
        withContext(Dispatchers.IO) { dao.getProductoPorId(id) }
    //Obtiene productos por su ID
    suspend fun insertarProductos(productos: List<ProductosEntity>) =
        withContext(Dispatchers.IO) { dao.insertarProductos(productos) }
    //Inserta o reemplaza una lista de productos
}