package com.example.levelup.model.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BoletasDao {

    // ----------- CRUD LOCAL -------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(boleta: BoletaEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarBoletas(lista: List<BoletaEntity>)

    @Query("SELECT * FROM boletas ORDER BY id DESC")
    fun obtenerBoletas(): Flow<List<BoletaEntity>>

    @Query("SELECT * FROM boletas WHERE id = :id LIMIT 1")
    fun boletaPorIdLocal(id: Int): Flow<BoletaEntity?>

    @Query("SELECT * FROM boletas WHERE backendId = :backendId LIMIT 1")
    fun boletaPorBackendId(backendId: Long): Flow<BoletaEntity?>

    @Query("DELETE FROM boletas")
    suspend fun eliminarTodas()
}

