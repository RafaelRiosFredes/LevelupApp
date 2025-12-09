package com.example.levelup.model.repository

import com.example.levelup.remote.AuthApiService
import com.example.levelup.remote.LoginRequestDTO
import com.example.levelup.remote.LoginResponseDTO
import com.example.levelup.remote.RegistroUsuarioRemoteDTO
import com.example.levelup.remote.UsuarioDTO
import com.example.levelup.remote.UsuarioResponseRemoteDTO
import com.example.levelup.remote.UsuariosApiService


class UsuariosRepository(
    private val apiUsuarios: UsuariosApiService,
    private val apiAuth: AuthApiService
) {

    suspend fun login(email: String, pass: String): LoginResponseDTO {
        return apiAuth.login(LoginRequestDTO(email, pass))
    }

    suspend fun registrar(dto: RegistroUsuarioRemoteDTO): UsuarioResponseRemoteDTO {
        return apiAuth.registrar(dto)
    }

    suspend fun obtenerUsuario(id: Long): UsuarioDTO {
        return apiUsuarios.obtenerUsuario(id)
    }

    suspend fun obtenerUsuarios(): List<UsuarioDTO> {
        return apiUsuarios.obtenerUsuarios()
    }

    suspend fun eliminarUsuario(id: Long) {
        apiUsuarios.eliminarUsuario(id)
    }
}


