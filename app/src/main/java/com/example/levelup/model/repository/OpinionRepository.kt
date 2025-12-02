package com.example.levelup.model.repository

import com.example.levelup.remote.OpinionCreateRemoteDTO
import com.example.levelup.remote.OpinionRemoteDTO
import com.example.levelup.remote.OpinionesApiService

class OpinionesRepository(
    private val api: OpinionesApiService
) {

    suspend fun obtenerOpiniones(idProducto: Long): List<OpinionRemoteDTO> {
        return api.obtenerOpiniones(idProducto)
    }

    suspend fun enviarOpinion(
        idProducto: Long,
        comentario: String,
        estrellas: Int
    ): OpinionRemoteDTO {
        return api.enviarOpinion(
            idProducto,
            OpinionCreateRemoteDTO(comentario, estrellas)
        )
    }

    suspend fun eliminarOpinion(idProducto: Long) {
        api.eliminarOpinion(idProducto)
    }
}
