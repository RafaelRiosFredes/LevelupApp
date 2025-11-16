package com.example.levelup.model.repository

import com.example.levelup.model.data.BoletaEntity
import com.example.levelup.model.data.BoletasDao
import com.example.levelup.remote.BoletaApiService
import com.example.levelup.remote.mappers.toDTO
import com.example.levelup.remote.mappers.toEntity
import kotlinx.coroutines.flow.Flow

class BoletaRepository(
    private val dao: BoletasDao,
    private val api: BoletaApiService
) {

    // -------- ROOM --------
    fun obtenerBoletas(): Flow<List<BoletaEntity>> = dao.obtenerBoletas()

    fun obtenerBoletaLocal(id: Int) = dao.boletaPorIdLocal(id)

    suspend fun insertarLocal(boleta: BoletaEntity): Long =
        dao.insertar(boleta)

    suspend fun insertarBoletasLocal(lista: List<BoletaEntity>) =
        dao.insertarBoletas(lista)


    // -------- BACKEND --------
    suspend fun crearBoletaBackend(boleta: BoletaEntity): BoletaEntity? {
        return try {
            val dtoRespuesta = api.crearBoleta(boleta.toDTO())
            val entity = dtoRespuesta.toEntity()
            dao.insertar(entity)
            entity
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun obtenerBoletasBackend() {
        try {
            val remotas = api.obtenerBoletas()
            val entidades = remotas.map { it.toEntity() }
            dao.eliminarTodas()
            dao.insertarBoletas(entidades)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun obtenerBoletaBackendPorId(id: Long): BoletaEntity? {
        return try {
            val dto = api.obtenerBoletaId(id)
            val entity = dto.toEntity()
            dao.insertar(entity)
            entity
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
