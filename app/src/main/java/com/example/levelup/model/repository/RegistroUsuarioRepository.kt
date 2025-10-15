package com.example.levelup.model.repository

import com.example.levelup.model.data.RegistroUsuarioDao
import com.example.levelup.model.data.RegistroUsuarioEntity

class RegistroUsuarioRepository (private val dao: RegistroUsuarioDao) {
    suspend fun insertar(usuario: RegistroUsuarioEntity) = dao.insertarUsuario(usuario)
    suspend fun obtenerPorCorreo(correo: String) = dao.obtenerPorCorreo(correo)
}