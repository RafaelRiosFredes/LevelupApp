package com.example.levelup.model.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OpinionesDao {

    @Query("SELECT * FROM opiniones WHERE productoId = :id ORDER BY id DESC")
    fun obtenerOpiniones(id: Long): Flow<List<OpinionEntity>>

    @Insert
    suspend fun insertarOpinion(opinion: OpinionEntity)
}
