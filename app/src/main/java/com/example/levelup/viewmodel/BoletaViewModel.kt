package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.data.BoletaEntity
import com.example.levelup.model.repository.BoletaRepository
import com.example.levelup.remote.RetrofitBuilder
import kotlinx.coroutines.launch

class BoletaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BoletaRepository

    init {
        val db = AppDatabase.getInstance(application)
        val dao = db.boletaDao()
        val api = RetrofitBuilder.boletaApi
        repository = BoletaRepository(dao, api)
    }

    /**
     * Genera una boleta y devuelve el ID generado.
     * - Guarda primero en Room (local)
     * - Luego intenta enviarla al backend
     */
    suspend fun generarBoleta(
        total: Double,
        cantidadProductos: Int,
        detalle: String
    ): Long {

        val boleta = BoletaEntity(
            fecha = System.currentTimeMillis(),
            total = total,
            cantidadProductos = cantidadProductos,
            detalle = detalle
        )

        // 1) Guardar en ROOM → devuelve ID auto generado
        val boletaId = repository.crearBoletaLocal(boleta)

        // 2) Enviar al backend SIN bloquear la pantalla
        viewModelScope.launch {
            repository.enviarBoletaBackend(boleta.copy(id = boletaId))
        }

        return boletaId
    }

    /**
     * Obtiene UNA boleta por ID desde Room (flujo)
     */
    fun obtenerBoleta(id: Long) = repository.obtenerBoletaLocalPorId(id)

    /*Obtiene TODAS las boletas guardadas localmente*/

    fun obtenerBoletas() = repository.obtenerBoletasLocal()
}
