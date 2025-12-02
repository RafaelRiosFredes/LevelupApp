package com.example.levelup.remote

import retrofit2.http.*

interface OpinionesApiService {

    @GET("productos/{id}/opiniones")
    suspend fun obtenerOpiniones(
        @Path("id") idProducto: Long
    ): List<OpinionRemoteDTO>

    @POST("productos/{id}/opiniones")
    suspend fun enviarOpinion(
        @Path("id") idProducto: Long,
        @Body body: OpinionCreateRemoteDTO
    ): OpinionRemoteDTO

    @DELETE("productos/{id}/opiniones")
    suspend fun eliminarOpinion(
        @Path("id") idProducto: Long
    )
}
