package com.example.levelup.remote

import retrofit2.http.*

interface BoletaApiService {

    @POST("api/v1/boletas")
    suspend fun crearBoleta(
        @Body body: BoletaCreateDTO
    ): BoletaRemoteDTO

    @GET("api/v1/boletas")
    suspend fun obtenerBoletas(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponseBoletaDTO

    @GET("api/v1/boletas/{id}")
    suspend fun obtenerBoletaId(
        @Path("id") id: Long
    ): BoletaRemoteDTO
}