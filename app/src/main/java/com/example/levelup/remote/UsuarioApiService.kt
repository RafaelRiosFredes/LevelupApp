package com.example.levelup.remote

import retrofit2.http.*

interface UsuariosApiService {

    @GET("api/v1/usuarios")
    suspend fun obtenerUsuarios(): List<UsuarioDTO>

    @GET("api/v1/usuarios/{id}")
    suspend fun obtenerUsuario(@Path("id") id: Long): UsuarioDTO

    @POST("api/v1/usuarios")
    suspend fun crearUsuario(@Body usuario: UsuarioDTO): UsuarioDTO

    @PUT("api/v1/usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: Long,
        @Body usuario: UsuarioDTO
    ): UsuarioDTO

    @DELETE("api/v1/usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Long)
}