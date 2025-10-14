package com.example.levelup.model.local

import androidx.room.*
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductosDao {

    @Query("SELECT * FROM productos ")
    fun observarTodos(): Flow<List<ProductosEntity>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun obtenerPorId(id: Int): ProductosEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(expense: ProductosEntity): Long

    @Update
    suspend fun actualizar(expense: ProductosEntity)

    @Delete
    suspend fun eliminar(expense: ProductosEntity)

    @Query("DELETE FROM productos")
    suspend fun eliminarTodos()

}