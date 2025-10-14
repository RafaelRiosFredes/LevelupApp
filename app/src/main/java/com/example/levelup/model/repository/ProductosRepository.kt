package com.example.levelup.model.repository

import com.example.levelup.model.local.ProductosDao
import com.example.levelup.model.local.ProductosEntity
import kotlinx.coroutines.flow.Flow


class ProductosRepository (private val dao: ProductosDao){


    fun observarProductos(): Flow<List<ProductosEntity>> = dao.observarTodos()

    suspend fun obtener(id: Int) = dao.obtenerPorId(id)

    suspend fun guardar(
        id: Int?,
        descripcion: String,
        monto: Double,
        categoria: String
    ) {
        if (id == null || id == 0) {
            dao.insertar(
                ProductosEntity(
                    descripcion = descripcion.trim(),
                    monto = monto,
                    categoria = categoria.trim()
                )
            )
        } else {
            dao.actualizar(
                ProductosEntity(
                    id = id,
                    descripcion = descripcion.trim(),
                    monto = monto,
                    categoria = categoria.trim()
                )
            )
        }
    }

    suspend fun eliminar(productos: ProductosEntity) = dao.eliminar(productos)
    suspend fun eliminarTodos() = dao.eliminarTodos()

}