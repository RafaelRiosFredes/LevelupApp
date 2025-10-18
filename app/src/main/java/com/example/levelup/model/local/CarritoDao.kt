package com.example.levelup.model.local


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CarritoDao {
    @Query("SELECT * FROM carrito")
    suspend fun getCarrito(): List<CarritoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregarAlCarrito(item: CarritoEntity)

    @Query("DELETE FROM carrito WHERE productoId = :productoId")
    suspend fun eliminarDelCarrito(productoId: Int)

    @Query("DELETE FROM carrito")
    suspend fun limpiarCarrito()

    @Query("UPDATE carrito SET cantidad = :cantidad WHERE productoId = :productoId")
    suspend fun actualizarCantidad(productoId: Int, cantidad: Int)
}