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
    fun obtenerPorId(id: Int): Flow<ProductosEntity?>

}
