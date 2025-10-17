package com.example.levelup.model.repository

import com.example.levelup.model.data.LoginDao
import com.example.levelup.model.local.LoginEntity

class LoginRepository(private val dao: LoginDao) {


    // Valida que el correo y contraseña existan
    suspend fun validarUsuario(correo: String, contrasena: String): Boolean {
        val usuario = dao.obtenerPorCorreo(correo)
        return usuario?.contrasena == contrasena
    }
}
