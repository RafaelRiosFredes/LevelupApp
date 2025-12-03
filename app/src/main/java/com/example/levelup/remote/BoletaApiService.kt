package com.example.levelup.remote

import retrofit2.http.*

interface BoletaApiService {

    @POST("api/v1/boletas")
    suspend fun crearBoleta(@Header("Authorization") auth:String,
                            @Body boleta: BoletaCreateDTO): BoletaRemoteDTO

    @GET("api/v1/boletas")
    suspend fun obtenerBoletas(
        @Header("Authorization") auth: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponseBoletaDTO

    @GET("api/v1/boletas/{id}")
    suspend fun obtenerBoletaId(
        @Header("Authorization") auth: String,
        @Path("id") id: Long): BoletaRemoteDTO


}
