package com.example.levelup.model

data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val aceptaTerminos: Boolean = false, //Confirmacion de términos
    val errores: UsuarioErrores = UsuarioErrores() //Objeto que contiene los errores por campo
)
