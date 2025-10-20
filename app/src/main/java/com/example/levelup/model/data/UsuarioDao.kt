package com.example.levelup.model.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: UsuarioEntity)

    @Update
    suspend fun actualizarUsuario(usuario: UsuarioEntity)

    @Delete
    suspend fun eliminarUsuario(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios")
    fun obtenerTodos(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuarios WHERE id = :id")
    fun obtenerPorId(id: Int): Flow<UsuarioEntity?>

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun obtenerPorCorreo(correo: String): UsuarioEntity?

    @Query("DELETE FROM usuarios WHERE correo = :correo")
    suspend fun eliminarPorCorreo(correo: String)
}
