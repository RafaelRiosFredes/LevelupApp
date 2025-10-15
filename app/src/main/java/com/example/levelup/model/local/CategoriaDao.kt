package com.example.levelup.model.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM categorias")
    fun obtenerTodas(): Flow<List<CategoriaEntity>>


}