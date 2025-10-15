package com.example.levelup.model.repository

import com.example.levelup.model.data.RegistroUsuarioEntity
import com.example.levelup.model.local.AppDatabase

class LoginRepository(private val db: AppDatabase) {

    suspend fun validarUsuario(correo: String, contrasena: String): RegistroUsuarioEntity? {
        return db.registroUsuarioDao().obtenerPorCorreo(correo)?.takeIf {
            it.contrasena == contrasena
        }
    }
}
