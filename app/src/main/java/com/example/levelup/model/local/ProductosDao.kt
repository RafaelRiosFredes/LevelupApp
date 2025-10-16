package com.example.levelup.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductosDao {

    @Query("SELECT * FROM productos")
    suspend fun getAllProductos(): List<ProductosEntity>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun getProductoPorId(id: Int): ProductosEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProductos(productos: List<ProductosEntity>)
}
