package com.example.levelup.model.repository

import com.example.levelup.model.local.CategoriaDao
import com.example.levelup.model.local.CategoriaEntity
import kotlinx.coroutines.flow.Flow

class CategoriaRepository(private val dao: CategoriaDao) {
    fun obtenerCategorias(): Flow<List<CategoriaEntity>> = dao.obtenerTodos()
}