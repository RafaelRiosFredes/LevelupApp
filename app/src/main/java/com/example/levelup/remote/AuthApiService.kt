package com.example.levelup.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/registro")
    suspend fun registrar(@Body dto: RegistroUsuarioRemoteDTO): UsuarioResponseRemoteDTO
}