package com.example.levelup.model.repository

import com.example.levelup.model.data.BoletaEntity
import com.example.levelup.model.data.BoletasDao
import com.example.levelup.remote.BoletaApiService
import com.example.levelup.remote.mappers.toCreateDTO
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


    // -----------------------------------------------------------
    // -------- BACKEND  (Este es el método que preguntas) --------
    // -----------------------------------------------------------

    suspend fun crearBoletaBackend(boleta: BoletaEntity): BoletaEntity? {
        return try {
            // Convertir BoletaEntity → BoletaCreateDTO (lo que backend pide)
            val dtoRespuesta = api.crearBoleta(boleta.toCreateDTO())

            //  Convertir respuesta del backend → Entity de Room
            val entity = dtoRespuesta.toEntity()

            // Guarda en Room
            dao.insertar(entity)

            // Retornar la entidad ya guardada
            entity
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // -------- Obtener todas las boletas del backend --------

    suspend fun obtenerBoletasBackend() {
        try {
            val page = api.obtenerBoletas()
            val entidades = page.content.map { it.toEntity() }
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
