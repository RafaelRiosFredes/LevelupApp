package com.example.levelup.model.repository

import com.example.levelup.model.data.CategoriaDao
import com.example.levelup.model.data.CategoriaEntity
import kotlinx.coroutines.flow.Flow

class CategoriaRepository(private val dao: CategoriaDao) {
    fun obtenerCategorias(): Flow<List<CategoriaEntity>> = dao.obtenerTodas()
}