package com.example.levelup.remote

import com.example.levelup.remote.ProductosDTO
import retrofit2.http.*

interface ProductosApiService {


    @GET("productos")
    suspend fun obtenerProductos(
        @Query("nombre") nombre: String? = null,
        @Query("idCategoria") idCategoria: Long? = null,
        @Query("minPrecio") minPrecio: Long? = null,
        @Query("maxPrecio") maxPrecio: Long? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "idProducto,asc"
    ): PageRemoteDTO<ProductoRemoteDTO>

    @GET("productos/{id}")
    suspend fun obtenerProductoPorId(@Path("id") id: Long): ProductoRemoteDTO


    @POST("productos")
    @Headers("Content-Type: application/json")
    suspend fun crearProducto(@Body producto: ProductoCreateRemoteDTO): ProductoRemoteDTO


    @PUT("productos/{id}")
    @Headers("Content-Type: application/json")
    suspend fun actualizarProducto(
        @Path("id") id: Long,
        @Body body: ProductoUpdateRemoteDTO
    ): ProductoRemoteDTO


    @DELETE("productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Long)
}
