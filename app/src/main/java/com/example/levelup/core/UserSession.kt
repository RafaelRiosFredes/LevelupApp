package com.example.levelup.core

object UserSession {

    var id: Int? = null
    var correo: String? = null
    var rol: String = "user"
    var nombre: String? = null
    var apellidos: String? = null

    // NUEVO: Aquí guardaremos la llave de seguridad
    var jwt: String? = null

    fun isLogged(): Boolean {
        return id != null
    }

    // Actualizamos la función para pedir el token
    fun login(
        id: Int,
        correo: String,
        rol: String,
        nombre: String,
        apellidos: String,
        jwt: String // <--- Nuevo parámetro
    ) {
        this.id = id
        this.correo = correo
        this.rol = rol.lowercase()
        this.nombre = nombre
        this.apellidos = apellidos
        this.jwt = jwt // <--- Guardamos el token
    }

    fun logout() {
        id = null
        correo = null
        rol = "user"
        nombre = null
        apellidos = null
        jwt = null // <--- Borramos el token al salir
    }
}