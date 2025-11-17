package com.example.levelup.remote

import com.example.levelup.remote.ProductosDTO
import retrofit2.http.*

interface ProductosApiService {


    @GET("productos")
    suspend fun obtenerProductos(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageProductosRemoteDTO

    @GET("productos/{id}")
    suspend fun obtenerProductoPorId(@Path("id") id: Long): ProductoRemoteDTO


    @POST("productos")
    @Headers("Content-Type: application/json")
    suspend fun crearProducto(@Body producto: ProductosDTO): ProductosDTO



    @PUT("productos/{id}")
    @Headers("Content-Type: application/json")
    suspend fun actualizarProducto(
        @Path("id") id: Long,
        @Body producto: ProductosDTO
    ): ProductosDTO


    @DELETE("productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Long)
}
