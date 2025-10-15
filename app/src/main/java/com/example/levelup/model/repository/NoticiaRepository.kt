package com.example.levelup.model.repository

import com.example.levelup.model.data.NoticiaEntity

class NoticiaRepository  {

    fun obtenerNoticias(): List<NoticiaEntity> = listOf(
        NoticiaEntity(
            id = 1,
            titulo  = "Nuevo tráiler de GTA VI",
            descripcion = "Rockstar lanza un nuevo adelanto mostrando Vice City con un nivel de detalle impresionante.",
            imagen = "https://cdn.mos.cms.futurecdn.net/D4a6jA5fKzXx2E6gTZB4vC.jpg",
            fuente = "The Clinic"
        ),
        NoticiaEntity(
            id = 1,
            titulo  = "PlayStation anuncia PS6",
            descripcion = "Sony confirma el desarrollo de la próxima consola con soporte total para realidad aumentada.",
            imagen = "https://i.blogs.es/ps6-future-console/ps6-future-console.jpg",
            fuente = "La Cuarta"
        ),
        NoticiaEntity(
            id = 1,
            titulo  = "Steam alcanza récord histórico de usuarios",
            descripcion = "Más de 36 millones de jugadores activos simultáneamente en la plataforma de Valve.",
            imagen = "https://cdn.cloudflare.steamstatic.com/steam/apps/593110/header.jpg",
            fuente = "La Quinta"
        ),

    )
}