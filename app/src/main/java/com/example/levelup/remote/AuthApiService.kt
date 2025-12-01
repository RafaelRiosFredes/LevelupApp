package com.example.levelup.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    // Backend: AuthController @RequestMapping("/api/v1/auth")

    @POST("api/v1/auth/registro")
    suspend fun registrar(@Body dto: RegistroUsuarioRemoteDTO): UsuarioResponseRemoteDTO

    @POST("api/v1/auth/login")
    suspend fun login(@Body dto: LoginRequestDTO): LoginResponseDTO
}