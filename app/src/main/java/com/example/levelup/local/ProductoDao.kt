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
    suspend fun getAllProductos(): List<ProductosEntity>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun getProductoPorId(id: Int): ProductosEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProductos(productos: List<ProductosEntity>)
}