package com.example.levelup.model.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RegistroUsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: RegistroUsuarioEntity)

    @Query(value = "SELECT * FROM usuarios WHERE correo = :correo")
    suspend fun obtenerPorCorreo(correo: String): RegistroUsuarioEntity?

    @Query("DELETE FROM usuarios WHERE correo = :correo")
    suspend fun eliminarPorCorreo(correo: String)

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :contrasena LIMIT 1")
    suspend fun verificarLogin(correo: String, contrasena: String): RegistroUsuarioEntity?
}