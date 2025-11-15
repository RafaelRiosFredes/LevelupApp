package com.example.levelup.remote

import com.example.levelup.remote.ProductosDTO
import retrofit2.http.*

interface ProductosApiService {

    // ---------- READ ----------
    @GET("productos")
    suspend fun obtenerProductos(): List<ProductosDTO>

    @GET("productos/{id}")
    suspend fun obtenerProductoPorId(@Path("id") id: Long): ProductosDTO


    // ---------- CREATE ----------
    @POST("productos")
    @Headers("Content-Type: application/json")
    suspend fun crearProducto(@Body producto: ProductosDTO): ProductosDTO


    // ---------- UPDATE ----------
    @PUT("productos/{id}")
    @Headers("Content-Type: application/json")
    suspend fun actualizarProducto(
        @Path("id") id: Long,
        @Body producto: ProductosDTO
    ): ProductosDTO


    // ---------- DELETE ----------
    @DELETE("productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Long)
}
