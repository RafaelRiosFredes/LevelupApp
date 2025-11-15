// UserSession.kt
package com.example.levelup.core

object UserSession {

    var id: Int? = null
    var correo: String? = null
    var rol: String = "user"

    fun isLogged(): Boolean {
        return id != null
    }

    fun login(id: Int, correo: String, rol: String) {
        this.id = id
        this.correo = correo
        this.rol = rol.lowercase()
    }

    fun logout() {
        id = null
        correo = null
        rol = "user"
    }
}
