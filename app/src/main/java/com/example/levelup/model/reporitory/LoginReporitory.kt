package com.example.levelup.model.repository

import com.example.levelup.model.data.LoginDao
import com.example.levelup.model.local.LoginEntity

class LoginRepository(private val dao: LoginDao) {

    suspend fun validarUsuario(correo: String, contrasena: String): Boolean {
        val usuario = dao.obtenerPorCorreo(correo)
        return usuario?.contrasena == contrasena
    }

    suspend fun registrarUsuarioTemporal(correo: String, contrasena: String) {
        dao.insertarUsuario(LoginEntity(correo = correo, contrasena = contrasena))
    }
}
