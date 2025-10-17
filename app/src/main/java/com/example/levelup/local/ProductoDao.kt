package com.example.levelup.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos")
    fun obtenerProductos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE enCarrito = 1")
    fun obtenerCarrito(): Flow<List<ProductoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: ProductoEntity)

    @Update
    suspend fun actualizar(producto: ProductoEntity)
}
