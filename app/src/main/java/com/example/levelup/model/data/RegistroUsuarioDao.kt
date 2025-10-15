package com.example.levelup.model.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroUsuarioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarUsuario(usuario: RegistroUsuarioEntity)

    @Query(value = "SELECT * FROM registroUsuario WHERE correo = :correo")
    suspend fun obtenerPorCorreo(correo: String): RegistroUsuarioEntity?




}