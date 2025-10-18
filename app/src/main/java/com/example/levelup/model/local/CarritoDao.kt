package com.example.levelup.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.levelup.model.local.CarritoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {

    @Query("SELECT * FROM carrito")
    fun getAllCarrito(): Flow<List<CarritoEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertItem(item: CarritoEntity)

    @Update
    suspend fun updateItem(item: CarritoEntity)

    @Delete
    suspend fun deleteItem(item: CarritoEntity)

    @Query("DELETE FROM carrito")
    suspend fun clearCarrito()
}