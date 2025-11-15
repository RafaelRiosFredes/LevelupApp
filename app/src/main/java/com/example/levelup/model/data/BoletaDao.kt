package com.example.levelup.model.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BoletaDao {

    @Insert
    suspend fun insertarBoleta(boleta: BoletaEntity): Long

    @Query("SELECT * FROM boletas ORDER BY fecha DESC")
    fun obtenerTodas(): Flow<List<BoletaEntity>>

    @Query("SELECT * FROM boletas WHERE id = :id LIMIT 1")
    fun obtenerPorId(id: Long): Flow<BoletaEntity?>
}
