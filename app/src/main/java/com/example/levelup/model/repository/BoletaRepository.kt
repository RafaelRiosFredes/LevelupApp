package com.example.levelup.model.repository

import com.example.levelup.model.data.BoletaDao
import com.example.levelup.model.data.BoletaEntity
import com.example.levelup.remote.BoletaApiService
import com.example.levelup.remote.mappers.toDTO
import com.example.levelup.remote.mappers.toEntity
import kotlinx.coroutines.flow.Flow

class BoletaRepository(
    private val dao: BoletaDao,
    private val api: BoletaApiService
) {

    // ------- ROOM -------
    suspend fun crearBoletaLocal(boleta: BoletaEntity): Long {
        return dao.insertarBoleta(boleta)
    }

    fun obtenerBoletasLocal(): Flow<List<BoletaEntity>> = dao.obtenerTodas()

    fun obtenerBoletaLocalPorId(id: Long): Flow<BoletaEntity?> = dao.obtenerPorId(id)


    // ------- BACKEND -------
    suspend fun enviarBoletaBackend(boleta: BoletaEntity) {
        try {
            api.crearBoleta(boleta.toDTO())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun obtenerBoletaDesdeBackend(id: Long): BoletaEntity? {
        return try {
            val dto = api.obtenerBoleta(id)
            val entity = dto.toEntity()
            dao.insertarBoleta(entity)
            entity
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun obtenerTodasDesdeBackend(): List<BoletaEntity> {
        return try {
            api.obtenerTodasLasBoletas()
                .map { it.toEntity() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

