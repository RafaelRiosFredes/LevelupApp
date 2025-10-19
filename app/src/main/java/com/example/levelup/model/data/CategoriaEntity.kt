package com.example.levelup.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class CategoriaEntity (
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val nombre: String,
    val icon_url: String
)