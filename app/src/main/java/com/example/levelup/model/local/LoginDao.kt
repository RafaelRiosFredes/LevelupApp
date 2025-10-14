package com.example.levelup.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
 // import com.example.levelup.model.reporitory.LoginReporitory
import kotlinx.coroutines.flow.Flow


@Dao
interface LoginDao {

    @Query("SELECT * FROM usuarios")
    fun observarTodos(): Flow<List<LoginEntity>>

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :contrasena")
    fun getUserByCorreoAndContrasena(correo: String, contrasena: String): LoginEntity?

}