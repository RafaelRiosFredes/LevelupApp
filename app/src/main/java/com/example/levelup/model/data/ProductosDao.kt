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

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    fun obtenerPorId(id: Long): Flow<ProductosEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProductos(productos: List<ProductosEntity>)

    // buscar por id del backend
    @Query("SELECT * FROM productos WHERE backendId = :backendId LIMIT 1")
    suspend fun obtenerPorBackendId(backendId: Long): ProductosEntity?

    // limpiar tabla
    @Query("DELETE FROM productos")
    suspend fun eliminarTodos()
}
