package com.example.levelup.remote.mappers

import android.util.Base64
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.remote.UsuarioDTO

// ENTITY → DTO
fun UsuarioEntity.toDTO(): UsuarioDTO =
    UsuarioDTO(
        id = backendId,
        nombres = nombres,
        apellidos = apellidos,
        correo = correo,
        contrasena = contrasena,
        telefono = telefono,
        fechaNacimiento = fechaNacimiento,

        fotoPerfil = fotoPerfil?.let {
            Base64.encodeToString(it, Base64.NO_WRAP)
        },

        duoc = duoc,
        descApl = descApl,
        rol = rol
    )

// DTO → ENTITY
fun UsuarioDTO.toEntity(): UsuarioEntity =
    UsuarioEntity(
        id = 0,
        backendId = id,

        nombres = nombres,
        apellidos = apellidos,
        correo = correo,
        contrasena = contrasena,
        telefono = telefono,
        fechaNacimiento = fechaNacimiento,

        fotoPerfil = fotoPerfil?.let {
            try {
                Base64.decode(it, Base64.DEFAULT)
            } catch (e: Exception) {
                null
            }
        },

        duoc = duoc,
        descApl = descApl,
        rol = rol
    )
