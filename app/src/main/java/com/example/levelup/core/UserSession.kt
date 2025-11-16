// UserSession.kt
package com.example.levelup.core

object UserSession {

    var id: Int? = null
    var correo: String? = null
    var rol: String = "user"

    var nombre: String? = null
    var apellidos: String? = null

    fun isLogged(): Boolean {
        return id != null
    }

    fun login(
        id: Int,
        correo: String,
        rol: String,
        nombre: String,
        apellidos: String
    ) {
        this.id = id
        this.correo = correo
        this.rol = rol.lowercase()
        this.nombre = nombre
        this.apellidos = apellidos
    }

    fun logout() {
        id = null
        correo = null
        rol = "user"
        nombre = null
        apellidos = null
    }
}
