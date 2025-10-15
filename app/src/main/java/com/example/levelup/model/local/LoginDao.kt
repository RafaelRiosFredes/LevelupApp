package com.example.levelup.model.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.levelup.model.local.LoginEntity

@Dao
interface LoginDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarUsuario(usuario: LoginEntity)

    @Query("SELECT * FROM usuarios_login WHERE correo = :correo")
    suspend fun obtenerPorCorreo(correo: String): LoginEntity?
}
