package com.example.levelup.remote.mappers

import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.remote.UsuarioDTO

fun UsuarioEntity.toDTO(): UsuarioDTO =
    UsuarioDTO(
        id = backendId,
        nombres = nombres,
        apellidos = apellidos,
        correo = correo,
        contrasena = contrasena,
        telefono = telefono,
        fechaNacimiento = fechaNacimiento,
        duoc = duoc,
        descApl = descApl,
        rol = rol
    )

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
        duoc = duoc,
        descApl = descApl,
        rol = rol
    )
