package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.data.OpinionEntity
import com.example.levelup.model.data.OpinionesDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class OpinionesViewModel(
    private val dao: OpinionesDao
) : ViewModel() {

    fun obtenerOpiniones(productoId: Long): Flow<List<OpinionEntity>> =
        dao.obtenerOpiniones(productoId)

    fun insertarOpinion(opinion: OpinionEntity) {
        viewModelScope.launch {
            dao.insertarOpinion(opinion)
        }
    }
}
