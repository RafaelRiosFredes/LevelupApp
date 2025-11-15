package com.example.levelup.remote

import retrofit2.http.*

interface UsuariosApiService {

    @GET("usuarios")
    suspend fun obtenerUsuarios(): List<UsuarioDTO>

    @GET("usuarios/{id}")
    suspend fun obtenerUsuario(@Path("id") id: Long): UsuarioDTO

    @POST("usuarios")
    suspend fun crearUsuario(@Body usuario: UsuarioDTO): UsuarioDTO

    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: Long,
        @Body usuario: UsuarioDTO
    ): UsuarioDTO

    @DELETE("usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Long)
}
