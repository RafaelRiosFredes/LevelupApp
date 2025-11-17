package com.example.levelup.remote

import retrofit2.http.*

interface BoletaApiService {

    @POST("api/boletas")
    suspend fun crearBoleta(@Body boleta: BoletaCreateDTO): BoletaRemoteDTO

    @GET("api/boletas")
    suspend fun obtenerBoletas(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponseBoletaDTO

    @GET("api/boletas/{id}")
    suspend fun obtenerBoletaId(@Path("id") id: Long): BoletaRemoteDTO
}
