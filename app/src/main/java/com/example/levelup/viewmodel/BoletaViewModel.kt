package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.data.BoletaEntity
import com.example.levelup.model.repository.BoletaRepository
import kotlinx.coroutines.launch

class BoletaViewModel(
    application: Application,
    private val repository: BoletaRepository
) : AndroidViewModel(application) {

    fun obtenerBoletas() = repository.obtenerBoletas()
    fun obtenerBoleta(id: Int) = repository.obtenerBoletaLocal(id)

    suspend fun crearBoletaRoom(boleta: BoletaEntity): Long =
        repository.insertarLocal(boleta)

    suspend fun crearBoletaBackend(boleta: BoletaEntity): BoletaEntity? =
        repository.crearBoletaBackend(boleta)

    fun sincronizar() = viewModelScope.launch {
        repository.obtenerBoletasBackend()
    }
}
