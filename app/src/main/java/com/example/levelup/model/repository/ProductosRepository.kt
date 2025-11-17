package com.example.levelup.model.repository

import com.example.levelup.model.data.ProductosDao
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.remote.ProductosApiService
import com.example.levelup.remote.mappers.toDTO
import com.example.levelup.remote.mappers.toEntity
import kotlinx.coroutines.flow.Flow

class ProductosRepository(
    private val dao: ProductosDao,
    private val api: ProductosApiService
) {


    //   CRUD LOCAL (ROOM)


    fun obtenerProductos(): Flow<List<ProductosEntity>> = dao.obtenerTodos()

    fun obtenerProductoPorId(id: Long): Flow<ProductosEntity?> = dao.obtenerPorId(id)

    suspend fun insertarProducto(producto: ProductosEntity) = dao.insertar(producto)

    suspend fun actualizarProducto(producto: ProductosEntity) = dao.actualizar(producto)

    suspend fun eliminarProducto(producto: ProductosEntity) = dao.eliminar(producto)



    //   CRUD BACKEND


    suspend fun obtenerProductoDesdeBackend(backendId: Long): ProductosEntity? {
        return try {
            val dto = api.obtenerProductoPorId(backendId)
            val entity = dto.toEntity()
            dao.insertar(entity)
            entity
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun crearProducto(producto: ProductosEntity) {
        try {
            val creado = api.crearProducto(producto.toDTO())
            dao.insertar(creado.toEntity())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun actualizarProductoBackend(producto: ProductosEntity) {
        try {
            if (producto.backendId == null) {
                println(" Error: backendId es null, no se puede actualizar .")
                return
            }

            val actualizado = api.actualizarProducto(producto.backendId, producto.toDTO())
            dao.actualizar(actualizado.toEntity())

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun eliminarProductoBackend(producto: ProductosEntity) {
        try {
            if (producto.backendId == null) {
                println("Error: backendId es null, no se puede eliminar .")
                return
            }

            api.eliminarProducto(producto.backendId)
            dao.eliminar(producto)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    //   SINCRONIZACIÓN


    suspend fun sincronizarProductosDesdeBackend() {
        try {
            val page = api.obtenerProductos()
            val productosRemotos = page.content

            val entidades = productosRemotos.map { it.toEntity() }

            dao.eliminarTodos()
            dao.insertarProductos(entidades)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
