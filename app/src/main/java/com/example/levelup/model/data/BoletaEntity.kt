package com.example.levelup.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "boletas")
data class BoletaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,                 // id local (room)

    val backendId: Long? = null,     // id real del backend (idBoleta)
    val total: Long,
    val descuento: Int? = null,
    val fechaEmision: String,
    val usuarioIdBackend: Int,
    val usuarioNombre: String?,
    val usuarioApellidos: String?,
    val usuarioCorreo: String?,
    val detalleTexto: String
)
