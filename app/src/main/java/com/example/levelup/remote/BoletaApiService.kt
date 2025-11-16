package com.example.levelup.remote

import retrofit2.http.*

interface BoletaApiService {

    @POST("boletas")
    suspend fun crearBoleta(@Body boleta: BoletaDTO): BoletaDTO

    @GET("boletas")
    suspend fun obtenerBoletas(): List<BoletaDTO>

    @GET("boletas/{id}")
    suspend fun obtenerBoletaId(@Path("id") id: Long): BoletaDTO
}
