package com.example.levelup.model.repository

import com.example.levelup.local.data.RegistroUsuarioDao

class LoginRepository(private val dao: RegistroUsuarioDao) {

    suspend fun obtenerUsuarioPorCorreo(correo: String) = dao.obtenerPorCorreo(correo)
}
