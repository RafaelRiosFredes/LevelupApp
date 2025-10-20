package com.example.levelup.model.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: ProductosEntity)

    @Update
    suspend fun actualizar(producto: ProductosEntity)

    @Delete
    suspend fun eliminar(producto: ProductosEntity)

    @Query("SELECT * FROM productos")
    fun obtenerTodos(): Flow<List<ProductosEntity>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun obtenerPorId(id: Int): ProductosEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProductos(productos: List<ProductosEntity>)
}
