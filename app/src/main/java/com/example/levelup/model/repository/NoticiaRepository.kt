package com.example.levelup.model.repository

import com.example.levelup.R
import com.example.levelup.model.data.NoticiaEntity

class NoticiaRepository {

    fun obtenerNoticias(): List<NoticiaEntity> = listOf(
        NoticiaEntity(
            id = 1,
            titulo = "Nuevo tráiler de GTA VI",
            descripcion = "Rockstar lanza un nuevo adelanto mostrando Vice City con un nivel de detalle impresionante.",
            imagen = R.drawable.gta.toString(),
            fuente = "The Clinic"
        ),

        NoticiaEntity(
            id = 2,
            titulo = "Project Zomboid recibe una gran actualización multijugador",
            descripcion = "La última versión del popular survival introduce nuevos sistemas de vehículos, animaciones mejoradas y servidores dedicados más estables.",
            imagen = R.drawable.pz.toString(),
            fuente = "La cuarta"
        ),

    NoticiaEntity(
            id = 3,
            titulo = "Steam alcanza récord histórico de usuarios",
            descripcion = "Más de 36 millones de jugadores activos simultáneamente en la plataforma de Valve.",
            imagen = R.drawable.steam.toString(),
            fuente = "La Quinta"
        )
    )
}
