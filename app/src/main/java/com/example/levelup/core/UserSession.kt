package com.example.levelup.core

object UserSession {

    var id: Long? = null
    var correo: String? = null
    var rol: String? = null
    var nombre: String? = null
    var apellidos: String? = null
    var jwt: String? = null

    fun isLogged(): Boolean {
        return id != null
    }

    // Actualizamos la función para pedir el token
    fun login(
        id: Long,
        correo: String,
        rol: String,
        nombre: String,
        apellidos: String,
        jwt: String
    ) {
        this.id = id
        this.correo = correo
        this.rol = rol
        this.nombre = nombre
        this.apellidos = apellidos
        this.jwt = jwt
    }

    fun logout() {
        id = null
        correo = null
        rol = null
        nombre = null
        apellidos = null
        jwt = null
    }
}