package com.example.levelup.model.local

import androidx.room.*
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy


@Dao
interface ProductosDao {

    @Query("SELECT * FROM productos ")
    suspend fun obtenerProductos(): List<ProductosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProductos(productos: List<ProductosEntity>)
}

