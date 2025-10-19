package com.example.levelup_gamerapp.model.repository


class LoginRepository(private val dao: RegistroUsuarioDAO) {

    suspend fun login(correo: String, contrasena: String): Boolean {
        val usuario = dao.verificarLogin(correo, contrasena)
        return usuario != null
    }
}
