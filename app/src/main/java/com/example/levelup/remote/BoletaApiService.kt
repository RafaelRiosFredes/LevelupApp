package com.example.levelup.remote

import retrofit2.http.*

interface BoletaApiService {

    @POST("boletas")
    suspend fun crearBoleta(@Body boleta: BoletaCreateRequestDTO): BoletaRemoteDTO

    @GET("boletas")
    suspend fun obtenerBoletas(): List<BoletaRemoteDTO>

    @GET("boletas/{id}")
    suspend fun obtenerBoletaId(@Path("id") id: Long): BoletaRemoteDTO
}
