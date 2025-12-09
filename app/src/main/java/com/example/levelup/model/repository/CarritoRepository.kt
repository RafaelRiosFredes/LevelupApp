package com.example.levelup.model.repository

import com.example.levelup.model.data.CarritoDao
import com.example.levelup.model.data.CarritoEntity
import kotlinx.coroutines.flow.Flow

class CarritoRepository(private val dao: CarritoDao) {

    val carrito: Flow<List<CarritoEntity>> = dao.obtenerCarrito()

    suspend fun buscarPorBackendId(id: Long): CarritoEntity? {
        return dao.obtenerPorBackendId(id)
    }

    suspend fun agregar(item: CarritoEntity) = dao.insertar(item)

    suspend fun actualizar(item: CarritoEntity) = dao.actualizar(item)

    suspend fun eliminar(item: CarritoEntity) = dao.eliminar(item)

    suspend fun eliminarTodo() = dao.eliminarTodo()
}
