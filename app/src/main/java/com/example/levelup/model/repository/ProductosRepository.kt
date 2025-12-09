package com.example.levelup.model.repository

import com.example.levelup.model.data.ProductosDao
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.remote.ProductosApiService
import com.example.levelup.remote.mappers.toCreateRemote
import com.example.levelup.remote.mappers.toEntity
import com.example.levelup.remote.mappers.toUpdateRemote
import kotlinx.coroutines.flow.Flow

class ProductosRepository(
    private val dao: ProductosDao,
    private val api: ProductosApiService
) {

    // =======================
    //   CRUD LOCAL (ROOM)
    // =======================

    fun obtenerProductos(): Flow<List<ProductosEntity>> = dao.obtenerTodos()

    fun obtenerProductoPorId(id: Long): Flow<ProductosEntity?> = dao.obtenerPorId(id)

    suspend fun insertarProducto(producto: ProductosEntity) = dao.insertar(producto)

    suspend fun actualizarProducto(producto: ProductosEntity) = dao.actualizar(producto)

    suspend fun eliminarProducto(producto: ProductosEntity) = dao.eliminar(producto)


    // =======================
    //   CRUD BACKEND
    // =======================

    suspend fun obtenerProductoDesdeBackend(backendId: Long): ProductosEntity? {
        return try {
            val dto = api.obtenerProductoPorId(backendId)
            val entity = dto.toEntity()
            dao.insertar(entity)   // REPLACE por índice unique backendId
            entity
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun crearProducto(producto: ProductosEntity) {
        try {
            val creadoRemoto = api.crearProducto(producto.toCreateRemote())
            val entity = creadoRemoto.toEntity()
            dao.insertar(entity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun actualizarProductoBackend(producto: ProductosEntity) {
        try {
            val backendId = producto.backendId
            if (backendId == null) {
                println("Error: backendId es null, no se puede actualizar.")
                return
            }

            val actualizadoRemoto =
                api.actualizarProducto(backendId, producto.toUpdateRemote())

            val entityActualizada = actualizadoRemoto.toEntity()
                .copy(id = producto.id)  // mantenemos id local

            dao.actualizar(entityActualizada)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun eliminarProductoBackend(producto: ProductosEntity) {
        try {
            val backendId = producto.backendId
            if (backendId == null) {
                println("Error: backendId es null, no se puede eliminar.")
                return
            }

            api.eliminarProducto(backendId)
            dao.eliminar(producto)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // =======================
    //   SINCRONIZACIÓN
    // =======================

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

    suspend fun crearProductoRetornandoEntidad(producto: ProductosEntity): ProductosEntity? {
        return try {
            // 1. Enviamos al backend
            val creadoRemoto = api.crearProducto(producto.toCreateRemote())
            // 2. Convertimos la respuesta (que trae el ID real) a entidad local
            val entity = creadoRemoto.toEntity()
            // 3. Guardamos en Room
            dao.insertar(entity)
            // 4. Retornamos la entidad con el ID correcto
            entity
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun obtenerCategoriasBackend(): List<com.example.levelup.remote.CategoriaRemoteDTO> {
        return try {
            api.obtenerCategorias()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
