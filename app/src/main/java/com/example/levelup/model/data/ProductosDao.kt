package com.example.levelup.model.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao// Para acceder a la base de datos
interface ProductosDao {

    @Query("SELECT * FROM productos")
    fun getAllProductos(): kotlinx.coroutines.flow.Flow<List<ProductosEntity>>
    //Obtiene todos los productos en tiempo real
    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun getProductoPorId(id: Int): ProductosEntity?
    //Busca productos por su ID
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProductos(productos: List<ProductosEntity>)
    //Inserta o reemplaza productos en la base de datos

}
