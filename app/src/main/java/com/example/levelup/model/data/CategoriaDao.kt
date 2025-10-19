package com.example.levelup.model.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM categorias")
    fun obtenerTodos(): Flow<List<CategoriaEntity>>
}